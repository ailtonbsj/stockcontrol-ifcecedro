package br.edu.ifce.cedro.stockcontrol.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.edu.ifce.cedro.stockcontrol.service.ProdutoService;

@WebServlet(urlPatterns = {"/produto", "/produto/novo", "/produto/editar", "/produto/remover"})
public class ProdutoController extends HttpServlet {

	private final ProdutoService service = new ProdutoService();

	private final String viewNamePlural = "Produtos";
	private final String urlName = "/produto";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		request.setAttribute("viewName", viewNamePlural);

		String path = request.getServletPath();
		String id = request.getParameter("id");

		if(path.equals("/produto")) listAll(request, response);
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
		request.setAttribute("unidades", service.findAllUnidades());
		request.setAttribute("quantidade", "0");
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
