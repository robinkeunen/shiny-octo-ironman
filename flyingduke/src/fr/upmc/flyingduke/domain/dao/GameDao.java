package fr.upmc.flyingduke.domain.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Game.OddsContainer;
import fr.upmc.flyingduke.domain.Game.ScoreContainer;
import fr.upmc.flyingduke.exceptions.MissingUUIDException;

public class GameDao {
	private static final String GAME_KIND = "GAME_KIND";
	private static final String HOME_TEAM_UUID = "HOME_TEAM_UUID";
	private static final String AWAY_TEAM_UUID = "AWAY_TEAM_UUID";
	private static final String DATE = "DATE";
	private static final String ODDS = "ODDS";
	private static final String SCORE = "SCORE";
	private static final String ODDS_HOME = "ODDS_HOME";
	private static final String ODDS_AWAY = "ODDS_AWAY";

	private final static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	/**
	 * Returns a  Game instance>
	 * TODO If the requested game is not in the DAO, a REST request is sent. 
	 * @param uuid
	 * @return the game for the uuid
	 * @throws EntityNotFoundException
	 */
	public static Game get(String uuid) throws EntityNotFoundException {

		// get Entity
		Key key = KeyFactory.createKey(GAME_KIND, uuid);
		Entity entity = datastore.get(key);

		Game game = gameFromEntity(entity);

		return game;
	}

	/**
	 * Stores the given game in the datastore.
	 * @param game the game to persist
	 * @throws MissingUUIDException thrown if the uuid for the game is not set.
	 */
	public static void store(Game game) throws MissingUUIDException {
		if (game.getUUID() == null) 
			throw new MissingUUIDException();

		Entity entity = new Entity(GAME_KIND, game.getUUID());
		entity.setProperty(HOME_TEAM_UUID, game.getHomeTeamUUID());
		entity.setProperty(AWAY_TEAM_UUID, game.getAwayTeamUUID());
		entity.setProperty(DATE, game.getDate());

		// inner entity for odds
		OddsContainer odds = game.getOdds();
		if (odds != null) {
			EmbeddedEntity embeddedEntity = new EmbeddedEntity();
			embeddedEntity.setProperty(ODDS_HOME, odds.getHome());
			embeddedEntity.setProperty(ODDS_AWAY, odds.getAway());
			entity.setProperty(ODDS, embeddedEntity);
		}
		
		ScoreContainer score = game.getScores();
		if (score != null) {
			entity.setProperty(SCORE, ScoreBuilder.makeScoreEntity(score));
		}
		
		System.out.println("store " + game.toString());
		datastore.put(entity);
	}

	/**
	 * Returns a list of the game later than now. 
	 * The list is not longer than gameLimit.
	 * @param gameLimit the maximum result list size 
	 * @return the list of the future games.
	 */
	public static List<Game> futureGames(int gameLimit) {
		Date now = new Date();
		Filter from = new FilterPredicate(
				DATE, 
				FilterOperator.GREATER_THAN_OR_EQUAL, 
				now);

		Query q = new Query(GAME_KIND).setFilter(from).addSort(DATE);
		PreparedQuery pq = datastore.prepare(q);

		// convert to objects
		List<Game> games = new LinkedList<>();
		Game game = null;
		for (Entity entity: pq.asIterable(FetchOptions.Builder.withLimit(gameLimit))) {
			game = gameFromEntity(entity);
			games.add(game);
		}
		return games;
	}

	/**
	 * The list of the game scheduled for the given day. Pay attention to the timezone. 
	 * Example to get the games in the East Coast time zone 
	 * 
	 * Calendar someday = Calendar.getInstance();
	 * someday.setTimeZone(TimeZone.getTimeZone("America/New_York")); 
	 *
	 * someday.set(2014, 0, 10);
	 *	for (Game gamequery: GameDao.gameForDay(someday)) {
	 *		page.println(gamequery.toString());
	 *	}
	 *
	 * @param day search parameter
	 * @return The list of the game scheduled for the given day.
	 */
	public static List<Game> gameForDay(Calendar day) {
		// get hour range for the day
		day.setTimeZone(TimeZone.getTimeZone("America/New_York")); 
		Calendar startHour = (Calendar) day.clone();
		startHour.set(Calendar.HOUR_OF_DAY, 0);
		startHour.set(Calendar.MINUTE, 0);

		Calendar endHour = (Calendar) day.clone();
		endHour.set(Calendar.HOUR_OF_DAY, 23);
		endHour.set(Calendar.MINUTE, 59);

		// build predicates
		Filter from = new FilterPredicate(DATE, 
				FilterOperator.GREATER_THAN_OR_EQUAL, 
				startHour.getTime());
		Filter to = new FilterPredicate(DATE, 
				FilterOperator.LESS_THAN_OR_EQUAL,
				endHour.getTime());
		// build range
		Filter range = CompositeFilterOperator.and(from, to);

		// query the store
		Query query = new Query(GAME_KIND).setFilter(range).addSort(DATE);
		PreparedQuery pq = datastore.prepare(query);

		// convert to objects
		List<Game> games = new LinkedList<>();
		Game game = null;
		for (Entity entity: pq.asIterable()) {
			game = gameFromEntity(entity);
			games.add(game);
		}

		return games;
	}

	private static Game gameFromEntity(Entity entity) {
		// get properties
		Object homeTeamUUID = entity.getProperty(HOME_TEAM_UUID);
		Object awayTeamUUID = entity.getProperty(AWAY_TEAM_UUID);
		Object date = entity.getProperty(DATE);
		Object oddsO = entity.getProperty(ODDS);
		Object scoreO = entity.getProperty(SCORE);

		// build game
		Game game = new Game(entity.getKey().getName());
		if (homeTeamUUID != null)
			game.setAwayTeamUUID((String) awayTeamUUID);
		if (awayTeamUUID != null)
			game.setHomeTeamUUID((String) homeTeamUUID);
		if (date != null)
			game.setDate((Date) date);		
		if (oddsO != null) {
			EmbeddedEntity oddsEE = (EmbeddedEntity) oddsO;
			game.setOdds(
					(Double) oddsEE.getProperty(ODDS_HOME),
					(Double) oddsEE.getProperty(ODDS_AWAY));
		}
		if (scoreO != null) {
			ScoreContainer sc = 
					ScoreBuilder.scoreFromEntity((EmbeddedEntity) scoreO);
			game.setScores(sc.getHome(), sc.getAway());
		}			
		
		return game;
	}
	
	private static class ScoreBuilder {
		private static final String HOME_SCORE = "HOME_SCORE";
		private static final String AWAY_SCORE = "AWAY_SCORE";
		
		public static EmbeddedEntity makeScoreEntity(ScoreContainer scores) {
			EmbeddedEntity ee = new EmbeddedEntity();
			ee.setProperty(HOME_SCORE , scores.getHome());
			ee.setProperty(AWAY_SCORE, scores.getHome());

			return ee;
		}
		
		public static ScoreContainer scoreFromEntity(EmbeddedEntity ee) {
			Object homeO = ee.getProperty(HOME_SCORE);
			Object awayO = ee.getProperty(AWAY_SCORE);
			
			int home = 0;
			int away = 0;
			if (homeO != null) 
				home = ((Long) homeO).intValue(); 
			if (awayO != null)
				away = ((Long) awayO).intValue();
			
			return new ScoreContainer(home, away);			
		}
		
	}

}
