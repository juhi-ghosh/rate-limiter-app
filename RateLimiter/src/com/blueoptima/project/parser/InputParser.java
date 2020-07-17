/**
 * 
 */
package com.blueoptima.project.parser;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;

import com.blueoptima.project.model.RateLimitParam;
import com.blueoptima.project.model.UserProfileInput;
import com.blueoptima.project.runner.WorkerThread;
import com.blueoptima.project.utility.Constants;

/**
 * @author jghosh
 * Class for Jackson Streaming parser 
 */
public class InputParser implements IInputParsingTool {

	public static final String USER_FIRSTNAME = "firstname";
	public static final String USER_LASTNAME = "lastname";
	public static final String USER_LOCATION = "location";

	private JsonFactory jsonfactory;
	private int numberOfRecords;
	private JsonParser jsonParser;
	private UserProfileInput user;
	private ExecutorService executor;
	RateLimitParam githubRateLimit;

	public InputParser(RateLimitParam githubRateLimit) {
		this.githubRateLimit = githubRateLimit;
	}

	//It parses the input, reading Firstname, Lastname and Location 
	public void processJsonInput(String filename) {

		File jsonFile = new File(filename);

		try {
			initializeParser(jsonFile);
			JsonToken jsonToken = jsonParser.nextToken();

			while (jsonToken != JsonToken.END_ARRAY) { 			// Iterate all elements of array
				String fieldname = jsonParser.getCurrentName(); // get current name of token
				if (USER_FIRSTNAME.equals(fieldname)) {
					jsonToken = jsonParser.nextToken(); 		// read next token
					user.setFirstName(jsonParser.getText());
				}

				if (USER_LASTNAME.equals(fieldname)) {
					jsonToken = jsonParser.nextToken();
					user.setLastName(jsonParser.getText());
				}

				if (USER_LOCATION.equals(fieldname)) {
					jsonToken = jsonParser.nextToken();
					user.setLocation(jsonParser.getText());
				}

				if (jsonToken == JsonToken.END_OBJECT) {
					// do some processing, using worker threads 

					Runnable worker = new WorkerThread(user, githubRateLimit);
					executor.execute(worker);

					user = null;
					user = new UserProfileInput();
					numberOfRecords++;
					
				}
				jsonToken = jsonParser.nextToken();
			}

			shutdownParser();
		} catch (JsonProcessingException jpe) {
			System.out.println(String.format("%1$s\n%2$s", Constants.ERROR_REPO_PARSING, jpe));
		} catch (IOException e) {
			System.out.println(String.format("%1$s\n%2$s", Constants.ERROR_READING_JSON, e));
		}
	}

	private void initializeParser(File jsonFile) throws JsonParseException, IOException {
		jsonfactory = new JsonFactory(); 						// init factory
		numberOfRecords = 0;
		
		jsonParser = jsonfactory.createJsonParser(jsonFile);	// create JSON parser
		user = new UserProfileInput();
		
		//init thread pool of fixed size
		executor = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);
	}

	private void shutdownParser() {
		executor.shutdown();
		while (!executor.isTerminated()) {
		}

		System.out.println(String.format("%1$s%2$s", Constants.PROGRAM_EXIT_MSG, numberOfRecords));
	}

}
