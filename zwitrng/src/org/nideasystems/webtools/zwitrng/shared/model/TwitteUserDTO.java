package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.Date;


public class TwitteUserDTO implements IDTO {
	
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getGetDescription() {
		return getDescription;
	}

	public void setDescription(String getDescription) {
		this.getDescription = getDescription;
	}

	public Integer getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(Integer followersCount) {
		this.followersCount = followersCount;
	}

	public Integer getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(Integer friendsCount) {
		this.friendsCount = friendsCount;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}

	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

	public String getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(String statusesCount) {
		this.statusesCount = statusesCount;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	private Date created;
	
	private Date modified;
	
	
	private String name;
	
	private String screenName;
	
	private String getDescription;
	
	private Integer followersCount;
	
	private Integer friendsCount;
	
	private String profileImageURL;
	
	private String statusesCount;
	
	private String location;
	
	private Long id;
	
	private String Url;

	
	

}
