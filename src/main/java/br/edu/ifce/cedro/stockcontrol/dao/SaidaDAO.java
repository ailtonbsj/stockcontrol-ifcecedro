package br.edu.ifce.cedro.stockcontrol.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.model.Saida;

public class SaidaDAO {

	public List<Saida> findAll() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			return session.createQuery("from " + Saida.class.getCanonicalName(), Saida.class).getResultList();
		} finally {
			session.close();
			sf.close();
		}
	}

	public Saida findById(Integer id) {
		Session session = (new Configuration().configure().buildSessionFactory()).openSession();
		Saida saida = session.get(Saida.class, id);
		session.close();
		return saida;
	}

	public void deleteById(Integer id) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.remove(session.get(Saida.class, id));
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	public void insert(Saida saida) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.persist(saida);
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	public void update(Saida saida) {
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

}
