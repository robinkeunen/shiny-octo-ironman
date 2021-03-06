package fr.upmc.flyingduke.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import fr.upmc.flyingduke.domain.FDUser;
import fr.upmc.flyingduke.domain.dao.FDUserDao;
import fr.upmc.flyingduke.exceptions.ExistingUserException;

@SuppressWarnings("serial")
public class CreateUserServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		System.out.println("CreateUserServlet:post");
		UserService userService = UserServiceFactory.getUserService();
		User googleUser = userService.getCurrentUser();
		System.out.println(googleUser.getEmail());

		try {
			FDUserDao fdUserDao = new FDUserDao();
			FDUser user = fdUserDao.create(googleUser);
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			user.setFirstName(firstName);
			user.setLastName(lastName);
			double wallet = 100.0;
			user.setWallet(wallet);
			fdUserDao.update(user);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/views/home.jsp");
		    dispatcher.forward(request, response);
		} catch (ExistingUserException | EntityNotFoundException e) {
			request.setAttribute("exception", e);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/views/servererror.jsp");
		    dispatcher.forward(request, response);
		}
	}	
}
