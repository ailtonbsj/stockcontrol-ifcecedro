package br.edu.ifce.cedro.stockcontrol.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.edu.ifce.cedro.stockcontrol.dao.EntradaDAO;
import br.edu.ifce.cedro.stockcontrol.dao.ProdutoDAO;
import br.edu.ifce.cedro.stockcontrol.mapper.EntradaMapper;
import br.edu.ifce.cedro.stockcontrol.model.Entrada;
import br.edu.ifce.cedro.stockcontrol.model.Produto;

public class EntradaService {

    private final EntradaDAO dao = new EntradaDAO();
    private final ProdutoDAO produtoDao = new ProdutoDAO();

    public List<Entrada> findAll() {
        return dao.findAll();
    }

    public List<Produto> findAllProdutos() {
        return produtoDao.findAll();
    }

    public void findById(HttpServletRequest request, String id) {
        Entrada entrada = dao.findById(Integer.parseInt(id));
        request.setAttribute("produtos", findAllProdutos());
        EntradaMapper.toRequest(entrada, request);
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
        Entrada entrada = new Entrada();
        EntradaMapper.toModel(entrada, request);
        dao.insert(entrada);
        produtoDao.recalcularSaldo(entrada.getProduto().getId());
    }

    public void update(HttpServletRequest request, String id) {
        Entrada entrada = new Entrada();
        entrada.setId(Integer.valueOf(id));
        EntradaMapper.toModel(entrada, request);
        dao.update(entrada);
        // O saldo segue o produto que veio do formulário.
        produtoDao.recalcularSaldo(entrada.getProduto().getId());
    }

}
