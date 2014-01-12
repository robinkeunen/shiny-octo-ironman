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
		ServletContext ctxt = getServletContext();
		String gameUUID = request.getParameter("gameid");

		Game game = null;;
		try {
			GameDao gameDao = new GameDao();
			game = gameDao.get(gameUUID);
			ctxt.setAttribute("game", game);
		} catch (EntityNotFoundException e) {
			response.sendRedirect("/error");
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/match.jsp"); 
		dispatcher.forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// get context attributes
		ServletContext ctxt = getServletContext();
		Boolean error = true;
		FDUser fdUser = (FDUser) ctxt.getAttribute("fdUser");
		Game game = (Game) ctxt.getAttribute("game");
		
		BetDao betDao = new BetDao();
		FDUserDao fdUserDao = new FDUserDao();
		
		Calendar now = Calendar.getInstance();
		now.setTimeZone(TimeZone.getTimeZone("America/New_York")); 
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE));
		
		//Check if the Game has started
		/*if (now.getTimeInMillis() > game.getDate().getTime()){
			ctxt.setAttribute("error", error);
			ctxt.setAttribute("errorMessage", "Game has already started. Bets are closed.");
			response.sendRedirect("/views/match.jsp");
			return;
		}*/
		
		// get form parameters
		String betValueString = request.getParameter("betValue");
		try{
			Integer betValue = Integer.parseInt(betValueString);
			if (betValue <= 0){
				ctxt.setAttribute("error", error);
				ctxt.setAttribute("errorMessage", "Bet must be over 0$");
				response.sendRedirect("/views/match.jsp");
				return;
			}else if (betValue > fdUser.getWallet()){
				ctxt.setAttribute("error", error);
				ctxt.setAttribute("errorMessage", "You don't have enough money");
				response.sendRedirect("/views/match.jsp");
				return;
			}
			Bet bet = betDao.create(fdUser);
			bet.setAmount(betValue);
			String team = request.getParameter("team");
			if (team.equalsIgnoreCase("home")){
				bet.setChoice(BetChoice.HOME);
				bet.setOdds(game.getOdds().getHome());
				System.out.println("home choisi");
			}else if(team.equalsIgnoreCase("away")){
				bet.setChoice(BetChoice.AWAY);
				bet.setOdds(game.getOdds().getAway());
			}
			bet.setComputed(false);
			bet.setGameUUID(game.getUUID());
			betDao.update(bet);
			fdUser.setWallet(fdUser.getWallet()-betValue);
			fdUserDao.update(fdUser);
			Boolean betDone = true;
			ctxt.setAttribute("betDone", betDone);
			response.sendRedirect("/views/match.jsp");
		}catch(Exception e){
			System.out.println("error Cast");
			ctxt.setAttribute("error", error);
			ctxt.setAttribute("errorMessage", "Bet must be a number");
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/match.jsp"); 
			try {
				dispatcher.forward(request,response);
			} catch (ServletException e1) {
				response.sendRedirect("/error");
			}
		}

	}


}
