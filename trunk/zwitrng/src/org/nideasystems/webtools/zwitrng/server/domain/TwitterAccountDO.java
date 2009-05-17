package org.nideasystems.webtools.zwitrng.server.domain;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TwitterAccountDO {

	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String twitterName;
	
	@Persistent
	private String twitterPass;

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

	public void setTwitterPass(String twitterPass) {
		this.twitterPass = twitterPass;
	}

	public String getTwitterPass() {
		return twitterPass;
	}
	
}
