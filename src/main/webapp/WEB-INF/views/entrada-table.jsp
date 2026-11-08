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
    </div>
</section>

<section class="content">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-12">
                <c:if test="${not empty param.erro}">
                    <div class="alert alert-danger" role="alert">${param.erro}</div>
                </c:if>
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><fmt:message key="${operacao}"/></h3>
                    </div>
                    <div class="card-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th style="width: 10px">#</th>
                                    <th><fmt:message key="field.product"/></th>
                                    <th><fmt:message key="field.supplier"/></th>
                                    <th><fmt:message key="field.quantity"/></th>
                                    <th><fmt:message key="field.validity"/></th>
                                    <th><fmt:message key="field.entryDate"/></th>
                                    <th><fmt:message key="field.observation"/></th>
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
                                    <td>
                                    <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${entrada.validade}" />
                                    </td>
                                    <td>
                                    <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${entrada.data}" />
         							</td>
                                    <td>${entrada.observacao}</td>
                                    <td>
                                    	<a class="badge badge-success" href="/entrada/editar?id=${entrada.id}"><fmt:message key="op.update"/></a>
                                    	<a class="badge badge-danger" href="/entrada/remover?id=${entrada.id}" onclick="return confirmDel()"><fmt:message key="op.delete"/></a>
                                    </td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</div>

<%@include file="../footer.jspf"  %>
