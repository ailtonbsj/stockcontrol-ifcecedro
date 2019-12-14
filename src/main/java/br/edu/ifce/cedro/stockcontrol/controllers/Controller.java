package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
		request.setAttribute("operacao", "Adicionar");
		request.getRequestDispatcher("/WEB-INF/views"+ getUrlName() +"-form.jsp").forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		
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
