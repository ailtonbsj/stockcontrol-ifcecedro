package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.daos.EntradaDAO;
import br.edu.ifce.cedro.stockcontrol.daos.ProdutoDAO;
import br.edu.ifce.cedro.stockcontrol.models.Entrada;
import br.edu.ifce.cedro.stockcontrol.models.Produto;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/entrada")
public class EntradaController extends Controller {

	private final EntradaDAO dao = new EntradaDAO();
	private final ProdutoDAO produtoDao = new ProdutoDAO();

	@Override
	protected String getUrlName() {
		return "/entrada";
	}
	
	@Override
	protected String getViewName() {
		return "Entrada";
	}
	
	@Override
	protected String getViewNamePlural() {
		return "Entradas";
	}
	
	@Override
	protected String getModel() {
		return Entrada.class.getCanonicalName();
	}
	
	@Override
	protected void setFields(Object item) {
		Produto p = new Produto();
		p.setId(Integer.parseInt(request.getParameter("produto")));
		((Entrada) item).setProduto(p);
		((Entrada) item).setFornecedor(request.getParameter("fornecedor"));
		((Entrada) item).setQuantidade(Integer.valueOf(request.getParameter("quantidade")));
		((Entrada) item).setValidade(dateTimeLocalToDate(request.getParameter("validade")));
		((Entrada) item).setData(dateTimeLocalToDate(request.getParameter("data")));
		((Entrada) item).setObservacao(request.getParameter("observacao"));
	}
	
	@Override
	protected void setFieldsForm(Object item) {
		request.setAttribute("id", ((Entrada) item).getId().toString());
		request.setAttribute("produtoId", ((Entrada) item).getProduto().getId());
		request.setAttribute("fornecedor", ((Entrada) item).getFornecedor());
		request.setAttribute("quantidade", ((Entrada) item).getQuantidade().toString());
		request.setAttribute("validade", dateToDateTimeLocal(((Entrada) item).getValidade()));
		request.setAttribute("data", dateToDateTimeLocal(((Entrada) item).getData()));
		request.setAttribute("observacao", ((Entrada) item).getObservacao());
	}
	
	@Override
	protected void showEmptyForm() throws ServletException, IOException {
		request.setAttribute("produtos", ProdutoController.getList(Produto.class.getCanonicalName()));
		request.setAttribute("data", dateToDateTimeLocal());
		super.showEmptyForm();
	}
	
	@Override
	protected void showFilledForm(String id) throws ServletException, IOException {
		request.setAttribute("produtos", ProdutoController.getList(Produto.class.getCanonicalName()));
		super.showFilledForm(id);
	}
	
	@Override
	protected void add(Session session) {
		Entrada entrada = new Entrada();
		setFields((Object) entrada);
		dao.salvar(session, entrada);
		produtoDao.recalcularSaldo(session, entrada.getProduto().getId());
	}
	
	@Override
	protected void update(Session session, String id) {
		super.update(session, id);
		// Precisa ser depois do update: setFields troca a referência do produto pela
		// que veio do formulário, e é essa a que o saldo deve ser recalculado.
		Entrada en = dao.buscarPorId(session, Integer.parseInt(id));
		produtoDao.recalcularSaldo(session, en.getProduto().getId());
	}
	
	@Override
	protected boolean remove(String id) throws IOException {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		int produtoId = dao.buscarPorId(session, Integer.parseInt(id)).getProduto().getId();
		boolean excluido = super.remove(id);
		if (excluido) {
			produtoDao.recalcularSaldo(session, produtoId);
		}
		session.close();
		return excluido;
	}
}
