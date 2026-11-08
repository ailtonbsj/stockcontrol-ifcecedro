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
					<form role="form" action="/produto" method="post">
						<div class="card-body">
							<div class="form-group">
								<label for="idNumber"><fmt:message key="field.id"/></label>
								<input name="id" type="text" class="form-control" id="idNumber" placeholder="<fmt:message key='field.id'/>"
									value="${id}" readonly="readonly">
							</div>
							<div class="form-group">
								<label for="nome"><fmt:message key="field.name"/></label>
								<input name="nome" type="text" class="form-control" id="nome" placeholder="<fmt:message key='field.name'/>"
									value="${nome}">
							</div>
							<div class="form-group">
								<label for="quantidade"><fmt:message key="field.quantity"/></label>
								<input name="quantidade" type="text" class="form-control" id="quantidade"
									placeholder="<fmt:message key='field.quantity'/>" value="${quantidade}" readonly="readonly">
							</div>
							<div class="form-group">
								<label for="minimo"><fmt:message key="field.minimum"/></label>
								<input name="minimo" type="text" class="form-control" id="minimo"
									placeholder="<fmt:message key='field.minimum'/>" value="${minimo}">
							</div>
							<div class="form-group">
								<label><fmt:message key="field.unit"/></label>
								<select name="unidade" class="form-control">
									<c:forEach items="${unidades}" var="unidade">
										<option value="${unidade.id}"
											<c:if test="${unidadeId == unidade.id}">selected="selected"</c:if>>
											${unidade.descricao} (${unidade.simbolo})</option>
									</c:forEach>
								</select>
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
</div>

<%@include file="../footer.jspf"%>
