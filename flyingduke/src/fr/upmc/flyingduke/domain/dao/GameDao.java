package fr.upmc.flyingduke.domain.dao;

import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Team;
import fr.upmc.flyingduke.exceptions.MissingUUIDException;

public class GameDao {
	private static final String GAME_KIND = "GAME_KIND";
	private static final String HOME_TEAM_UUID = "HOME_TEAM_UUID";
	private static final String AWAY_TEAM_UUID = "AWAY_TEAM_UUID";
	private static final String DATE = "DATE";
	
	private final static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	/**
	 * Returns a shallow Game instance : the team fields only have their uuid set.
	 * TODO If the requested game is not in the DAO, a REST request is sent. 
	 * @param uuid
	 * @return the game for the uuid
	 * @throws EntityNotFoundException
	 */
	public static Game shallowGet(String uuid) throws EntityNotFoundException {
		
		// get Entity
		Key key = KeyFactory.createKey(GAME_KIND, uuid);
		Entity entity = datastore.get(key);
		
		// get properties
		String homeTeamUUID = (String) entity.getProperty(HOME_TEAM_UUID);
		String awayTeamUUID = (String) entity.getProperty(HOME_TEAM_UUID);
		Date date = (Date) entity.getProperty(DATE);
				
		// build game
		Game game = new Game(uuid);
		game.setAwayTeam(new Team(awayTeamUUID));
		game.setHomeTeam(new Team(homeTeamUUID));
		game.setDate(date);
		
		return game;
	}
	
	/**
	 * TODO Returns a Deep Game instance : the team fields have their field set.
	 * TODO If the requested game is not in the DAO, a REST request is sent.
	 * @param uuid
	 * @return
	 */
	public Game deepGet(String uuid) {
		return null;
	}
	
	/**
	 * 
	 * @param game
	 * @throws MissingUUIDException 
	 */
	public static void store(Game game) throws MissingUUIDException {
		if (game.getUUID() == null) 
			throw new MissingUUIDException();
	
		Entity entity = new Entity(GAME_KIND, game.getUUID());
		entity.setProperty(HOME_TEAM_UUID, game.getHomeTeam().getUUID());
		entity.setProperty(AWAY_TEAM_UUID, game.getAwayTeam().getUUID());
		entity.setProperty(DATE, game.getDate());
		
		System.out.println("store " + game.toString());
		datastore.put(entity);
		
	}
}
