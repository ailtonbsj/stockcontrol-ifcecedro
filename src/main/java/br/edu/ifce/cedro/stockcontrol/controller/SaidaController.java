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

import br.edu.ifce.cedro.stockcontrol.dao.ProdutoDAO;
import br.edu.ifce.cedro.stockcontrol.dao.SaidaDAO;
import br.edu.ifce.cedro.stockcontrol.model.Saida;
import br.edu.ifce.cedro.stockcontrol.service.SaidaService;
import mapper.SaidaMapper;

import static br.edu.ifce.cedro.stockcontrol.util.Util.dateToDateTimeLocal;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/saida")
public class SaidaController extends HttpServlet {

	HttpServletRequest request;
	HttpServletResponse response;

	private final SaidaDAO dao = new SaidaDAO();
	private final ProdutoDAO produtoDao = new ProdutoDAO();
	private final SaidaService service = new SaidaService();

	private final String viewNamePlural = "Saidas";
	private final String urlName = "/saida";

	protected void showFilledForm(String id) throws ServletException, IOException {
		request.setAttribute("operacao", "Atualizar");
		request.setAttribute("produtos", produtoDao.listar());

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			Saida saida = session.get(Saida.class, Integer.parseInt(id));
			SaidaMapper.toRequest(saida, request);
			request.getRequestDispatcher("/WEB-INF/views" + urlName + "-form.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		session.close();
	}

	protected boolean remove(String id) throws IOException {
		String bloqueio = service.verificaRemocao(id);
		if (bloqueio != null) {
			response.sendRedirect(urlName + "?erro=" + URLEncoder.encode(bloqueio, "UTF-8"));
			return false;
		}

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		// Lido antes da exclusão: é o produto cujo saldo será recalculado.
		int produtoId = dao.buscarPorId(session, Integer.parseInt(id)).getProduto().getId();

		try {
			Object saida = session.get(Saida.class, Integer.parseInt(id));
			session.beginTransaction();
			session.remove(saida);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		response.sendRedirect(urlName);

		produtoDao.recalcularSaldo(produtoId);
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
		request.setAttribute("produtos", produtoDao.listar());
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

		String id = req.getParameter("id");

		if(id.equals("")) service.add(req);
		else service.update(req, id);

		resp.sendRedirect(urlName);
	}

}