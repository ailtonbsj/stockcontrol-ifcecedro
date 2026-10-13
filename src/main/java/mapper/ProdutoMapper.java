package mapper;

import javax.servlet.http.HttpServletRequest;
import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.model.Unidade;

public class ProdutoMapper {

    public static void toRequest(Produto model, HttpServletRequest request) {
        request.setAttribute("id", model.getId().toString());
		request.setAttribute("nome", model.getNome());
		request.setAttribute("quantidade", model.getQuantidade().toString());
		request.setAttribute("minimo", model.getMinimo().toString());
		request.setAttribute("unidadeId", model.getUnidade().getId().toString());
    }

	public static void toModel(Produto model, HttpServletRequest request) {
		model.setNome(request.getParameter("nome"));
		model.setQuantidade(Integer.valueOf(request.getParameter("quantidade")));
		model.setMinimo(Integer.valueOf(request.getParameter("minimo")));
		Unidade unidade = new Unidade();
		unidade.setId(Integer.valueOf(request.getParameter("unidade")));
		model.setUnidade(unidade);
	}

}
