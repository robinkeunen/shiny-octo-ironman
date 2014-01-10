<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Administration dashboard</title>

</head>

<body>
	<%
		UserService userService = UserServiceFactory.getUserService();
		User googleUser = userService.getCurrentUser();
		if (googleUser == null) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
			return;
		}
	%>
	<form action="/FillUp" Method="POST">
	<INPUT type=submit name="button" value="Get All Teams">
	</form>
	<form action="/FillUp" Method="POST">
	<INPUT type=submit name="button" value="Get All Games">
	</form>
	<form action="/FillUp" Method="POST">
	Day <input type="text" name="day">
	Month <input type="text" name="month">
	Year<input type="text" name="year">
	<INPUT type=submit name="button" value="Get Games For This Day">
	</form>
	
	
	<p>
		Hi Admin - do some admin then 
		<a href="<%= userService.createLogoutURL("/administration") %>">
			Logout </a>
	</p>

	<%
	%>
	
	

</body>
</html>
