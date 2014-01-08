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
		User user = (User) pageContext.getAttribute("user");
		if (user == null) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
			return;
		}
	%>
	<p>
		Hi <%= user.getNickname() %> - do some admin then 
		<a href="<%= userService.createLogoutURL("/administration") %>">
			Logout </a>
	</p>

	<%
		}
	%>
	
	

</body>
</html>
