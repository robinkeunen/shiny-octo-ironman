package fr.upmc.flyingduke.domain.dao;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

import fr.upmc.flyingduke.domain.Bet;
import fr.upmc.flyingduke.domain.BetChoice;
import fr.upmc.flyingduke.domain.FDUser;

public class BetDao {
	public final static String BET_KIND = "BET_KIND";
	private final static String GAME_UUID = "GAME_UUID";
	private final static String CHOICE = "CHOICE";
	private final static String AMOUNT = "AMOUNT";
	private final static String ODDS = "ODDS";
	private final static String COMPUTED = "COMPUTED";
	private final static String DATE = "DATE";

	private final static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


	/**
	 * A bet must be instantiated through the datastore to get an id.
	 * The conputed field is set to false by default.  
	 * @param punter the fduser placing the bet
	 * @return
	 */
	public Bet create(FDUser punter) {
		// create fduser and bet key
		Key punterKey = KeyFactory
				.createKey(FDUserDao.FD_USER_KIND, punter.getId());
		Entity entity = new Entity(BET_KIND, punterKey);
		entity.setProperty(COMPUTED, false);

		// set properties
		datastore.put(entity);

		return new Bet(entity.getKey().getId(), punter.getId());
	}

	/**
	 * Updates the bet at the bet's id in the datastore. The id of bet
	 * must be created through BetDao.create 
	 * @param bet the Bet to save
	 * @throws EntityNotFoundException thrown if the bet has not been created in base.
	 */
	public void update(Bet bet) throws EntityNotFoundException {
		System.out.println("put " + bet.toString());

		// Check if bet is in base
		Key key = createBetKey(bet.getId(), bet.getPunterID());
		Entity entity = datastore.get(key);

		// update properties
		entity.setProperty(GAME_UUID, bet.getGameUUID());
		entity.setProperty(CHOICE, bet.getChoice().toString());
		entity.setProperty(AMOUNT, bet.getAmount());
		entity.setProperty(ODDS, bet.getOdds());
		entity.setProperty(COMPUTED, bet.isComputed());
		if (bet.getDate() != null)
			entity.setProperty(DATE, bet.getDate());

		// update in base
		datastore.put(entity);	

	}

	/**
	 * Retrieves a bet in the datastore for the given parameters 
	 * @param id the bet id
	 * @param punterId the fduser who placed the bet
	 * @return the bet in the datastore for the given parameters
	 * @throws EntityNotFoundException thrown if the fduser for the punterId
	 * does not exist in the base
	 */
	public Bet get(long id, long punterId) throws EntityNotFoundException {

		// get entity
		Key key = createBetKey(id, punterId);
		Entity entity = datastore.get(key);

		// get properties

		return betFromEntity(entity);
	}

	/**
	 * Retrieves all the bets for the given user.
	 * @param fduser the fduser who placed the bet
	 * @return a list of all the bets for the given user
	 */
	public List<Bet> getBetForFDUser(FDUser fduser) {
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

	/**
	 * Retrieves all the bets that have not been computed for the given user.
	 * @param fduser the fduser who placed the bet
	 * @return a list of all the bets for the given user where computed == false
	 */
	public List<Bet> getBets2Compute(FDUser fduser) {
		Key fduserKey = KeyFactory.createKey(FDUserDao.FD_USER_KIND, fduser.getId());

		// build filter
		FilterPredicate filter = new FilterPredicate(
				COMPUTED, 
				FilterOperator.EQUAL, 
				false);
		
		// build query
		Query query = new Query(BET_KIND).setAncestor(fduserKey).setFilter(filter);
		PreparedQuery pq = datastore.prepare(query);

		List<Bet> bets = new LinkedList<Bet>();
		for (Entity entity: pq.asIterable()) {
			bets.add(betFromEntity(entity));
		}

		return bets;
	}
	
	public List<Bet> getBetsForTeam(String uuid, int max) {
		Filter filter = new FilterPredicate(
				GAME_UUID, 
				FilterOperator.EQUAL, 
				uuid);
		
		Query query = new Query(BET_KIND).setAncestor(FDUserDao.ancestor);
		query.setFilter(filter);
		query.addSort(DATE, SortDirection.DESCENDING);
		
		PreparedQuery pq = datastore.prepare(query);
		
		List<Bet> bets = new LinkedList<>();
		for (Entity entity: pq.asIterable(FetchOptions.Builder.withLimit(max))) {
			Bet bet = betFromEntity(entity);
			bets.add(bet);
		}
		
		return bets;
	}


	private Key createBetKey(long betId, long punterId) {
		Key punterKey = KeyFactory
				.createKey(FDUserDao.FD_USER_KIND, punterId);
		Key key = KeyFactory
				.createKey(punterKey, BET_KIND, betId);
		return key;
	}

	private Bet betFromEntity(Entity entity) {

		// get properties
		Object gameUUID = entity.getProperty(GAME_UUID); 
		Object choiceO = entity.getProperty(CHOICE);
		Object amountO = entity.getProperty(AMOUNT);
		Object oddsO = entity.getProperty(ODDS);
		Object computed = entity.getProperty(COMPUTED);
		Object date = entity.getProperty(DATE);

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
			double amount = ((Double) amountO);
			bet.setAmount(amount);
		}
		if (oddsO != null){
			double odds = (double) oddsO;
			bet.setOdds(odds);
		}
		if (computed != null) {
			bet.setComputed((boolean) computed);
		}
		if (computed != null) {
			bet.setComputed((boolean) computed);
		}
		if (date != null) {
			bet.setDate((Date) date);
		}

		return bet;
	}

}
