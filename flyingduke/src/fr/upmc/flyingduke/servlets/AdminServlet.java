package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AdminServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		System.out.println("AdminServlet: JESUISLA");

		System.out.println("Fin de la query");

		System.out.println("apres sleep");

		//Parser parser = new Parser();
		//parser.parseGamesForDay(xmlResult);
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user == null) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		} 
		else {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/views/admin.jsp"); 
			dispatcher.forward(request,response);

		} // login else

	}
}
