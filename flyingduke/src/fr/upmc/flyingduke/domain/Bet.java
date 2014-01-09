package fr.upmc.flyingduke.domain;

import java.util.Map;

public class Bet {
	private final long id;
	private String gameID;
	private BetChoice choice; 
	private int amount;
	private Map<BetChoice, Float> cotes;
	
	public Bet(long id) {
		this.id = id;
	}
	
	/**
	 * @return the gameID
	 */
	public String getGameID() {
		return gameID;
	}
	/**
	 * @param gameID the gameID to set
	 */
	public void setGameID(String gameID) {
		this.gameID = gameID;
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
	public int getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the cote
	 */
	public float getCote() {
		return cotes.get(choice);
	}
	/**
	 * @param cotes the cotes to set
	 */
	public void setCotes(float home, float away, float tie) {
		this.cotes.put(BetChoice.HOME, home);
		this.cotes.put(BetChoice.AWAY, away);
		this.cotes.put(BetChoice.TIE, tie);		
	}


}
