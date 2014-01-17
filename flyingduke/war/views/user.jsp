<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="fr.upmc.flyingduke.domain.FDUser" %>
<%@ page import="fr.upmc.flyingduke.domain.Bet" %>
<%@ page import="fr.upmc.flyingduke.domain.Game" %>
<%@ page import="fr.upmc.flyingduke.domain.Team" %>
<%@ page import="fr.upmc.flyingduke.domain.BetChoice" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.FDUserDao" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.GameDao" %>
<%@ page import="fr.upmc.flyingduke.domain.dao.TeamDao" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>

<%@ page import="fr.upmc.flyingduke.domain.dao.FDUserDao" %>
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

    <title>User page</title>

    <!-- Bootstrap core CSS -->
    <link href="/stylesheets/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/stylesheets/user-custom.css" rel="stylesheet"> 
      
    <!-- Glyphicons -->  

    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]><script src="../../docs-assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
<%
FDUser fdUser;
FDUserDao fdUserDao = new FDUserDao();
System.out.println("doGet HomeServlet");
DecimalFormat twoDigitsFormat = new DecimalFormat("##.##");
UserService userService = UserServiceFactory.getUserService();
User googleUser = userService.getCurrentUser();
if (googleUser == null){
	response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
	return;
}
System.out.println("MAIL : " + googleUser.getEmail());
if ((fdUser = fdUserDao.getFromGoogleUser(googleUser)) == null){
	System.out.println("Redirection vers création");
	response.sendRedirect("/views/createUser.jsp");
	return;
}
System.out.println("la Verification User finie");
List<Bet> futureBets = (List<Bet>) request.getAttribute("futurebets");
List<Bet> pastBets = (List<Bet>) request.getAttribute("pastbets");

