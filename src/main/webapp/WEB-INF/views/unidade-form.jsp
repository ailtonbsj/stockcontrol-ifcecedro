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
                <!-- INICIO FORM -->
                <!-- general form elements -->
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><fmt:message key="${operacao}"/></h3>
                    </div>
                    <!-- /.card-header -->
                    <!-- form start -->
                    <form role="form" action="/unidade" method="post">
                        <div class="card-body">
                            <div class="form-group">
                                <label for="idNumber"><fmt:message key="field.id"/></label>
                                <input name="id" type="text" class="form-control" id="idNumber" placeholder="<fmt:message key='field.id'/>" value="${id}" readonly="readonly">
                            </div>
                            <div class="form-group">
                                <label for="simbolo"><fmt:message key="field.symbol"/></label>
                                <input name="simbolo" type="text" class="form-control" id="simbolo" placeholder="<fmt:message key='field.symbol'/>" value="${simbolo}">
                            </div>
                            <div class="form-group">
                                <label for="descricao"><fmt:message key="field.description"/></label>
                                <input name="descricao" type="text" class="form-control" id="descricao" placeholder="<fmt:message key='field.description'/>" value="${descricao}">
                            </div>
                        </div>
                        <!-- /.card-body -->

                        <div class="card-footer">
                            <button type="submit" class="btn btn-primary"><fmt:message key="op.save"/></button>
                        </div>
                    </form>
                </div>
                <!-- /.card -->
                <!-- FIM FORM -->
            </div>
            <!--/.col (left) -->
        </div>
        <!-- /.row -->
    </div><!-- /.container-fluid -->
</section>
<!-- /.content -->
</div>

<%@include file="../footer.jspf"  %>
