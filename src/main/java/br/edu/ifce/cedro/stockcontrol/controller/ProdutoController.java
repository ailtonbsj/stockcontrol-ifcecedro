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
import br.edu.ifce.cedro.stockcontrol.dao.UnidadeDAO;
import br.edu.ifce.cedro.stockcontrol.model.Produto;
import mapper.ProdutoMapper;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/produto")
public class ProdutoController extends HttpServlet {

	HttpServletRequest request;
	HttpServletResponse response;

	private final ProdutoDAO dao = new ProdutoDAO();
	private final UnidadeDAO unidadeDao = new UnidadeDAO();

	private final String viewNamePlural = "Produtos";
	private final String urlName = "/produto";

	protected void add(){
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		Produto produto = new Produto();
		ProdutoMapper.toModel(produto, request);
		dao.salvar(session, produto);

		session.close();
	}

	protected String verificaRemocao(String id) {
		long movimentacoes = dao.contarMovimentacoes(Integer.valueOf(id));

		if (movimentacoes > 0) {
			return "Este produto possui " + movimentacoes
					+ " movimentação(ões) de estoque e não pode ser excluído.";
		}

		return null;
	}

	protected void showFilledForm(String id) throws ServletException, IOException {
		request.setAttribute("operacao", "Atualizar");
		request.setAttribute("unidades", unidadeDao.listar());

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			Produto produto = session.get(Produto.class, Integer.parseInt(id));
			ProdutoMapper.toRequest(produto, request);
			request.getRequestDispatcher("/WEB-INF/views" + urlName + "-form.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		session.close();
	}

	protected boolean remove(String id) throws IOException {
		String bloqueio = verificaRemocao(id);
		if (bloqueio != null) {
			response.sendRedirect(urlName + "?erro=" + URLEncoder.encode(bloqueio, "UTF-8"));
			return false;
		}

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			Object produto = session.get(Produto.class, Integer.parseInt(id));
			session.beginTransaction();
			session.remove(produto);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		response.sendRedirect(urlName);
		session.close();
		return true;
	}

	/** Quantos registros da entidade informada apontam para o registro id. */
	protected static long contaDependencias(String modelName, String campo, String id) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			return session.createQuery(
					"select count(*) from " + modelName + " where " + campo + ".id = :id", Long.class)
					.setParameter("id", Integer.valueOf(id)).uniqueResult();
		} finally {
			session.close();
			sf.close();
		}
	}

	protected void listAll() throws ServletException, IOException {
		request.setAttribute("operacao", "Listar");

		request.setAttribute(viewNamePlural, dao.listar());
		request.getRequestDispatcher("/WEB-INF/views" + urlName + "-table.jsp").forward(request, response);
	}

	protected void update(String id) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		Produto produto;
		try {
			produto = session.get(Produto.class, Integer.parseInt(id));
			ProdutoMapper.toModel(produto, request);
			session.beginTransaction();
			session.update(produto);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		session.close();
	}

	protected void showEmptyForm() throws ServletException, IOException {
		request.setAttribute("operacao", "Adicionar");
		request.setAttribute("unidades", unidadeDao.listar());
		request.setAttribute("quantidade", "0");
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
		this.request = req;
		this.response = resp;

		String id = req.getParameter("id");



		if(id.equals("")) add();
		else update(id);

		resp.sendRedirect(urlName);
	}

}