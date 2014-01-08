package fr.upmc.flyingduke.domain;

import java.util.List;

public class Team {
	private String uuid;
	private String name;
	private String alias;
	private List<Person> players;
	
	

	public Team(String uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the players
	 */
	public List<Person> getPlayers() {
		return players;
	}
	/**
	 * @param players the players to set
	 */
	public void setPlayers(List<Person> players) {
		this.players = players;
	}
	/**
	 * @return the id
	 */
	public String getUUID() {
		return uuid;
	}
	/**
	 * @param id the id to set
	 */
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}


}
