package br.edu.ifce.cedro.stockcontrol.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.edu.ifce.cedro.stockcontrol.service.SaidaService;

import static br.edu.ifce.cedro.stockcontrol.util.Util.dateToDateTimeLocal;

@WebServlet(urlPatterns = {"/saida", "/saida/novo", "/saida/editar", "/saida/remover"})
public class SaidaController extends HttpServlet {

	private final SaidaService service = new SaidaService();

	private final String viewNamePlural = "Saidas";
	private final String urlName = "/saida";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		request.setAttribute("viewName", viewNamePlural);

		String path = request.getServletPath();
		String id = request.getParameter("id");

		if(path.equals("/saida")) listAll(request, response);
		else if(path.contains("/novo")) showEmptyForm(request, response);
		else if(path.contains("/editar")) showFilledForm(request, response, id);
		else if(path.contains("/remover"))
			service.deleteById(response, Integer.valueOf(id), urlName);
	}

	private void listAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("operacao", "Listar");
		request.setAttribute(viewNamePlural, service.findAll());
		request.getRequestDispatcher("/WEB-INF/views" + urlName + "-table.jsp").forward(request, response);
	}

	private void showEmptyForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("operacao", "Adicionar");
		request.setAttribute("produtos", service.findAllProdutos());
		request.setAttribute("data", dateToDateTimeLocal());
		request.getRequestDispatcher("/WEB-INF/views"+ urlName +"-form.jsp").forward(request, response);
	}

	private void showFilledForm(HttpServletRequest request, HttpServletResponse response, String id) throws ServletException, IOException {
		service.findById(request, id);
		request.setAttribute("operacao", "Atualizar");
		request.getRequestDispatcher("/WEB-INF/views" + urlName + "-form.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		if(id.isBlank()) service.insert(request);
		else service.update(request, id);
		response.sendRedirect(urlName);
	}

}
