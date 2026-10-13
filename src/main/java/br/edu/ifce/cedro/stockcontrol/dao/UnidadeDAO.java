package br.edu.ifce.cedro.stockcontrol.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.model.Unidade;

/**
 * Acesso aos dados da unidade de medida. O {@code UnidadeController} cuida da
 * requisição e da apresentação; toda a conversa com o Hibernate fica aqui.
 *
 * As operações abrem e fecham a própria sessão, para que o controller cuide
 * só da requisição e da apresentação.
 */
public class UnidadeDAO {

	/**
	 * Grava uma nova unidade, abrindo e fechando a própria sessão para que o
	 * controller não precise conhecer o Hibernate.
	 */
	public void salvar(Unidade unidade) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.save(unidade);
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	/**
	 * Grava as alterações de uma unidade já existente. A instância chega
	 * desanexada, com a chave preenchida, e é mesclada na sessão que o próprio DAO
	 * abre: o controller não precisa carregar o registro antes de gravar.
	 */
	public void atualizar(Unidade unidade) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.merge(unidade);
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	/** Todas as unidades, para montar a lista e o preenchimento dos formulários. */
	public List<Unidade> listar() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			return session.createQuery("from " + Unidade.class.getCanonicalName(), Unidade.class).getResultList();
		} finally {
			session.close();
			sf.close();
		}
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