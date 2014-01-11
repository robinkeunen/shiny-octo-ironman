
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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="fr.upmc.flyingduke.domain.FDUser"%>
<%@ page import="fr.upmc.flyingduke.domain.dao.FDUserDao"%>


<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="/images/duke_logo.png">

    <title>Jumbotron Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="/stylesheets/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
      
    <!-- Glyphicons -->  
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]><script src="../../docs-assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
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
Calendar today = Calendar.getInstance();
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
today.setTimeZone(TimeZone.getTimeZone("America/New_York")); 
today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
List<Game> gamesList = gameDao.gameForDay(today);

%>
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/home">Flying duke</a>
        </div>
        <div class="navbar-collapse collapse">
          <form class="navbar-form navbar-right" role="form">
              <span class="label">getUsername</span>
              <a class="btn btn-sm btn-success" href="get login url">
                Sign out
              </a>
          </form>
        </div><!--/.navbar-collapse -->
      </div>
    </div>

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <div class="container">
        <h1>Welcome!</h1>
        <p>This is an awesome gambling website created by <em>Antoine the Pilgrim</em>, <em>Robin the Hipster</em> and the <em>Invisible Basil</em>! Register, get 100$ and start gambling on NBA matches! You might get rich sooner than later!</p>
        <p><a href="rules.html" class="btn btn-primary btn-default" role="button">
            Rules &raquo;</a></p>
      </div>
    </div>

    <div class="container">
      <!-- Example row of columns -->
      <div class="row">
        <div class="col-md-8">
            <h2>Upcoming games</h2>
            
            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-sm-3"><strong>Time</strong></div>
                        <div class="col-sm-3"><strong>Home</strong></div>
                        <div class="col-sm-1"></div>
                        <div class="col-sm-3"><strong>Away</strong></div>
                        <div class="col-sm-1"></div>
                        <div class="col-sm-1"></div>
                    </div>
                </div>
            
          
                 <ul class="list-group">
            <%
DecimalFormat twoDigitsFormat = new DecimalFormat("##.##");
for(Game game : gamesList){ 
	%>   
                     <a href="/match?gameid=<%=game.getUUID() %>" class="list-group-item">
                        <div class="row">
                            <div class="col-xs-12 col-sm-3"><%= game.getDate()%></div>
                            <div class="col-xs-9 col-sm-3"><%= game.getHomeTeam().getName()%> </div>
                            <div class="col-xs-3 col-sm-1"><%=twoDigitsFormat.format(game.getOdds().getHome()) %> </div>
                            <div class="col-xs-9 col-sm-3"><%= game.getAwayTeam().getName()%> </div>
                            <div class="col-xs-3 col-sm-1"><%=twoDigitsFormat.format(game.getOdds().getAway()) %></div>
                            <div class="col-xs-2 col-sm-1 btn-link active hidden-xs">
                                    <span class="glyphicon glyphicon-arrow-right"></span>
                            </div>
                        </div>
                     </a>
                     <%} %>
                </ul>       
            </div>
            
                
                  </div>

        <div class="col-md-4">
          <h2>Heading</h2>
          <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
          <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
        </div>

          
    </div>
    </div>

<div class="container">
    
      <hr>
      <footer>
        <p>This website was built as an assignment for the aar course</p>
      </footer>
    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
    </body>
</html>
