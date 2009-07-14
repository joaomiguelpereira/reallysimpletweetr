package org.nideasystems.webtools.zwitrng.shared.model;

import java.io.Serializable;

public class TwitterUserDTO implements IDTO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3252789706672220852L;

	
	private String twitterScreenName;

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
	private boolean isNew = false;
	
	private String twitterStatusText;
	
	private boolean imFollowing = false;
	private boolean imBlocking = false;
	//if I'm following and if he/she's following me
	private boolean mutualFriendShip = false;
	
	public boolean isImFollowing() {
		return imFollowing;
	}
	public void setImFollowing(boolean imFollowing) {
		this.imFollowing = imFollowing;
	}
	public boolean isImBlocking() {
		return imBlocking;
	}
	public void setImBlocking(boolean imBlocking) {
		this.imBlocking = imBlocking;
	}
	public boolean isMutualFriendShip() {
		return mutualFriendShip;
	}
	public void setMutualFriendShip(boolean mutualFriendShip) {
		this.mutualFriendShip = mutualFriendShip;
	}
	public String getTwitterScreenName() {
		return twitterScreenName;
	}
	public void setTwitterScreenName(String twitterScreenName) {
		this.twitterScreenName = twitterScreenName;
	}
	
	public String getTwitterName() {
		return twitterName;
	}
	public void setTwitterName(String twitterName) {
		this.twitterName = twitterName;
	}
	public String getTwitterLocation() {
		return twitterLocation;
	}
	public void setTwitterLocation(String twitterLocation) {
		this.twitterLocation = twitterLocation;
	}
	public String getTwitterWeb() {
		return twitterWeb;
	}
	public void setTwitterWeb(String twitterWeb) {
		this.twitterWeb = twitterWeb;
	}
	public String getTwitterDescription() {
		return twitterDescription;
	}
	public void setTwitterDescription(String twitterDescription) {
		this.twitterDescription = twitterDescription;
	}
	public String getTwitterImageUrl() {
		return twitterImageUrl;
	}
	public void setTwitterImageUrl(String twitterImageUrl) {
		this.twitterImageUrl = twitterImageUrl;
	}
	public Integer getTwitterFollowers() {
		return twitterFollowers;
	}
	public void setTwitterFollowers(Integer twitterFollowers) {
		this.twitterFollowers = twitterFollowers;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTwitterUpdates() {
		return twitterUpdates;
	}
	public void setTwitterUpdates(Integer twitterUpdates) {
		this.twitterUpdates = twitterUpdates;
	}
	public Integer getTwitterFriends() {
		return twitterFriends;
	}
	public void setTwitterFriends(Integer twitterFriends) {
		this.twitterFriends = twitterFriends;
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
	}

	
}
