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

import br.edu.ifce.cedro.stockcontrol.dao.UnidadeDAO;
import br.edu.ifce.cedro.stockcontrol.model.Unidade;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/unidade")
public class UnidadeController extends HttpServlet {

	HttpServletRequest request;
	HttpServletResponse response;

	private final UnidadeDAO dao = new UnidadeDAO();

	protected String getViewName() {
		return "Unidade";
	}
	
	protected String getViewNamePlural() {
		return "Unidades";
	}
	
	protected String getUrlName() {
		return "/unidade";
	}
	
	protected String getModel() {
		return Unidade.class.getCanonicalName();
	}
	
	protected void setFieldsForm(Object unidade) {
		request.setAttribute("id", ((Unidade) unidade).getId().toString());
		request.setAttribute("simbolo", ((Unidade) unidade).getSimbolo());
		request.setAttribute("descricao", ((Unidade) unidade).getDescricao());
	}
	
	protected void setFields(Object item) {
		((Unidade) item).setSimbolo(request.getParameter("simbolo"));
		((Unidade) item).setDescricao(request.getParameter("descricao"));
	}
	
	protected void add(Session session) {
		Unidade unidade = new Unidade();
		setFields((Object) unidade);
		dao.salvar(session, unidade);
	}

	protected String verificaRemocao(String id) {
		long produtos = dao.contarProdutos(Integer.valueOf(id));

		if (produtos > 0) {
			return "Esta unidade está em uso por " + produtos + " produto(s) e não pode ser excluída.";
		}

		return null;
	}
	
	protected void showFilledForm(String id) throws ServletException, IOException {
		request.setAttribute("operacao", "Atualizar");
		
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		
		try {
			Object unidade = session.get(Class.forName(getModel()), Integer.parseInt(id));
			setFieldsForm(unidade);
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
			Object unidade = session.get(Class.forName(getModel()), Integer.parseInt(id));
			session.beginTransaction();
			session.remove(unidade);
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
	
	protected void listAll() throws ServletException, IOException {
		request.setAttribute("operacao", "Listar");
		
		request.setAttribute(getViewNamePlural(), getList(getModel()));
		request.getRequestDispatcher("/WEB-INF/views" + getUrlName() + "-table.jsp").forward(request, response);
	}
	
	protected void update(Session session, String id) {
		Object unidade;
		try {
			unidade = session.get(Class.forName(getModel()), Integer.parseInt(id));
			setFields(unidade);
			session.beginTransaction();
			session.update(unidade);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void showEmptyForm() throws ServletException, IOException {
		request.setAttribute("operacao", "Adicionar");
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
