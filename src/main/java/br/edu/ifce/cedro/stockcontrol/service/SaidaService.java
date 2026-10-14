package br.edu.ifce.cedro.stockcontrol.service;

import javax.servlet.http.HttpServletRequest;

import br.edu.ifce.cedro.stockcontrol.dao.ProdutoDAO;
import br.edu.ifce.cedro.stockcontrol.dao.SaidaDAO;
import br.edu.ifce.cedro.stockcontrol.model.Saida;
import mapper.SaidaMapper;

public class SaidaService {

    private final SaidaDAO dao = new SaidaDAO();
    private final ProdutoDAO produtoDao = new ProdutoDAO();

    public void add(HttpServletRequest request) {
		Saida saida = new Saida();
		SaidaMapper.toModel(saida, request);
		dao.salvar(saida);
		produtoDao.recalcularSaldo(saida.getProduto().getId());
	}

	public void update(HttpServletRequest request, String id) {
		try {
			Saida saida = new Saida();
			saida.setId(Integer.parseInt(id));
			SaidaMapper.toModel(saida, request);

			dao.atualizar(saida);

			// O saldo segue o produto que veio do formulário: é essa a referência que
			// o mapper acabou de gravar na movimentação.
			produtoDao.recalcularSaldo(saida.getProduto().getId());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saída não tem restrição de exclusão: apagar uma só faz o saldo do produto
	 * voltar a ser maior, nunca deixa referência órfã.
	 */
	public String verificaRemocao(String id) {
		return null;
	}

}