package org.nideasystems.webtools.zwitrng.shared.model;



public class TwitterAccountDTO implements IModel {

	private String twitterScreenName;
	private String twitterPassword;
	private String twitterUserName;	
	private String twitterDescription;
	private String twitterImageUrl;
	private Integer twitterFollowers;
	public Integer getTwitterFollowers() {
		return twitterFollowers;
	}
	public void setTwitterFollowers(Integer twitterFollowers) {
		this.twitterFollowers = twitterFollowers;
	}
	public Integer getTwitterFollowing() {
		return twitterFollowing;
	}
	public void setTwitterFollowing(Integer twitterFollowing) {
		this.twitterFollowing = twitterFollowing;
	}
	public Integer getTwitterUpdates() {
		return twitterUpdates;
	}
	public void setTwitterUpdates(Integer twitterUpdates) {
		this.twitterUpdates = twitterUpdates;
	}
	private Integer twitterFollowing;
	private Integer twitterUpdates;
	
	
	public void setTwitterPassword(String twitterPassword) {
		this.twitterPassword = twitterPassword;
	}
	public String getTwitterPassword() {
		return twitterPassword;
	}
	public void setTwitterScreenName(String twitterScreenName) {
		this.twitterScreenName = twitterScreenName;
	}
	public String getTwitterScreenName() {
		return twitterScreenName;
	}
	public void setTwitterUserName(String twitterUserName) {
		this.twitterUserName = twitterUserName;
	}
	public String getTwitterUserName() {
		return twitterUserName;
	}
	public void setTwitterDescription(String twitterDescription) {
		this.twitterDescription = twitterDescription;
	}
	public String getTwitterDescription() {
		return twitterDescription;
	}
	public void setTwitterImageUrl(String twitterImageUrl) {
		this.twitterImageUrl = twitterImageUrl;
	}
	public String getTwitterImageUrl() {
		return twitterImageUrl;
	}
	
}
