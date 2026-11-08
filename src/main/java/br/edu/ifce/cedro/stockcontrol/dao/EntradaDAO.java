package br.edu.ifce.cedro.stockcontrol.dao;

import java.util.List;

import org.hibernate.Session;
import br.edu.ifce.cedro.stockcontrol.util.HibernateUtil;

import br.edu.ifce.cedro.stockcontrol.model.Entrada;

public class EntradaDAO {

	public List<Entrada> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			return session.createQuery("from " + Entrada.class.getCanonicalName(), Entrada.class).getResultList();
		} finally {
			session.close();
		}
	}

	public Entrada findById(Integer id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Entrada entrada = session.get(Entrada.class, id);
		session.close();
		return entrada;
	}

	public void deleteById(Integer id) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session.beginTransaction();
			session.remove(session.get(Entrada.class, id));
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

	public void insert(Entrada entrada) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session.beginTransaction();
			session.persist(entrada);
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

	public void update(Entrada entrada) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session.beginTransaction();
			session.merge(entrada);
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

}
