/**
 * 
 */
package com.blueoptima.project.model;

import com.blueoptima.project.utility.Constants;

/**
 * @author jghosh
 * Lock Object
 */
public class RateLimitParam {

	public final long X_RateLimit_Limit = Constants.GITHUB_RATE_LIMIT;
	private long X_RateLimit_Reset;
	private long X_RateLimit_Remaining;
	private boolean resetIndicator;
	private long timestamp;

	/**
	 * @return the x_RateLimit_Reset
	 */
	public long getX_RateLimit_Reset() {
		return X_RateLimit_Reset;
	}

	/**
	 * @param x_RateLimit_Reset the x_RateLimit_Reset to set
	 */
	public void setX_RateLimit_Reset(long x_RateLimit_Reset) {
		X_RateLimit_Reset = x_RateLimit_Reset;
	}

	/**
	 * @return the githubRequestCount
	 */

	public long getX_RateLimit_Remaining() {
		return X_RateLimit_Remaining;
	}

	/**
	 * @param githubRequestCount the githubRequestCount to set
	 */
	public void setX_RateLimit_Remaining(long X_RateLimit_Remaining) {
		this.X_RateLimit_Remaining = X_RateLimit_Remaining;
	}

	/**
	 * @return the resetIndicator
	 */
	public boolean isResetIndicator() {
		return resetIndicator;
	}

	/**
	 * @param resetIndicator the resetIndicator to set
	 */
	public void setResetIndicator(boolean resetIndicator) {
		this.resetIndicator = resetIndicator;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
