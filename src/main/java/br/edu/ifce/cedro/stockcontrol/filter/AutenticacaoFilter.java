package br.edu.ifce.cedro.stockcontrol.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AutenticacaoFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			chain.doFilter(request, response);
			return;
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String contextPath = httpRequest.getContextPath();
		String uri = httpRequest.getRequestURI();
		String path = uri;
		if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
			path = path.substring(contextPath.length());
		}

		if (isRecursoPublico(path)) {
			chain.doFilter(request, response);
			return;
		}

		HttpSession session = httpRequest.getSession(false);
		if (session != null && session.getAttribute("usuarioLogado") != null) {
			chain.doFilter(request, response);
			return;
		}

		httpResponse.sendRedirect(contextPath + "/login");
	}

	private boolean isRecursoPublico(String path) {
		return "/login".equals(path)
				|| path.startsWith("/plugins/")
				|| path.startsWith("/css/")
				|| path.startsWith("/js/")
				|| path.startsWith("/img/")
				|| path.startsWith("/dist/")
				|| path.endsWith(".css")
				|| path.endsWith(".js")
				|| path.endsWith(".png")
				|| path.endsWith(".jpg")
				|| path.endsWith(".jpeg")
				|| path.endsWith(".gif")
				|| path.endsWith(".svg")
				|| path.endsWith(".woff")
				|| path.endsWith(".woff2")
				|| path.endsWith(".ttf")
				|| path.endsWith(".map")
				|| path.endsWith(".ico");
	}

	@Override
	public void destroy() {
	}
}
