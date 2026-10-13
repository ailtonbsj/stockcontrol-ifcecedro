package br.edu.ifce.cedro.stockcontrol.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.model.Entrada;
import br.edu.ifce.cedro.stockcontrol.model.Estoque;
import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.model.Saida;

/**
 * Acesso aos dados do produto. O {@code ProdutoController} cuida da requisição
 * e da apresentação; toda a conversa com o Hibernate fica aqui.
 *
 * As operações que recebem uma {@link Session} usam a sessão aberta pelo
 * controller, para não duplicar a transação. As que não recebem abrem e fecham
 * a própria sessão, porque são chamadas fora do ciclo de gravação.
 */
public class ProdutoDAO {

	/**
	 * Grava um novo produto. A transação é a da sessão recebida, que já está
	 * aberta pelo controller.
	 */
	public void salvar(Session session, Produto produto) {
		session.beginTransaction();
		session.save(produto);
		session.getTransaction().commit();
	}

	/** Todos os produtos, para montar a lista e o preenchimento dos formulários. */
	public List<Produto> listar() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			return session.createQuery("from " + Produto.class.getCanonicalName(), Produto.class).getResultList();
		} finally {
			session.close();
			sf.close();
		}
	}

	/**
	 * Quantas movimentações de estoque existem para o produto informado. Entrada e
	 * Saida são subclasses de {@link Estoque}, então contar na tabela base cobre as
	 * duas.
	 */
	public long contarMovimentacoes(Integer id) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			return session.createQuery(
					"select count(*) from " + Estoque.class.getCanonicalName() + " where produto.id = :id", Long.class)
					.setParameter("id", id).uniqueResult();
		} finally {
			session.close();
			sf.close();
		}
	}

	/**
	 * Recalcula o saldo do produto a partir das movimentações: tudo que entrou
	 * menos tudo que saiu. É aqui que o saldo é recalculado porque entradas e
	 * saídas são as duas faces do mesmo número.
	 *
	 * As somas são feitas no banco, agrupadas por produto, em vez de trazer todas
	 * as movimentações para a memória.
	 */
	public void recalcularSaldo(Session session, Integer produtoId) {
		Produto produto = session.get(Produto.class, produtoId);

		Long entradas = session
				.createQuery("select sum(e.quantidade) from " + Entrada.class.getCanonicalName()
						+ " e where e.produto.id = :id", Long.class)
				.setParameter("id", produtoId).uniqueResult();

		Long saidas = session
				.createQuery("select sum(s.quantidade) from " + Saida.class.getCanonicalName()
						+ " s where s.produto.id = :id", Long.class)
				.setParameter("id", produtoId).uniqueResult();

		produto.setQuantidade((int) ((entradas == null ? 0L : entradas) - (saidas == null ? 0L : saidas)));

		session.beginTransaction();
		session.save(produto);
		session.getTransaction().commit();
	}
}