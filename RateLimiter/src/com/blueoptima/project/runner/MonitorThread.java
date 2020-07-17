/**
 * 
 */
package com.blueoptima.project.runner;

import com.blueoptima.project.model.RateLimitParam;
import com.blueoptima.project.utility.Constants;

/**
 * @author jghosh
 * Class for a checking monitor on the rate limits remaining 
 */
public class MonitorThread implements Runnable {
	RateLimitParam githubRateLimit;
	private int seconds;
	private boolean run = true;

	public MonitorThread(RateLimitParam githubRateLimit, int delay) {
		this.githubRateLimit = githubRateLimit;
		this.seconds = delay;
	}

	public void shutdown() {
		this.run = false;
	}

	@Override
	public void run() {
		while (run) {
			try {
				System.out.println(
						"MonitorThread started githubRemaining: " + githubRateLimit.getX_RateLimit_Remaining());
				Thread.sleep(seconds * 1000);
				
				//Checking if the Rate Limit Remaining for Github API calls is finished or not
				if (githubRateLimit.getX_RateLimit_Remaining() > 5) {
					//acquiring Lock first
					synchronized (githubRateLimit) {
						if (githubRateLimit.getX_RateLimit_Remaining() > 5)
							githubRateLimit.notify();
					}
				} else {
					// get active at this POT
					// long githubResetAt = githubRateLimit.getX_RateLimit_Reset()*1000L;
					// long currentTimestamp = System.currentTimeMillis();
					// long sleepingTime = githubResetAt-currentTimestamp+10;
					
					//acquiring Lock first
					synchronized (githubRateLimit) {
						long startTime = githubRateLimit.getTimestamp();
						long currentTimestamp = System.currentTimeMillis();
						long sleepingTime = startTime + 360000L - currentTimestamp;

						githubRateLimit.setResetIndicator(true);						
						System.out
								.println(String.format("Rate Limit Exceeded at TIMESTAMP: %1$s Sleeping for %2$s millis",
										currentTimestamp, sleepingTime));
						if(sleepingTime < 0)
							sleepingTime = 360000L;
						Thread.sleep(sleepingTime);
						githubRateLimit.setX_RateLimit_Remaining(Constants.GITHUB_RATE_LIMIT);
					}
				}
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}

	}
}