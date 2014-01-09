package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import fr.upmc.flyingduke.domain.Bet;
import fr.upmc.flyingduke.domain.BetChoice;
import fr.upmc.flyingduke.domain.FDUser;
import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Player;
import fr.upmc.flyingduke.domain.Team;
import fr.upmc.flyingduke.domain.dao.BetDao;
import fr.upmc.flyingduke.domain.dao.GameDao;
import fr.upmc.flyingduke.domain.dao.TeamDao;
import fr.upmc.flyingduke.domain.dao.FDUserDao;
import fr.upmc.flyingduke.exceptions.ExistingUserException;
import fr.upmc.flyingduke.exceptions.MissingUUIDException;

@SuppressWarnings("serial")
public class FlyingdukeServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User googleuser = userService.getCurrentUser();

		if (googleuser != null) {

			// clean accounts
			FDUserDao.deleteUser(googleuser);
			
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

			// game
			Game game = new Game(UUID.randomUUID().toString());
			game.setAwayTeam(away);
			game.setHomeTeam(home);
			game.setDate(new Date());

			// user 
			FDUser fdUser = null;
			try {
				fdUser = FDUser.createFDUser(googleuser);
			} catch (ExistingUserException e1) {
				e1.printStackTrace();
			}
			fdUser.setFirstName("Robin");
			fdUser.setLastName("Keunen");
			fdUser.setWallet(100);

			// bet
			Bet bet = Bet.createBet(fdUser);
			bet.setAmount(23);
			bet.setChoice(BetChoice.AWAY);
			bet.setGameUUID(game.getUUID());
			bet.setOdds(1.5);

			// stores 
			try {
				GameDao.store(game);
				TeamDao.store(home);
				TeamDao.store(away);
				FDUserDao.update(fdUser);
				BetDao.update(bet);
				BetDao.get(bet.getId(), bet.getPunterID());

				FDUser fdtest = FDUserDao.getFromGoogleUser(googleuser);
				System.out.println("query test: " + fdtest.toString());
			} catch (MissingUUIDException e) {
				e.printStackTrace();
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			resp.setContentType("text/plain");
			PrintWriter page = resp.getWriter();
			page.println("Hello, " + googleuser.getNickname());
			page.println("store et get tests");
			page.println(userService.createLogoutURL(req.getRequestURI()));

		} else {
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}
	}
}
