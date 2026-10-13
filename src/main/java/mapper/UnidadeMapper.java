package mapper;

import javax.servlet.http.HttpServletRequest;
import br.edu.ifce.cedro.stockcontrol.model.Unidade;

public class UnidadeMapper {

    public static void toRequest(Unidade model, HttpServletRequest request) {
		request.setAttribute("id", model.getId().toString());
		request.setAttribute("simbolo", model.getSimbolo());
		request.setAttribute("descricao", model.getDescricao());
    }

	public static void toModel(Unidade model, HttpServletRequest request) {
        model.setSimbolo(request.getParameter("simbolo"));
		model.setDescricao(request.getParameter("descricao"));
	}

}
