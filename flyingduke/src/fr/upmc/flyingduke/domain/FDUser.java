package fr.upmc.flyingduke.domain;

import com.google.appengine.api.users.User;

import fr.upmc.flyingduke.domain.dao.FDUserDao;
import fr.upmc.flyingduke.exceptions.ExistingUserException;

/**
 * Flying Duck user. Name used to differentiate from 
 * com.google.appengine.api.users.User 
 * @author roke
 *
 */
public class FDUser {
	
	private final long id;
	private String firstName;
	private String lastName;
	private User googleuser;
	private double wallet; // Amount of money per user
	
	/**
	 * Contructor for FDUsers. This constructor creates an entity
	 * in the store and sets a unique id to the user instance.
	 * @return
	 * @throws ExistingUserException One user per google account
	 */
	public static FDUser createFDUser(User googleuser) throws ExistingUserException {
		FDUserDao fdUserDao = new FDUserDao();
		return fdUserDao.create(googleuser);
	}
	
	/**
	 * Constructor used by the dao to build an instance of FDUser.
	 * This constructor should not be used by other classes than FDUserDao
	 * @param id
	 */
	public FDUser(long id) {
		this.id = id;
	}
	
	/**
	 * @return the wallet
	 */
	public double getWallet() {
		return wallet;
	}
	/**
	 * @param wallet the wallet to set
	 */
	public void setWallet(double wallet) {
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
	 * @return the googleuser
	 */
	public User getGoogleuser() {
		return googleuser;
	}

	/**
	 * @param googleuser the googleuser to set
	 */
	public void setGoogleuser(User googleuser) {
		this.googleuser = googleuser;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FDUser [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", googleuser=" + googleuser + ", wallet="
				+ wallet + "]";
	}

	
}
