package br.edu.ifce.cedro.stockcontrol.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;

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
import br.edu.ifce.cedro.stockcontrol.service.UnidadeService;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/unidade")
public class UnidadeController extends HttpServlet {

	HttpServletRequest request;
	HttpServletResponse response;

	private final UnidadeDAO dao = new UnidadeDAO();
	private final UnidadeService service = new UnidadeService();

	private final String viewNamePlural = "Unidades";
	private final String urlName = "/unidade";
	
	protected void showFilledForm(String id) throws ServletException, IOException {
		try {
			service.findById(request, id);
			request.getRequestDispatcher("/WEB-INF/views" + urlName + "-form.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	protected boolean remove(String id) throws IOException {
		String bloqueio = service.verificaRemocao(id);
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
	
	protected void listAll() throws ServletException, IOException {
		request.setAttribute("operacao", "Listar");
		
		request.setAttribute(viewNamePlural, dao.listar());
		request.getRequestDispatcher("/WEB-INF/views" + urlName + "-table.jsp").forward(request, response);
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
		
		String id = req.getParameter("id");
		
		if(id.isBlank()) service.add(req);
		else service.update(req, id);
		
		resp.sendRedirect(urlName);
	}

}
