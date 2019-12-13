package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.hibernate.Session;

import br.edu.ifce.cedro.stockcontrol.models.Entrada;
import br.edu.ifce.cedro.stockcontrol.models.Produto;

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
	protected void add(Session session) {
		Entrada entrada = new Entrada();
		entrada.setData(new Date());
		entrada.setFornecedor("Joao");
		entrada.setQuantidade(2);
		entrada.setValidade(new Date());
		entrada.setProduto(session.get(Produto.class, 3));
		//setFields((Object) entrada);
		session.beginTransaction();
		session.save(entrada);
		session.getTransaction().commit();
	}
}
