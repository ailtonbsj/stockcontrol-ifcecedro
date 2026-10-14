package br.edu.ifce.cedro.stockcontrol.service;

import javax.servlet.http.HttpServletRequest;

import br.edu.ifce.cedro.stockcontrol.dao.ProdutoDAO;
import br.edu.ifce.cedro.stockcontrol.dao.UnidadeDAO;
import br.edu.ifce.cedro.stockcontrol.model.Produto;
import mapper.ProdutoMapper;

public class ProdutoService {

    private final ProdutoDAO dao = new ProdutoDAO();
	private final UnidadeDAO unidadeDao = new UnidadeDAO();

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

	public void findById(HttpServletRequest request, String id) {
		Produto produto = dao.findById(Integer.parseInt(id));
		request.setAttribute("operacao", "Atualizar");
		request.setAttribute("unidades", unidadeDao.listar());
		ProdutoMapper.toRequest(produto, request);
	}

}