package org.nideasystems.webtools.zwitrng.client.objects;

public class TwitterAccountObj {

	private String twitterScreenName;
	private String twitterPassword;
	private String twitterUserName;	
	private String twitterDescription;
	private String twitterImageUrl;
	
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
