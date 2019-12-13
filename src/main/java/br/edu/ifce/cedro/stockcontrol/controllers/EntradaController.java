package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.hibernate.Session;

import br.edu.ifce.cedro.stockcontrol.models.Entrada;
import br.edu.ifce.cedro.stockcontrol.models.Produto;
import br.edu.ifce.cedro.stockcontrol.models.Unidade;

@WebServlet(urlPatterns = "/entrada")
public class EntradaController extends Controller {
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
		((Entrada) item).setData(new Date());
		((Entrada) item).setFornecedor(request.getParameter("fornecedor"));
		((Entrada) item).setQuantidade(Integer.valueOf(request.getParameter("quantidade")));
		((Entrada) item).setValidade(new Date());
		Produto p = new Produto();
		p.setId(Integer.parseInt(request.getParameter("produto")));
		((Entrada) item).setProduto(p);
		((Entrada) item).setObservacao(request.getParameter("observacao"));
	}
	
	@Override
	protected void showEmptyForm() throws ServletException, IOException {
		request.setAttribute("produtos", ProdutoController.getList(Produto.class.getCanonicalName()));
		super.showEmptyForm();
	}
	
	@Override
	protected void add(Session session) {
		Entrada entrada = new Entrada();
		setFields((Object) entrada);
		session.beginTransaction();
		session.save(entrada);
		session.getTransaction().commit();
	}
}
