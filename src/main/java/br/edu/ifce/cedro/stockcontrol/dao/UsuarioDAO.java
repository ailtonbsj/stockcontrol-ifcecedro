package br.edu.ifce.cedro.stockcontrol.dao;

import org.hibernate.Session;
import br.edu.ifce.cedro.stockcontrol.util.HibernateUtil;

import br.edu.ifce.cedro.stockcontrol.model.Usuario;

public class UsuarioDAO {

	public Usuario findByLogin(String login) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			return session.createQuery(
					"from " + Usuario.class.getCanonicalName() + " where login = :login", Usuario.class)
					.setParameter("login", login).getSingleResultOrNull();
		} finally {
			session.close();
		}
	}

	public long count() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			return session.createQuery("select count(*) from " + Usuario.class.getCanonicalName(), Long.class)
					.getSingleResultOrNull();
		} finally {
			session.close();
		}
	}

	public void insert(Usuario usuario) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			session.persist(usuario);
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}
}
