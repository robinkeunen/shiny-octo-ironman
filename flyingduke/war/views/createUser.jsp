<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="fr.upmc.flyingduke.domain.FDUser" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.FDUserDao" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>User</title>
</head>
<body>
<%
FDUserDao fdUserQuery = new FDUserDao();
FDUser fdUser;
System.out.println("doGet HomeServlet");
UserService userService = UserServiceFactory.getUserService();
User googleUser = userService.getCurrentUser();
if (googleUser == null){
	response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
	return;
}
System.out.println("MAIL : " + googleUser.getEmail());
if ((fdUser = fdUserQuery.getFromGoogleUser(googleUser)) != null){
	System.out.println("Redirection vers création");
	response.sendRedirect("/views/home.jsp");
	return;
}
System.out.println("la Verification User finie");
%>
<form action="/CreateUser" Method="POST">
	First Name <input type="text" name="firstName">
	Last Name <input type="text" name="lastName">
	<INPUT type=submit name="buttonCreate" value="Create User">
	</form>
	

</body>
</html>