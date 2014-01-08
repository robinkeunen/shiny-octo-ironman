package fr.upmc.flyingduke.domain.dao;

import com.google.appengine.api.datastore.Entity;

import fr.upmc.flyingduke.domain.Team;

public class TeamDao {
	private static final String TEAM_KIND = "TEAM_KIND";

	public static Team get(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void store(Team team) { 
		Entity teamEntity = new Entity(TEAM_KIND, team.getUUID());
		//teamEntity.setProperty(propertyName, value);
	}
	
}
