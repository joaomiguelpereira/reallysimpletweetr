package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class CampaignInstanceDO {

	private static final long serialVersionUID = 387081705474081349L;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent 
	private Date lastRun;
	
	@Persistent 
	private Date nextRun;
	
	@Persistent
	private Integer lastUsedTemplateNameIndex;
	@Persistent
	private List<String> tweetTemplates;
	
	
	@Persistent
	private int tweetsSent;


	public Key getKey() {
		return key;
	}


	public void setKey(Key key) {
		this.key = key;
	}


	public Date getLastRun() {
		return lastRun;
	}


	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}


	public Date getNextRun() {
		return nextRun;
	}


	public void setNextRun(Date nextRun) {
		this.nextRun = nextRun;
	}


	public int getTweetsSent() {
		return tweetsSent;
	}


	public void setTweetsSent(int tweetsSent) {
		this.tweetsSent = tweetsSent;
	}


	public void setNextTemplateNameIndex(Integer lastUsedTemplateNameIndex) {
		this.lastUsedTemplateNameIndex = lastUsedTemplateNameIndex;
	}


	public Integer getNextTemplateNameIndex() {
		return lastUsedTemplateNameIndex;
	}


	public void setTweetTemplates(List<String> tweetTemplates) {
		this.tweetTemplates = tweetTemplates;
	}


	public List<String> getTweetTemplates() {
		return tweetTemplates;
	}
	
	

	
	
	
	

}
