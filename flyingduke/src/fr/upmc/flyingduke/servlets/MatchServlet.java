package fr.upmc.flyingduke.servlets;

import java.io.IOException;

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
		System.out.println("DOGET MATCH!!");
		ServletContext ctxt = getServletContext();

		System.out.println(request.getParameter("game"));
		String gameUUID = request.getParameter("gameid");

		Game game = null;;
		try {
			game = GameDao.get(gameUUID);
			ctxt.setAttribute("game", game);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("apres");
		response.sendRedirect("views/match.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// get context attributes
		ServletContext ctxt = getServletContext();
		Boolean error = true;
		FDUser fdUser = (FDUser) ctxt.getAttribute("fdUser");
		Game game = (Game) ctxt.getAttribute("game");
		
		// get form parameters
		System.out.println("\n\nMatch:DoPost");
		String betValueString = request.getParameter("betValue");
		try{
			Integer betValue = Integer.parseInt(betValueString);
			if (betValue <= 0 || betValue > fdUser.getWallet()){
				System.out.println("MatchServlet: wrong value, user has " + fdUser.getWallet());
				ctxt.setAttribute("error", error);
				response.sendRedirect("/views/match.jsp");
				return;
			}
			Bet bet = BetDao.create(fdUser);
			bet.setAmount(betValue);
			String team = request.getParameter("team");
			if (team.equalsIgnoreCase("home")){
				bet.setChoice(BetChoice.HOME);
				bet.setOdds(game.getOdds().getHome());
				System.out.println("home choisi");
			}else if(team.equalsIgnoreCase("away")){
				bet.setChoice(BetChoice.AWAY);
				System.out.println("away choisi");
				bet.setOdds(game.getOdds().getAway());
			}
			bet.setComputed(false);
			bet.setGameUUID(game.getUUID());
			System.out.println(game.getUUID());
			BetDao.update(bet);
			fdUser.setWallet(fdUser.getWallet()-betValue);
			FDUserDao.update(fdUser);
			response.sendRedirect("/views/home.jsp");
		}catch(Exception e){
			System.out.println("error Cast");
			ctxt.setAttribute("error", error);
			response.sendRedirect("/views/match.jsp");
		}

	}


}
