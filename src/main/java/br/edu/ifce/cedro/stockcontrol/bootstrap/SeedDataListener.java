package br.edu.ifce.cedro.stockcontrol.bootstrap;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.edu.ifce.cedro.stockcontrol.model.Entrada;
import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.model.Saida;
import br.edu.ifce.cedro.stockcontrol.model.Unidade;

/**
 * Popula o banco com dados fictícios para que a aplicação já abra com conteúdo
 * na primeira execução (basta um <code>docker compose up</code>).
 *
 * O povoamento só acontece quando o banco está completamente vazio, então
 * reiniciar o container não duplica nada e dados reais cadastrados pelo usuário
 * são preservados.
 */
@WebListener
public class SeedDataListener implements ServletContextListener {

	private static final Logger LOG = Logger.getLogger(SeedDataListener.class.getName());

	/** Configure APP_SEED=false no compose para iniciar com o banco vazio. */
	private static final String ENV_HABILITA = "APP_SEED";

	private static final long DIA = 24L * 60L * 60L * 1000L;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (!isHabilitado()) {
			LOG.info("Povoamento de dados fictícios desativado (" + ENV_HABILITA + "=false).");
			return;
		}

		SessionFactory sessionFactory = null;
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
			Session session = sessionFactory.openSession();

			try {
				session.beginTransaction();
				semear(session);
				session.getTransaction().commit();
			} catch (RuntimeException e) {
				session.getTransaction().rollback();
				throw e;
			} finally {
				session.close();
			}
		} catch (Exception e) {
			// Os dados fictícios são um conforto para demonstração: se o banco não
			// estiver disponível ainda, a aplicação precisa subir do mesmo jeito.
			LOG.log(Level.SEVERE, "Não foi possível popular o banco com dados fictícios.", e);
		} finally {
			if (sessionFactory != null) {
				sessionFactory.close();
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// Nada a liberar: a SessionFactory usada aqui já foi fechada.
	}

	private void semear(Session session) {
		if (!estaVazio(session)) {
			LOG.info("O banco já contém dados; povoamento ignorado.");
			return;
		}

		Unidade un = unidade(session, "UN", "Unidade");
		Unidade kg = unidade(session, "KG", "Quilograma");
		Unidade lt = unidade(session, "LT", "Litro");
		Unidade cx = unidade(session, "CX", "Caixa");
		Unidade pc = unidade(session, "PC", "Pacote");

		Produto cafe = produto(session, "Café torrado e moído", 10, kg);
		Produto acucar = produto(session, "Açúcar cristal", 20, kg);
		Produto copos = produto(session, "Copo descartável 200ml", 30, cx);
		Produto papel = produto(session, "Papel A4 75g (resma)", 15, un);
		Produto alcool = produto(session, "Álcool etílico 70% 1L", 12, lt);
		Produto detergente = produto(session, "Detergente neutro", 8, un);
		Produto sacos = produto(session, "Saco para lixo 100L", 5, pc);
		produto(session, "Grampo de papel", 4, cx);

		entrada(session, cafe, "Café do Cerrado Ltda", 60, diasAFrente(210), diasAtras(15), "Lote 2026-A");
		saida(session, cafe, "Secretaria Acadêmica", 45, diasAtras(11), "Atendimento aos laboratórios");

		entrada(session, acucar, "Usina Horizonte", 100, diasAFrente(300), diasAtras(20), "Compra mensal");
		saida(session, acucar, "Cozinha do Campus", 62, diasAtras(14), "Consumo do refeitório");

		entrada(session, copos, "Plásticos do Vale", 40, diasAFrente(500), diasAtras(8), "Reposição do estoque");
		saida(session, copos, "Coordenação de Eventos", 30, diasAtras(5), "Semana de acolhimento");

		entrada(session, papel, "Papelaria Central", 25, diasAFrente(730), diasAtras(30), "Reposição do arquivo");
		saida(session, papel, "Núcleo de Registro Acadêmico", 9, diasAtras(6), "Impressões do semestre");

		entrada(session, alcool, "Química Central", 18, diasAFrente(400), diasAtras(12), "Aquisição trimestral");
		saida(session, alcool, "Laboratório de Química", 14, diasAtras(4), "Aulas práticas");

		entrada(session, detergente, "Higiene-Pro", 12, diasAFrente(600), diasAtras(25), "Reposição da manutenção");
		saida(session, detergente, "Zeladoria", 3, diasAtras(3), "Limpeza do bloco B");

		entrada(session, sacos, "Pak Clean", 6, diasAFrente(540), diasAtras(18), "Reposição da coleta");
		saida(session, sacos, "Zeladoria", 1, diasAtras(7), "Coleta do refeitório");

		LOG.info("Dados fictícios carregados: unidades, produtos e movimentações de exemplo.");
	}

	/** O povoamento só acontece em um banco realmente vazio, para não misturar
	 *  dados fictícios com o que já foi cadastrado. */
	private boolean estaVazio(Session session) {
		return session.createQuery("select count(*) from Unidade", Long.class).uniqueResult() == 0
				&& session.createQuery("select count(*) from Produto", Long.class).uniqueResult() == 0
				&& session.createQuery("select count(*) from Entrada", Long.class).uniqueResult() == 0
				&& session.createQuery("select count(*) from Saida", Long.class).uniqueResult() == 0;
	}

	private Unidade unidade(Session session, String simbolo, String descricao) {
		Unidade unidade = new Unidade();
		unidade.setSimbolo(simbolo);
		unidade.setDescricao(descricao);
		session.save(unidade);
		return unidade;
	}

	private Produto produto(Session session, String nome, int minimo, Unidade unidade) {
		Produto produto = new Produto();
		produto.setNome(nome);
		produto.setMinimo(minimo);
		produto.setUnidade(unidade);
		// O saldo sai das movimentações registradas abaixo.
		produto.setQuantidade(0);
		session.save(produto);
		return produto;
	}

	private void entrada(Session session, Produto produto, String fornecedor, int quantidade, Date validade,
			Date data, String observacao) {
		Entrada entrada = new Entrada();
		entrada.setProduto(produto);
		entrada.setFornecedor(fornecedor);
		entrada.setQuantidade(quantidade);
		entrada.setValidade(validade);
		entrada.setData(data);
		entrada.setObservacao(observacao);
		session.save(entrada);

		produto.setQuantidade(produto.getQuantidade() + quantidade);
	}

	private void saida(Session session, Produto produto, String retirante, int quantidade, Date data,
			String observacao) {
		Saida saida = new Saida();
		saida.setProduto(produto);
		saida.setRetirante(retirante);
		saida.setQuantidade(quantidade);
		saida.setData(data);
		saida.setObservacao(observacao);
		session.save(saida);

		produto.setQuantidade(produto.getQuantidade() - quantidade);
	}

	private static Date diasAtras(int dias) {
		return new Date(System.currentTimeMillis() - dias * DIA);
	}

	private static Date diasAFrente(int dias) {
		return new Date(System.currentTimeMillis() + dias * DIA);
	}

	private boolean isHabilitado() {
		String valor = System.getenv(ENV_HABILITA);
		return valor == null || valor.isEmpty() || Boolean.parseBoolean(valor);
	}
}