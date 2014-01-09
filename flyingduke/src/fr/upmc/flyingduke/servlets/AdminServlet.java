package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import fr.upmc.flyingduke.Utils.RESTQuery;

@SuppressWarnings("serial")
public class AdminServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user == null) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		} 
		else {
			ServletContext pageContext = getServletContext();
			System.out.println("Administration");
			pageContext.setAttribute("user", user);
			String valeur = "eccdde3e-f6ec-4f33-8f23-a269dc80b374";
			RESTQuery classeTest = new RESTQuery();
			String resultat = classeTest.getGamesByDate("17", "01", "2014");
			System.out.println("resultat " + resultat);
			// get calendar for today
			Calendar calendar = Calendar.getInstance();
			calendar.set(2014, 0, 9); 
			// TODO uncomment when making real requests
			//String xml = (new RESTQuery()).getGamesForDay(calendar);
			
			// "-paste here-" is a string literal you must allow it in 
			// preferences -> java -> Editor -> Typing -> "Escape text when pasting into a string literal"
			String xml = "-<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- Generation started @ 2014-01-08 06:24:21 +0000 --><league xmlns=\"http://feed.elasticstats.com/schema/basketball/schedule-v2.0.xsd\" id=\"4353138d-4c22-4396-95d8-5f587d2df25c\" name=\"NBA\" alias=\"NBA\">  <daily-schedule date=\"2014-01-08\">    <games>      <game id=\"28a4f54d-b7a2-48dd-bdf5-86d29ffb16d1\" status=\"scheduled\" coverage=\"full\" home_team=\"583ecd4f-fb46-11e1-82cb-f4ce4684ea4c\" away_team=\"583ecf50-fb46-11e1-82cb-f4ce4684ea4c\" scheduled=\"2014-01-09T00:00:00+00:00\">        <venue id=\"8a7580c0-4052-54d5-85cd-85aab6200acf\" name=\"AT&amp;T Center\" capacity=\"18581\" address=\"One AT&amp;T Center Parkway\" city=\"San Antonio\" state=\"TX\" zip=\"78219\" country=\"USA\"/>        <home name=\"San Antonio Spurs\" alias=\"SAS\" id=\"583ecd4f-fb46-11e1-82cb-f4ce4684ea4c\">        </home>        <away name=\"Dallas Mavericks\" alias=\"DAL\" id=\"583ecf50-fb46-11e1-82cb-f4ce4684ea4c\">        </away>        <broadcast network=\"ESPN\" satellite=\"206\" internet=\"WatchESPN\"/>      </game>      <game id=\"5dcacc49-7344-4723-a60c-dde8e8789f9b\" status=\"scheduled\" coverage=\"full\" home_team=\"583ecda6-fb46-11e1-82cb-f4ce4684ea4c\" away_team=\"583ec928-fb46-11e1-82cb-f4ce4684ea4c\" scheduled=\"2014-01-09T00:00:00+00:00\">        <venue id=\"62cc9661-7b13-56e7-bf4a-bba7ad7be8da\" name=\"Air Canada Centre\" capacity=\"19800\" address=\"40 Bay St.\" city=\"Toronto\" state=\"ON\" zip=\"M5J 2X2\" country=\"CA\"/>        <home name=\"Toronto Raptors\" alias=\"TOR\" id=\"583ecda6-fb46-11e1-82cb-f4ce4684ea4c\">        </home>        <away name=\"Detroit Pistons\" alias=\"DET\" id=\"583ec928-fb46-11e1-82cb-f4ce4684ea4c\">        </away>        <broadcast network=\"NBA LP\" satellite=\"751-1\"/>      </game>      <game id=\"44f9e156-ccb5-4aeb-999e-990149f3a6be\" status=\"scheduled\" coverage=\"full\" home_team=\"583ec9d6-fb46-11e1-82cb-f4ce4684ea4c\" away_team=\"583ec825-fb46-11e1-82cb-f4ce4684ea4c\" scheduled=\"2014-01-09T00:30:00+00:00\">        <venue id=\"7a330bcd-ac0f-50ca-bc29-2460e5c476b3\" name=\"Barclays Center\" capacity=\"18200\" address=\"620 Atlantic Avenue.\" city=\"Brooklyn\" state=\"NY\" zip=\"11217\" country=\"USA\"/>        <home name=\"Brooklyn Nets\" alias=\"BKN\" id=\"583ec9d6-fb46-11e1-82cb-f4ce4684ea4c\">        </home>        <away name=\"Golden State Warriors\" alias=\"GSW\" id=\"583ec825-fb46-11e1-82cb-f4ce4684ea4c\">        </away>        <broadcast network=\"NBA LP\" satellite=\"755-1\"/>      </game>      <game id=\"b7a9e4a3-a92b-4d9f-a328-91f3947b8db8\" status=\"scheduled\" coverage=\"full\" home_team=\"583ecb8f-fb46-11e1-82cb-f4ce4684ea4c\" away_team=\"583ec7cd-fb46-11e1-82cb-f4ce4684ea4c\" scheduled=\"2014-01-09T00:30:00+00:00\">        <venue id=\"fd21f639-8a47-51ac-a5dd-590629d445cf\" name=\"Philips Arena\" capacity=\"18729\" address=\"One Philips Drive\" city=\"Atlanta\" state=\"GA\" zip=\"30303\" country=\"USA\"/>        <home name=\"Atlanta Hawks\" alias=\"ATL\" id=\"583ecb8f-fb46-11e1-82cb-f4ce4684ea4c\">        </home>        <away name=\"Indiana Pacers\" alias=\"IND\" id=\"583ec7cd-fb46-11e1-82cb-f4ce4684ea4c\">        </away>        <broadcast network=\"NBA LP\" satellite=\"753-1\"/>      </game>      <game id=\"4ac7a063-8c7c-4f90-8552-827954b7593f\" status=\"scheduled\" coverage=\"full\" home_team=\"583ecc9a-fb46-11e1-82cb-f4ce4684ea4c\" away_team=\"583ec8d4-fb46-11e1-82cb-f4ce4684ea4c\" scheduled=\"2014-01-09T01:00:00+00:00\">        <venue id=\"6ec47e31-f778-5319-b6c4-3254e076ba0e\" name=\"New Orleans Arena\" capacity=\"17188\" address=\"1501 Girod St.\" city=\"New Orleans\" state=\"LA\" zip=\"70113\" country=\"USA\"/>        <home name=\"New Orleans Pelicans\" alias=\"NOP\" id=\"583ecc9a-fb46-11e1-82cb-f4ce4684ea4c\">        </home>        <away name=\"Washington Wizards\" alias=\"WAS\" id=\"583ec8d4-fb46-11e1-82cb-f4ce4684ea4c\">        </away>        <broadcast network=\"NBA LP\" satellite=\"757-1\"/>      </game>      <game id=\"a3db8692-8e18-4f27-91b2-82adbf7a96f7\" status=\"scheduled\" coverage=\"full\" home_team=\"583ecb3a-fb46-11e1-82cb-f4ce4684ea4c\" away_team=\"583ecae2-fb46-11e1-82cb-f4ce4684ea4c\" scheduled=\"2014-01-09T01:00:00+00:00\">        <venue id=\"5b239206-57ce-50aa-baaa-627f3349dfdc\" name=\"Toyota Center\" capacity=\"18043\" address=\"1510 Polk St.\" city=\"Houston\" state=\"TX\" zip=\"77002\" country=\"USA\"/>        <home name=\"Houston Rockets\" alias=\"HOU\" id=\"583ecb3a-fb46-11e1-82cb-f4ce4684ea4c\">        </home>        <away name=\"Los Angeles Lakers\" alias=\"LAL\" id=\"583ecae2-fb46-11e1-82cb-f4ce4684ea4c\">        </away>        <broadcast network=\"NBA LP\" satellite=\"759-1\"/>      </game>      <game id=\"c6118361-daf6-42c2-94b8-7b18a09a01ea\" status=\"scheduled\" coverage=\"full\" home_team=\"583eca2f-fb46-11e1-82cb-f4ce4684ea4c\" away_team=\"583ecfa8-fb46-11e1-82cb-f4ce4684ea4c\" scheduled=\"2014-01-09T02:30:00+00:00\">        <venue id=\"7aed802e-3562-5b73-af1b-3859529f9b95\" name=\"Target Center\" capacity=\"19356\" address=\"600 First Ave. North\" city=\"Minneapolis\" state=\"MN\" zip=\"55403\" country=\"USA\"/>        <home name=\"Minnesota Timberwolves\" alias=\"MIN\" id=\"583eca2f-fb46-11e1-82cb-f4ce4684ea4c\">        </home>        <away name=\"Phoenix Suns\" alias=\"PHX\" id=\"583ecfa8-fb46-11e1-82cb-f4ce4684ea4c\">        </away>        <broadcast network=\"ESPN\" satellite=\"206\" internet=\"WatchESPN\"/>      </game>      <game id=\"676e3c35-1545-409a-b061-43aa0d71312a\" status=\"scheduled\" coverage=\"full\" home_team=\"583ed056-fb46-11e1-82cb-f4ce4684ea4c\" away_team=\"583ed157-fb46-11e1-82cb-f4ce4684ea4c\" scheduled=\"2014-01-09T03:00:00+00:00\">        <venue id=\"1d1f74a2-7b35-56f0-8cbd-552c51cb2c14\" name=\"Moda Center at the Rose Quarter\" capacity=\"19980\" address=\"1 Center Court\" city=\"Portland\" state=\"OR\" zip=\"97227\" country=\"USA\"/>        <home name=\"Portland Trail Blazers\" alias=\"POR\" id=\"583ed056-fb46-11e1-82cb-f4ce4684ea4c\">        </home>        <away name=\"Orlando Magic\" alias=\"ORL\" id=\"583ed157-fb46-11e1-82cb-f4ce4684ea4c\">        </away>        <broadcast network=\"NBA LP\" satellite=\"761-1\"/>      </game>      <game id=\"ceb4a587-32fb-400d-b74b-0c44f3850034\" status=\"scheduled\" coverage=\"full\" home_team=\"583ecdfb-fb46-11e1-82cb-f4ce4684ea4c\" away_team=\"583eccfa-fb46-11e1-82cb-f4ce4684ea4c\" scheduled=\"2014-01-09T03:30:00+00:00\">        <venue id=\"792ec100-691e-5e16-8ef8-79b2b6ee38ba\" name=\"Staples Center\" capacity=\"18997\" address=\"1111 S. Figueroa St.\" city=\"Los Angeles\" state=\"CA\" zip=\"90015\" country=\"USA\"/>        <home name=\"Los Angeles Clippers\" alias=\"LAC\" id=\"583ecdfb-fb46-11e1-82cb-f4ce4684ea4c\">        </home>        <away name=\"Boston Celtics\" alias=\"BOS\" id=\"583eccfa-fb46-11e1-82cb-f4ce4684ea4c\">        </away>        <broadcast network=\"NBA LP\" satellite=\"763-1\"/>      </game>    </games>  </daily-schedule></league><!-- Generation ended @ 2014-01-08 06:24:21 +0000 -->-";
			
			// write response
			//response.setContentType("text/plain");
        	//PrintWriter page = response.getWriter();
            //page.println(xml);


			//RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/admin.jsp"); 
			//dispatcher.forward(request,response);

		} // login else

	}
}
