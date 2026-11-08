<%@include file="../header.jspf"%>

<section class="content-header">
	<div class="container-fluid">
		<div class="row mb-2">
			<div class="col-sm-6">
				<h1><fmt:message key="${viewName}"/></h1>
			</div>
			<div class="col-sm-6">
				<ol class="breadcrumb float-sm-right">
					<li class="breadcrumb-item"><a href="/"><fmt:message key="nav.home"/></a></li>
					<li class="breadcrumb-item active"><fmt:message key="${viewName}"/></li>
				</ol>
			</div>
		</div>
	</div>
</section>

<section class="content">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="card-header">
						<h3 class="card-title"><fmt:message key="${operacao}"/></h3>
					</div>
					<form role="form" action="/entrada" method="post">
						<div class="card-body">
							<div class="form-group">
								<label for="idNumber"><fmt:message key="field.id"/></label>
								<input name="id" type="text" class="form-control" id="idNumber" placeholder="<fmt:message key='field.id'/>"
									value="${id}" readonly="readonly">
							</div>
							<div class="form-group">
								<label><fmt:message key="field.product"/></label>
								<select name="produto" class="form-control">
									<c:forEach items="${produtos}" var="produto">
										<option value="${produto.id}"
											<c:if test="${produtoId == produto.id}">selected="selected"</c:if>>
											${produto.nome} (${produto.unidade.descricao})</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label for="fornecedor"><fmt:message key="field.supplier"/></label>
								<input name="fornecedor" type="text" class="form-control" id="fornecedor"
									placeholder="<fmt:message key='field.supplier'/>" value="${fornecedor}">
							</div>
							<div class="form-group">
								<label for="quantidade"><fmt:message key="field.quantity"/></label>
								<input name="quantidade" type="text" class="form-control" id="quantidade"
									placeholder="<fmt:message key='field.quantity'/>" value="${quantidade}">
							</div>
							<div class="form-group">
								<label for="validade"><fmt:message key="field.validity"/></label>
								<div class="input-group">
									<input name="validade" type="datetime-local" class="form-control float-right"
										id="validade" placeholder="<fmt:message key='field.validity'/>" value="${validade}">
									<div class="input-group-append">
										<span class="input-group-text"><i class="far fa-calendar-alt"></i></span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="data"><fmt:message key="field.entryDate"/></label>
								<div class="input-group">
									<input name="data" type="datetime-local" class="form-control float-right"
										id="data" placeholder="<fmt:message key='field.entryDate'/>" value="${data}">
									<div class="input-group-append">
										<span class="input-group-text"><i class="far fa-calendar-alt"></i></span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="observacao"><fmt:message key="field.observation"/></label>
								<input name="observacao" type="text" class="form-control" id="observacao"
									placeholder="<fmt:message key='field.observation'/>" value="${observacao}">
							</div>
						</div>
						<div class="card-footer">
							<button type="submit" class="btn btn-primary"><fmt:message key="op.save"/></button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</section>

<script type="text/javascript">
	$(function() {
		$('#datetimepicker2').datetimepicker({
			locale : '${sessionScope.appLocaleBase == "en" ? "en" : "pt-br"}'
		});
	});
</script>

</div>

<%@include file="../footer.jspf"%>
