package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

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

import fr.upmc.flyingduke.Utils.RESTQuery;

@SuppressWarnings("serial")
public class AdminServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

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
