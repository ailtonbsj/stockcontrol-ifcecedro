package br.edu.ifce.cedro.stockcontrol.util;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import br.edu.ifce.cedro.stockcontrol.model.Entrada;
import br.edu.ifce.cedro.stockcontrol.model.Estoque;
import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.model.Saida;
import br.edu.ifce.cedro.stockcontrol.model.Unidade;
import br.edu.ifce.cedro.stockcontrol.model.Usuario;

public final class HibernateUtil {

	private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		Map<String, Object> settings = new HashMap<>();
		settings.put("hibernate.connection.driver_class", env("HIBERNATE_CONNECTION_DRIVER_CLASS", "com.mysql.cj.jdbc.Driver"));
		settings.put("hibernate.connection.url", env("HIBERNATE_CONNECTION_URL",
				"jdbc:mysql://db:3306/stockcontrol-ifce?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8"));
		settings.put("hibernate.connection.username", env("HIBERNATE_CONNECTION_USERNAME", "root"));
		settings.put("hibernate.connection.password", env("HIBERNATE_CONNECTION_PASSWORD", "root"));
		settings.put("hibernate.dialect", env("HIBERNATE_DIALECT", "org.hibernate.dialect.MySQLDialect"));
		settings.put("hibernate.hbm2ddl.auto", env("HIBERNATE_HBM2DDL_AUTO", "update"));
		settings.put("hibernate.show_sql", env("HIBERNATE_SHOW_SQL", "true"));
		settings.put("hibernate.format_sql", env("HIBERNATE_FORMAT_SQL", "false"));

		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(settings).build();
		try {
			return new MetadataSources(registry)
					.addAnnotatedClass(Unidade.class)
					.addAnnotatedClass(Produto.class)
					.addAnnotatedClass(Estoque.class)
					.addAnnotatedClass(Entrada.class)
					.addAnnotatedClass(Saida.class)
					.addAnnotatedClass(Usuario.class)
					.buildMetadata()
					.buildSessionFactory();
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(registry);
			throw new ExceptionInInitializerError(e);
		}
	}

	private static String env(String name, String defaultValue) {
		String value = System.getenv(name);
		return (value == null || value.isEmpty()) ? defaultValue : value;
	}

	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}

	private HibernateUtil() {
	}
}
