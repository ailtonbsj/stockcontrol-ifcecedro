package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.hibernate.Session;

import br.edu.ifce.cedro.stockcontrol.models.Produto;
import br.edu.ifce.cedro.stockcontrol.models.Unidade;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/produto")
public class ProdutoController extends Controller {

	@Override
	protected String getViewName() {
		return "Produto";
	}

	@Override
	protected String getViewNamePlural() {
		return "Produtos";
	}

	@Override
	protected String getUrlName() {
		return "/produto";
	}

	@Override
	protected String getModel() {
		return Produto.class.getCanonicalName();
	}
	
	@Override
	protected void add(Session session) {
		Produto produto = new Produto();
		setFields((Object) produto);
		session.beginTransaction();
		session.save(produto);
		session.getTransaction().commit();
	}

	@Override
	protected void setFields(Object item) {	
		((Produto) item).setNome(request.getParameter("nome"));
		((Produto) item).setQuantidade(Integer.valueOf(request.getParameter("quantidade")));
		Unidade unidade = new Unidade();
		unidade.setId(Integer.valueOf(request.getParameter("unidade")));
		((Produto) item).setUnidade(unidade);
	}

	@Override
	protected void setFieldsForm(Object produto) {
		request.setAttribute("id", ((Produto) produto).getId().toString());
		request.setAttribute("nome", ((Produto) produto).getNome());
		request.setAttribute("quantidade", ((Produto) produto).getQuantidade().toString());
		request.setAttribute("unidadeId", ((Produto) produto).getUnidade().getId().toString());
	}
	
	protected void showEmptyForm() throws ServletException, IOException {
		request.setAttribute("unidades", UnidadeController.getList(Unidade.class.getCanonicalName()));
		super.showEmptyForm();
	}
	
	protected void showFilledForm(String id) throws ServletException, IOException {
		request.setAttribute("unidades", UnidadeController.getList(Unidade.class.getCanonicalName()));
		super.showFilledForm(id);
	}
}
