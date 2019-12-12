package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;
import java.util.List;

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

import br.edu.ifce.cedro.stockcontrol.models.Unidade;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/unidade")
public class UnidadeServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("viewName", "Unidades");
		String update = req.getParameter("up");
		String delete = req.getParameter("del");
		
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		
		if(req.getParameter("add") != null) {
			req.getRequestDispatcher("/WEB-INF/views/unidade-form.jsp").forward(req, resp);
		} else if(update != null) {
			Unidade unidade = session.get(Unidade.class, Integer.parseInt(update));
			req.setAttribute("id", unidade.getId().toString());
			req.setAttribute("simbolo", unidade.getSimbolo());
			req.setAttribute("descricao", unidade.getDescricao());
			req.getRequestDispatcher("/WEB-INF/views/unidade-form.jsp").forward(req, resp);
		} else if(delete != null) {
			System.out.println("DELETE!");
			Unidade unidade = session.get(Unidade.class, Integer.parseInt(delete));
			session.beginTransaction();
			session.remove(unidade);
			session.getTransaction().commit();
			
			resp.sendRedirect("/unidade");
		} else {
			CriteriaBuilder cb =  session.getCriteriaBuilder();
			CriteriaQuery<Unidade> cq = cb.createQuery(Unidade.class);
			Root<Unidade> rootEntry = cq.from(Unidade.class);
			CriteriaQuery<Unidade> all = cq.select(rootEntry);
		    TypedQuery<Unidade> allQuery = session.createQuery(all);
		    List<Unidade> unidades = allQuery.getResultList();
		    
			session.close();
			
			req.setAttribute("unidades", unidades);
			req.getRequestDispatcher("/WEB-INF/views/unidade-table.jsp").forward(req, resp);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		
		Unidade unidade;
		session.beginTransaction();
		
		if(id.equals("")) {
			unidade = new Unidade();
		} else {
			unidade = session.get(Unidade.class, Integer.parseInt(id));
		}
		
		unidade.setSimbolo(req.getParameter("simbolo"));
		unidade.setDescricao(req.getParameter("descricao"));
		
		if(id.equals("")) {
			session.save(unidade);
		} else {
			session.update(unidade);
		}
		
		session.getTransaction().commit();
		session.close();
		
		resp.sendRedirect("/unidade");
	}
}
