package fr.upmc.flyingduke.domain.dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import fr.upmc.flyingduke.domain.FDUser;

public class FDUserDao {
	public static final String FD_USER_KIND = "FD_USER_KIND";
	private static final String FIRST_NAME = "FIRST_NAME";
	private static final String LAST_NAME = "LAST_NAME";
	private static final String EMAIL = "EMAIL";
	private static final String WALLET = "WALLET";

	public static FDUser get(long id) throws EntityNotFoundException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		// get Entity
		Key key = KeyFactory.createKey(FD_USER_KIND, id);
		Entity entity = datastore.get(key);
		
		// get properties
		String firstName = (String) entity.getProperty(FIRST_NAME);
		String lastName = (String) entity.getProperty(LAST_NAME);
		Email email = (Email) entity.getProperty(EMAIL);
		int wallet  = ((Long) entity.getProperty(WALLET)).intValue();
		
		// build user
		FDUser user = new FDUser(id);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setWallet(wallet);
		
		return user;
	}
	
	/**
	 * Creates a new user in the base. The google datastore will
	 * generate an id for the user.
	 * @param user
	 */
	public static FDUser create() {
		// create entity
		Entity entity = new Entity(FD_USER_KIND);
		
		// put in store, will generate a key
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(entity);
		
		// return the User with the assigned id
		return new FDUser(entity.getKey().getId());
	}

	/**
	 * Updates the fields of a specific user. checks id the user is in base.
	 * @param user the user to save
	 * @return the id generated by the datastore
	 * @throws EntityNotFoundException thrown if the user is not in the base.
	 * Users must be created from FDUserDao.create()
	 */
	public static void update(FDUser user) throws EntityNotFoundException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		// check if user is in base
		Key key = KeyFactory.createKey(FD_USER_KIND, user.getId());
		Entity entity = datastore.get(key);

		// update properties
		entity.setProperty(FIRST_NAME, user.getFirstName());
		entity.setProperty(LAST_NAME, user.getLastName());
		entity.setProperty(EMAIL, user.getEmail());
		entity.setProperty(WALLET, user.getWallet());

		// update in base
		datastore.put(entity);
	}




}
