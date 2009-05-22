package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.Date;


public class TwitterUpdateDTO implements IModel{

	//When was Update create
	private Date createdAt;
	private long id;
	private long replyToStatusId;
	private long InReplyToStatusId;
	private long inReplyToUserId;
	private int rateLimitLimit;
	private int rateLimitRemaining;
	private long rateLimitReset;
	private String source;
	private String text;
	private TwitterAccountDTO twitterAccount;
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getReplyToStatusId() {
		return replyToStatusId;
	}
	public void setReplyToStatusId(long replyToStatusId) {
		this.replyToStatusId = replyToStatusId;
	}
	public long getInReplyToUserId() {
		return inReplyToUserId;
	}
	public void setInReplyToUserId(long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public TwitterAccountDTO getTwitterAccount() {
		return twitterAccount;
	}
	public void setTwitterAccount(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}
	public void setInReplyToStatusId(long inReplyToStatusId) {
		InReplyToStatusId = inReplyToStatusId;
	}
	public long getInReplyToStatusId() {
		return InReplyToStatusId;
	}
	
	
}
