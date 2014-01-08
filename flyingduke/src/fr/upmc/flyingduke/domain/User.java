package fr.upmc.flyingduke.domain;

public class User extends Person {

	
	private String mail;
	private int wallet; // Amount of money per user
	
	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}
	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	/**
	 * @return the wallet
	 */
	public int getWallet() {
		return wallet;
	}
	/**
	 * @param wallet the wallet to set
	 */
	public void setWallet(int wallet) {
		this.wallet = wallet;
	}
	
}
