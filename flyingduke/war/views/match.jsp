<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="fr.upmc.flyingduke.domain.FDUser" %>
<%@ page import="fr.upmc.flyingduke.domain.Game" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.FDUserDao" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Match</title>
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
if ((fdUser = fdUserQuery.getFromGoogleUser(googleUser)) == null){
	System.out.println("Redirection vers création");
	response.sendRedirect("/views/createUser.jsp");
	return;
}
ServletContext ctxt = getServletContext();
Game game = (Game) ctxt.getAttribute("game");
ctxt.setAttribute("fdUser", fdUser);
Boolean erreur = (Boolean) ctxt.getAttribute("erreur");
DecimalFormat twoDigitsFormat = new DecimalFormat("##.##");
System.out.println("erreur");
if (erreur != null && erreur){
	System.out.println("erreur");
	%>
	Bet's value must be an integer between 0 and <%=fdUser.getWallet() %> <br/>
	<%
	erreur=false;
	ctxt.setAttribute("erreur",erreur);
}
%>

GAME
<br/>
<form action="/match" Method="POST">
<input type="radio" name="team" value="home" checked="checked"/> Home <%= game.getHomeTeam().getUUID()%> Odds <%= twoDigitsFormat.format(game.getOdds().getHome())%>
<br/>
<input type="radio" value="away" name="team"/> Away <%= game.getAwayTeam().getUUID()%> Odds <%= twoDigitsFormat.format(game.getOdds().getAway())%>
<br/>

Value <input type="text" name="betValue"><br/>
<br/>
<INPUT type=submit name="buttonBet" value="Bet">
</form>
</body>
</html>