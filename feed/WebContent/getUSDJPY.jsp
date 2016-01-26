<%@page import="search.USDJPY"%>
<%@ page language="java" contentType="application/json"
    pageEncoding="ISO-8859-1"%>
{"_source":{"yvalue":"<%=USDJPY.getValue() %>"}}