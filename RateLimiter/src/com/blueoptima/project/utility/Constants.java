package com.blueoptima.project.utility;

/*
 * Class storing all constant values used in the application.
 */
public class Constants {
	
	public static final String INPUT_FILE = "dataset/input.json";
	public static final String ERROR_USER_NOT_FOUND = "User Data Not Found";
	public static final String ERROR_REPO_PARSING = "User Repo JSON Could Not be Parsed";
	public static final String ERROR_READING_JSON = "Error in reading JSON by Jackson Parser";
	public static final String ERROR_REPO_NOT_FOUND = "User Github Repo is unavailable";
	public static final String ERROR_GITHUB_API = "Github API Failed : HTTP error code : ";
	public static final String ERROR_FILE_NOT_CREATED = "Error in File creation for User";
	public static final String ERROR_WRITING_USER_FILE = "Error in writing File for User";
	public static final String NEWLINE = "\n";
	public static final String ERROR_USER_NOT_PROCESSED = "User Profile Could Not Be Fetched";
	public static final String PROGRAM_EXIT_MSG = "Finished all threads, Total Records Processed : ";
	public static final long GITHUB_RATE_LIMIT = 60;
	public static final int THREAD_POOL_SIZE = 5;
	public static final int MONITOR_RECHECK_INTERVAL = 2;
	
}
