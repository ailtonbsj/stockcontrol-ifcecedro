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
                                    <th>Retirante</th>
                                    <th>Quantidade</th>
                                    <th>Data de saída</th>
                                    <th>Observação</th>
                                    <th style="width: 132px"></th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach items="${Saidas}" var="saida">
                                <tr>
                                    <td>${saida.id}</td>
                                    <td>${saida.produto.nome}</td>
                                    <td>${saida.retirante}</td>
                                    <td>${saida.quantidade} ${saida.produto.unidade.simbolo}</td>
                                    <td>                                    
                                    <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${saida.data}" />
         							</td>
                                    <td>${saida.observacao}</td>
                                    <td>
                                    	<a class="badge badge-success" href="/saida?up=${saida.id}">Atualizar</a>
                                    	<a class="badge badge-danger" href="/saida?del=${saida.id}" onclick="return confirmDel()">Excluir</a>
                                    </td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <!-- /.card-body -->
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