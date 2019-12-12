package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@SuppressWarnings("serial")
public abstract class Controller extends HttpServlet {
	
	HttpServletRequest request;
	HttpServletResponse response;
	
	protected void showFilledForm(String id) throws ServletException, IOException {
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
	
	protected void remove(String id) throws IOException {
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
	}
	
	protected void listAll() throws ServletException, IOException {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		CriteriaBuilder cb =  session.getCriteriaBuilder();
		try {
			@SuppressWarnings("unchecked")
			CriteriaQuery<Object> cq = (CriteriaQuery<Object>) cb.createQuery(Class.forName(getModel()));
			@SuppressWarnings("unchecked")
			Root<Object> rootEntry = (Root<Object>) cq.from(Class.forName(getModel()));
			CriteriaQuery<Object> all = cq.select(rootEntry);
		    TypedQuery<Object> allQuery = session.createQuery(all);
		    List<Object> list = allQuery.getResultList();
		    request.setAttribute(getViewNamePlural(), list);
			request.getRequestDispatcher("/WEB-INF/views" + getUrlName() + "-table.jsp").forward(request, response);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	    session.close();
	}
	
	protected abstract void add(Session session);
	
	protected abstract void setFields(Object item);
	
	protected abstract void setFieldsForm(Object item);
	
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
	
	protected abstract String getViewName();

	protected abstract String getViewNamePlural();
	
	protected abstract String getUrlName();
	
	protected abstract String getModel();
	
	protected void showEmptyForm() throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views"+ getUrlName() +"-form.jsp").forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
		String id = req.getParameter("id");
		
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
				
		if(id.equals("")) add(session);
		else update(session, id);
		
		session.close();
		
		resp.sendRedirect(getUrlName());
	}
}
