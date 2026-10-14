package br.edu.ifce.cedro.stockcontrol.service;

import javax.servlet.http.HttpServletRequest;

import br.edu.ifce.cedro.stockcontrol.dao.EntradaDAO;
import br.edu.ifce.cedro.stockcontrol.dao.ProdutoDAO;
import br.edu.ifce.cedro.stockcontrol.model.Entrada;
import mapper.EntradaMapper;

public class EntradaService {

    private final EntradaDAO dao = new EntradaDAO();
    private final ProdutoDAO produtoDao = new ProdutoDAO();

    public void add(HttpServletRequest request) {
		Entrada entrada = new Entrada();
		EntradaMapper.toModel(entrada, request);
		dao.salvar(entrada);
		produtoDao.recalcularSaldo(entrada.getProduto().getId());
	}

	public void update(HttpServletRequest request, String id) {
		try {
			Entrada entrada = new Entrada();
			entrada.setId(Integer.parseInt(id));
			EntradaMapper.toModel(entrada, request);

			dao.atualizar(entrada);

			// O saldo segue o produto que veio do formulário: é essa a referência que
			// o mapper acabou de gravar na movimentação.
			produtoDao.recalcularSaldo(entrada.getProduto().getId());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Entrada não tem restrição de exclusão: é ela que origina o saldo do produto,
	 * então apagar uma não deixa referência órfã.
	 */
	public String verificaRemocao(String id) {
		return null;
	}

}