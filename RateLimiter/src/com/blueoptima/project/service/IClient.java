/**
 * 
 */
package com.blueoptima.project.service;

import java.util.HashMap;

import com.blueoptima.project.model.UserProfileInput;

/**
 * @author jghosh
 *An interface used to expose the methods for reading data from the Github REST API
 */
public interface IClient {

	public String getUserProfile(UserProfileInput user);
	public String createRequest(UserProfileInput user);
	public String getUserRepoList(String repo_url);
	public String callWebService(String query, String acceptParam);
	public HashMap<String, String> getUserNoOfCommits(HashMap<String, String> repo_list);

}
