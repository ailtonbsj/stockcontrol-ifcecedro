<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${not empty sessionScope.appLocaleBase ? sessionScope.appLocaleBase : 'pt'}">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title><fmt:message key="login.title"/></title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="/plugins/fontawesome-free/css/all.min.css">
	<link rel="stylesheet" href="/css/adminlte.min.css">
	<link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet">
</head>
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo">
			<a href="/login"><b>IFCE</b> StockControl</a>
		</div>
		<div class="card">
			<div class="card-body login-card-body">
				<div class="d-flex justify-content-end mb-2">
					<select class="form-control form-control-sm" style="width:auto" aria-label="<fmt:message key='login.language'/>"
						onchange="window.location='/login?lang='+encodeURIComponent(this.value)">
						<option value="pt" <c:if test="${sessionScope.appLocaleBase != 'en'}">selected</c:if>><fmt:message key="login.lang.pt"/></option>
						<option value="en" <c:if test="${sessionScope.appLocaleBase == 'en'}">selected</c:if>><fmt:message key="login.lang.en"/></option>
					</select>
				</div>

				<p class="login-box-msg"><fmt:message key="login.heading"/></p>

				<c:if test="${not empty erroLogin}">
					<div class="alert alert-danger" role="alert"><fmt:message key="${erroLogin}"/></div>
				</c:if>

				<form action="/login" method="post">
					<div class="input-group mb-3">
						<input type="text" name="login" class="form-control" placeholder="<fmt:message key='login.username'/>"
							value="<c:out value='${loginInformado}'/>" required autofocus>
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="fas fa-user"></span>
							</div>
						</div>
					</div>
					<div class="input-group mb-3">
						<input type="password" name="senha" class="form-control" placeholder="<fmt:message key='login.password'/>" required>
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="fas fa-lock"></span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-12">
							<button type="submit" class="btn btn-primary btn-block"><fmt:message key="login.submit"/></button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script src="/plugins/jquery/jquery.min.js"></script>
	<script src="/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script src="/js/adminlte.min.js"></script>
</body>
</html>
