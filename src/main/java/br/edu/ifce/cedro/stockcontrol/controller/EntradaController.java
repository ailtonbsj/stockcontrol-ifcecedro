package br.edu.ifce.cedro.stockcontrol.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.dao.EntradaDAO;
import br.edu.ifce.cedro.stockcontrol.dao.ProdutoDAO;
import br.edu.ifce.cedro.stockcontrol.model.Entrada;
import br.edu.ifce.cedro.stockcontrol.model.Produto;

import static br.edu.ifce.cedro.stockcontrol.util.Util.dateToDateTimeLocal;
import static br.edu.ifce.cedro.stockcontrol.util.Util.dateTimeLocalToDate;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/entrada")
public class EntradaController extends HttpServlet {

	HttpServletRequest request;
	HttpServletResponse response;

	private final EntradaDAO dao = new EntradaDAO();
	private final ProdutoDAO produtoDao = new ProdutoDAO();

	private final String viewNamePlural = "Entradas";
	private final String urlName = "/entrada";
	private final String model = Entrada.class.getCanonicalName();

	protected void setFieldsForm(Entrada entrada) {
		request.setAttribute("id", entrada.getId().toString());
		request.setAttribute("produtoId", entrada.getProduto().getId());
		request.setAttribute("fornecedor", entrada.getFornecedor());
		request.setAttribute("quantidade", entrada.getQuantidade().toString());
		request.setAttribute("validade", dateToDateTimeLocal(entrada.getValidade()));
		request.setAttribute("data", dateToDateTimeLocal(entrada.getData()));
		request.setAttribute("observacao", entrada.getObservacao());
	}

	protected void setFields(Entrada item) {
		Produto p = new Produto();
		p.setId(Integer.parseInt(request.getParameter("produto")));
		item.setProduto(p);
		item.setFornecedor(request.getParameter("fornecedor"));
		item.setQuantidade(Integer.valueOf(request.getParameter("quantidade")));
		item.setValidade(dateTimeLocalToDate(request.getParameter("validade")));
		item.setData(dateTimeLocalToDate(request.getParameter("data")));
		item.setObservacao(request.getParameter("observacao"));
	}

	protected void add(Session session) {
		Entrada entrada = new Entrada();
		setFields(entrada);
		dao.salvar(session, entrada);
		produtoDao.recalcularSaldo(session, entrada.getProduto().getId());
	}

	/**
	 * Entrada não tem restrição de exclusão: é ela que origina o saldo do produto,
	 * então apagar uma não deixa referência órfã.
	 */
	protected String verificaRemocao(String id) {
		return null;
	}

	protected void showFilledForm(String id) throws ServletException, IOException {
		request.setAttribute("operacao", "Atualizar");
		request.setAttribute("produtos", ProdutoController.getList(Produto.class.getCanonicalName()));

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			Entrada entrada = session.get(Entrada.class, Integer.parseInt(id));
			setFieldsForm(entrada);
			request.getRequestDispatcher("/WEB-INF/views" + urlName + "-form.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		session.close();
	}

	protected boolean remove(String id) throws IOException {
		String bloqueio = verificaRemocao(id);
		if (bloqueio != null) {
			response.sendRedirect(urlName + "?erro=" + URLEncoder.encode(bloqueio, "UTF-8"));
			return false;
		}

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		// Lido antes da exclusão: é o produto cujo saldo será recalculado.
		int produtoId = dao.buscarPorId(session, Integer.parseInt(id)).getProduto().getId();

		try {
			Object entrada = session.get(Class.forName(model), Integer.parseInt(id));
			session.beginTransaction();
			session.remove(entrada);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		response.sendRedirect(urlName);

		produtoDao.recalcularSaldo(session, produtoId);
		session.close();
		return true;
	}

	/** Quantos registros da entidade informada apontam para o registro id. */
	protected static long contaDependencias(String modelName, String campo, String id) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			return session.createQuery(
					"select count(*) from " + modelName + " where " + campo + ".id = :id", Long.class)
					.setParameter("id", Integer.valueOf(id)).uniqueResult();
		} finally {
			session.close();
			sf.close();
		}
	}

	protected static List<Object> getList(String modelName) throws ServletException, IOException {
		List<Object> list = new ArrayList<Object>();

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			CriteriaBuilder cb =  session.getCriteriaBuilder();
			@SuppressWarnings("unchecked")
			CriteriaQuery<Object> cq = (CriteriaQuery<Object>) cb.createQuery(Class.forName(modelName));
			@SuppressWarnings("unchecked")
			Root<Object> rootEntry = (Root<Object>) cq.from(Class.forName(modelName));
			CriteriaQuery<Object> all = cq.select(rootEntry);
		    TypedQuery<Object> allQuery = session.createQuery(all);
		    list = allQuery.getResultList();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	    session.close();
	    return list;
	}

	protected void listAll() throws ServletException, IOException {
		request.setAttribute("operacao", "Listar");

		request.setAttribute(viewNamePlural, getList(model));
		request.getRequestDispatcher("/WEB-INF/views" + urlName + "-table.jsp").forward(request, response);
	}

	protected void update(Session session, String id) {
		Entrada entrada;
		try {
			entrada = session.get(Entrada.class, Integer.parseInt(id));
			setFields(entrada);
			session.beginTransaction();
			session.update(entrada);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		// Precisa ser depois do update: setFields troca a referência do produto pela
		// que veio do formulário, e é essa a que o saldo deve ser recalculado.
		Entrada en = dao.buscarPorId(session, Integer.parseInt(id));
		produtoDao.recalcularSaldo(session, en.getProduto().getId());
	}

	protected void showEmptyForm() throws ServletException, IOException {
		request.setAttribute("operacao", "Adicionar");
		request.setAttribute("produtos", ProdutoController.getList(Produto.class.getCanonicalName()));
		request.setAttribute("data", dateToDateTimeLocal());
		request.getRequestDispatcher("/WEB-INF/views"+ urlName +"-form.jsp").forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

		// As páginas são servidas em UTF-8: sem isso os acentos digitados nos
		// formulários chegariam decodificados como ISO-8859-1.
		req.setCharacterEncoding("UTF-8");
		this.request = req;
		this.response = resp;

		req.setAttribute("viewName", viewNamePlural);

		String add = req.getParameter("add");
		String up = req.getParameter("up");
		String del = req.getParameter("del");

		if(add != null) showEmptyForm();
		else if(up != null) showFilledForm(up);
		else if(del != null) remove(del);
		else listAll();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		this.request = req;
		this.response = resp;

		String id = req.getParameter("id");

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		if(id.equals("")) add(session);
		else update(session, id);

		session.close();

		resp.sendRedirect(urlName);
	}

}