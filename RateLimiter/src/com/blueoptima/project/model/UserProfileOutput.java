/**
 * 
 */
package com.blueoptima.project.model;

import java.util.Map;

/**
 * @author jghosh
 *
 */
public class UserProfileOutput {

	// string for public profile data
	private String publicProfileData;

	// hashmap for commit count on each repo of one user
	private Map<String, String> userRepoData;

	/**
	 * @return the publicProfileData
	 */
	public String getPublicProfileData() {
		return publicProfileData;
	}

	/**
	 * @param publicProfileData the publicProfileData to set
	 */
	public void setPublicProfileData(String publicProfileData) {
		this.publicProfileData = publicProfileData;
	}

	/**
	 * @return the userRepoData
	 */
	public Map<String, String> getUserRepoData() {
		return userRepoData;
	}

	/**
	 * @param userRepoData the userRepoData to set
	 */
	public void setUserRepoData(Map<String, String> userRepoData) {
		this.userRepoData = userRepoData;
	}
}
