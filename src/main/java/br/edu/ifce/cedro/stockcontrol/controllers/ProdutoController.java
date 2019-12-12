package br.edu.ifce.cedro.stockcontrol.controllers;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setFields(Object item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setFieldsForm(Object item) {
		// TODO Auto-generated method stub
		
	}
}
