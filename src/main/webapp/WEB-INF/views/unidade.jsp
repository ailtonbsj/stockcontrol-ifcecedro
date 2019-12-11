<%@page import="java.util.List"%>
<%@page import="br.edu.ifce.cedro.stockcontrol.models.Unidade"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Unidade</title>
</head>
<body>
<ul>
<c:forEach items="${unidades}" var="unidade">
	<li>${unidade.descricao} [${unidade.simbolo}]</li>
</c:forEach>
</ul>
</body>
</html>