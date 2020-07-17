/**
 * 
 */
package com.blueoptima.project.model;

/**
 * @author jghosh
 *
 */
public class UserProfileInput {
	
	private String firstName;
	private String lastName;
	private String location;
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
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return String.format("%1$s %2$s, %3$s", this.getFirstName(), this.getLastName(), this.getLocation());
	}
	
	 
}
