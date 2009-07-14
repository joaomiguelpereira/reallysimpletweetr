package org.nideasystems.webtools.zwitrng.shared.model;

import java.io.Serializable;

public class RateLimitsDTO implements IDTO, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4497626861798726718L;
	private int rateLimitLimit;
	private int rateLimitRemaining;
	private long rateLimitReset;
	public int getRateLimitLimit() {
		return rateLimitLimit;
	}
	public void setRateLimitLimit(int rateLimitLimit) {
		this.rateLimitLimit = rateLimitLimit;
	}
	public int getRateLimitRemaining() {
		return rateLimitRemaining;
	}
	public void setRateLimitRemaining(int rateLimitRemaining) {
		this.rateLimitRemaining = rateLimitRemaining;
	}
	public long getRateLimitReset() {
		return rateLimitReset;
	}
	public void setRateLimitReset(long rateLimitReset) {
		this.rateLimitReset = rateLimitReset;
	}

}
