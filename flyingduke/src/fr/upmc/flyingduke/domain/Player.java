package fr.upmc.flyingduke.domain;

public class Player extends Person {

	private String position;

	public Player(String uuid) {
		super(uuid);
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
		return "Player { " + super.toString() + " } position=" + position + "]";
	}

}
