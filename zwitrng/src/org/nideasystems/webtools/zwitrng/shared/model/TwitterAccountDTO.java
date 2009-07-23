package org.nideasystems.webtools.zwitrng.shared.model;




public class TwitterAccountDTO extends TwitterUserDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6608385823608001282L;
	private Boolean isOAuthenticated = false;
	private String oAuthToken;
	private String oAuthTokenSecret;
	private String oAuthLoginUrl;
	private int followBackQueueSize;
	private int ignoreUsersListSize;
	private int autoFollowedUsersSize;
	private int newFollowers;
	private int newFriends;
	private int newBlocking;
	
	private RateLimitsDTO rateLimits;
		
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
	
	public void setIsOAuthenticated(Boolean isOAuthenticated) {
		this.isOAuthenticated = isOAuthenticated;
	}
	public Boolean getIsOAuthenticated() {
		return isOAuthenticated;
	}
	
	
	public void setTwitterUpdateDto(TwitterUpdateDTO twitterUpdateDto) {
		this.twitterUpdateDto = twitterUpdateDto;
	}
	public TwitterUpdateDTO getTwitterUpdateDto() {
		return twitterUpdateDto;
	}
	
	public void setNewFollowers(int newFollowers) {
		this.newFollowers = newFollowers;
	}
	public int getNewFollowers() {
		return newFollowers;
	}
	public void setNewFriends(int newFriends) {
		this.newFriends = newFriends;
	}
	public int getNewFriends() {
		return newFriends;
	}
	public void setNewBlocking(int newBlocking) {
		this.newBlocking = newBlocking;
	}
	public int getNewBlocking() {
		return newBlocking;
	}
	
	public void setRateLimits(RateLimitsDTO rateLimits) {
		this.rateLimits = rateLimits;
	}
	public RateLimitsDTO getRateLimits() {
		return rateLimits;
	}
	public void setFollowBackQueueSize(int followBackQueueSize) {
		this.followBackQueueSize = followBackQueueSize;
	}
	public int getFollowBackQueueSize() {
		return followBackQueueSize;
	}
	public void setIgnoreUsersListSize(int ignoreUsersListSize) {
		this.ignoreUsersListSize = ignoreUsersListSize;
	}
	public int getIgnoreUsersListSize() {
		return ignoreUsersListSize;
	}
	public void setAutoFollowedUsersSize(int autoFollowedUsersSize) {
		this.autoFollowedUsersSize = autoFollowedUsersSize;
	}
	public int getAutoFollowedUsersSize() {
		return autoFollowedUsersSize;
	}
	
	
}
