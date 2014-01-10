<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="fr.upmc.flyingduke.domain.FDUser"%>
<%@ page import="fr.upmc.flyingduke.domain.dao.FDUserDao"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Home</title>
</head>
<body>
	<%
		FDUserDao fdUserQuery = new FDUserDao();
		FDUser fdUser;
		System.out.println("doGet HomeServlet");
		UserService userService = UserServiceFactory.getUserService();
		User googleUser = userService.getCurrentUser();
		if (googleUser == null) {
			response.sendRedirect(userService.createLoginURL(request
					.getRequestURI()));
			return;
		}
		System.out.println("MAIL : " + googleUser.getEmail());
		if ((fdUser = fdUserQuery.getFromGoogleUser(googleUser)) == null) {
			System.out.println("redirect creation");
			response.sendRedirect("/views/createUser.jsp");
			return;
		}
		System.out.println("User Verification ok");
	%>
        
        <h1>Insert Something</h1>
        here
	

</body>
</html>