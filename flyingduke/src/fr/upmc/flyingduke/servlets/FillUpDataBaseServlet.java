package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Player;
import fr.upmc.flyingduke.domain.Team;
import fr.upmc.flyingduke.domain.dao.GameDao;
import fr.upmc.flyingduke.domain.dao.TeamDao;
import fr.upmc.flyingduke.exceptions.MissingUUIDException;
import fr.upmc.flyingduke.utils.Parser;
import fr.upmc.flyingduke.utils.RESTQuery;

public class FillUpDataBaseServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
			String action = request.getParameter("action");
			String xmlResult="";
			RESTQuery requestsLauncher = new RESTQuery();
			Parser parser = new Parser();
			TeamDao teamDao = new TeamDao();
			GameDao gameDao = new GameDao();
			if (action.equalsIgnoreCase("getGamesDay")){
				xmlResult = requestsLauncher.getGamesForDay();
				ArrayList<Game> gamesList = parser.parseGamesForDay(xmlResult);
				for (Game game : gamesList){
					System.out.println(game.getDate());
					try {
						gameDao.store(game);
					} catch (MissingUUIDException e) {
						e.printStackTrace();
					}
				}	
			}else if (action.equalsIgnoreCase("getAllGames")){
				xmlResult = requestsLauncher.getAllGames();
				ArrayList<Game> gamesList = parser.parseAllGames(xmlResult);
				for (Game game : gamesList){
					try {
						gameDao.store(game);
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
						teamDao.store(team);
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
			}
			
		
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
			String month = Integer.toString(monthInt);
			xmlResult = requestsLauncher.getGamesByDate(request.getParameter("day"), request.getParameter("month"), request.getParameter("year"));
			System.out.println(dayInt + " " + monthInt + " " + yearInt);
			Date date = new Date(yearInt -1900,monthInt,dayInt);
			ArrayList<Game> gamesList = parser.parseGamesForDate(xmlResult, date);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/admin.jsp"); 
		dispatcher.forward(request,response);
	}
}
