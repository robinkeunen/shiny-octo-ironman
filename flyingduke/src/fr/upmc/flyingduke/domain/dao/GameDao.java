package fr.upmc.flyingduke.domain.dao;

import java.util.Date;
import java.util.UUID;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import fr.upmc.flyingduke.domain.Game;
import fr.upmc.flyingduke.domain.Team;

public class GameDao {
	private static final String GAME_KIND = "GAME_KIND";
	private static final String HOME_TEAM_UUID = "HOME_TEAM_UUID";
	private static final String AWAY_TEAM_UUID = "AWAY_TEAM_UUID";
	private static final String DATE = "DATE";
	
	/**
	 * Returns a shallow Game instance : the team fields only have their uuid set
	 * @param uuid
	 * @return
	 * @throws EntityNotFoundException
	 */
	public static Game shallowGet(String uuid) throws EntityNotFoundException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// get Entity
		Key getKey = KeyFactory.createKey(GAME_KIND, uuid);
		Entity entityGame = datastore.get(getKey);
		
		// get properties
		String homeTeamUUID = (String) entityGame.getProperty(HOME_TEAM_UUID);
		String awayTeamUUID = (String) entityGame.getProperty(HOME_TEAM_UUID);
		Date date = (Date) entityGame.getProperty(DATE);
				
		// build game
		Game game = new Game(uuid);
		Team awayTeam = new Team(awayTeamUUID);
		Team homeTeam = new Team(homeTeamUUID);
		game.setAwayTeam(awayTeam);
		game.setHomeTeam(homeTeam);
		game.setDate(date);
		
		return game;
	}
	
	public Game deepGet(String uuid) {
		return null;
	}
	
	/**
	 * 
	 * @param game
	 */
	public static void store(Game game) {
		//Key key = KeyFactory.createKey("uuid", game.getUUID());
		Entity gameEntity = new Entity(GAME_KIND, game.getUUID());
		gameEntity.setProperty(HOME_TEAM_UUID, game.getHomeTeam().getUUID());
		gameEntity.setProperty(AWAY_TEAM_UUID, game.getAwayTeam().getUUID());
		gameEntity.setProperty(DATE, game.getDate());
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(gameEntity);
		
		try {
			get(game.getUUID()).toString();
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
