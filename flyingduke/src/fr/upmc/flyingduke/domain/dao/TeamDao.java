package fr.upmc.flyingduke.domain.dao;

import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import fr.upmc.flyingduke.domain.Player;
import fr.upmc.flyingduke.domain.Team;
import fr.upmc.flyingduke.exceptions.MissingUUIDException;

public class TeamDao {
	private static final String TEAM_KIND = "TEAM_KIND";
	private static final String NAME = "NAME";
	private static final String ALIAS = "ALIAS";
	private static final String PLAYERS = "PLAYERS";

	private final static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	/**
	 * Reconstitutes the team for the given uuid.
	 * Players have only their uuid field set.
	 * @param uuid
	 * @return
	 * @throws EntityNotFoundException 
	 */
	public static Team shallowGet(String uuid) throws EntityNotFoundException {

		// get Entity
		Key key = KeyFactory.createKey(TEAM_KIND, uuid);
		Entity entity = datastore.get(key);

		return teamFromEntity(entity);
	}

	
	/**
	 * Stores a team object in the database. The UUID must be set.
	 * The name and alias should be set. The players may be set.
	 * @param team
	 * @throws MissingUUIDException 
	 */
	public static void store(Team team) throws MissingUUIDException { 
		if (team.getUUID() == null)
			throw new MissingUUIDException();

		Entity teamEntity = new Entity(TEAM_KIND, team.getUUID());
		teamEntity.setProperty(TEAM_KIND, team.getUUID());
		teamEntity.setProperty(NAME, team.getName());
		teamEntity.setProperty(ALIAS, team.getAlias());
		if (team.getPlayers() != null) {
			List <EmbeddedEntity> playersEE = new LinkedList<EmbeddedEntity>();
			for (Player player: team.getPlayers()){
				playersEE.add(PlayerEntityBuilder.makePlayerEntity(player));
			}
			teamEntity.setProperty(PLAYERS, playersEE);
		}

		System.out.println("store" + team.toString());
		datastore.put(teamEntity);
	}

	private static Team teamFromEntity(Entity entity) {
		// get properties
		Object name = entity.getProperty(NAME);
		Object alias = entity.getProperty(ALIAS);

		List<Player> players = null;
		if (entity.hasProperty(PLAYERS)) {
			@SuppressWarnings("unchecked")
			List<EmbeddedEntity> playerEEs = (List<EmbeddedEntity>) entity.getProperty(PLAYERS);
			players = new LinkedList<>();
			for (EmbeddedEntity playerEE: playerEEs) {
				players.add(PlayerEntityBuilder.playerFromEntity(playerEE));
			}
		}

		// build team
		Team team = new Team(entity.getKey().getName());
		if (name != null)
			team.setName((String) name);
		if (alias != null)
			team.setAlias((String) alias);
		if (players != null)
			team.setPlayers(players); 		
		return team;
	}
	
	private static class PlayerEntityBuilder {
		
		private static final String UUID = "UUID";
		private static final String FIRST_NAME = "FIRST_NAME";
		private static final String LAST_NAME = "LAST_NAME";
		private static final String JERSEY = "JERSEY";
		private static final String POSITION = "POSITION";
		
		public static EmbeddedEntity makePlayerEntity(Player player) {
			EmbeddedEntity ee = new EmbeddedEntity();
			ee.setProperty(UUID, player.getUUID());
			ee.setProperty(FIRST_NAME, player.getFirstName());
			ee.setProperty(LAST_NAME, player.getLastName());
			ee.setProperty(JERSEY, player.getJersey());
			ee.setProperty(POSITION, player.getPosition());
			
			return ee;
		}

		public static Player playerFromEntity(EmbeddedEntity playerEE) {
			// get properties
			String uuid =  (String) playerEE.getProperty(UUID);
			Object firstNameO = playerEE.getProperty(FIRST_NAME);
			Object lastNameO = playerEE.getProperty(LAST_NAME);
			Object jerseyO = playerEE.getProperty(JERSEY);
			Object positionO = playerEE.getProperty(POSITION);
			
			// build player
			Player player = new Player(uuid);
			if (firstNameO != null)
				player.setFirstName((String) firstNameO);
			if (lastNameO != null)
				player.setLastName((String) lastNameO);
			if (jerseyO != null)
				player.setJersey(((Long) jerseyO).intValue());
			if (positionO != null)
				player.setPosition((String) positionO);
			
			return player;
		}
	}
	
}
