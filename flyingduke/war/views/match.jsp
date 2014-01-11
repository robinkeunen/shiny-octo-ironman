<%@page import="fr.upmc.flyingduke.servlets.HomeServlet"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="fr.upmc.flyingduke.domain.FDUser" %>
<%@ page import="fr.upmc.flyingduke.domain.Game" %>
<%@ page import="fr.upmc.flyingduke.domain.Team" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.FDUserDao" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.TeamDao" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.upmc.flyingduke.domain.Player" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
    <link href="/stylesheets/game-custom.css" rel="stylesheet">

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
System.out.println("MATCH.JSP");
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
TeamDao teamDao = new TeamDao();
Team homeTeam = teamDao.deepGet(game.getHomeTeamUUID());
Team awayTeam = teamDao.deepGet(game.getAwayTeamUUID());
List<Player> playersHome = homeTeam.getPlayers();
List<Player> playersAway = awayTeam.getPlayers();
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
          <form class="navbar-form navbar-right" role="form">
              <span class="label">getUsername</span>
              <a class="btn btn-sm btn-success" href="get login url">
                Sign out
              </a>
          </form>
        </div><!--/.navbar-collapse -->
      </div>
    </div>

    <div class="container">
      <!-- Example row of columns -->
      <div class="row">
          <div class="col-sm-6 col-lg-4">
          <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title"><%=homeTeam.getName() %> <small>Home</small></h3>
                    
                </div>            
            
            <div class="panel-body">
                <div class="row">
                    <div class="col-xs-6">
                        <ul class="list-unstyled">
                            <li>Win ratio:  <%=homeTeam.getWinRatio()%></li>
                            <li>Points for: <%=homeTeam.getPointsFor() %></li>
                            <li>Points againts: <%=homeTeam.getPointsAgainst() %></li>
                        </ul>
                    </div>
                    <div class="cos-xs-6">
                        <h1 class="text-center">1.25</h1>
                    </div>
                </div>
            </div>
            <table class="table table-striped ">
             
              <%for(Player player : playersHome){
              	%>
              
                <tr>
                    <td><%=player.getFirstName() %></td>
                    <td><%=player.getLastName() %></td>
                    <td><%=player.getPosition() %></td>
                </tr>
                <%} %>
              </table>

          </div>
      </div>
          
        <div class="col-sm-6 col-lg-4">
          <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title"><%=awayTeam.getName()%> <small>Away</small></h3>
            </div>
            
            <div class="panel-body">
                <div class="row">
                    <div class="col-xs-6">
                        <ul class="list-unstyled">
                            <li>Win ratio:  <%=awayTeam.getWinRatio()%></li>
                            <li>Points for: <%=awayTeam.getPointsFor() %></li>
                            <li>Points againts: <%=awayTeam.getPointsAgainst() %></li>
                        </ul>
                    </div>
                    <div class="cos-xs-6">
                        <h1 class="text-center">1.25</h1>
                    </div>
                </div>
            </div>

            
            <table class="table table-striped ">
             
              <%for(Player player : playersAway){
              	%>
              
                <tr>
                    <td><%=player.getFirstName() %></td>
                    <td><%=player.getLastName() %></td>
                    <td><%=player.getPosition() %></td>
                </tr>
                <%} %>
              </table>
              </div>
          </div>
          
          
          <div class="clearfix visible-md visible-sm col-sm-3"></div>
          
          
        <div class="col-sm-6  col-lg-4">
          <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Place a bet</h3>
            </div>
            
            <div class="panel-body">
                <form class="form-horizontal">
                    <div class="form-froup">
                        <button type="button" class="btn btn-primary btn-lg btn-block"> <!-- add active with js -->
                            <%=homeTeam.getName() %> </button>
                        <button type="button" class="btn btn-danger btn-lg btn-block ">
                            <%=awayTeam.getName() %> </button>
                    </div>
                    
                    <div class="form-group invisible"></div>
                        
                    <div class="form-group">
                        <div class="col-xs-8">
                            <div class="input-group ">
                                <span class="input-group-addon">$</span>
                                <input type="text" class="form-control text-right input-lg" placeholder="10">
                            </div>
                        </div>
                        
                        <div class="col-xs-4">
                            <button type="button" class="btn btn-success btn-lg btn-block">
                                Bet <span class="glyphicon glyphicon-chevron-down"> </span></button>
                        </div>

                    </div>
                
               </form>
            </div>
          </div>
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