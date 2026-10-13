package br.edu.ifce.cedro.stockcontrol.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.model.Saida;

/**
 * Acesso aos dados da saída de estoque. O {@code SaidaController} cuida da
 * requisição e da apresentação; toda a conversa com o Hibernate fica aqui.
 *
 * As operações de escrita abrem e fecham a própria sessão, para que o
 * controller cuide só da requisição e da apresentação.
 */
public class SaidaDAO {

	/**
	 * Grava uma nova saída, abrindo e fechando a própria sessão para que o
	 * controller não precise conhecer o Hibernate.
	 */
	public void salvar(Saida saida) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.save(saida);
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	/**
	 * Grava as alterações de uma saída já existente. A instância chega desanexada,
	 * com a chave preenchida, e é mesclada na sessão que o próprio DAO abre: o
	 * controller não precisa carregar o registro antes de gravar.
	 */
	public void atualizar(Saida saida) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.merge(saida);
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	/**
	 * Busca a saída pela chave dentro da sessão corrente. Por usar a sessão que já
	 * está aberta, o Hibernate devolve a instância já carregada, sem nova consulta.
	 */
	public Saida buscarPorId(Session session, Integer id) {
		return session.get(Saida.class, id);
	}

	/** Todas as saídas, para montar a lista da tela. */
	public List<Saida> listar() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			return session.createQuery("from " + Saida.class.getCanonicalName(), Saida.class).getResultList();
		} finally {
			session.close();
			sf.close();
		}
	}
}