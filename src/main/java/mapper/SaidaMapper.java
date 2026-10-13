package mapper;

import javax.servlet.http.HttpServletRequest;

import br.edu.ifce.cedro.stockcontrol.model.Produto;
import br.edu.ifce.cedro.stockcontrol.model.Saida;

import static br.edu.ifce.cedro.stockcontrol.util.Util.dateToDateTimeLocal;
import static br.edu.ifce.cedro.stockcontrol.util.Util.dateTimeLocalToDate;

public class SaidaMapper {

	public static void toRequest(Saida model, HttpServletRequest request) {
		request.setAttribute("id", model.getId().toString());
		request.setAttribute("produtoId", model.getProduto().getId());
		request.setAttribute("retirante", model.getRetirante());
		request.setAttribute("quantidade", model.getQuantidade().toString());
		request.setAttribute("data", dateToDateTimeLocal(model.getData()));
		request.setAttribute("observacao", model.getObservacao());
	}

	public static void toModel(Saida model, HttpServletRequest request) {
		Produto p = new Produto();
		p.setId(Integer.parseInt(request.getParameter("produto")));
		model.setProduto(p);
		model.setRetirante(request.getParameter("retirante"));
		model.setQuantidade(Integer.valueOf(request.getParameter("quantidade")));
		model.setData(dateTimeLocalToDate(request.getParameter("data")));
		model.setObservacao(request.getParameter("observacao"));
	}

}