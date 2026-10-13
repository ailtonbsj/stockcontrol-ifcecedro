package br.edu.ifce.cedro.stockcontrol.dao;

import org.hibernate.Session;

import br.edu.ifce.cedro.stockcontrol.model.Saida;

/**
 * Acesso aos dados da saída de estoque. O {@code SaidaController} cuida da
 * requisição e da apresentação; toda a conversa com o Hibernate fica aqui.
 *
 * As operações recebem a {@link Session} aberta pelo controller, para não
 * duplicar a transação nem abrir uma segunda sessão no meio da requisição.
 */
public class SaidaDAO {

	/**
	 * Grava uma nova saída. A transação é a da sessão recebida, que já está
	 * aberta pelo controller.
	 */
	public void salvar(Session session, Saida saida) {
		session.beginTransaction();
		session.save(saida);
		session.getTransaction().commit();
	}

	/**
	 * Busca a saída pela chave dentro da sessão corrente. Por usar a sessão que já
	 * está aberta, o Hibernate devolve a instância já carregada, sem nova consulta.
	 */
	public Saida buscarPorId(Session session, Integer id) {
		return session.get(Saida.class, id);
	}
}