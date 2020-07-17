/**
 * 
 */
package com.blueoptima.project.runner;

import com.blueoptima.project.model.RateLimitParam;
import com.blueoptima.project.parser.IInputParsingTool;
import com.blueoptima.project.parser.InputParser;
import com.blueoptima.project.utility.Constants;

/**
 * @author jghosh
 * Main class to start the application
 *
 */
public class ApplicationRunner {
	
	
	public static void main(String argv[]) {
		
		//Setting the lock object RateLimitParam with default values 
		RateLimitParam githubRateLimit = new RateLimitParam();
		githubRateLimit.setX_RateLimit_Reset(0);
		githubRateLimit.setX_RateLimit_Remaining(Constants.GITHUB_RATE_LIMIT);
		githubRateLimit.setResetIndicator(true);
		githubRateLimit.setTimestamp(System.currentTimeMillis());
		
		//start the monitoring thread
        MonitorThread monitor = new MonitorThread(githubRateLimit, Constants.MONITOR_RECHECK_INTERVAL);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();
		
		String jsonFilePath = Constants.INPUT_FILE;
		
		//Invoking the InputParser passing it the lock object – RateLimitParam.
		IInputParsingTool jksonParser = new InputParser(githubRateLimit);
		jksonParser.processJsonInput(jsonFilePath);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
        monitor.shutdown();
		
	}
}
