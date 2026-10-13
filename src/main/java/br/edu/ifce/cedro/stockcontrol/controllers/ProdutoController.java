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
import br.edu.ifce.cedro.stockcontrol.models.Produto;
import br.edu.ifce.cedro.stockcontrol.models.Unidade;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/produto")
public class ProdutoController extends HttpServlet {

	HttpServletRequest request;
	HttpServletResponse response;

	private final ProdutoDAO dao = new ProdutoDAO();

	protected String getViewName() {
		return "Produto";
	}

	protected String getViewNamePlural() {
		return "Produtos";
	}

	protected String getUrlName() {
		return "/produto";
	}

	protected String getModel() {
		return Produto.class.getCanonicalName();
	}

	protected void setFieldsForm(Object produto) {
		request.setAttribute("id", ((Produto) produto).getId().toString());
		request.setAttribute("nome", ((Produto) produto).getNome());
		request.setAttribute("quantidade", ((Produto) produto).getQuantidade().toString());
		request.setAttribute("minimo", ((Produto) produto).getMinimo().toString());
		request.setAttribute("unidadeId", ((Produto) produto).getUnidade().getId().toString());
	}

	protected void setFields(Object item) {
		((Produto) item).setNome(request.getParameter("nome"));
		((Produto) item).setQuantidade(Integer.valueOf(request.getParameter("quantidade")));
		((Produto) item).setMinimo(Integer.valueOf(request.getParameter("minimo")));
		Unidade unidade = new Unidade();
		unidade.setId(Integer.valueOf(request.getParameter("unidade")));
		((Produto) item).setUnidade(unidade);
	}

	protected void add(Session session) {
		Produto produto = new Produto();
		setFields((Object) produto);
		dao.salvar(session, produto);
	}

	protected String verificaRemocao(String id) {
		long movimentacoes = dao.contarMovimentacoes(Integer.valueOf(id));

		if (movimentacoes > 0) {
			return "Este produto possui " + movimentacoes
					+ " movimentação(ões) de estoque e não pode ser excluído.";
		}

		return null;
	}

	protected void showFilledForm(String id) throws ServletException, IOException {
		request.setAttribute("operacao", "Atualizar");
		request.setAttribute("unidades", UnidadeController.getList(Unidade.class.getCanonicalName()));

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			Object produto = session.get(Class.forName(getModel()), Integer.parseInt(id));
			setFieldsForm(produto);
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

		try {
			Object produto = session.get(Class.forName(getModel()), Integer.parseInt(id));
			session.beginTransaction();
			session.remove(produto);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		response.sendRedirect(getUrlName());
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
		Object produto;
		try {
			produto = session.get(Class.forName(getModel()), Integer.parseInt(id));
			setFields(produto);
			session.beginTransaction();
			session.update(produto);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void showEmptyForm() throws ServletException, IOException {
		request.setAttribute("operacao", "Adicionar");
		request.setAttribute("unidades", UnidadeController.getList(Unidade.class.getCanonicalName()));
		request.setAttribute("quantidade", "0");
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