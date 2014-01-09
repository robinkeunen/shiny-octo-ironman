package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Player;
import fr.upmc.flyingduke.domain.Team;
import fr.upmc.flyingduke.domain.dao.GameDao;
import fr.upmc.flyingduke.domain.dao.TeamDao;
import fr.upmc.flyingduke.exceptions.MissingUUIDException;

@SuppressWarnings("serial")
public class FlyingdukeServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {
        	
        	// tests data
        	
        	// players
        	Player p1 = new Player(UUID.randomUUID().toString());
        	p1.setFirstName("Robin");
        	p1.setLastName("Keunen");
        	p1.setPosition("AL");
        	Player p2 = new Player(UUID.randomUUID().toString());
        	p2.setFirstName("Antoine");
        	p2.setLastName("Flinois");
        	p2.setPosition("GJ");
        	Player p3 = new Player(UUID.randomUUID().toString());
        	p3.setFirstName("Reda");
        	p3.setLastName("Bendraou");
        	p3.setPosition("QL");
        	Player p4 = new Player(UUID.randomUUID().toString());
        	p4.setFirstName("Vincent");
        	p4.setLastName("Google");
        	p4.setPosition("YT");
        	Player p5 = new Player(UUID.randomUUID().toString());
        	p5.setFirstName("Mustafa");
        	p5.setLastName("Champignon");
        	p5.setPosition("AL");
        	
        	List<Player> plist1 = new LinkedList<Player>();
        	plist1.add(p1); plist1.add(p2); plist1.add(p3);
        	List<Player> plist2 = new LinkedList<Player>();
        	plist2.add(p4); plist2.add(p5); plist2.add(p3);   
        	
        	// teams
        	Team away = new Team("583ecd4f-fb46-11e1-82cb-f4ce4684ea4c");
        	away.setName("Californian Avengers");
        	away.setAlias("CLA");
        	away.setPlayers(plist1);
        	
        	Team home = new Team("583ecf50-fb46-11e1-82cb-f4ce4684ea4c");
        	home.setName("San Francisco Giants");
        	home.setAlias("SFG");
        	home.setPlayers(plist2);
        	
        	Game game = new Game(UUID.randomUUID().toString());
        	game.setAwayTeam(away);
        	game.setHomeTeam(home);
        	game.setDate(new Date());
        	
        	
        	try {
				GameDao.store(game);
				TeamDao.store(home);
	        	TeamDao.store(away);
			} catch (MissingUUIDException e) {
				e.printStackTrace();
			}
        	
        	
            resp.setContentType("text/plain");
        	PrintWriter page = resp.getWriter();
            page.println("Hello, " + user.getNickname());
            page.println("store et get tests");
            
        } else {
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }
	}
}
