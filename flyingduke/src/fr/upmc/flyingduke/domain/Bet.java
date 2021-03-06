package fr.upmc.flyingduke.domain;

import java.util.Date;

import fr.upmc.flyingduke.domain.dao.BetDao;

public class Bet {
	private final long id;
	private final long punterID;
	private String gameUUID;
	private BetChoice choice; 
	private double amount;
	private double odds;
	private boolean computed;
	private Date date;
	
	/**
	 * Constructor for Bet. This constructor creates an entity
	 * in the store and sets a unique id to the user instance.
	 * @param punter the user placing the bet.
	 * @return an instance of Bet with unique id generated by the store
	 */
	public static Bet createBet(FDUser punter) {
		BetDao betDao = new BetDao();
		return betDao.create(punter);
	}
	
	/**
	 * Constructor used by the dao to build an instance of Bet.
	 * This constructor should not be used by other classes than BetDao
	 * @param id
	 */
	public Bet(long betId, long punterId) {
		this.id = betId;
		this.punterID = punterId;
	}
	
	/**
	 * @return the gameID
	 */
	public String getGameUUID() {
		return gameUUID;
	}
	/**
	 * @param gameID the gameID to set
	 */
	public void setGameUUID(String uuid) {
		this.gameUUID = uuid;
	}
	/**
	 * @return the choice
	 */
	public BetChoice getChoice() {
		return choice;
	}
	/**
	 * @param choice the choice to set
	 */
	public void setChoice(BetChoice choice) {
		this.choice = choice;
	}
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the odds
	 */
	public double getOdds() {
		return odds;
	}

	/**
	 * @param odds the odds to set
	 */
	public void setOdds(double odds) {
		this.odds = odds;
	}

	public long getPunterID() {
		return punterID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Bet [id=" + id + ", punterID=" + punterID + ", gameUUID="
				+ gameUUID + ", choice=" + choice + ", amount=" + amount
				+ ", computed=" + computed + ", odds=" + odds + "]";
	}

	/**
	 * @return the computed
	 */
	public boolean isComputed() {
		return computed;
	}

	/**
	 * @param computed the computed to set
	 */
	public void setComputed(boolean computed) {
		this.computed = computed;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
