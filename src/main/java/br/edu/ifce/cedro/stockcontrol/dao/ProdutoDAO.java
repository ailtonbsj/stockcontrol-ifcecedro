package br.edu.ifce.cedro.stockcontrol.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.model.Entrada;
import br.edu.ifce.cedro.stockcontrol.model.Estoque;
import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.model.Saida;

public class ProdutoDAO {

	public List<Produto> findAll() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			return session.createQuery("from " + Produto.class.getCanonicalName(), Produto.class).getResultList();
		} finally {
			session.close();
			sf.close();
		}
	}

	public Produto findById(Integer id) {
		Session session = (new Configuration().configure().buildSessionFactory()).openSession();
		Produto produto = session.get(Produto.class, id);
		session.close();
		return produto;
	}

	public long countMovimentacoes(Integer id) {
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

	public void deleteById(Integer id) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.remove(session.get(Produto.class, id));
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	public void insert(Produto produto) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.save(produto);
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	public void update(Produto produto) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.merge(produto);
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	public void recalcularSaldo(Integer produtoId) {
		Session session = (new Configuration().configure().buildSessionFactory()).openSession();

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

		session.close();
	}
}
