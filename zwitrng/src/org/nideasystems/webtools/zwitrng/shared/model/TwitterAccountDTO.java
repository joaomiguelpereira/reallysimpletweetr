package org.nideasystems.webtools.zwitrng.shared.model;



public class TwitterAccountDTO implements IModel {

	private String twitterScreenName;
	private String twitterPassword;
	private String twitterName;	
	private String twitterLocation;
	private String twitterWeb;
	private String twitterDescription;
	private String twitterImageUrl;
	private Integer twitterFollowers;
	private Integer id;
	private String location;
	private Integer twitterUpdates;
	private Integer twitterFriends;
	
	
	
	private Boolean isOAuthenticated = false;
	private String oAuthToken;
	private String oAuthTokenSecret;
	private String oAuthLoginUrl;
	
	private TwitterUpdateDTO twitterUpdateDto;
	
	
	public String getOAuthToken() {
		return oAuthToken;
	}
	public void setOAuthToken(String authToken) {
		oAuthToken = authToken;
	}
	public String getOAuthTokenSecret() {
		return oAuthTokenSecret;
	}
	public void setOAuthTokenSecret(String authTokenSecret) {
		oAuthTokenSecret = authTokenSecret;
	}
	public String getOAuthLoginUrl() {
		return oAuthLoginUrl;
	}
	public void setOAuthLoginUrl(String authLoginUrl) {
		oAuthLoginUrl = authLoginUrl;
	}
	public Integer getTwitterFollowers() {
		return twitterFollowers;
	}
	public void setTwitterFollowers(Integer twitterFollowers) {
		this.twitterFollowers = twitterFollowers;
	}
	
	public Integer getTwitterUpdates() {
		return twitterUpdates;
	}
	public void setTwitterUpdates(Integer twitterUpdates) {
		this.twitterUpdates = twitterUpdates;
	}
	
	
	
	
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
	public void setTwitterName(String twitterUserName) {
		this.twitterName = twitterUserName;
	}
	public String getTwitterName() {
		return twitterName;
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
	public void setIsOAuthenticated(Boolean isOAuthenticated) {
		this.isOAuthenticated = isOAuthenticated;
	}
	public Boolean getIsOAuthenticated() {
		return isOAuthenticated;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return id;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocation() {
		return location;
	}
	
	public void setTwitterUpdateDto(TwitterUpdateDTO twitterUpdateDto) {
		this.twitterUpdateDto = twitterUpdateDto;
	}
	public TwitterUpdateDTO getTwitterUpdateDto() {
		return twitterUpdateDto;
	}
	public void setTwitterFriends(Integer friends) {
		this.twitterFriends = friends;
	}
	public Integer getTwitterFriends() {
		return twitterFriends;
	}
	public void setTwitterLocation(String twitterLocation) {
		this.twitterLocation = twitterLocation;
	}
	public String getTwitterLocation() {
		return twitterLocation;
	}
	public void setTwitterWeb(String twitterWeb) {
		this.twitterWeb = twitterWeb;
	}
	public String getTwitterWeb() {
		return twitterWeb;
	}
	
}
