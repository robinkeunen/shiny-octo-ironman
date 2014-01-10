package fr.upmc.flyingduke.domain.dao;

import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import fr.upmc.flyingduke.domain.Bet;
import fr.upmc.flyingduke.domain.BetChoice;
import fr.upmc.flyingduke.domain.FDUser;

public class BetDao {
	public final static String BET_KIND = "BET_KIND";
	private final static String GAME_UUID = "GAME_UUID";
	private final static String CHOICE = "CHOICE";
	private final static String AMOUNT = "AMOUNT";
	private final static String ODDS = "ODDS";
	
	private final static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


	public static Bet create(FDUser punter) {
		// create fduser and bet key
		Key punterKey = KeyFactory
				.createKey(FDUserDao.FD_USER_KIND, punter.getId());
		Entity entity = new Entity(BET_KIND, punterKey);

		// set properties
		datastore.put(entity);

		return new Bet(entity.getKey().getId(), punter.getId());
	}

	public static void update(Bet bet) throws EntityNotFoundException {
		System.out.println("put " + bet.toString());
		
		// Check if bet is in base
		Key key = createBetKey(bet.getId(), bet.getPunterID());
		Entity entity = datastore.get(key);

		// update properties
		entity.setProperty(GAME_UUID, bet.getGameUUID());
		entity.setProperty(CHOICE, bet.getChoice().toString());
		entity.setProperty(AMOUNT, bet.getAmount());
		entity.setProperty(ODDS, bet.getOdds());

		// update in base
		datastore.put(entity);	

	}

	public static Bet get(long id, long punterId) throws EntityNotFoundException {
		
		// get entity
		Key key = createBetKey(id, punterId);
		Entity entity = datastore.get(key);

		// get properties
				
		return betFromEntity(entity);
	}

	public static List<Bet> getBetForFDUser(FDUser fduser) {
		// build parent key
		Key fduserKey = KeyFactory.createKey(FDUserDao.FD_USER_KIND, fduser.getId());
		
		// build query
		Query query = new Query(BET_KIND).setAncestor(fduserKey);
		PreparedQuery pq = datastore.prepare(query);
		
		List<Bet> bets = new LinkedList<Bet>();
		for (Entity entity: pq.asIterable()) {
			bets.add(betFromEntity(entity));
		}
		
		return bets;
	}
	
	private static Key createBetKey(long betId, long punterId) {
		Key punterKey = KeyFactory
				.createKey(FDUserDao.FD_USER_KIND, punterId);
		Key key = KeyFactory
				.createKey(punterKey, BET_KIND, betId);
		return key;
	}
	
	private static Bet betFromEntity(Entity entity) {
		
		// get properties
		Object gameUUID = entity.getProperty(GAME_UUID); 
		Object choiceO = entity.getProperty(CHOICE);
		Object amountO = entity.getProperty(AMOUNT);
		Object oddsO = entity.getProperty(ODDS);
		
		// build bet
		long parentId = entity.getKey().getParent().getId();
		Bet bet = new Bet(entity.getKey().getId(), parentId);
		
		if (gameUUID != null)
			bet.setGameUUID((String) gameUUID);
		if (choiceO != null) {
			BetChoice choice = BetChoice.valueOf((String) choiceO);
			bet.setChoice(choice);
		}
		if (amountO != null) {
			int amount = ((Long) amountO).intValue();
			bet.setAmount(amount);
		}

		if (oddsO != null){
			double odds = (double) oddsO;
			bet.setOdds(odds);
		}

		return bet;
	}

}
