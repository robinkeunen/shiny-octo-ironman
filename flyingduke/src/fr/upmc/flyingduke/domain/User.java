package fr.upmc.flyingduke.domain;

import com.google.appengine.api.datastore.Email;

public class User {
	
	private final long id;
	private String firstName;
	private String lastName;
	private Email email;
	private int wallet; // Amount of money per user
	
	public User(long id) {
		this.id = id;
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

	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public Email getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(Email email) {
		this.email = email;
	}
	
}
