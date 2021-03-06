package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.EntityNotFoundException;

import fr.upmc.flyingduke.domain.Bet;
import fr.upmc.flyingduke.domain.BetChoice;
import fr.upmc.flyingduke.domain.FDUser;
import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Player;
import fr.upmc.flyingduke.domain.Team;
import fr.upmc.flyingduke.domain.dao.BetDao;
import fr.upmc.flyingduke.domain.dao.FDUserDao;
import fr.upmc.flyingduke.domain.dao.GameDao;
import fr.upmc.flyingduke.domain.dao.TeamDao;
import fr.upmc.flyingduke.exceptions.MissingUUIDException;
import fr.upmc.flyingduke.utils.Parser;
import fr.upmc.flyingduke.utils.RESTQuery;

@SuppressWarnings("serial")
public class FillUpDataBaseServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String action = request.getParameter("action");
		String xmlResult = "";
		List<String> xmlResultList = new ArrayList<String>();
		RESTQuery requestsLauncher = new RESTQuery();
		Parser parser = new Parser();

		GameDao gameDao = new GameDao();
		TeamDao teamDao = new TeamDao();
		BetDao betDao = new BetDao();
		FDUserDao fdUserDao = new FDUserDao();

		if (action.equalsIgnoreCase("getGamesDay")){
			try {
				xmlResultList = requestsLauncher.getGamesForDay();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ArrayList<Game> gamesList = parser.parseGamesForDay(xmlResultList);
			for (Game game : gamesList){
				System.out.println(game.getDate());
				try {
					gameDao.store(game);

					System.out.println(game.getUUID());
					System.out.println("NOM Home :");
					System.out.println("NOM EXTERIEUR :");
				} catch (MissingUUIDException e) {
					request.setAttribute("exception", e);
					RequestDispatcher dispatcher = request.getRequestDispatcher("/views/servererror.jsp");
					dispatcher.forward(request, response);				} 
			}	

		}else if (action.equalsIgnoreCase("getAllGames")){
			xmlResult = requestsLauncher.getAllGames();
			ArrayList<Game> gamesList = parser.parseAllGames(xmlResult);
			for (Game game : gamesList){
				try {
					gameDao.store(game);
				} catch (MissingUUIDException e) {
					request.setAttribute("exception", e);
					RequestDispatcher dispatcher = request.getRequestDispatcher("/views/servererror.jsp");
					dispatcher.forward(request, response);				}
			}
		}else if(action.equalsIgnoreCase("getAllTeams")){
			System.out.println("GET ALL TEAMS LANCE");
			xmlResult = requestsLauncher.getAllTeams();
			ArrayList<Team> teamsList = parser.parseAllTeams(xmlResult);
			for (Team team : teamsList){
				String xmlPlayersTeam = requestsLauncher.getTeamByUUID(team.getUUID());
				try {
					Thread.sleep(1000);
					ArrayList<Player> playersList = parser.parsePlayersTeam(xmlPlayersTeam);
					team.setPlayers(playersList);
					teamDao.store(team);
					System.out.println("NAME");
					System.out.println(team.getName());
				} catch (ParserConfigurationException | SAXException e1) {
					request.setAttribute("exception", e1);
					RequestDispatcher dispatcher = request.getRequestDispatcher("/views/servererror.jsp");
					dispatcher.forward(request, response);				
				}catch (MissingUUIDException e) {
					request.setAttribute("exception", e);
					RequestDispatcher dispatcher = request.getRequestDispatcher("/views/servererror.jsp");
					dispatcher.forward(request, response);				
				} catch (InterruptedException e) {
					request.setAttribute("exception", e);
					RequestDispatcher dispatcher = request.getRequestDispatcher("/views/servererror.jsp");
					dispatcher.forward(request, response);				
				}
			}
		}else if(action.equalsIgnoreCase("betsComputing")){
			Calendar yesterday = Calendar.getInstance();
			List<Game> gameList;

			Calendar today = Calendar.getInstance();
			today.setTimeZone(TimeZone.getTimeZone("America/New_York")); 
			today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
			int hour = today.get(Calendar.HOUR_OF_DAY);
			System.out.println("HOUR + " + hour);
			if (hour >= 0 && hour < 6){
				//if it's before 6 A.M, fetch games of yesterday
				yesterday.setTimeZone(TimeZone.getTimeZone("America/New_York"));
				yesterday.add(Calendar.DATE, -1);
				gameList = gameDao.gameForDay(yesterday);  
				System.out.println(gameList);
			}else{
				//else fetch today's games
				gameList = gameDao.gameForDay(today);  
			}
			RESTQuery queryLauncher = new RESTQuery();

			List<FDUser> usersList = fdUserDao.getAllFDUsers();
			for(Game game : gameList){
				String xmlBoxScore = queryLauncher.getGameByUUID(game.getUUID());
				String over = parser.parseGameBoxScore(xmlBoxScore);
				//Sleep for 1s because of the API limits
				System.out.println("OVER ? :" + over);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					request.setAttribute("exception", e);
					RequestDispatcher dispatcher = request.getRequestDispatcher("/views/servererror.jsp");
				    dispatcher.forward(request, response);				}
				if (!over.equalsIgnoreCase("NotOver")){
					System.out.println("Partie terminee");
					BetChoice winningTeam = BetChoice.AWAY;
					game.setScores(90,100);
					if (over.equalsIgnoreCase("home")){
						winningTeam = BetChoice.HOME; 
						game.setScores(100, 90);
						System.out.println("Victoire a domicile");
					}
					for (FDUser fdUser : usersList){
						List<Bet> listBetsUser = betDao.getBets2Compute(fdUser);
						for (Bet bet : listBetsUser){
							if (!bet.isComputed() && bet.getGameUUID().equalsIgnoreCase(game.getUUID())){
								System.out.println("fdUser" + fdUser.getFirstName() + "winningTeam " + winningTeam );
								bet.setComputed(true);
								if(bet.getChoice().equals(winningTeam)){
									System.out.println("L'utilisateur a gagne son pari");
									Double gain = (bet.getAmount() * bet.getOdds());
									fdUser.setWallet(fdUser.getWallet() + gain);
								}
								try {
									betDao.update(bet);
									gameDao.store(game);
									fdUserDao.update(fdUser);
								} catch (EntityNotFoundException | MissingUUIDException e) {
									System.out.println("Update of user or Game impossible");
									request.setAttribute("exception", e);
									RequestDispatcher dispatcher = request.getRequestDispatcher("/views/servererror.jsp");
								    dispatcher.forward(request, response);
								}

							}
						}
					}
				}
			}
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("/administration");
		dispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		RESTQuery requestsLauncher = new RESTQuery();
		Parser parser = new Parser();

		GameDao gameDao = new GameDao();
		TeamDao teamDao = new TeamDao();

		String button = request.getParameter("button");
		String xmlResult = "";
		System.out.println(button);
		switch(button){
		case("Get All Teams"):
			xmlResult = requestsLauncher.getAllTeams();
		ArrayList<Team> teamsList = parser.parseAllTeams(xmlResult);
		for (Team team : teamsList){
			try {
				teamDao.store(team);
			} catch (MissingUUIDException e) {
				request.setAttribute("exception", e);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/views/servererror.jsp");
			    dispatcher.forward(request, response);			}
		}

		break;
		case("Get Games For This Day"):
			System.out.println("Get Games for Day");
		try{
			int monthInt = Integer.parseInt((request.getParameter("month")));
			int dayInt = Integer.parseInt((request.getParameter("day")));
			int yearInt = Integer.parseInt((request.getParameter("year")));
			//Month system is 0 based.
			monthInt--;
			//String month = Integer.toString(monthInt);
			xmlResult = requestsLauncher.getGamesByDate(request.getParameter("day"), request.getParameter("month"), request.getParameter("year"));
			System.out.println(dayInt + " " + monthInt + " " + yearInt);
			ArrayList<Game> gamesList = parser.parseGamesForDate(xmlResult);
			System.out.println("Fin du parser");
			for (Game game : gamesList){
				System.out.println(game.getDate());
				gameDao.store(game);
			}
			System.out.println("FIN");

		}catch (Exception e){
			System.out.println("Month is not an integer");
		}
		break;
		case("Get All Games"):
			xmlResult = requestsLauncher.getAllGames();
		ArrayList<Game> gamesList = parser.parseAllGames(xmlResult);
		System.out.println("GAMEs");
		for (Game game : gamesList){
			try {
				gameDao.store(game);
			} catch (MissingUUIDException e) {
				request.setAttribute("exception", e);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/error");
			    dispatcher.forward(request, response);			}
		}

		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/admin.jsp"); 
		dispatcher.forward(request,response);
	}
}
