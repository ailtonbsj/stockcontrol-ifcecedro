package mapper;

import javax.servlet.http.HttpServletRequest;

import br.edu.ifce.cedro.stockcontrol.model.Entrada;
import br.edu.ifce.cedro.stockcontrol.model.Produto;

import static br.edu.ifce.cedro.stockcontrol.util.Util.dateToDateTimeLocal;
import static br.edu.ifce.cedro.stockcontrol.util.Util.dateTimeLocalToDate;

public class EntradaMapper {

	public static void toRequest(Entrada model, HttpServletRequest request) {
		request.setAttribute("id", model.getId().toString());
		request.setAttribute("produtoId", model.getProduto().getId());
		request.setAttribute("fornecedor", model.getFornecedor());
		request.setAttribute("quantidade", model.getQuantidade().toString());
		request.setAttribute("validade", dateToDateTimeLocal(model.getValidade()));
		request.setAttribute("data", dateToDateTimeLocal(model.getData()));
		request.setAttribute("observacao", model.getObservacao());
	}

	public static void toModel(Entrada model, HttpServletRequest request) {
		Produto p = new Produto();
		p.setId(Integer.parseInt(request.getParameter("produto")));
		model.setProduto(p);
		model.setFornecedor(request.getParameter("fornecedor"));
		model.setQuantidade(Integer.valueOf(request.getParameter("quantidade")));
		model.setValidade(dateTimeLocalToDate(request.getParameter("validade")));
		model.setData(dateTimeLocalToDate(request.getParameter("data")));
		model.setObservacao(request.getParameter("observacao"));
	}

}