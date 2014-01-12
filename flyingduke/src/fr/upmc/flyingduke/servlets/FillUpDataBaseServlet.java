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
		String xmlResult="";
		RESTQuery requestsLauncher = new RESTQuery();
		Parser parser = new Parser();

		if (action.equalsIgnoreCase("getGamesDay")){
			xmlResult = requestsLauncher.getGamesForDay();
			ArrayList<Game> gamesList = parser.parseGamesForDay(xmlResult);
			for (Game game : gamesList){
				System.out.println(game.getDate());
				try {
					GameDao.store(game);

					System.out.println(game.getUUID());
					System.out.println("NOM Home :");
					System.out.println("NOM EXTERIEUR :");
				} catch (MissingUUIDException e) {
					e.printStackTrace();
				} 
			}	

		}else if (action.equalsIgnoreCase("getAllGames")){
			xmlResult = requestsLauncher.getAllGames();
			ArrayList<Game> gamesList = parser.parseAllGames(xmlResult);
			for (Game game : gamesList){
				try {
					GameDao.store(game);
				} catch (MissingUUIDException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
					TeamDao.store(team);
					System.out.println("NAME");
					System.out.println(team.getName());
				} catch (ParserConfigurationException | SAXException e1) {
					e1.printStackTrace();
				}catch (MissingUUIDException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if(action.equalsIgnoreCase("betsComputing")){
			Calendar yesterday = Calendar.getInstance();
			List<Game> gameList;

			Calendar today = Calendar.getInstance();
			today.setTimeZone(TimeZone.getTimeZone("America/New_York")); 
			today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
			int hour = today.get(Calendar.HOUR_OF_DAY);
			System.out.println("avant le if de betComputings");
			if (hour >= 0 && hour < 6){
				//if it's before 6 A.M, fetch games of yesterday
				yesterday.add(Calendar.DATE, -1);
				gameList = GameDao.gameForDay(yesterday);  
				System.out.println(gameList);
			}else{
				//else fetch today's games
				gameList = GameDao.gameForDay(today);  
			}
			System.out.println("apres le if");
			RESTQuery queryLauncher = new RESTQuery();

			List<FDUser> usersList = FDUserDao.getAllFDUsers();
			for(Game game : gameList){
				System.out.println("UUID : " + game.getUUID());
				System.out.println("DATE : " + game.getDate());
				String xmlBoxScore = queryLauncher.getGameByUUID(game.getUUID());
				String over = parser.parseGameBoxScore(xmlBoxScore);
				//Sleep for 1s because of the API limits
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!over.equalsIgnoreCase("NotOver")){
					System.out.println("Partie terminee");
					BetChoice winningTeam = BetChoice.AWAY;
					if (over.equalsIgnoreCase("home")){
						winningTeam = BetChoice.HOME; 
						System.out.println("Victoire a domicile");
					}
					for (FDUser fdUser : usersList){
						System.out.println("USERName : " + fdUser.getFirstName());
						List<Bet> listBetsUser = BetDao.getBets2Compute(fdUser);
						for (Bet bet : listBetsUser){
							System.out.println("GAMEIDBet " + bet.getGameUUID());
							System.out.println("GameId " + game.getUUID());
							if (!bet.isComputed() && bet.getGameUUID().equalsIgnoreCase(game.getUUID())){
								System.out.println("Il y a un bet a faire d'un amount de " + bet.getAmount());
								System.out.println("winningTEAM  :" + winningTeam);
								System.out.println("betTeam  :" + bet.getChoice());
								
								bet.setComputed(true);
								if(bet.getChoice().equals(winningTeam)){
									System.out.println("Il a gagne");
									Double gainDouble = (bet.getAmount() * bet.getOdds());
									int gain = gainDouble.intValue();
									fdUser.setWallet(fdUser.getWallet() + gain);
								}
									try {
										BetDao.update(bet);
										FDUserDao.update(fdUser);
									} catch (EntityNotFoundException e) {
										System.out.println("Update of user impossible");
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

		String button = request.getParameter("button");
		String xmlResult = "";
		System.out.println(button);
		switch(button){
		case("Get All Teams"):
			xmlResult = requestsLauncher.getAllTeams();
		ArrayList<Team> teamsList = parser.parseAllTeams(xmlResult);
		for (Team team : teamsList){
			try {
				TeamDao.store(team);
			} catch (MissingUUIDException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				GameDao.store(game);
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
				GameDao.store(game);
			} catch (MissingUUIDException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/admin.jsp"); 
		dispatcher.forward(request,response);
	}
}
