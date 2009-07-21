package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.Date;
import java.util.List;

public class CampaignDTO implements IDTO {

	private long id = -1;
	private String Name;

	private List<String> templatesNames;

	private Date startDate;
	private Date endDate;
	private int timeBetweenTweets;
	private int maxTweets;
	private CampaignStatus status;
	private int tweetsSent;
	private TimeUnits timeUnit;
	private Date created;
	private Date modified;
	private int startHourOfTheDay = 0;
	private int endHourOfTheDay = 22;
	private Date nextRun;
	private Date lastRun;
	private boolean useTemplatesRandomly;
	private boolean allowRepeatTemplates;
	private boolean trackClicksOnLinks;
	private boolean limitNumberOfTweetsSent;
	private String info; 

	public int getStartHourOfTheDay() {
		return startHourOfTheDay;
	}

	public void setStartHourOfTheDay(int startHourOfTheDay) {
		this.startHourOfTheDay = startHourOfTheDay;
	}

	public int getEndHourOfTheDay() {
		return endHourOfTheDay;
	}

	public void setEndHourOfTheDay(int endHourOfTheDay) {
		this.endHourOfTheDay = endHourOfTheDay;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
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

	public int getMaxTweets() {
		return maxTweets;
	}

	public void setMaxTweets(int maxTweets) {
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

	/*
	 * public String getFilterOperatorAsText() { return
	 * filterOperator.equals(FilterOperator.AND)?"and":"or"; } public
	 * FilterOperator getFilterOperator() { return filterOperator; } public void
	 * setFilterOperator(FilterOperator filterOperator) { this.filterOperator =
	 * filterOperator; }
	 */
	public void setTimeUnit(TimeUnits timeUnit) {
		this.timeUnit = timeUnit;
	}

	public TimeUnits getTimeUnit() {
		return timeUnit;
	}

	public void setCreated(Date created) {
		this.created = created;

	}

	public void setModified(Date modified) {
		this.modified = modified;

	}

	public Date getCreated() {
		return created;
	}

	public Date getModified() {
		return modified;
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

	public void setTemplatesNames(List<String> templatesNames) {
		this.templatesNames = templatesNames;
	}

	public List<String> getTemplatesNames() {
		return templatesNames;
	}

	public void setUseTemplatesRandomly(boolean useTemplatesRandomly) {
		this.useTemplatesRandomly = useTemplatesRandomly;
	}

	public boolean isUseTemplatesRandomly() {
		return useTemplatesRandomly;
	}

	public void setAllowRepeatTemplates(boolean allowRepeatTemplates) {
		this.allowRepeatTemplates = allowRepeatTemplates;
	}

	public boolean isAllowRepeatTemplates() {
		return allowRepeatTemplates;
	}

	public void setTrackClicksOnLinks(boolean trackClicksOnLinks) {
		this.trackClicksOnLinks = trackClicksOnLinks;
	}

	public boolean isTrackClicksOnLinks() {
		return trackClicksOnLinks;
	}

	public void setLimitNumberOfTweetsSent(boolean limitNumberOfTweetsSent) {
		this.limitNumberOfTweetsSent = limitNumberOfTweetsSent;
	}

	public boolean isLimitNumberOfTweetsSent() {
		return limitNumberOfTweetsSent;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

}
