package br.edu.ifce.cedro.stockcontrol.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.model.Unidade;

public class UnidadeDAO {

	public List<Unidade> findAll() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			return session.createQuery("from " + Unidade.class.getCanonicalName(), Unidade.class).getResultList();
		} finally {
			session.close();
			sf.close();
		}
	}

	public Unidade findById(Integer id) {
		Session session = (new Configuration().configure().buildSessionFactory()).openSession();
		Unidade unidade = session.get(Unidade.class, id);
		session.close();
		return unidade;
	}

	public long countProduto(Integer id) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			return session.createQuery(
					"select count(*) from " + Produto.class.getCanonicalName() + " where unidade.id = :id", Long.class)
					.setParameter("id", id).getSingleResultOrNull();
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
			session.remove(session.get(Unidade.class, id));
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	public void insert(Unidade unidade) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			session.beginTransaction();
			session.persist(unidade);
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	public void update(Unidade unidade) {
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

}