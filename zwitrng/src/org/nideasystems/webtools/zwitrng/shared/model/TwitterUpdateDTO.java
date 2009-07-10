package org.nideasystems.webtools.zwitrng.shared.model;

import java.io.Serializable;
import java.util.Date;

import org.nideasystems.webtools.zwitrng.shared.UpdatesType;


public class TwitterUpdateDTO implements IDTO, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4765112483942956314L;
	//When was Update create
	private Date createdAt;
	private long id;
	private long replyToStatusId;
	private long InReplyToStatusId = -1;
	private long inReplyToUserId = -1;
	private String inReplyToScreenName = null;
	
	private int rateLimitLimit;
	private int rateLimitRemaining;
	private long rateLimitReset;
	private String source;
	private String text;
	private TwitterAccountDTO twitterAccount;
	private UpdatesType type;
	
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
	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}
	public UpdatesType getType() {
		return type;
	}
	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}
	public void setType(UpdatesType type) {
		this.type = type;
		
	}
	
	
}
