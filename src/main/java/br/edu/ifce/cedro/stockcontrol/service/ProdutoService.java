package br.edu.ifce.cedro.stockcontrol.service;

import javax.servlet.http.HttpServletRequest;

import br.edu.ifce.cedro.stockcontrol.dao.ProdutoDAO;
import br.edu.ifce.cedro.stockcontrol.model.Produto;
import mapper.ProdutoMapper;

public class ProdutoService {

    private final ProdutoDAO dao = new ProdutoDAO();

    public void add(HttpServletRequest request) {
		Produto produto = new Produto();
		ProdutoMapper.toModel(produto, request);
		dao.salvar(produto);
	}

	public void update(HttpServletRequest request, String id) {
		try {
			Produto produto = new Produto();
			produto.setId(Integer.parseInt(id));
			ProdutoMapper.toModel(produto, request);

			dao.atualizar(produto);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public String verificaRemocao(String id) {
		long movimentacoes = dao.contarMovimentacoes(Integer.valueOf(id));

		if (movimentacoes > 0) {
			return "Este produto possui " + movimentacoes
					+ " movimentação(ões) de estoque e não pode ser excluído.";
		}

		return null;
	}

}