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
<%@ page import="java.util.List" %>
<%@ page import="fr.upmc.flyingduke.domain.Player" %>
<%@ page import="java.text.DecimalFormat" %>
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
    
    <%
FDUser fdUser = null;
DecimalFormat twoDigitsFormat = new DecimalFormat("##.##");

UserService userService = UserServiceFactory.getUserService();
User googleUser = userService.getCurrentUser();
if (googleUser == null){
	response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
	return;
}
System.out.println("MAIL : " + googleUser.getEmail());
FDUserDao fdUserDao = new FDUserDao();
TeamDao teamDao = new TeamDao();
if ((fdUser = fdUserDao.getFromGoogleUser(googleUser)) == null){
	response.sendRedirect("/views/createUser.jsp");
	return;
}
ServletContext ctxt = getServletContext();
Game game = (Game) ctxt.getAttribute("game");
ctxt.setAttribute("fdUser", fdUser);



Team homeTeam = teamDao.deepGet(game.getHomeTeamUUID());
Team awayTeam = teamDao.deepGet(game.getAwayTeamUUID());
List<Player> playersHome = homeTeam.getPlayers();
List<Player> playersAway = awayTeam.getPlayers();
%>

 <title><%= homeTeam.getAlias() %> vs <%= awayTeam.getAlias() %> </title>

  </head>

  <body>



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
              <a href="/user" class="label"><%= fdUser.getFirstName() %> <%= fdUser.getLastName() %></a>
              <a href="/user" class="label">
              	<span class="glyphicon glyphicon-dashboard"></span>
              </a>
              <a class="btn btn-sm btn-success" href="<%= userService.createLoginURL(request.getRequestURI()) %>">
                Sign out
              </a>
          </form>
        </div><!--/.navbar-collapse -->
      </div>
    </div>

    <div class="container">
      <!-- Example row of columns -->
      <div class="row">
      
       <!-- bet form for large screens -->
        <div class="col-xs-12 col-sm-offset-2 col-sm-8 hidden-lg hidden-md">
          <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Place a bet</h3>
            </div>
            
            <div class="panel-body">
                <form action="/match" method="post" class="form-horizontal">
                <div class="form-group">
                        <div class="col-xs-12">
                            <div class="input-group ">
                                <span class="input-group-addon">$</span>
                                <input type="text" name="betValue" class="form-control text-right input-lg" placeholder="You have <%= fdUser.getWallet() %> $" required autofocus>
                            </div>
                        </div>			
                    </div> 
                
                    <div class="form-froup">
                        <button type="submit" value="home" name="team" class="btn btn-primary btn-lg btn-block"> 
                            <%=homeTeam.getName() %> </button>
                        <button type="submit" value="away" name="team" class="btn btn-danger btn-lg btn-block ">
                            <%=awayTeam.getName() %> </button>
                    </div>
                                           
                                         
               </form>
            </div>
          </div>
          </div>
          
          <%
          Boolean error = (Boolean) ctxt.getAttribute("error");
          if (error != null && error){
          	%>
        		<div class="alert alert-danger hidden-lg hidden-md"><%=ctxt.getAttribute("errorMessage") %></div>
        		<%
        	}
          Boolean betDone = (Boolean) ctxt.getAttribute("betDone");
          if(betDone){
          	%>
        	<div class="alert alert-success hidden-lg hidden-md">Your bet has been registered.</div>
        		<%
        		betDone = false;
        		ctxt.setAttribute("betDone", betDone);
          }
          %>
          
          </div>
          <!-- End bet form -->
          
          <div class="col-sm-6 col-md-4">
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
                            <li>Points against: <%=homeTeam.getPointsAgainst() %></li>
                        </ul>
                    </div>
                    <div class="cos-xs-6">
                        <h1 class="text-center"><%=twoDigitsFormat.format(game.getOdds().getHome()) %></h1>
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
          
        <div class="col-sm-6 col-md-4">
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
                            <li>Points against: <%=awayTeam.getPointsAgainst() %></li>
                        </ul>
                    </div>
                    <div class="cos-xs-6">
                        <h1 class="text-center"><%=twoDigitsFormat.format(game.getOdds().getAway()) %></h1>
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
          
          
         <!-- bet form for large screens -->
        <div class="col-sm-6  col-md-4 visible-md visible-lg">
          <div class="panel panel-default">
          
            <div class="panel-heading">
                <h3 class="panel-title">Place a bet</h3>
            </div>
            
            <div class="panel-body">
                <form action="/match" method="post" class="form-horizontal">
                <div class="form-group">
                        <div class="col-xs-12">
                            <div class="input-group ">
                                <span class="input-group-addon">$</span>
                                <input type="text" name="betValue" class="form-control text-right input-lg" placeholder="You have <%= fdUser.getWallet() %> $">
                            </div>
                        </div>			
                    </div> 
                
                    <div class="form-froup">
                        <button type="submit" value="home" name="team" class="btn btn-primary btn-lg btn-block"> 
                            <%=homeTeam.getName() %> </button>
                        <button type="submit" value="away" name="team" class="btn btn-danger btn-lg btn-block ">
                            <%=awayTeam.getName() %> </button>
                    </div>                                         
               </form>
            </div>
          </div>
          <%
          error = (Boolean) ctxt.getAttribute("error");
          if (error != null && error){
          	%>
        		<div class="alert alert-danger visible-md visible-lg"><%=ctxt.getAttribute("errorMessage") %></div>
        		<%
        		error=false;
        		ctxt.setAttribute("error", error);
        	}
          betDone = (Boolean) ctxt.getAttribute("betDone");
          if(betDone){
          	%>
        	<div class="alert alert-success visible-md visible-lg">Your bet has been registered.</div>
        		<%
        		betDone = false;
        		ctxt.setAttribute("betDone", betDone);
          }
          %>
		
	
          </div>
          <!-- End bet form -->
          
      </div>
    </div>

     <div class="container">
         <hr>
         <footer class="text-right">
        <p class="text-muted">This website was built as an assignment for the aar course 
          <span class="visible-xs text-muted ">xs</span>
          <span class="visible-sm text-muted ">sm</span>
          <span class="visible-md text-muted ">md</span>
          <span class="visible-lg text-muted ">lg</span></p>
         </footer>
      </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
</body>
</html>