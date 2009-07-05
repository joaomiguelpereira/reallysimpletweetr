package org.nideasystems.webtools.zwitrng.server.domain;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TwitterAccountDO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6651739528025255161L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String twitterName;
	
	@Persistent
	private String oAuthToken;
	
	@Persistent
	private String oAuthTokenSecret;

	@Persistent
	private String oAuthLoginUrl;

	public void setTwitterName(String twitterName) {
		this.twitterName = twitterName;
	}

	public String getTwitterName() {
		return twitterName;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public void setOAuthTokenSecret(String oAuthTokenSecret) {
		this.oAuthTokenSecret = oAuthTokenSecret;
	}

	public String getOAuthTokenSecret() {
		return oAuthTokenSecret;
	}

	public void setOAuthToken(String oAuthToken) {
		this.oAuthToken = oAuthToken;
	}

	public String getOAuthToken() {
		return oAuthToken;
	}

	public void setOAuthLoginUrl(String oAuthLoginUrl) {
		this.oAuthLoginUrl = oAuthLoginUrl;
	}

	public String getOAuthLoginUrl() {
		return oAuthLoginUrl;
	}

	
}
