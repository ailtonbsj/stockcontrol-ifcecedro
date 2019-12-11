<%@page import="java.util.List"%>
<%@page import="br.edu.ifce.cedro.stockcontrol.models.Unidade"%>
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
<%
List<Unidade> unidades = (List<Unidade>) request.getAttribute("unidades");
for(Unidade unidade : unidades) {
	request.setAttribute("unidade", unidade);

%>
<li>${unidade.simbolo}  - ${unidade.descricao}</li>
<%
}
%>
</ul>
</body>
</html>