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
		refreshAmount(session, entrada.getProduto().getId());
	}
	
	@Override
	protected void update(Session session, String id) {
		super.update(session, id);
		Entrada en = session.get(Entrada.class, Integer.parseInt(id));
		refreshAmount(session, en.getProduto().getId());
	}
	
	@Override
	protected boolean remove(String id) throws IOException {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Entrada en = session.get(Entrada.class, Integer.parseInt(id));
		int produtoId = en.getProduto().getId();
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
