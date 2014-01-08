package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Team;
import fr.upmc.flyingduke.domain.dao.GameDao;

@SuppressWarnings("serial")
public class FlyingdukeServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {
        	
        	// tests
        	Game game = new Game(UUID.randomUUID().toString());
        	Team away = new Team("583ecd4f-fb46-11e1-82cb-f4ce4684ea4c");
        	Team home = new Team("583ecf50-fb46-11e1-82cb-f4ce4684ea4c");
        	game.setAwayTeam(away);
        	game.setHomeTeam(home);
        	game.setDate(new Date());
        	
        	GameDao.store(game);
        	
        	
        	
            resp.setContentType("text/plain");
        	PrintWriter page = resp.getWriter();
            page.println("Hello, " + user.getNickname());
            page.println("store et get tests");
            
        } else {
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }
	}
}
