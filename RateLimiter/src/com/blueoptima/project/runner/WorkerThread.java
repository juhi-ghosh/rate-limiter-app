/**
 * 
 */
package com.blueoptima.project.runner;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonProcessingException;

import com.blueoptima.project.model.RateLimitParam;
import com.blueoptima.project.model.UserProfileInput;
import com.blueoptima.project.model.UserProfileOutput;
import com.blueoptima.project.parser.GithubResponseParser;
import com.blueoptima.project.service.IClient;
import com.blueoptima.project.service.RestClient;
import com.blueoptima.project.utility.Constants;
import com.blueoptima.project.utility.IPostUserToDataStore;
import com.blueoptima.project.utility.PostUserToFile;
import com.blueoptima.project.utility.Validator;

/**
 * @author jghosh
 * class initializes a thread 
 */
public class WorkerThread implements Runnable {

	private UserProfileInput user;
	private RateLimitParam githubRateLimit;
	
	public WorkerThread(UserProfileInput user, RateLimitParam githubRateLimit) {
		this.user = user;
		this.githubRateLimit = githubRateLimit;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " Start. Developer Command = " + user.toString());
		processUserProfile();
		System.out.println(Thread.currentThread().getName() + " End.");
	}

	//Thread for fetching data from Github for users provided in input and parse the json response received from the REST API
	private void processUserProfile() {

		IClient githubClient = new RestClient(githubRateLimit);
		GithubResponseParser githubResponse = new GithubResponseParser();

		String jsonUserData = githubClient.getUserProfile(user);

		if (!Validator.isNull(jsonUserData)) {
			try {
				String repo_url = githubResponse.extractRepoPath(jsonUserData);

				String jsonData = githubClient.getUserRepoList(repo_url);

				if (!Validator.isNull(jsonData)) {
					HashMap<String, String> repo_list = githubResponse.extractRepoList(jsonData);

					HashMap<String, String> repoMap = githubClient.getUserNoOfCommits(repo_list);

					HashMap<String, String> repoCommitsMap = githubResponse.extractCommitCount(repoMap, user);

					UserProfileOutput userProfile = new UserProfileOutput();

					userProfile.setUserRepoData(repoCommitsMap);
					userProfile.setPublicProfileData(jsonUserData);

					IPostUserToDataStore fileHandle = new PostUserToFile();
					fileHandle.addUserToDataStore(userProfile, user);
					
					
				} else {
					System.out.println(Constants.ERROR_REPO_NOT_FOUND);
				}
			} catch (JsonProcessingException jpe) {
				System.out.println(String.format("%1$s\n%2$s", Constants.ERROR_REPO_PARSING, jpe));
			} catch (IOException io) {
				System.out.println(String.format("%1$s\n%2$s", Constants.ERROR_READING_JSON, io));
			} catch (Exception np) {
				System.out.println(String.format("%1$s For User: %2$s\n%3$s", Constants.ERROR_USER_NOT_PROCESSED,
						user.toString(), np));
			}
		} else {
			System.out.println(String.format("%1$s For User: %2$s", Constants.ERROR_USER_NOT_FOUND, user.toString()));
		}
	}
}