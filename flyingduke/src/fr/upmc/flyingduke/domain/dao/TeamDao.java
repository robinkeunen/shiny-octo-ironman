package fr.upmc.flyingduke.domain.dao;

import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
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
		
		// get properties
		String name = (String) entity.getProperty(NAME);
		String alias = (String) entity.getProperty(ALIAS);
		
		List<Player> players = null;
		if (entity.hasProperty(PLAYERS)) {
			List<String> playerUUIDs = (List<String>) entity.getProperty(PLAYERS);
			players = new LinkedList<>();
			for (String playerUUID: playerUUIDs) {
				players.add(new Player(playerUUID));
			}
		}
		
		// build team
		Team team = new Team(uuid);
		team.setName(name);
		team.setAlias(alias);
		team.setPlayers(players); 
		
		return team;
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
			List <String> playerUUIDs = new LinkedList<String>();
			for (Player player: team.getPlayers()){
				playerUUIDs.add(player.getUUID());
			}
			teamEntity.setProperty(PLAYERS, playerUUIDs);
		}

		System.out.println("store" + team.toString());
		datastore.put(teamEntity);
	}
	
}
