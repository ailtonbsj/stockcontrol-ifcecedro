package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
		session.beginTransaction();
		session.save(entrada);
		session.getTransaction().commit();
	}
}
