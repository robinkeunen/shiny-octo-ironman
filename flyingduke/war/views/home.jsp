
<%@page import="java.util.GregorianCalendar"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="fr.upmc.flyingduke.domain.FDUser" %>
<%@ page import="fr.upmc.flyingduke.domain.Game" %>
<%@ page import="fr.upmc.flyingduke.domain.Team" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.FDUserDao" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.TeamDao" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.GameDao" %>
<%@ page import="fr.upmc.flyingduke.utils.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
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

    <title>Flying Duke</title>

    <!-- Bootstrap core CSS -->
    <link href="/stylesheets/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
      
    <!-- Glyphicons -->  

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
    FDUserDao fdUserDao = new FDUserDao();
    GameDao gameDao = new GameDao();
    System.out.println("doGet HomeServlet");
UserService userService = UserServiceFactory.getUserService();
User googleUser = userService.getCurrentUser();
if (googleUser == null){
	response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
	return;
}
System.out.println("la");
System.out.println("MAIL : " + googleUser.getEmail());
if ((fdUser = fdUserDao.getFromGoogleUser(googleUser)) == null){
	System.out.println("home.jsp: Redirect to user creation");
	response.sendRedirect("/views/createUser.jsp");
	return;
}
System.out.println("la");

System.out.println("home.jsp: End of user verification");
Calendar tomorrow = Calendar.getInstance();
Calendar today = Calendar.getInstance();
today.setTimeZone(TimeZone.getTimeZone("America/New_York")); 
today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
tomorrow.add(Calendar.DATE, +1);
List<Game> gamesListToday = gameDao.gameForDay(today);
List<Game> gamesListTomorrow = gameDao.gameForDay(tomorrow);
List<Game> gamesList = gamesListToday;
System.out.println("Creation de la liste des games d'aujourd'hui et demain");
gamesList.addAll(gamesListTomorrow);
System.out.println("Creation terminée");
ServletContext ctxt = getServletContext();
ctxt.setAttribute("betDone",false);
ctxt.setAttribute("error", false);
%>


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

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <div class="container">
        <h1>Welcome!</h1>
        <p>This is an awesome gambling website created by <em>Antoine the Pilgrim</em>, <em>Robin the Hipster</em> and the <em>Invincible Basil</em>! Register, get 100$ and start gambling on NBA matches! You might get rich sooner than later!</p>
        <button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
          More &raquo;
        </button>
          
          <!-- Modal -->
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel"><h2>More about this web site</h2></h4>
                  </div>
                  <div class="modal-body">

                     <h3>Rules</h3>
                      <p>
                          To start gambling on our website, you must first register through your Google account.
                          On your first login, you will receive $100. 
                          You can start gambling them as soon as you register. 
                          The home page displays the games for this day. 
                          Click on a game to have more information and place a bet.
                      </p>
                      <p> For each game, we compute the odds of each team. Let's say you want to bet on this game featuring the Warriors and the Knicks:</p>
                      <ul class="list-group">
                     <a href="#" class="list-group-item">
                        <div class="row">   
                            <div class="col-xs-9">Golden States Warriors</div>
                            <div class="col-xs-3">1.2</div>
                            <div class="col-xs-9">New York Knicks</div>
                            <div class="col-xs-3">1.5</div>
                        </div>
                     </a>
                      </ul>
                      
                      <p>The Warriors playing at home are more likely to win, the odds are lower (1.2). The Knicks have their odds at 1.5. When you gamble, the amount is instantly withdrawn from your account. If you bet $10 on the Warriors, you will receive $12 if they win, for a total gain of $2. If they are defeated, you lose your $10. </p>
                      
                      <p>The results of the games are updated once every day. We will compute your gains at that time.</p>
                      
                     <h3>Technologies</h3>
                      This web application has been built on top of <a href="https://developers.google.com/appengine/">Google App Engine</a> with Java Servlets and JSPs. 
                      Persistence is provided by the App Engine's data repository, the High Replication Datastore (HRD).
                      The style of the app uses the <a href="http://getbootstrap.com/">Bootstrap</a> framework.
                     <h3></h3> 
                </div>
                  
                    <div class="modal-footer">

                  </div>
                    
                </div><!-- /.modal-content -->
              </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
          
      </div>
    </div>

    <div class="container">
      <!-- Example row of columns -->
      <div class="row">
        <div class="col-lg-10"> 
            <h2>Upcoming games</h2>
            
            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading hidden-xs">
                    <div class="row ">
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
				TeamDao teamDao = new TeamDao();
				for(Game game : gamesList){ 
					Team homeTeam = teamDao.deepGet(game.getHomeTeamUUID());
					Team awayTeam = teamDao.deepGet(game.getAwayTeamUUID());
			%>   
                     <a href="/match?gameid=<%=game.getUUID() %>" class="list-group-item">
                        <div class="row">  
                            <div class="col-xs-12 col-sm-3"><%= new SimpleDateFormat("MM'/'dd' - 'HH:mm").format(game.getDate())%></div>
                            <div class="col-xs-9 col-sm-3"><%= homeTeam.getName()%> </div>
                            <div class="col-xs-3 col-sm-1"><%=twoDigitsFormat.format(game.getOdds().getHome()) %> </div>
                            <div class="col-xs-9 col-sm-3"><%= awayTeam.getName()%> </div>
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

        <div class="col-lg-2 visible-lg">
        	<h2 class="invisible">User panel</h2>
             <div class="row">  
                 <div class="col-xs-12">
                     <div class="panel panel-primary ">
                        <div href="#" class="panel-heading"> 
                            <strong><%= fdUser.getFirstName() %> <%= fdUser.getLastName() %></strong>
                        </div>
              
                        <div class="panel-body">
                            <div class="center-clock text-center" ><h1 id="money" ><%=twoDigitsFormat.format(fdUser.getWallet()) %>$</h1></div>
                        </div>
                    </div>
                 </div>
             </div>
        </div>

          
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
