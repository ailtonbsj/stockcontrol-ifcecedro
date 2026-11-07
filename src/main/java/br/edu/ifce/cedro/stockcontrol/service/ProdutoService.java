package br.edu.ifce.cedro.stockcontrol.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.edu.ifce.cedro.stockcontrol.dao.ProdutoDAO;
import br.edu.ifce.cedro.stockcontrol.dao.UnidadeDAO;
import br.edu.ifce.cedro.stockcontrol.mapper.ProdutoMapper;
import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.model.Unidade;

public class ProdutoService {

    private final ProdutoDAO dao = new ProdutoDAO();
    private final UnidadeDAO unidadeDao = new UnidadeDAO();

    public List<Produto> findAll() {
        return dao.findAll();
    }

    public List<Unidade> findAllUnidades() {
        return unidadeDao.findAll();
    }

    public void findById(HttpServletRequest request, String id) {
        Produto produto = dao.findById(Integer.parseInt(id));
        request.setAttribute("unidades", findAllUnidades());
        ProdutoMapper.toRequest(produto, request);
    }

    public String validate(Integer id) {
        long movimentacoes = dao.countMovimentacoes(Integer.valueOf(id));
        if (movimentacoes > 0) {
            return "Este produto possui " + movimentacoes
                    + " movimentação(ões) de estoque e não pode ser excluído.";
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
        Produto produto = new Produto();
        ProdutoMapper.toModel(produto, request);
        dao.insert(produto);
    }

    public void update(HttpServletRequest request, String id) {
        Produto produto = new Produto();
        produto.setId(Integer.valueOf(id));
        ProdutoMapper.toModel(produto, request);
        dao.update(produto);
    }

}
