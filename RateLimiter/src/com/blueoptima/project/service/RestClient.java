/**
 * 
 */
package com.blueoptima.project.service;

import java.util.HashMap;
import java.util.Map.Entry;

import com.blueoptima.project.model.RateLimitParam;
import com.blueoptima.project.model.UserProfileInput;
import com.blueoptima.project.utility.Constants;
import com.blueoptima.project.utility.Validator;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author jghosh
 *class RestClient is used to make calls to different webservices provided by Github 
 */
public class RestClient implements IClient {

	public static final String GITHUB_API_SEARCH_USER = "https://api.github.com/search/users?q=";
	public static final String GITHUB_API_PARAM_ACCEPT = "application/vnd.github.mercy-preview+json";

	private RateLimitParam githubRateLimit;

	public RestClient(RateLimitParam githubRateLimit) {
		this.githubRateLimit = githubRateLimit;
	}

	//Calling Github API after notified by monitor thread
	@Override
	public String callWebService(String apiURL, String acceptParam) {

		Client client = Client.create();

		//acquiring Lock first
		synchronized (githubRateLimit) {
			try {
				System.out.println(apiURL + " waiting to get notified at time:" + System.currentTimeMillis());
				githubRateLimit.wait();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}

		System.out.println(apiURL + " waiter thread got notified at time:" + System.currentTimeMillis());

		WebResource webResource = client.resource(apiURL);
		ClientResponse response = webResource.accept(acceptParam).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			System.out.println(String.format("%1$s  %2$s For API: %3$s", Constants.ERROR_GITHUB_API,
					response.getStatus(), apiURL));
			return null;
		}

		//acquiring Lock first
		synchronized (githubRateLimit) {
			long X_RateLimit_Remaining = Long.parseLong(response.getMetadata().get("X-RateLimit-Remaining").get(0));
			githubRateLimit.setX_RateLimit_Remaining(X_RateLimit_Remaining);

			//long githubRateLimitReset = Long.parseLong(response.getMetadata().get("X-RateLimit-Reset").get(0));
			//githubRateLimit.setX_RateLimit_Reset(githubRateLimitReset);
			
			//For each First request of a Github API reset window, timestamp is saved in the githubRateLimit object
			if(githubRateLimit.isResetIndicator()) {
				githubRateLimit.setTimestamp(System.currentTimeMillis());
				githubRateLimit.setResetIndicator(false);
			}
		}

		String APIOutput = response.getEntity(String.class);
		return APIOutput;
	}

	//Given user data object calls Github API for user profile data
	@Override
	public String getUserProfile(UserProfileInput user) {
		String jsonQuery = createRequest(user);
		String APIOutput = callWebService(GITHUB_API_SEARCH_USER + jsonQuery, GITHUB_API_PARAM_ACCEPT);
		return APIOutput;
	}

	@Override
	public String createRequest(UserProfileInput user) {
		String jsonQuery = "";

		String location = user.getLocation();
		if (Validator.isNull(location))
			jsonQuery = String.format("%1$s+%2$s+in:name", user.getFirstName(), user.getLastName());
		else {
			location = Validator.removeSpaces(location);
			jsonQuery = String.format("%1$s+%2$s+in:name+location:%3$s", user.getFirstName(), user.getLastName(),
					location);
		}

		return jsonQuery;
	}

	//Given repo url for user calls Github API for user repo list
	@Override
	public String getUserRepoList(String repo_url) {
		String APIOutput = callWebService(repo_url, GITHUB_API_PARAM_ACCEPT);
		return APIOutput;
	}

	//Given each repo's commit url for user calls Github API for repo commits list
	@Override
	public HashMap<String, String> getUserNoOfCommits(HashMap<String, String> repo_list) {

		String APIOutput = null;
		HashMap<String, String> repoMap = new HashMap<String, String>();

		for (Entry<String, String> commit_url : repo_list.entrySet()) {
			APIOutput = callWebService(commit_url.getValue(), GITHUB_API_PARAM_ACCEPT);
			if (!Validator.isNull(APIOutput)) {
				repoMap.put(commit_url.getKey(), APIOutput);
			}
		}

		return repoMap;
	}
}
