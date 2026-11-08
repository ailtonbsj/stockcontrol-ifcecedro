package br.edu.ifce.cedro.stockcontrol.dao;

import java.util.List;

import org.hibernate.Session;
import br.edu.ifce.cedro.stockcontrol.util.HibernateUtil;

import br.edu.ifce.cedro.stockcontrol.model.Saida;

public class SaidaDAO {

	public List<Saida> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			return session.createQuery("from " + Saida.class.getCanonicalName(), Saida.class).getResultList();
		} finally {
			session.close();
		}
	}

	public Saida findById(Integer id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Saida saida = session.get(Saida.class, id);
		session.close();
		return saida;
	}

	public void deleteById(Integer id) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session.beginTransaction();
			session.remove(session.get(Saida.class, id));
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

	public void insert(Saida saida) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session.beginTransaction();
			session.persist(saida);
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

	public void update(Saida saida) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session.beginTransaction();
			session.merge(saida);
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

}
