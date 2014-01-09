package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import fr.upmc.flyingduke.Utils.Parser;
import fr.upmc.flyingduke.Utils.RESTQuery;
import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Team;
import fr.upmc.flyingduke.domain.dao.GameDao;
import fr.upmc.flyingduke.domain.dao.TeamDao;
import fr.upmc.flyingduke.exceptions.MissingUUIDException;

public class FillUpDataBaseServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
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
			xmlResult = requestsLauncher.getGamesByDate(request.getParameter("day"), month, request.getParameter("year"));
			System.out.println(xmlResult);
			Date date = new Date(dayInt,monthInt,yearInt);
			ArrayList<Game> gamesList = parser.parseGamesForDate(xmlResult, date);
			System.out.println("Fin du parser");
			for (Game game : gamesList){
				gameDao.store(game);
			}
			
			}catch (Exception e){
				System.out.println("Month is not an integer");
			}
			
		}
	}
}
