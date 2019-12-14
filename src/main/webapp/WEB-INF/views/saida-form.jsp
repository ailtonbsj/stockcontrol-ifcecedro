<%@include file="../header.jspf"%>

<section class="content-header">
	<div class="container-fluid">
		<div class="row mb-2">
			<div class="col-sm-6">
				<h1>${viewName}</h1>
			</div>
			<div class="col-sm-6">
				<ol class="breadcrumb float-sm-right">
					<li class="breadcrumb-item"><a href="#">Inicio</a></li>
					<li class="breadcrumb-item active">${viewName}</li>
				</ol>
			</div>
		</div>
	</div>
	<!-- /.container-fluid -->
</section>

<!-- Main content -->
<section class="content">
	<div class="container-fluid">
		<div class="row">
			<!-- left column -->
			<div class="col-md-12">
				<!-- INICIO FORM -->
				<!-- general form elements -->
				<div class="card">
					<div class="card-header">
						<h3 class="card-title">${operacao}</h3>
					</div>
					<!-- /.card-header -->
					<!-- form start -->
					<form role="form" action="/entrada" method="post">
						<div class="card-body">
							<div class="form-group">
								<label for="idNumber">Id</label> <input name="id" type="text"
									class="form-control" id="idNumber" placeholder="Id"
									value="${id}" readonly="readonly">
							</div>
							<div class="form-group">
								<label>Produto</label> <select name="produto"
									class="form-control">
									<c:forEach items="${produtos}" var="produto">
										<option value="${produto.id}"
											<c:if test="${produtoId == produto.id}">selected="selected"</c:if>>
											${produto.nome}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label for="retirante">Retirante</label> <input
									name="retirante" type="text" class="form-control"
									id="retirante" placeholder="Retirante" value="${retirante}">
							</div>
							<div class="form-group">
								<label for="quantidade">Quantidade</label> <input
									name="quantidade" type="text" class="form-control"
									id="quantidade" placeholder="Quantidade" value="${quantidade}">
							</div>
							<div class="form-group">
								<label for="data">Data de saida</label>
								<div class="input-group">
									<input type="datetime-local" class="form-control float-right"
										id="data" placeholder="Validade" value="${data}">
									<div class="input-group-append">
										<span class="input-group-text"> <i
											class="far fa-calendar-alt"></i>
										</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="observacao">Observação</label> <input
									name="observacao" type="text" class="form-control"
									id="observacao" placeholder="Observação" value="${observacao}">
							</div>
							
							<!-- 							<div class="form-group"> -->
<!-- 								<div class="input-group date" id="datetimepicker2" -->
<!-- 									data-target-input="nearest"> -->
<!-- 									<input type="text" class="form-control datetimepicker-input" -->
<!-- 										data-target="#datetimepicker2" /> -->
<!-- 									<div class="input-group-append" data-target="#datetimepicker2" -->
<!-- 										data-toggle="datetimepicker"> -->
<!-- 										<div class="input-group-text"> -->
<!-- 											<i class="fa fa-calendar"></i> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</div> -->

						</div>
						<!-- /.card-body -->

						<div class="card-footer">
							<button type="submit" class="btn btn-primary">Salvar</button>
						</div>
					</form>
				</div>
				<!-- /.card -->
				<!-- FIM FORM -->
			</div>
			<!--/.col (left) -->
		</div>
		<!-- /.row -->
	</div>
	<!-- /.container-fluid -->
</section>
<!-- /.content -->

<script type="text/javascript">
	$(function() {
		$('#datetimepicker2').datetimepicker({
			locale : 'pt-br'
		});
	});
</script>

</div>

<%@include file="../footer.jspf"%>