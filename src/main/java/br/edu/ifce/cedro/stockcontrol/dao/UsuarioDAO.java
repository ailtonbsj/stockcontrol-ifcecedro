package br.edu.ifce.cedro.stockcontrol.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.model.Usuario;

public class UsuarioDAO {

	public Usuario findByLogin(String login) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			return session.createQuery(
					"from " + Usuario.class.getCanonicalName() + " where login = :login", Usuario.class)
					.setParameter("login", login).uniqueResult();
		} finally {
			session.close();
			sf.close();
		}
	}

	public long count() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			return session.createQuery("select count(*) from " + Usuario.class.getCanonicalName(), Long.class)
					.uniqueResult();
		} finally {
			session.close();
			sf.close();
		}
	}

	public void insert(Usuario usuario) {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			session.beginTransaction();
			session.save(usuario);
			session.getTransaction().commit();
		} finally {
			session.close();
			sf.close();
		}
	}
}
