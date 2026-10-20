package br.edu.ifce.cedro.stockcontrol.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.model.Entrada;

public class EntradaDAO {

	public List<Entrada> findAll() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			return session.createQuery("from " + Entrada.class.getCanonicalName(), Entrada.class).getResultList();
		} finally {
			session.close();
			sf.close();
		}
	}

	public Entrada findById(Integer id) {
		Session session = (new Configuration().configure().buildSessionFactory()).openSession();
		Entrada entrada = session.get(Entrada.class, id);
		session.close();
		return entrada;
	}

	public void deleteById(Integer id) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.beginTransaction();
			session.remove(session.get(Entrada.class, id));
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}

	public void insert(Entrada entrada) {
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

	public void update(Entrada entrada) {
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

}