ArrayList<Bet> winningBets = new ArrayList<Bet>();
ArrayList<Bet> losingBets = new ArrayList<Bet>();
TeamDao teamDao = new TeamDao();
GameDao gameDao = new GameDao();
for (Bet bet : pastBets){
	Game gameForBet = gameDao.get(bet.getGameUUID());
	System.out.println("AVANT LES SCORES");
	int scoreHome = gameForBet.getScores().getHome();
	System.out.println("Apres LES SCORES");
	int scoreAway = gameForBet.getScores().getAway();
	String winner = (scoreHome > scoreAway) ? "home" : "away";
	BetChoice winningTeam = (winner.equalsIgnoreCase("home")) ? BetChoice.HOME : BetChoice.AWAY;
	if (bet.getChoice().equals(winningTeam)){
		winningBets.add(bet);
	}else{
		losingBets.add(bet);
	}
}
System.out.println("Sortie");
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
 
 	<div class="container">
      <!-- Example row of columns -->
      <div class="row">
          
          <div class="col-sm-2 col-xs-12">
             <div class="row">  
                 <div class="col-xs-12">
                     <div class="panel panel-primary ">
                        <div class="panel-heading"> 
                            <strong><%= fdUser.getFirstName() %> <%= fdUser.getLastName() %></strong>
                        </div>
              
                        <div class="panel-body">
                            <div class="center-clock text-center" ><h1 id="money" ><%=twoDigitsFormat.format(fdUser.getWallet()) %>$</h1></div>
                        </div>
                    </div>
                 </div>
             </div>
         </div>
          
        <div class="col-sm-10">         
            <div class="row">
                <div class="panel panel-default ">
                  <div class="panel-heading"><strong> Upcoming bets </strong></div>
                </div>
            </div>
            
            <div class="row">
            <%System.out.println("Entrée"); 
            for (Bet bet : futureBets){ 
              Game game = gameDao.get(bet.getGameUUID());
              game.setScores(100, 90);
              int homeScore = game.getScores().getHome();
              int awayScore = game.getScores().getAway();
              Team homeTeam = teamDao.deepGet(game.getHomeTeamUUID());
              Team awayTeam = teamDao.deepGet(game.getAwayTeamUUID());
              
              boolean betHome = false;
              String homeBtnClass = "btn-default";
              String awayBtnClass = "btn-primary";
              
              double betOdds = bet.getOdds();
              
              if (bet.getChoice().equals(BetChoice.HOME)){
              	betHome = true;
              	homeBtnClass = "btn-primary";
              	awayBtnClass = "btn-default";
              }
              
              SimpleDateFormat df = new SimpleDateFormat("MM'/'dd' - 'HH:mm");
              
              %>
                                                 
                <div class="col-lg-3 col-md-4 col-sm-6">
                    <div class="panel panel-info ">
                        <div class="panel-heading text-center"> 
                            <%= df.format(game.getDate()) %>
                        </div>
              
                        <div class="panel-body">
                            <div class="btn-group text-center center-block">
                                <div class="btn <%= homeBtnClass %> btn-lg col-xs-6"><%= homeTeam.getAlias() %> <small></small></div>
                                <div class="btn <%= awayBtnClass %> btn-lg col-xs-6"><%= awayTeam.getAlias() %> <small></small></div>
                                </div>
                            <div class="text-center center-block" >
                                <div class="invisible">invisible</div>
                                <h2 id="money"><abbr title="Bid"><%=twoDigitsFormat.format(bet.getAmount()) %>$</abbr></h2>
                            </div>
                        </div>
                    </div>
                </div>
                <%}
                System.out.println("Sortie");%>
              
              </div> 
                <div class="row">
                <div class="panel panel-default ">
                  <div class="panel-heading"> <strong>Past bets</strong> </div>
                </div>
            </div>
           <div class="row">
            
              <%System.out.println("Sortie"); 
              for (Bet bet : losingBets){ 
              Game game = gameDao.get(bet.getGameUUID());
              int homeScore = game.getScores().getHome();
              int awayScore = game.getScores().getAway();
              Team homeTeam = teamDao.deepGet(game.getHomeTeamUUID());
              Team awayTeam = teamDao.deepGet(game.getAwayTeamUUID());
              Double gain = (bet.getAmount() * bet.getOdds());			
              double betOdds = bet.getOdds();
              %>
              
            <div class="col-lg-3 col-md-4 col-sm-6">
                    <div class="panel panel-info ">
                        <div class="panel-heading text-center"> 
                            <%=game.getDate() %>
                        </div>
            
                        <div class="panel-body">
                            <div class="btn-group text-center center-block">
                            <%BetChoice choice = BetChoice.HOME;
                            if (bet.getChoice().equals(choice)){ %>
                                <div class="btn btn-danger btn-lg col-xs-6"><%= homeTeam.getAlias()%> <small><%= twoDigitsFormat.format(betOdds) %></small></div>
                                <div class="btn btn-default btn-lg col-xs-6"><%= awayTeam.getAlias()%></div>
                                <%}else{%>
                                <div class="btn btn-default btn-lg col-xs-6"><%= homeTeam.getAlias()%></div>
                                <div class="btn btn-danger btn-lg col-xs-6"><%= awayTeam.getAlias()%> <small><%= twoDigitsFormat.format(betOdds) %></small></div>
                               <%}	%>
                                
                                
                                </div>
                            <div class="text-center center-block" >
                                <div class="invisible">invisible</div>
                                <h2 id="money"><abbr title="Gain">0</abbr></h2>
                            </div>
                        </div>
                        
                    </div>
                </div>
                <%} 
                System.out.println("Sortie");%>
                
                
               <%
              for (Bet bet : winningBets){ 
              Game game = gameDao.get(bet.getGameUUID());
              int homeScore = game.getScores().getHome();
              int awayScore = game.getScores().getAway();
              Team homeTeam = teamDao.deepGet(game.getHomeTeamUUID());
              Team awayTeam = teamDao.deepGet(game.getAwayTeamUUID());
              Double gain = (bet.getAmount() * bet.getOdds());			
              double betOdds = bet.getOdds();
              
              %>
               <div class="col-lg-3 col-md-4 col-sm-6">
                    <div class="panel panel-info ">
                        <div class="panel-heading text-center"> 
                            <%=game.getDate() %> 
                        </div>
              
                        <div class="panel-body">
                            <div class="btn-group text-center center-block">
                            <%BetChoice choice = BetChoice.HOME;
                            if (bet.getChoice().equals(choice)){ %>
                                <div class="btn btn-success btn-lg col-xs-6"><%= homeTeam.getAlias()%> <small><%= twoDigitsFormat.format(betOdds) %></small></div>
                                <div class="btn btn-default btn-lg col-xs-6"><%= awayTeam.getAlias()%></div>
                                <%}else{%>
                                <div class="btn btn-default btn-lg col-xs-6"><%= homeTeam.getAlias()%></div>
                                <div class="btn btn-success btn-lg col-xs-6"><%= awayTeam.getAlias()%> <small><%= twoDigitsFormat.format(betOdds) %></small></div>
                               <%}	%>
                                </div>
                            <div class="text-center center-block" >
                                <div class="invisible">invisible</div>
                                <h2 id="money"><abbr title="Gain"><%=twoDigitsFormat.format(gain) %></abbr></h2>
                            </div>
                        </div>
                        
                    </div>
                </div>
                <%} %>
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
   