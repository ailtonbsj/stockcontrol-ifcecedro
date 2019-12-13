<%@include file="../header.jspf"  %>

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
    </div><!-- /.container-fluid -->
</section>

<!-- Main content -->
<section class="content">
    <div class="container-fluid">
        <div class="row">
            <!-- left column -->
            <div class="col-md-12">
                <!-- INICIO TABELA -->
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">${operacao}</h3>
                    </div>
                    <!-- /.card-header -->
                    <div class="card-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th style="width: 10px">#</th>
                                    <th>Produto</th>
                                    <th>Fornecedor</th>
                                    <th>Quantidade</th>
                                    <th>Validade</th>
                                    <th>Data de entrada</th>
                                    <th>Observação</th>
                                    <th style="width: 132px"></th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach items="${Entradas}" var="entrada">
                                <tr>
                                    <td>${entrada.id}</td>
                                    <td>${entrada.produto.nome}</td>
                                    <td>${entrada.fornecedor}</td>
                                    <td>${entrada.quantidade} ${entrada.produto.unidade.simbolo}</td>
                                    <td>${entrada.validade}</td>
                                    <td>${entrada.data}</td>
                                    <td>${entrada.observacao}</td>
                                    <td>
                                    	<a class="badge badge-success" href="/entrada?up=${entrada.id}">Atualizar</a>
                                    	<a class="badge badge-danger" href="/entrada?del=${entrada.id}">Excluir</a>
                                    </td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <!-- /.card-body -->
                    <div class="card-footer clearfix">
                        <!-- <ul class="pagination pagination-sm m-0 float-right">
                            <li class="page-item"><a class="page-link" href="#">&laquo;</a></li>
                            <li class="page-item"><a class="page-link" href="#">1</a></li>
                            <li class="page-item"><a class="page-link" href="#">2</a></li>
                            <li class="page-item"><a class="page-link" href="#">3</a></li>
                            <li class="page-item"><a class="page-link" href="#">&raquo;</a></li>
                        </ul>  -->
                    </div>
                </div>
                <!-- /.card -->
                <!-- FIM TABELA -->
            </div>
            <!--/.col (left) -->
        </div>
        <!-- /.row -->
    </div><!-- /.container-fluid -->
</section>
<!-- /.content -->
</div>

<%@include file="../footer.jspf"  %>