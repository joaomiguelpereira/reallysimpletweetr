<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link type="text/css" rel="stylesheet" href="Zwitrng.css">
<title>Web Application Starter Project</title>
</head>
<body>
Teste Query Strin:
<%=request.getQueryString()%>
<br>
Attributes:

<%
	out.println("<br>");
	for (Enumeration e = request.getAttributeNames(); e
			.hasMoreElements();) {

		String aName = e.nextElement().toString();
		out.println(aName + ": " + request.getAttribute(aName));
		out.println("<br>");

	}
%>

Parameters:
<%
	String loginUrl = request.getParameter("loginUrl");
	out.println("<br>");
	for (Enumeration e = request.getParameterNames(); e
			.hasMoreElements();) {

		String aName = e.nextElement().toString();
		out.println(aName + ": " + request.getParameter(aName));
		out.println("<br>");
	}
%>


headers:
<%
	out.println("<br>");
	for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {

		String aName = e.nextElement().toString();
		out.println(aName + ": " + request.getHeader(aName));
		out.println("<br>");
	}
	session.setAttribute("teste", "test3");
%>


Attributes in Session:
<%
	out.println("<br>--");
	for (Enumeration e = request.getSession().getAttributeNames(); e
			.hasMoreElements();) {

		String aName = e.nextElement().toString();
		out.println(aName + ": "
				+ request.getSession().getAttribute(aName));
		out.println("<br>");
	}
%>

<a href="<%=loginUrl%>">login with twitter</a>
</body>

<%@page import="java.util.Enumeration"%></html>