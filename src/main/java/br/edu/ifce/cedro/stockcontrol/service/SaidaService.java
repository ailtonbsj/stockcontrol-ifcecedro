package br.edu.ifce.cedro.stockcontrol.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.edu.ifce.cedro.stockcontrol.dao.ProdutoDAO;
import br.edu.ifce.cedro.stockcontrol.dao.SaidaDAO;
import br.edu.ifce.cedro.stockcontrol.mapper.SaidaMapper;
import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.model.Saida;

public class SaidaService {

    private final SaidaDAO dao = new SaidaDAO();
    private final ProdutoDAO produtoDao = new ProdutoDAO();

    public List<Saida> findAll() {
        return dao.findAll();
    }

    public List<Produto> findAllProdutos() {
        return produtoDao.findAll();
    }

    public void findById(HttpServletRequest request, String id) {
        Saida saida = dao.findById(Integer.parseInt(id));
        request.setAttribute("produtos", findAllProdutos());
        SaidaMapper.toRequest(saida, request);
    }

    public String validate(Integer id) {
        return null;
    }

    public void deleteById(HttpServletResponse response, Integer id, String redirectUrl) {
        try {
            String invalidMessage = this.validate(id);
            if (invalidMessage != null) {
                response.sendRedirect(redirectUrl + "?erro=" + URLEncoder.encode(invalidMessage, "UTF-8"));
                return;
            }
            // Lido antes da exclusão: é o produto cujo saldo será recalculado.
            Integer produtoId = dao.findById(id).getProduto().getId();
            dao.deleteById(id);
            produtoDao.recalcularSaldo(produtoId);
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insert(HttpServletRequest request) {
        Saida saida = new Saida();
        SaidaMapper.toModel(saida, request);
        dao.insert(saida);
        produtoDao.recalcularSaldo(saida.getProduto().getId());
    }

    public void update(HttpServletRequest request, String id) {
        Saida saida = new Saida();
        saida.setId(Integer.valueOf(id));
        SaidaMapper.toModel(saida, request);
        dao.update(saida);
        // O saldo segue o produto que veio do formulário.
        produtoDao.recalcularSaldo(saida.getProduto().getId());
    }

}
