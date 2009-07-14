package org.nideasystems.webtools.zwitrng.shared.model;




public class TwitterAccountDTO extends TwitterUserDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6608385823608001282L;
/*	private String twitterScreenName;
	private String twitterPassword;
	private String twitterName;	
	private String twitterLocation;
	private String twitterWeb;
	private String twitterDescription;
	private String twitterImageUrl;
	private Integer twitterFollowers;
	private Integer id;
	//private String location;
	private Integer twitterUpdates;
	private Integer twitterFriends;
*/	private Boolean isOAuthenticated = false;
	private String oAuthToken;
	private String oAuthTokenSecret;
	private String oAuthLoginUrl;
	private int newFollowers;
	private int newFriends;
	private int newBlocking;
	//Indicates if the user is a new follower/friend
	
	private RateLimitsDTO rateLimits;
	
	
	
	//private ExtendedTwitterAccountDTO extendedUserAccount = null;
	
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
	
	/*public void setLocation(String location) {
		this.location = location;
	}
	public String getLocation() {
		return location;
	}*/
	
	public void setTwitterUpdateDto(TwitterUpdateDTO twitterUpdateDto) {
		this.twitterUpdateDto = twitterUpdateDto;
	}
	public TwitterUpdateDTO getTwitterUpdateDto() {
		return twitterUpdateDto;
	}
	
	/*public void setExtendedUserAccount(ExtendedTwitterAccountDTO extendedUserAccount) {
		this.extendedUserAccount = extendedUserAccount;
	}
	public ExtendedTwitterAccountDTO getExtendedUserAccount() {
		return extendedUserAccount;
	}
	public void setTwitterStatusText(String twitterStatusText) {
		this.twitterStatusText = twitterStatusText;
	}
	public String getTwitterStatusText() {
		return twitterStatusText;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public boolean isNew() {
		return isNew;
	}*/
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
	
	
}
