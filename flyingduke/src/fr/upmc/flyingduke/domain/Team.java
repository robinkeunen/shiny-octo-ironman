package fr.upmc.flyingduke.domain;

import java.util.List;

public class Team {
	private final String uuid;
	private String name;
	private String alias;
	private List<Player> players;
	
	

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
	public List<Player> getPlayers() {
		return players;
	}
	/**
	 * @param plist1 the players to set
	 */
	public void setPlayers(List<Player> plist1) {
		this.players = plist1;
	}
	/**
	 * @return the id
	 */
	public String getUUID() {
		return uuid;
	}
	

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Team [uuid=" + uuid + ", name=" + name + ", alias=" + alias
				+ ", players=[" + players + "]]";
	}


}
