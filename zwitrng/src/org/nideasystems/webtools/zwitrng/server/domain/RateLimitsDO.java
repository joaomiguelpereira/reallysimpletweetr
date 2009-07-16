package org.nideasystems.webtools.zwitrng.server.domain;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RateLimitsDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7669262857351582637L;

	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Integer rateLimitLimit;
	@Persistent
	private Integer rateLimitRemaining;
	@Persistent
	private Long rateLimitReset;
	@Persistent
	private TwitterAccountDO twitterAccount;
	
	public Integer getRateLimitLimit() {
		return rateLimitLimit;
	}
	public void setRateLimitLimit(Integer rateLimitLimit) {
		this.rateLimitLimit = rateLimitLimit;
	}
	public Integer getRateLimitRemaining() {
		return rateLimitRemaining;
	}
	public void setRateLimitRemaining(Integer rateLimitRemaining) {
		this.rateLimitRemaining = rateLimitRemaining;
	}
	public Long getRateLimitReset() {
		return rateLimitReset;
	}
	public void setRateLimitReset(Long rateLimitReset) {
		this.rateLimitReset = rateLimitReset;
	}
	public void setTwitterAccount(TwitterAccountDO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}
	public TwitterAccountDO getTwitterAccount() {
		return twitterAccount;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public Key getKey() {
		return key;
	}
	
}
