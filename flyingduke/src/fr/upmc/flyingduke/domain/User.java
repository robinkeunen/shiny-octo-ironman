package fr.upmc.flyingduke.domain;

import com.google.appengine.api.datastore.Email;

public class User extends Person {
	
	private Email email;
	private int wallet; // Amount of money per user
	
	public User(String uuid) {
		super(uuid);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the mail
	 */
	public Email getMail() {
		return email;
	}
	/**
	 * @param mail the mail to set
	 */
	public void setMail(Email email) {
		this.email = email;
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
