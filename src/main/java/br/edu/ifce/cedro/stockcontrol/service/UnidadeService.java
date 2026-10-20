package br.edu.ifce.cedro.stockcontrol.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.edu.ifce.cedro.stockcontrol.dao.UnidadeDAO;
import br.edu.ifce.cedro.stockcontrol.model.Unidade;
import mapper.UnidadeMapper;

public class UnidadeService {

    private final UnidadeDAO dao = new UnidadeDAO();

	public List<Unidade> findAll() {
		return dao.findAll();
	}

	public void findById(HttpServletRequest request, String id) {
		Unidade unidade = dao.findById(Integer.parseInt(id));
		UnidadeMapper.toRequest(unidade, request);
	}

	public String validate(Integer id) {
		long count = dao.countProduto(Integer.valueOf(id));
		if (count > 0) {
			return "Esta unidade está em uso por " + count + " produto(s) e não pode ser excluída.";
		}
		return null;
	}

	public void deleteById(HttpServletResponse response, Integer id, String redirectUrl) {
		try {
			String invalidMessage = this.validate(id);
			if (invalidMessage != null) {
				response.sendRedirect(redirectUrl + "?erro=" + URLEncoder.encode(invalidMessage, "UTF-8"));
				return;
			}
			dao.deleteById(id);
			response.sendRedirect(redirectUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void insert(HttpServletRequest request) {
		Unidade unidade = new Unidade();
		UnidadeMapper.toModel(unidade, request);
		dao.insert(unidade);
	}

	public void update(HttpServletRequest request, String id) {
		Unidade unidade = new Unidade();
		unidade.setId(Integer.valueOf(id));
		UnidadeMapper.toModel(unidade, request);
		dao.update(unidade);
	}

}
