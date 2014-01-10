<%@page import="java.util.GregorianCalendar"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="fr.upmc.flyingduke.domain.FDUser" %>
<%@ page import="fr.upmc.flyingduke.domain.Game" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.FDUserDao" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.GameDao" %>
<%@ page import="fr.upmc.flyingduke.utils.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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

System.out.println("la Verification User finie");
Calendar today = new GregorianCalendar();
GameDao gameDao = new GameDao();

/*RESTQuery queryLauncher = new RESTQuery();
Parser parser = new Parser();
String XMLresult = queryLauncher.getGamesForDay();
ArrayList<Game> list = parser.parseGamesForDay(XMLresult);
for (Game game : list){
	gameDao.store(game);
	System.out.println(game.getOdds().getHome());
}
*/
List<Game> gamesList = gameDao.gameForDay(today);

%>
Vous avez <%=fdUser.getWallet() %> euros et votre id est <%=fdUser.getId() %> 
<table>
<%
DecimalFormat twoDigitsFormat = new DecimalFormat("##.##");
for(Game game : gamesList){ 
	%><TR>
	<form action="/match" Method="GET">
	<TD>
	<%= twoDigitsFormat.format(game.getOdds().getHome()) %> 
	</TD>
	<TD>
	<%= game.getHomeTeam().getUUID()%>  VS
	</TD>
	<TD>
	<%= game.getAwayTeam().getUUID()%>
	</TD>
	<TD>
	<%= twoDigitsFormat.format(game.getOdds().getAway())%> 
	</TD>
	<TD>
	<input type="hidden" name="game" value=<%=game.getUUID()%>>
	<INPUT type=submit name="buttonBet" value="More">
	</TD>
	</form>
	</TR>
	<% }%>
	</table>
</body>
</html>