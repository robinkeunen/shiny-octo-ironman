package fr.upmc.flyingduke.domain;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Game {


	private String uuid;
	private Team awayTeam;
	private Team homeTeam;
	private Date date;
	private OddsContainer odds;
	
	public Game(String uuid) {
		this.uuid = uuid;
	}
	
	
	public Team getAwayTeam() {
		return awayTeam;
	}
	/**
	 * @param awayTeam the awayTeam to set
	 */
	public void setAwayTeam(Team awayTeam)  {
		this.awayTeam = awayTeam;
	}
	/**
	 * @return the homeTeam
	 */
	public Team getHomeTeam() {
		return homeTeam;
	}
	/**
	 * @param homeTeam the homeTeam to set
	 */
	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param calendar the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		DateFormat df = DateFormat.getInstance();
		df.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		
		return "Game [uuid=" + uuid + ", awayTeam=" + awayTeam.getUUID() + ", homeTeam="
				+ homeTeam.getUUID() + ", date=" + df.format(date) + ", odds="
				+ odds +"]";
	}


	public OddsContainer getOdds() {
		return odds;
	}


	public void setOdds(double home, double away) {
		this.odds = new OddsContainer(home, away);
	}
	
	public class OddsContainer {
		private double home;
		private double away;
		
		OddsContainer(double home, double away) {
			this.home = home;
			this.away = away;
		}

		/**
		 * @return the home
		 */
		public double getHome() {
			return home;
		}

		/**
		 * @return the away
		 */
		public double getAway() {
			return away;
		}
		
		public String toString() {
			return "[ " + home + ", " + away + "]"; 
		}
	}

	
}
