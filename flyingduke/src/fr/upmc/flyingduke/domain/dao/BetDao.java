package fr.upmc.flyingduke.domain.dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
		String gameUUID = (String) entity.getProperty(GAME_UUID);
		BetChoice choice = BetChoice.valueOf(
				(String) entity.getProperty(CHOICE));
		int amount = ((Long) entity.getProperty(AMOUNT)).intValue();
		double odds = (double) entity.getProperty(ODDS);
		
		// build bet
		Bet bet = new Bet(id, punterId);
		bet.setGameUUID(gameUUID);
		bet.setChoice(choice);
		bet.setAmount(amount);
		bet.setOdds(odds);
		
		return bet;
	}

	private static Key createBetKey(long betId, long punterId) {
		Key punterKey = KeyFactory
				.createKey(FDUserDao.FD_USER_KIND, punterId);
		Key key = KeyFactory
				.createKey(punterKey, BET_KIND, betId);
		return key;
	}

}
