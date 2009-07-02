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
<title>nIdeaSystems Tweet Pro System</title>
<link type="text/css" rel="stylesheet" href="Zwitrng.css">
<title>A professional solution for Twitter.</title>
</head>
<body>
<div id="main">
<%
	if (user != null) {
%>

<p>Hello, <%=user.getNickname()%>! (You can <a
	href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
out</a>.)</p>
<%
	} else {
%>
<p>Hello and welcome to nIdeaSystems Tweet Pro System.</p>
<!-- 
<p>We are currently developing this system for professional Twitter users. So what is this?</p>
<p>nIdeaSystems wants to give you:</p>
<ul>
<li>A powerful Twitter Marketing tool</li>
<li>Extensible to integrate other Social Networks and Web Services</li>
<li>A professional twitter client</li>
</ul>

<p>What this client can do for you?</p>
<ul>
<li>Keep tabs on what is going on</li>
<li>Send pre-created twittes automatically in reply to other twittes related to business</li>
<li>Keep your customers informed with information related to your business by sending pre-created twittes at regular intervals</li>
<li>Recommended users that might matter to your business, based on predefined criteria</li>
<li>Use templates to send twittes</li>
<li>Create groups</li>
<li>Save searches</li>
<li>Send twittes through multiple accounts</li>
<li>Organize conversations</li>
<li>Send automatic welcome message to new followers</li>
<li>Follow automatically users based on criteria</li>
<li>Send links to blogs and pages periodically from RSS feeds</li>
<li>and much more.</li>
</ul>

<p>nIdeaSystems Tweet Pro System is:</p>
<ul>
<li>Open Source</li>
<li> and runs on the cloud</li>
</ul> 

<p> This system is closed and you cannot use it right now. But if you're really willing to participate in this experience then let us know by 
sending an email to info at nideasystems.com</p>
 -->
 <p>If you know what you're doing then you can login with an google account.</p>
 <a
	href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
in</a> with a Google Account.</p>
<%
	}
%>

	
</div>
	
</body>
</html>