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
import mapper.UnidadeMapper;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/unidade")
public class UnidadeController extends HttpServlet {

	HttpServletRequest request;
	HttpServletResponse response;

	private final UnidadeDAO dao = new UnidadeDAO();

	private final String viewNamePlural = "Unidades";
	private final String urlName = "/unidade";
	
	protected void add(Session session) {
		Unidade unidade = new Unidade();
		UnidadeMapper.toModel(unidade, request);
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
			Unidade unidade = session.get(Unidade.class, Integer.parseInt(id));
			UnidadeMapper.toRequest(unidade, request);
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

		try {
			Object unidade = session.get(Unidade.class, Integer.parseInt(id));
			session.beginTransaction();
			session.remove(unidade);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		response.sendRedirect(urlName);
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
	
	protected static List<Unidade> getList() throws ServletException, IOException {
		List<Unidade> list = new ArrayList<>();
		
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		
		CriteriaBuilder cb =  session.getCriteriaBuilder();
		CriteriaQuery<Unidade> cq = (CriteriaQuery<Unidade>) cb.createQuery(Unidade.class);
		Root<Unidade> rootEntry = (Root<Unidade>) cq.from(Unidade.class);
		CriteriaQuery<Unidade> all = cq.select(rootEntry);
		TypedQuery<Unidade> allQuery = session.createQuery(all);
		list = allQuery.getResultList();
		
	    session.close();
	    return list;
	}
	
	protected void listAll() throws ServletException, IOException {
		request.setAttribute("operacao", "Listar");
		
		request.setAttribute(viewNamePlural, getList());
		request.getRequestDispatcher("/WEB-INF/views" + urlName + "-table.jsp").forward(request, response);
	}
	
	protected void update(Session session, String id) {
		Unidade unidade;
		try {
			unidade = session.get(Unidade.class, Integer.parseInt(id));
			UnidadeMapper.toModel(unidade, request);
			session.beginTransaction();
			session.update(unidade);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	protected void showEmptyForm() throws ServletException, IOException {
		request.setAttribute("operacao", "Adicionar");
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
