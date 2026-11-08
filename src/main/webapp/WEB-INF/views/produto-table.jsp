<%@include file="../header.jspf"  %>

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
    </div><!-- /.container-fluid -->
</section>

<!-- Main content -->
<section class="content">
    <div class="container-fluid">
        <div class="row">
            <!-- left column -->
            <div class="col-md-12">
                <c:if test="${not empty param.erro}">
                    <div class="alert alert-danger" role="alert">${param.erro}</div>
                </c:if>
                <!-- INICIO TABELA -->
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><fmt:message key="${operacao}"/></h3>
                    </div>
                    <!-- /.card-header -->
                    <div class="card-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th style="width: 10px">#</th>
                                    <th><fmt:message key="field.name"/></th>
                                    <th><fmt:message key="field.quantity"/></th>
                                    <th><fmt:message key="field.minimum"/></th>
                                    <th style="width: 132px"></th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach items="${Produtos}" var="produto">
                                <tr>
                                    <td>${produto.id}</td>
                                    <td>${produto.nome}</td>
                                    <td>${produto.quantidade} ${produto.unidade.simbolo}</td>
                                    <td>${produto.minimo} ${produto.unidade.simbolo}</td>
                                    <td>
                                    	<a class="badge badge-success" href="/produto/editar?id=${produto.id}"><fmt:message key="op.update"/></a>
                                    	<a class="badge badge-danger" href="/produto/remover?id=${produto.id}" onclick="return confirmDel()"><fmt:message key="op.delete"/></a>
                                    </td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <!-- /.card-body -->
                    <div class="card-footer clearfix">
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
