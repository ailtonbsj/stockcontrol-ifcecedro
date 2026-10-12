package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.models.Entrada;
import br.edu.ifce.cedro.stockcontrol.models.Produto;
import br.edu.ifce.cedro.stockcontrol.models.Saida;

@SuppressWarnings("serial")
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
		request.setAttribute("data", dateToDateTimeLocal());
		super.showEmptyForm();
	}
	
	@Override
	protected void showFilledForm(String id) throws ServletException, IOException {
		request.setAttribute("produtos", ProdutoController.getList(Produto.class.getCanonicalName()));
		super.showFilledForm(id);
	}
	
	@Override
	protected void setFields(Object item) {
		Produto p = new Produto();
		p.setId(Integer.parseInt(request.getParameter("produto")));
		((Saida) item).setProduto(p);
		((Saida) item).setRetirante(request.getParameter("retirante"));
		((Saida) item).setQuantidade(Integer.valueOf(request.getParameter("quantidade")));
		((Saida) item).setData(dateTimeLocalToDate(request.getParameter("data")));
		((Saida) item).setObservacao(request.getParameter("observacao"));
	}
	
	@Override
	protected void setFieldsForm(Object item) {
		request.setAttribute("id", ((Saida) item).getId().toString());
		request.setAttribute("produtoId", ((Saida) item).getProduto().getId());
		request.setAttribute("retirante", ((Saida) item).getRetirante());
		request.setAttribute("quantidade", ((Saida) item).getQuantidade().toString());
		request.setAttribute("data", dateToDateTimeLocal(((Saida) item).getData()));
		request.setAttribute("observacao", ((Saida) item).getObservacao());
	}
	
	@Override
	protected void add(Session session) {
		Saida saida = new Saida();
		setFields((Object) saida);
		session.beginTransaction();
		session.save(saida);
		session.getTransaction().commit();
		refreshAmount(session, saida.getProduto().getId());
	}
	
	@Override
	protected void update(Session session, String id) {
		super.update(session, id);
		Saida sd = session.get(Saida.class, Integer.parseInt(id));
		refreshAmount(session, sd.getProduto().getId());
	}
	
	@Override
	protected boolean remove(String id) throws IOException {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Saida sd = session.get(Saida.class, Integer.parseInt(id));
		int produtoId = sd.getProduto().getId();
		boolean excluido = super.remove(id);
		if (excluido) {
			refreshAmount(session, produtoId);
		}
		session.close();
		return excluido;
	}

	protected void refreshAmount(Session session, Integer id) {
		Produto produto = session.get(Produto.class, id);
		try {
			List<Object> entradas = getList(Entrada.class.getCanonicalName());
			int quant = 0;
			for (Object obj : entradas) {
				Entrada entradaItem = (Entrada) obj;
				if(entradaItem.getProduto().getNome().equals(produto.getNome())) {
					quant += entradaItem.getQuantidade();
				}
			}
			
			List<Object> saidas = getList(Saida.class.getCanonicalName());
			int diff = 0;
			for (Object obj : saidas) {
				Saida saidaItem = (Saida) obj;
				if(saidaItem.getProduto().getNome().equals(produto.getNome())) {
					diff += saidaItem.getQuantidade();
				}
			}
			
			produto.setQuantidade(quant-diff);
			session.beginTransaction();
			session.save(produto);
			session.getTransaction().commit();
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

}
