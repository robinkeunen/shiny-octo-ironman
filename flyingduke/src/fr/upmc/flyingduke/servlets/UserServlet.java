package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import fr.upmc.flyingduke.domain.Bet;
import fr.upmc.flyingduke.domain.FDUser;
import fr.upmc.flyingduke.domain.dao.BetDao;
import fr.upmc.flyingduke.domain.dao.FDUserDao;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(":UserServlet");
		
		FDUserDao fdUserDao = new FDUserDao();
		BetDao betDao = new BetDao();
		
		FDUser fdUser = null;
		UserService userService = UserServiceFactory.getUserService();
		User googleUser = userService.getCurrentUser();
		if (googleUser == null){
			try {
				response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
			} catch (IOException e) {
				try {
					response.sendRedirect("/error");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			return;
		}
		System.out.println("MAIL : " + googleUser.getEmail());
		if ((fdUser = fdUserDao.getFromGoogleUser(googleUser)) == null){
			try {
				response.sendRedirect("/views/createUser.jsp");
			} catch (IOException e) {
				try {
					response.sendRedirect("/error");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			return;
		}
		
		ServletContext ctxt = getServletContext();
		ctxt.setAttribute("fdUser", fdUser);
		
		List<Bet> bets = betDao.getBetForFDUser(fdUser);
		List<Bet> futureBets = new LinkedList<Bet>();
		List<Bet> pastBets =  new LinkedList<Bet>();
		
		for (Bet bet: bets) {
			if (bet.isComputed()) 
				pastBets.add(bet);
			else 
				futureBets.add(bet);
		}
		
		System.out.println("UserServlet:futurebets " + futureBets);
		System.out.println("UserServlet:pastbets " + pastBets);

		request.setAttribute("futurebets", futureBets);
		request.setAttribute("pastbets", pastBets);
		
		try {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/views/user.jsp");
		    dispatcher.forward(request, response);

		} catch (IOException | ServletException e) {
			try {
				response.sendRedirect("/error");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
