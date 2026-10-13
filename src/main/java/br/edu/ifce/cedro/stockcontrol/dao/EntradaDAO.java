package br.edu.ifce.cedro.stockcontrol.dao;

import org.hibernate.Session;

import br.edu.ifce.cedro.stockcontrol.model.Entrada;

/**
 * Acesso aos dados da entrada de estoque. O {@code EntradaController} cuida da
 * requisição e da apresentação; toda a conversa com o Hibernate fica aqui.
 *
 * As operações recebem a {@link Session} aberta pelo controller, para não
 * duplicar a transação nem abrir uma segunda sessão no meio da requisição.
 */
public class EntradaDAO {

	/**
	 * Grava uma nova entrada. A transação é a da sessão recebida, que já está
	 * aberta pelo controller.
	 */
	public void salvar(Session session, Entrada entrada) {
		session.beginTransaction();
		session.save(entrada);
		session.getTransaction().commit();
	}

	/**
	 * Busca a entrada pela chave dentro da sessão corrente. Por usar a sessão que já
	 * está aberta, o Hibernate devolve a instância já carregada, sem nova consulta.
	 */
	public Entrada buscarPorId(Session session, Integer id) {
		return session.get(Entrada.class, id);
	}
}