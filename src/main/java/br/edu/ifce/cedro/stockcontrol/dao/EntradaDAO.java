package br.edu.ifce.cedro.stockcontrol.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.model.Entrada;

/**
 * Acesso aos dados da entrada de estoque. O {@code EntradaController} cuida da
 * requisição e da apresentação; toda a conversa com o Hibernate fica aqui.
 *
 * As operações de escrita abrem e fecham a própria sessão, para que o
 * controller cuide só da requisição e da apresentação.
 */
public class EntradaDAO {

	/**
	 * Grava uma nova entrada, abrindo e fechando a própria sessão para que o
	 * controller não precise conhecer o Hibernate.
	 */
	public void salvar(Entrada entrada) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.save(entrada);
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	/**
	 * Grava as alterações de uma entrada já existente. A instância chega
	 * desanexada, com a chave preenchida, e é mesclada na sessão que o próprio DAO
	 * abre: o controller não precisa carregar o registro antes de gravar.
	 */
	public void atualizar(Entrada entrada) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.merge(entrada);
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	/**
	 * Busca a entrada pela chave dentro da sessão corrente. Por usar a sessão que já
	 * está aberta, o Hibernate devolve a instância já carregada, sem nova consulta.
	 */
	public Entrada buscarPorId(Session session, Integer id) {
		return session.get(Entrada.class, id);
	}

	/** Todas as entradas, para montar a lista da tela. */
	public List<Entrada> listar() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			return session.createQuery("from " + Entrada.class.getCanonicalName(), Entrada.class).getResultList();
		} finally {
			session.close();
			sf.close();
		}
	}
}