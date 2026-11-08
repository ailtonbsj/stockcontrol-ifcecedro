package br.edu.ifce.cedro.stockcontrol.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.service.ProdutoService;

public class NotificacaoFilter implements Filter {

	private final ProdutoService produtoService = new ProdutoService();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession(false);
			boolean autenticado = session != null && session.getAttribute("usuarioLogado") != null;
			if (autenticado && !isStaticResource(httpRequest.getRequestURI(), httpRequest.getContextPath())) {
				try {
					List<Produto> produtosAbaixoMinimo = produtoService.findAbaixoMinimo();
					request.setAttribute("produtosAbaixoMinimo", produtosAbaixoMinimo);
					request.setAttribute("quantidadeNotificacoes", produtosAbaixoMinimo.size());
				} catch (Exception e) {
					request.setAttribute("quantidadeNotificacoes", 0);
				}
			}
		}
		chain.doFilter(request, response);
	}

	private boolean isStaticResource(String uri, String contextPath) {
		String path = uri;
		if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
			path = path.substring(contextPath.length());
		}
		return path.startsWith("/plugins/")
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
