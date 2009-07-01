package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.FilterOperator;
import org.nideasystems.webtools.zwitrng.shared.model.TimeUnits;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class CampaignDO {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private String name;
	@Persistent
	private String filterByTemplateTags;
	@Persistent
	private String filterByTemplateText;
	@Persistent
	private FilterOperator filterOperator;
	@Persistent
	private Date startDate;
	@Persistent
	private Date endDate;
	@Persistent
	private int timeBetweenTweets;
	@Persistent
	private int maxTweetsPerTemplate;
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

	@Persistent
	private Date modified;

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

	public String getFilterByTemplateText() {
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
	}

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

	public int getMaxTweetsPerTemplate() {
		return maxTweetsPerTemplate;
	}

	public void setMaxTweetsPerTemplate(int maxTweetsPerTemplate) {
		this.maxTweetsPerTemplate = maxTweetsPerTemplate;
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

}
