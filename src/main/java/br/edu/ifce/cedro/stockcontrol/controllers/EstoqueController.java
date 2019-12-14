package br.edu.ifce.cedro.stockcontrol.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.hibernate.Session;

import br.edu.ifce.cedro.stockcontrol.models.Entrada;
import br.edu.ifce.cedro.stockcontrol.models.Produto;
import br.edu.ifce.cedro.stockcontrol.models.Saida;

@SuppressWarnings("serial")
public abstract class EstoqueController extends Controller {

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
