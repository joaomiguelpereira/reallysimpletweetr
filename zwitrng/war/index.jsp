<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="com.google.appengine.api.users.UserService"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="com.google.appengine.api.users.User"%>
<%@page import="twitter4j.Twitter"%>
<%@page import="twitter4j.http.RequestToken"%>
<%@page import="twitter4j.http.AccessToken"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="twitter4j.TwitterException"%>
<%@page import="twitter4j.Status"%>
<%@page import="java.io.InputStreamReader"%><html>

<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
%>

<% if (user!=null) { %>

	<script type="text/javascript" language="javascript" src="zwitrng/zwitrng.nocache.js"></script>
<% } %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link type="text/css" rel="stylesheet" href="Zwitrng.css">
<title>Web Application Starter Project</title>
</head>
<body>

<%
	if (user != null) {
%>

<p>Hello, <%=user.getNickname()%>! (You can <a
	href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
out</a>.)</p>
<%
	} else {
%>
<p>Hello! <a
	href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
in</a> with a Google Account.</p>
<%
	}
%>
<div id="main">
	
</div>
	
</body>
</html>