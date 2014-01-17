package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.EntityNotFoundException;

import fr.upmc.flyingduke.domain.FDUser;
import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.dao.FDUserDao;
import fr.upmc.flyingduke.domain.dao.GameDao;
import fr.upmc.flyingduke.domain.Bet;
import fr.upmc.flyingduke.domain.BetChoice;
import fr.upmc.flyingduke.domain.dao.BetDao;

@SuppressWarnings("serial")
public class MatchServlet extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		System.out.println("Je passe ici");
		String gameUUID = request.getParameter("gameid");

		Game game = null;
		try {
			GameDao gameDao = new GameDao();
			game = gameDao.get(gameUUID);
			request.setAttribute("game", game);
		} catch (EntityNotFoundException e) {
			request.setAttribute("exception", e);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/views/servererror.jsp");
			dispatcher.forward(request, response);
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/match.jsp"); 
		dispatcher.forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// get context attributes
		GameDao gameDao = new GameDao();
		FDUserDao userDao = new FDUserDao();
		Boolean error = true;
		Game game = null;
		try{
			BetDao betDao = new BetDao();
			FDUserDao fdUserDao = new FDUserDao();
			String[] userIdStr = request.getParameterValues("userId");
			String[] gameId = request.getParameterValues("gameId");
			//Get User
			double userId = Double.valueOf(userIdStr[0]);
			FDUser fdUser= userDao.get((long) userId);
			//Get Game
			game = gameDao.get(gameId[0]);
			request.setAttribute("game", game);
			//Set up Calendar
			Calendar now = Calendar.getInstance();
			now.setTimeZone(TimeZone.getTimeZone("America/New_York")); 
			now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE));
			//Set Up Request Dispatcher
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/match.jsp"); 

			//Check if the Game has started
			
			if (now.getTimeInMillis() > game.getDate().getTime()){
				request.setAttribute("error", error);
				request.setAttribute("errorMessage", "Game has already started. Bets are closed.");
				dispatcher.forward(request,response);
				return;
			}
	
			// get Bet amount
			String betValueString = request.getParameter("betValue");
			Double betValue = Double.parseDouble(betValueString);

			//Check if the bet amount is correct
			if (betValue <= 0 || betValue > fdUser.getWallet()){
				request.setAttribute("error", error);
				request.setAttribute("errorMessage", "Bet must be over 0$");
				if (betValue > fdUser.getWallet()){
					request.setAttribute("errorMessage", "You don't have enough money");
				}
				dispatcher.forward(request,response);
				return;
			}
			//Create Bet with the amount given by the user
			Bet bet = betDao.create(fdUser);
			bet.setAmount(betValue);
			String team = request.getParameter("team");
			//Set up Bet depending on the user's choice
			if (team.equalsIgnoreCase("home")){
				bet.setChoice(BetChoice.HOME);
				bet.setOdds(game.getOdds().getHome());
			}else if(team.equalsIgnoreCase("away")){
				bet.setChoice(BetChoice.AWAY);
				bet.setOdds(game.getOdds().getAway());
			}
			//Bet can't be computed anymore
			bet.setComputed(false);
			bet.setGameUUID(game.getUUID());
			betDao.update(bet);
			//Update user with the new Bet
			fdUser.setWallet(fdUser.getWallet()-betValue);
			fdUserDao.update(fdUser);
			//Set betDone attribute to inform user that his bet has been registered
			Boolean betDone = true;
			//Redirection to the game's page
			request.setAttribute("betDone", betDone);
			dispatcher.forward(request,response);
		}catch(Exception e){
			request.setAttribute("error", error);
			request.setAttribute("errorMessage", "Bet must be a number");
			request.setAttribute("game", game);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/match.jsp"); 
			try {
				dispatcher.forward(request,response);
			} catch (ServletException e1) {
				request.setAttribute("exception", e1);
				RequestDispatcher dispatcher1 = request.getRequestDispatcher("/views/servererror.jsp");
				try {
					dispatcher1.forward(request, response);
				} catch (ServletException e2) {
					e2.printStackTrace();
				}		
			}
		}

	}


}
