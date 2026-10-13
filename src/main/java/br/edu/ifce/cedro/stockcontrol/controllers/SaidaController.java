package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import br.edu.ifce.cedro.stockcontrol.daos.ProdutoDAO;
import br.edu.ifce.cedro.stockcontrol.daos.SaidaDAO;
import br.edu.ifce.cedro.stockcontrol.models.Produto;
import br.edu.ifce.cedro.stockcontrol.models.Saida;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/saida")
public class SaidaController extends HttpServlet {

	HttpServletRequest request;
	HttpServletResponse response;

	private final SaidaDAO dao = new SaidaDAO();
	private final ProdutoDAO produtoDao = new ProdutoDAO();

	protected String getViewName() {
		return "Saida";
	}

	protected String getViewNamePlural() {
		return "Saidas";
	}

	protected String getUrlName() {
		return "/saida";
	}

	protected String getModel() {
		return Saida.class.getCanonicalName();
	}

	protected void setFieldsForm(Object saida) {
		request.setAttribute("id", ((Saida) saida).getId().toString());
		request.setAttribute("produtoId", ((Saida) saida).getProduto().getId());
		request.setAttribute("retirante", ((Saida) saida).getRetirante());
		request.setAttribute("quantidade", ((Saida) saida).getQuantidade().toString());
		request.setAttribute("data", dateToDateTimeLocal(((Saida) saida).getData()));
		request.setAttribute("observacao", ((Saida) saida).getObservacao());
	}

	protected void setFields(Object item) {
		Produto p = new Produto();
		p.setId(Integer.parseInt(request.getParameter("produto")));
		((Saida) item).setProduto(p);
		((Saida) item).setRetirante(request.getParameter("retirante"));
		((Saida) item).setQuantidade(Integer.valueOf(request.getParameter("quantidade")));
		((Saida) item).setData(dateTimeLocalToDate(request.getParameter("data")));
		((Saida) item).setObservacao(request.getParameter("observacao"));
	}

	protected void add(Session session) {
		Saida saida = new Saida();
		setFields((Object) saida);
		dao.salvar(session, saida);
		produtoDao.recalcularSaldo(session, saida.getProduto().getId());
	}

	/**
	 * Saída não tem restrição de exclusão: apagar uma só faz o saldo do produto
	 * voltar a ser maior, nunca deixa referência órfã.
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
			Object saida = session.get(Class.forName(getModel()), Integer.parseInt(id));
			setFieldsForm(saida);
			request.getRequestDispatcher("/WEB-INF/views" + getUrlName() + "-form.jsp").forward(request, response);
		} catch (NumberFormatException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		session.close();
	}

	protected boolean remove(String id) throws IOException {
		String bloqueio = verificaRemocao(id);
		if (bloqueio != null) {
			response.sendRedirect(getUrlName() + "?erro=" + URLEncoder.encode(bloqueio, "UTF-8"));
			return false;
		}

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		// Lido antes da exclusão: é o produto cujo saldo será recalculado.
		int produtoId = dao.buscarPorId(session, Integer.parseInt(id)).getProduto().getId();

		try {
			Object saida = session.get(Class.forName(getModel()), Integer.parseInt(id));
			session.beginTransaction();
			session.remove(saida);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		response.sendRedirect(getUrlName());

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

	public static String dateToDateTimeLocal(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		return df.format(date);
	}

	public static String dateToDateTimeLocal() {
		Date date = Calendar.getInstance().getTime();
		return dateToDateTimeLocal(date);
	}

	public static Date dateTimeLocalToDate(String datetimeLocal) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(datetimeLocal);
		} catch (ParseException e) {
			return new Date();
		}
	}

	protected void listAll() throws ServletException, IOException {
		request.setAttribute("operacao", "Listar");

		request.setAttribute(getViewNamePlural(), getList(getModel()));
		request.getRequestDispatcher("/WEB-INF/views" + getUrlName() + "-table.jsp").forward(request, response);
	}

	protected void update(Session session, String id) {
		Object saida;
		try {
			saida = session.get(Class.forName(getModel()), Integer.parseInt(id));
			setFields(saida);
			session.beginTransaction();
			session.update(saida);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// Precisa ser depois do update: setFields troca a referência do produto pela
		// que veio do formulário, e é essa a que o saldo deve ser recalculado.
		Saida sd = dao.buscarPorId(session, Integer.parseInt(id));
		produtoDao.recalcularSaldo(session, sd.getProduto().getId());
	}

	protected void showEmptyForm() throws ServletException, IOException {
		request.setAttribute("operacao", "Adicionar");
		request.setAttribute("produtos", ProdutoController.getList(Produto.class.getCanonicalName()));
		request.setAttribute("data", dateToDateTimeLocal());
		request.getRequestDispatcher("/WEB-INF/views"+ getUrlName() +"-form.jsp").forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

		// As páginas são servidas em UTF-8: sem isso os acentos digitados nos
		// formulários chegariam decodificados como ISO-8859-1.
		req.setCharacterEncoding("UTF-8");
		this.request = req;
		this.response = resp;

		req.setAttribute("viewName", getViewNamePlural());

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

		resp.sendRedirect(getUrlName());
	}

}