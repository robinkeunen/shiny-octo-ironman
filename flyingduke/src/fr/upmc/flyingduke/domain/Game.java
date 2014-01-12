package fr.upmc.flyingduke.domain;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Game {
	
	private String uuid;
	private String awayTeamUUID;
	private String homeTeamUUID;
	private Date date;
	private OddsContainer odds;
	private ScoreContainer scores;
	
	public Game(String uuid) {
		this.uuid = uuid;
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
		
		return "Game [uuid=" + uuid + ", awayTeam=" + awayTeamUUID + ", homeTeam="
				+ homeTeamUUID + ", date=" + df.format(date) + ", odds="
				+ odds +"]";
	}


	public OddsContainer getOdds() {
		return odds;
	}


	public void setOdds(double home, double away) {
		this.odds = new OddsContainer(home, away);
	}
	
	public ScoreContainer getScores() {
		return scores;
	}
	
	public void setScores(int home, int away) {
		this.scores = new ScoreContainer(home, away);
	}

	/**
	 * @return the awayTeamUUID
	 */
	public String getAwayTeamUUID() {
		return awayTeamUUID;
	}

	/**
	 * @param awayTeamUUID the awayTeamUUID to set
	 */
	public void setAwayTeamUUID(String awayTeamUUID) {
		this.awayTeamUUID = awayTeamUUID;
	}

	/**
	 * @return the homeTeamUUID
	 */
	public String getHomeTeamUUID() {
		return homeTeamUUID;
	}

	/**
	 * @param homeTeamUUID the homeTeamUUID to set
	 */
	public void setHomeTeamUUID(String homeTeamUUID) {
		this.homeTeamUUID = homeTeamUUID;
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
	
	public static class ScoreContainer {
		private int home;
		private int away;
		
		public ScoreContainer(int home, int away) {
			this.home = home;
			this.away = away;
		}

		/**
		 * @return the home
		 */
		public int getHome() {
			return home;
		}

		/**
		 * @return the away
		 */
		public int getAway() {
			return away;
		}

		
	}
	

	
}
