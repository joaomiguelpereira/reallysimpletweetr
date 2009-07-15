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

<script src="http://www.google.com/jsapi"></script>

<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-2473783-5");
pageTracker._trackPageview();
} catch(err) {}</script>


<script>
  google.load("prototype", "1.6.0.3");
 </script>
<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
%>

<% if (user!=null) { %>
	
	<script type="text/javascript" language="javascript" src="zwitrng/zwitrng.nocache.js"></script>
<% } %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tweet Pro Systems</title>
<link type="text/css" rel="stylesheet" href="Zwitrng.css">
<title>A professional solution for Twitter.</title>
</head>
<body>

<div id="main">

<div id="topPanel">
	Version 0.1
</div>
<%
	if (user != null) {
%>
<div id="userInfo">
Hello, <%=user.getNickname()%>! (You can <a
	href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
out</a>.)
</div>

<%
	} else {
%>
<div id="userInfo">
<p>Hello and welcome to nIdeaSystems Tweet Pro System.</p>
 <p>If you know what you're doing then you can login with an google account.</p>
 <a
	href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
in</a> with a Google Account.</p>

</div>
<%
	}
%>
	
</div>
	
</body>

</html>