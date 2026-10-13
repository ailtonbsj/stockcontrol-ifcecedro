package br.edu.ifce.cedro.stockcontrol.daos;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.models.Produto;
import br.edu.ifce.cedro.stockcontrol.models.Unidade;

/**
 * Acesso aos dados da unidade de medida. O {@code UnidadeController} cuida da
 * requisição e da apresentação; toda a conversa com o Hibernate fica aqui.
 *
 * As operações que recebem uma {@link Session} usam a sessão aberta pelo
 * controller, para não duplicar a transação. As que não recebem abrem e fecham
 * a própria sessão, porque são chamadas fora do ciclo de gravação.
 */
public class UnidadeDAO {

	/**
	 * Grava uma nova unidade. A transação é a da sessão recebida, que já está
	 * aberta pelo controller.
	 */
	public void salvar(Session session, Unidade unidade) {
		session.beginTransaction();
		session.save(unidade);
		session.getTransaction().commit();
	}

	/** Quantos produtos estão usando a unidade informada. */
	public long contarProdutos(Integer id) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			return session.createQuery(
					"select count(*) from " + Produto.class.getCanonicalName() + " where unidade.id = :id", Long.class)
					.setParameter("id", id).uniqueResult();
		} finally {
			session.close();
			sf.close();
		}
	}
}