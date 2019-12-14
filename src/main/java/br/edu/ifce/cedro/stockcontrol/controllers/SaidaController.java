package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import br.edu.ifce.cedro.stockcontrol.models.Produto;
import br.edu.ifce.cedro.stockcontrol.models.Saida;

@WebServlet(urlPatterns = "/saida")
public class SaidaController extends Controller {
	@Override
	protected String getUrlName() {
		return "/saida";
	}
	
	@Override
	protected String getViewName() {
		return "Saida";
	}
	
	@Override
	protected String getViewNamePlural() {
		return "Saidas";
	}
	
	@Override
	protected String getModel() {
		return Saida.class.getCanonicalName();
	}
	
	@Override
	protected void showEmptyForm() throws ServletException, IOException {
		request.setAttribute("produtos", ProdutoController.getList(Produto.class.getCanonicalName()));
		super.showEmptyForm();
	}
}
