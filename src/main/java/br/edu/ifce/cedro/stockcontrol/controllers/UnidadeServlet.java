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
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		
		Unidade metro = new Unidade();
		metro.setSimbolo("m");
		metro.setDescricao("Metro");
		
		Unidade kilo = new Unidade();
		kilo.setSimbolo("kg");
		kilo.setDescricao("kilograma");
		
		session.beginTransaction();
		session.save(kilo);
		session.save(metro);
		session.getTransaction().commit();
		
		CriteriaBuilder cb =  session.getCriteriaBuilder();
		CriteriaQuery<Unidade> cq = cb.createQuery(Unidade.class);
		Root<Unidade> rootEntry = cq.from(Unidade.class);
		CriteriaQuery<Unidade> all = cq.select(rootEntry);
	    TypedQuery<Unidade> allQuery = session.createQuery(all);
	    List<Unidade> unidades = allQuery.getResultList();
		//Unidade unidade = session.get(Unidade.class, 1);
		session.close();
		
		req.setAttribute("unidades", unidades);
		req.getRequestDispatcher("/WEB-INF/views/unidade.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Unidade metro = new Unidade();
		metro.setSimbolo("m");
		metro.setDescricao("Metro");
		
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		session.save(metro);
		session.getTransaction().commit();
		session.close();
		
		resp.getWriter().println("<h1>Ola mundo</h1>");
	}
}
