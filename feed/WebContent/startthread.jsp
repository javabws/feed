<%@page import="search.USDCHF"%>
<%@page import="search.USDJPY"%>
<%@page import="search.GBPUSD"%>
<%@page import="search.EURUSD"%>
<%@page import="search.EURJPY"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
EURJPY.startThread();
EURUSD.startThread();
GBPUSD.startThread();
USDJPY.startThread();
USDCHF.startThread();
%>
<h2>Started....</h2><br>
<br>
<a href="stopthread.jsp"><button>Stop</button></a><br>
<a href="index.jsp"><button>Index Page</button></a>
</body>
</html>