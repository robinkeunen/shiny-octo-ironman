package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import fr.upmc.flyingduke.utils.Parser;
import fr.upmc.flyingduke.utils.RESTQuery;

@SuppressWarnings("serial")
public class AdminServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		System.out.println("JESUISLA");
		Calendar calendar = new GregorianCalendar();
		//RESTQuery queryLauncher = new RESTQuery();
		//String xmlResult = queryLauncher.getGamesForDay();
		System.out.println("Fin de la query");
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("Sleep a echoué");
		}*/
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
