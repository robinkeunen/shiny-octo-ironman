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
		System.out.println("lalalaal");
		System.out.println("ici");
		System.out.println("azerty");
		System.out.println(request.getParameter("game"));
		String gameUUID = request.getParameter("gameid");
		GameDao gameDao = new GameDao();
		Game game;
		try {
			game = gameDao.deepGet(gameUUID);
			System.out.println("PLAYERS SERVLET :" + game.getAwayTeam().getPlayers());
			ctxt.setAttribute("game", game);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("apres");
		response.sendRedirect("views/match.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletContext ctxt = getServletContext();
		Boolean erreur = true;
		FDUser fdUser = (FDUser) ctxt.getAttribute("fdUser");
		FDUserDao fdUserDao = new FDUserDao();
		Game game = (Game) ctxt.getAttribute("game");
		System.out.println("MatchDoPost");
		String betValueString = request.getParameter("betValue");
		try{
			Integer betValue = Integer.parseInt(betValueString);
			if (betValue <= 0 || betValue > fdUser.getWallet()){
				System.out.println("erreur Servlet");
				ctxt.setAttribute("erreur", erreur);
				response.sendRedirect("/views/match.jsp");
				return;
			}
			BetDao betDao = new BetDao();
			Bet bet = betDao.create(fdUser);
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
			betDao.update(bet);
			fdUser.setWallet(fdUser.getWallet()-betValue);
			fdUserDao.update(fdUser);
			response.sendRedirect("/views/home.jsp");
		}catch(Exception e){
			System.out.println("erreur Cast");
			ctxt.setAttribute("erreur", erreur);
			response.sendRedirect("/views/match.jsp");
		}

	}


}
