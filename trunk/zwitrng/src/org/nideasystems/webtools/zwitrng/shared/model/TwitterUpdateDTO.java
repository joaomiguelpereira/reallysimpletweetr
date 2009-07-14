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
	private String source;
	private String text;
	private TwitterUserDTO twitterUser;
	private TwitterAccountDTO sendingTwitterAccount;
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
	public long getInReplyToStatusId() {
		return InReplyToStatusId;
	}
	public void setInReplyToStatusId(long inReplyToStatusId) {
		InReplyToStatusId = inReplyToStatusId;
	}
	public long getInReplyToUserId() {
		return inReplyToUserId;
	}
	public void setInReplyToUserId(long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}
	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}
	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
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
	public TwitterUserDTO getTwitterUser() {
		return twitterUser;
	}
	public void setTwitterUser(TwitterUserDTO twitterUser) {
		this.twitterUser = twitterUser;
	}
	public TwitterAccountDTO getSendingTwitterAccount() {
		return sendingTwitterAccount;
	}
	public void setSendingTwitterAccount(TwitterAccountDTO sendingTwitterAccount) {
		this.sendingTwitterAccount = sendingTwitterAccount;
	}
	public UpdatesType getType() {
		return type;
	}
	public void setType(UpdatesType type) {
		this.type = type;
	}
	
	
	
}
