package guestbook;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class GuestbookServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7759593256585062849L;

	@Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	UserService userService = UserServiceFactory.getUserService();
    	User user = userService.getCurrentUser(); 
    			
    	if (user != null) {
    		resp.setContentType("text/plain");
    		resp.getWriter().println("Hello, world");
    		String domain = "http://127.0.0.1:8888";
    		resp.getWriter().println(domain + userService.createLogoutURL(req.getRequestURI()));
    	} else {
    		resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
    	}
    }
}