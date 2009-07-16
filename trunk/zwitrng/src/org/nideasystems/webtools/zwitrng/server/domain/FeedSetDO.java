package org.nideasystems.webtools.zwitrng.server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FeedSetDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6219678770474223539L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private String name;

	@Persistent
	private PersonaDO persona;
	
	@Persistent
	private Date created;
	@Persistent
	private Date modified;
	

	@Persistent
	private List<String> feedUrls;
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public List<String> getFeedUrls() {
		return feedUrls;
	}
	public void setFeedUrls(List<String> feedUrls) {
		this.feedUrls = feedUrls;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	public void addFeedUrl(String url) {
		if (this.feedUrls == null) {
			this.feedUrls = new ArrayList<String>();
		}
		this.feedUrls.add(url);
	}
	
	
	
	
	public void setPersona(PersonaDO persona) {
		this.persona = persona;
	}
	public PersonaDO getPersona() {
		return persona;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getCreated() {
		return created;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Date getModified() {
		return modified;
	}
	

}
