<%@page import="java.util.LinkedHashMap"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.LinkedList"%>
<%@page import="search.USDCHF"%>
<%@page import="search.USDJPY"%>
<%@page import="search.GBPUSD"%>
<%@page import="search.EURUSD"%>
<%@page import="search.EURJPY"%>
<%@ page language="java" contentType="application/json"
    pageEncoding="ISO-8859-1"%>
<%
String r=(String)request.getParameter("r");
LinkedList<String> yvalue=new LinkedList<String>();
if(r==null || r.equals(""))
{
	yvalue=null;
}
else if(r.equals("GBPUSD"))
{
	yvalue=GBPUSD.getYvalue();
}
else if(r.equals("USDCHF"))
{
	yvalue=USDCHF.getYvalue();
}
else if(r.equals("USDJPY"))
{
	yvalue=USDJPY.getYvalue();
}
else if(r.equals("EURJPY"))
{
	yvalue=EURJPY.getYvalue();
}
else if(r.equals("EURUSD"))
{
	yvalue=EURUSD.getYvalue();
}
%>
<%
JSONObject jsonobj=new JSONObject();
LinkedHashMap m=new LinkedHashMap();
m.put("yhistory", yvalue);
jsonobj.put("_source", m);
%>
<%=jsonobj %>