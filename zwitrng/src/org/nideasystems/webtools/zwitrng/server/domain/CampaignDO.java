package org.nideasystems.webtools.zwitrng.server.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.TimeUnits;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class CampaignDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 387081705474081349L;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private String name;
	@Persistent
	private String filterByTemplateTags;
	@Persistent
	private Date startDate;
	@Persistent
	private Date endDate;
	@Persistent
	private int timeBetweenTweets;
	@Persistent
	private Integer maxTweets;
	@Persistent
	private CampaignStatus status;
	@Persistent
	private int tweetsSent;
	@Persistent
	private TimeUnits timeUnit;
	// Parent Persona
	@Persistent
	private PersonaDO persona;
	@Persistent
	private Date created;
	
	@Persistent 
	private Date lastRun;
	
	@Persistent 
	private Date nextRun;
	
	@Persistent
	private Date modified;
	private Integer endHourOfTheDay=0;
	@Persistent
	private Integer startHourOfTheDay=22;
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilterByTemplateTags() {
		return filterByTemplateTags;
	}

	public void setFilterByTemplateTags(String filterByTemplateTags) {
		this.filterByTemplateTags = filterByTemplateTags;
	}

	/*public String getFilterByTemplateText() {
		return filterByTemplateText;
	}

	public void setFilterByTemplateText(String filterByTemplateText) {
		this.filterByTemplateText = filterByTemplateText;
	}

	public FilterOperator getFilterOperator() {
		return filterOperator;
	}

	public void setFilterOperator(FilterOperator filterOperator) {
		this.filterOperator = filterOperator;
	}*/

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getTimeBetweenTweets() {
		return timeBetweenTweets;
	}

	public void setTimeBetweenTweets(int timeBetweenTweets) {
		this.timeBetweenTweets = timeBetweenTweets;
	}

	public Integer getMaxTweets() {
		return maxTweets;
	}

	public void setMaxTweets(Integer maxTweets) {
		this.maxTweets = maxTweets;
	}

	public CampaignStatus getStatus() {
		return status;
	}

	public void setStatus(CampaignStatus status) {
		this.status = status;
	}

	public int getTweetsSent() {
		return tweetsSent;
	}

	public void setTweetsSent(int tweetsSent) {
		this.tweetsSent = tweetsSent;
	}

	public TimeUnits getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnits timeUnit) {
		this.timeUnit = timeUnit;
	}

	public PersonaDO getPersona() {
		return persona;
	}

	public void setPersona(PersonaDO persona) {
		this.persona = persona;
	}

	public void setEndHourOfTheDay(Integer endHourOfTheDay) {
		this.endHourOfTheDay = endHourOfTheDay;
		
	}

	public void setStartHourOfTheDay(Integer startHourOfTheDay) {
		this.startHourOfTheDay = startHourOfTheDay;
		
	}

	public Integer getEndHourOfTheDay() {
		return endHourOfTheDay;
	}

	public Integer getStartHourOfTheDay() {
		return startHourOfTheDay;
	}

	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}

	public Date getLastRun() {
		return lastRun;
	}

	public void setNextRun(Date nextRun) {
		this.nextRun = nextRun;
	}

	public Date getNextRun() {
		return nextRun;
	}

	


}
