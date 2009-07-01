package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.Date;



public class CampaignDTO implements IModel {

	private long id = -1;
	private String Name;
	private String filterByTemplateTags;
	private String filterByTemplateText;
	private FilterOperator filterOperator;
	private Date startDate;
	private Date endDate;
	private int minutesBetweenTweets;
	private int maxTweetsPerTemplate;
	private CampaignStatus status;
	private int tweetsSent;
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
	public int getMinutesBetweenTweets() {
		return minutesBetweenTweets;
	}
	public void setMinutesBetweenTweets(int minutesBetweenTweets) {
		this.minutesBetweenTweets = minutesBetweenTweets;
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
	public String getFilterOperatorAsText() {
		return filterOperator.equals(FilterOperator.AND)?"and":"or";
	}
	public FilterOperator getFilterOperator() {
		return filterOperator;
	}
	public void setFilterOperator(FilterOperator filterOperator) {
		this.filterOperator = filterOperator;
	}
	
	
}
