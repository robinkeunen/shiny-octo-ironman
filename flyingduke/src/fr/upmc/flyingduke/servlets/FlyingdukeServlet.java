package fr.upmc.flyingduke.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.EntityNotFoundException;
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
import fr.upmc.flyingduke.domain.dao.FDUserDao;
import fr.upmc.flyingduke.domain.dao.GameDao;
import fr.upmc.flyingduke.domain.dao.TeamDao;
import fr.upmc.flyingduke.exceptions.ExistingUserException;
import fr.upmc.flyingduke.exceptions.MissingUUIDException;

@SuppressWarnings("serial")
public class FlyingdukeServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User googleuser = userService.getCurrentUser();

		if (googleuser != null) {
			resp.setContentType("text/plain");
			PrintWriter page = resp.getWriter();
			
			// clean accounts

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
			game.setAwayTeamUUID(away.getUUID());
			game.setHomeTeamUUID(home.getUUID());
			game.setDate(new Date());

			// user 
			FDUser fdUser = null;
			try {
				fdUser = FDUser.createFDUser(googleuser);
			} catch (ExistingUserException e1) {
				fdUser = FDUserDao.getFromGoogleUser(googleuser);
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

			//GameDao.store(game);
			//TeamDao.store(home);
			//TeamDao.store(away);
			//FDUserDao.update(fdUser);
			try {
				BetDao.update(bet);
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			try {
				page.println("\nget games, set odds, store, restore");
				String someuuid = "eccdde3e-f6ec-4f33-8f23-a269dc80b374";
				Game gameq = GameDao.get(someuuid);
				gameq.setOdds(1.25, 1.1);
				GameDao.store(gameq);
				page.println(GameDao.get(someuuid));
				
			} catch (EntityNotFoundException | MissingUUIDException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			page.println("\nupdate bet");
			try {
				Bet computedBet = BetDao.get(5910974510923776L, 5629499534213120L);
				computedBet.setComputed(true);
				BetDao.update(computedBet);
			} catch (EntityNotFoundException e) {
				page.println("bet not found");
			}
			
			page.println("\nbets for user query");
			List<Bet> betsquery = BetDao.getBetForFDUser(fdUser);
			int bqc = 0;
			for (Bet betquery: betsquery) {
				page.print(bqc + ". ");
				page.println(betquery.toString());
				bqc++;
			}
			
			page.println("\nnot computed bets for user query");
			
			List<Bet> bets2compute = BetDao.getBets2Compute(fdUser);
			int b2cc = 0;
			for (Bet betquery: bets2compute) {
				page.print(b2cc + ". ");
				page.println(betquery.toString());
				b2cc++;
			}

			FDUser fdtest = FDUserDao.getFromGoogleUser(googleuser);
			System.out.println("query test: " + fdtest.toString());


			page.println("\ngame of day query 10");
			Calendar someday = Calendar.getInstance();
			someday.setTimeZone(TimeZone.getTimeZone("America/New_York")); 

			someday.set(2014, 0, 10);
			for (Game gamequery: GameDao.gameForDay(someday)) {
				page.println(gamequery.toString());
			}
			
			page.println("\ngame from now");
			for (Game gamequery: GameDao.futureGames(20)) {
				page.println(gamequery.toString());
			}
	

		} else {
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}
	}
}
