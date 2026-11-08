package br.edu.ifce.cedro.stockcontrol.dao;

import java.util.List;

import org.hibernate.Session;
import br.edu.ifce.cedro.stockcontrol.util.HibernateUtil;

import br.edu.ifce.cedro.stockcontrol.model.Entrada;
import br.edu.ifce.cedro.stockcontrol.model.Estoque;
import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.model.Saida;

public class ProdutoDAO {

	public List<Produto> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			return session.createQuery("from " + Produto.class.getCanonicalName(), Produto.class).getResultList();
		} finally {
			session.close();
		}
	}

	public Produto findById(Integer id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Produto produto = session.get(Produto.class, id);
		session.close();
		return produto;
	}

	public long countMovimentacoes(Integer id) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			return session.createQuery(
					"select count(*) from " + Estoque.class.getCanonicalName() + " where produto.id = :id", Long.class)
					.setParameter("id", id).getSingleResultOrNull();
		} finally {
			session.close();
		}
	}

	public void deleteById(Integer id) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session.beginTransaction();
			session.remove(session.get(Produto.class, id));
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

	public void insert(Produto produto) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session.beginTransaction();
			session.persist(produto);
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

	public void update(Produto produto) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session.beginTransaction();
			session.merge(produto);
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

	public void recalcularSaldo(Integer produtoId) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Produto produto = session.get(Produto.class, produtoId);

		Long entradas = session
				.createQuery("select sum(e.quantidade) from " + Entrada.class.getCanonicalName()
						+ " e where e.produto.id = :id", Long.class)
				.setParameter("id", produtoId).getSingleResultOrNull();

		Long saidas = session
				.createQuery("select sum(s.quantidade) from " + Saida.class.getCanonicalName()
						+ " s where s.produto.id = :id", Long.class)
				.setParameter("id", produtoId).getSingleResultOrNull();

		produto.setQuantidade((int) ((entradas == null ? 0L : entradas) - (saidas == null ? 0L : saidas)));

		session.beginTransaction();
		session.persist(produto);
		session.getTransaction().commit();

		session.close();
	}

	public List<Produto> findAbaixoMinimo() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			return session.createQuery(
					"select distinct p from " + Produto.class.getCanonicalName()
							+ " p left join fetch p.unidade where p.minimo is not null and coalesce(p.quantidade, 0) < p.minimo order by p.nome",
					Produto.class).getResultList();
		} finally {
			session.close();
		}
	}
}
