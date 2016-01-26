<%@page import="search.USDCHF"%>
<%@page import="search.USDJPY"%>
<%@page import="search.GBPUSD"%>
<%@page import="search.EURUSD"%>
<%@page import="search.EURJPY"%>
<%@ page language="java" contentType="application/json"
    pageEncoding="ISO-8859-1"%>
<%
String r=(String)request.getParameter("r");
String value="";
if(r==null || r.equals(""))
{
	value="NULL";
}
else if(r.equals("GBPUSD"))
{
	value=GBPUSD.getValue();
}
else if(r.equals("USDCHF"))
{
	value=USDCHF.getValue();
}
else if(r.equals("USDJPY"))
{
	value=USDJPY.getValue();
}
else if(r.equals("EURJPY"))
{
	value=EURJPY.getValue();
}
else if(r.equals("EURUSD"))
{
	value=EURUSD.getValue();
}
%>
{"_source":{"yvalue":"<%=value %>"}}