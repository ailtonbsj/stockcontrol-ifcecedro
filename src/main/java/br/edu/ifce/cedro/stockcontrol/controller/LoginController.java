package br.edu.ifce.cedro.stockcontrol.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifce.cedro.stockcontrol.dao.UsuarioDAO;
import br.edu.ifce.cedro.stockcontrol.model.Usuario;
import br.edu.ifce.cedro.stockcontrol.util.SenhaUtil;

@WebServlet(urlPatterns = { "/login", "/logout" })
public class LoginController extends HttpServlet {

	private final UsuarioDAO usuarioDAO = new UsuarioDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String path = request.getServletPath();

		if ("/logout".equals(path)) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("usuarioLogado") != null) {
			response.sendRedirect(request.getContextPath() + "/unidade");
			return;
		}

		request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");

		if (login == null) {
			login = "";
		}
		if (senha == null) {
			senha = "";
		}
		login = login.trim();

		Usuario usuario = usuarioDAO.findByLogin(login);
		if (usuario == null || !SenhaUtil.verificar(senha, usuario.getSenha())) {
			request.setAttribute("erroLogin", "Usuário ou senha inválidos.");
			request.setAttribute("loginInformado", login);
			request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
			return;
		}

		HttpSession antiga = request.getSession(false);
		if (antiga != null) {
			antiga.invalidate();
		}
		HttpSession session = request.getSession(true);
		usuario.setSenha(null);
		session.setAttribute("usuarioLogado", usuario);
		response.sendRedirect(request.getContextPath() + "/unidade");
	}
}
