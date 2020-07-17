/**
 * 
 */
package com.blueoptima.project.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.blueoptima.project.model.UserProfileInput;

/**
 * @author jghosh
 *
 */
public class GithubResponseParser {

	public static final String USER_ITEMS = "items";
	public static final String USER_REPO_URL = "repos_url";
	public static final String USER_REPO_NAME = "name";
	public static final String USER_COMMITS_URL = "commits_url";
	public static final String USER_COMMIT = "commit";
	public static final String USER_COMMIT_AUTHOR = "author";

	//For each user’s each repo, all commits are checked for an exact or partial match in name
	public HashMap<String, String> extractCommitCount(HashMap<String, String> repoMap, UserProfileInput user)
			throws JsonProcessingException, IOException {

		HashMap<String, String> repoCommitsMap = new HashMap<String, String>();

		if (repoMap.size() > 0) {
			for (Entry<String, String> repo : repoMap.entrySet()) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = null;

				String commitsText = repo.getValue();

				rootNode = objectMapper.readTree(commitsText);
				Iterator<JsonNode> commitsArr = rootNode.getElements();

				int commit_count = 0;
				while (commitsArr.hasNext()) {
					JsonNode commitDict = commitsArr.next().findPath(USER_COMMIT);
					String userName = commitDict.findPath(USER_COMMIT_AUTHOR).findPath(USER_REPO_NAME).asText();

					//checking if commit's author name is equal to user input name or contains the firstname or last name as user input
					if (userName.equalsIgnoreCase(String.format("%1$s %2$s", user.getFirstName(), user.getLastName()))
							|| userName.contains(user.getFirstName()) || userName.contains(user.getLastName())) {
						commit_count++;
					}
				}

				repoCommitsMap.put(repo.getKey(), String.valueOf(commit_count));
			}
		}

		return repoCommitsMap;
	}

	//For each Repo Found from above URL, repo name is extracted and commits url is extracted and a HashMap of this data is created and returned
	public HashMap<String, String> extractRepoList(String jsonData) throws JsonProcessingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		// reading JSON like DOM Parser
		JsonNode rootNode = null;

		rootNode = objectMapper.readTree(jsonData);
		Iterator<JsonNode> repoArr = rootNode.getElements();
		HashMap<String, String> repo_list = new HashMap<String, String>();

		String repo_name, commits_url;
		while (repoArr.hasNext()) {
			JsonNode repoElem = repoArr.next();
			repo_name = repoElem.findPath(USER_REPO_NAME).asText();
			commits_url = repoElem.findPath(USER_COMMITS_URL).asText();
			int len = commits_url.length();
			commits_url = commits_url.substring(0, len - 6);

			repo_list.put(repo_name, commits_url);
		}

		return repo_list;
	}

	//From user profile reads user Repo URL
	public String extractRepoPath(String jsonData) throws JsonProcessingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		// reading JSON like DOM Parser
		JsonNode rootNode = null;

		rootNode = objectMapper.readTree(jsonData);
		JsonNode itemsNode = rootNode.path(USER_ITEMS);

		JsonNode publicProfileData = itemsNode.getElements().next();
		JsonNode repos_url = publicProfileData.findPath(USER_REPO_URL);
		return repos_url.asText();
	}

}
