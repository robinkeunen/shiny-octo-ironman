package fr.upmc.flyingduke.domain;

public class Player {

	private final String uuid;
	private String firstName;
	private String lastName;
	private int jersey;
	private String position;

	public Player(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Player [uuid=" + uuid + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", jersey=" + jersey
				+ ", position=" + position + "]";
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
	 * @return the uuid
	 */
	public String getUUID() {
		return uuid;
	}

	public int getJersey() {
		return jersey;
	}

	public void setJersey(int jersey) {
		this.jersey = jersey;
	}

}
