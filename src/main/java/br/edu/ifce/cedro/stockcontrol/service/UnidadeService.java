package br.edu.ifce.cedro.stockcontrol.service;

import javax.servlet.http.HttpServletRequest;

import br.edu.ifce.cedro.stockcontrol.dao.UnidadeDAO;
import br.edu.ifce.cedro.stockcontrol.model.Unidade;
import mapper.UnidadeMapper;

public class UnidadeService {

    private final UnidadeDAO dao = new UnidadeDAO();

    public void add(HttpServletRequest request) {
		Unidade unidade = new Unidade();
		UnidadeMapper.toModel(unidade, request);
		dao.salvar(unidade);
	}

	public void update(HttpServletRequest request, String id) {
		try {
			Unidade unidade = new Unidade();
			unidade.setId(Integer.parseInt(id));
			UnidadeMapper.toModel(unidade, request);

			dao.atualizar(unidade);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public void findById(HttpServletRequest request, String id) {
		Unidade unidade = dao.findById(Integer.parseInt(id));
		request.setAttribute("operacao", "Atualizar");
		UnidadeMapper.toRequest(unidade, request);
	}

	public String verificaRemocao(String id) {
		long produtos = dao.contarProdutos(Integer.valueOf(id));

		if (produtos > 0) {
			return "Esta unidade está em uso por " + produtos + " produto(s) e não pode ser excluída.";
		}

		return null;
	}

}
