package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.dao.GameDao;
import fr.upmc.flyingduke.utils.ServletAttributes;

@SuppressWarnings("serial")
public class HomeServlet extends HttpServlet {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		GameDao gameDao = new GameDao();
		List<Game> futureGames = gameDao.futureGames(20); 
		req.setAttribute(ServletAttributes.FUTURE_GAMES, futureGames);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/home.jsp"); 
		dispatcher.forward(req, resp);
		
	}

}
