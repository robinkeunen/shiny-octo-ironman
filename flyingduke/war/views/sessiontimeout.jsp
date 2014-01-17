<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="fr.upmc.flyingduke.domain.FDUser" %>
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

    <title>Error</title>

    <!-- Bootstrap core CSS -->
    <link href="/stylesheets/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/stylesheets/custom.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]><script src="../../docs-assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>

<body>
<% System.out.println("servererror.jsp"); %>
    <div class="container invisible">
		<div class="jumbotron">			
		</div>
	</div>
    
    
	<div class="container">
		<div class="jumbotron">
			<h1>Session timeout</h1>
			Snap, your session timed out. Please login again.
			
		</div>
	</div>

<!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>

</body>
</html>