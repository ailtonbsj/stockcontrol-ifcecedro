package br.edu.ifce.cedro.stockcontrol.controllers;

import javax.servlet.annotation.WebServlet;

import org.hibernate.Session;

import br.edu.ifce.cedro.stockcontrol.models.Unidade;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/unidade")
public class UnidadeController extends Controller {
	
	@Override
	protected String getViewName() {
		return "Unidade";
	}
	
	@Override
	protected String getViewNamePlural() {
		return "Unidades";
	}
	
	@Override
	protected String getUrlName() {
		return "/unidade";
	}
	
	@Override
	protected String getModel() {
		return Unidade.class.getCanonicalName();
	}
	
	protected void setFieldsForm(Object unidade) {
		request.setAttribute("id", ((Unidade) unidade).getId().toString());
		request.setAttribute("simbolo", ((Unidade) unidade).getSimbolo());
		request.setAttribute("descricao", ((Unidade) unidade).getDescricao());
	}
	
	protected void setFields(Object item) {
		((Unidade) item).setSimbolo(request.getParameter("simbolo"));
		((Unidade) item).setDescricao(request.getParameter("descricao"));
	}
	
	protected void add(Session session) {
		Unidade unidade = new Unidade();
		setFields((Object) unidade);
		session.beginTransaction();
		session.save(unidade);
		session.getTransaction().commit();
	}
}
