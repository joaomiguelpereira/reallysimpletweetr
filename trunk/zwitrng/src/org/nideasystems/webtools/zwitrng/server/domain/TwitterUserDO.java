package org.nideasystems.webtools.zwitrng.server.domain;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * NOT USED
 * @author jpereira
 *
 */
public class TwitterUserDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2783833684278502989L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private Date created;
	@Persistent
	private Date modified;
	@Persistent
	private PersonaDO persona;
	@Persistent
	private String name;
	@Persistent
	private String screenName;
	@Persistent
	private String getDescription;
	@Persistent
	private Integer gollowersCount;
	@Persistent
	private Integer friendsCount;
	@Persistent
	private String profileImageURL;
	@Persistent
	private String statusesCount;
	@Persistent
	private String location;
	@Persistent
	private Long id;
	@Persistent
	private String Url;
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
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
	public PersonaDO getPersona() {
		return persona;
	}
	public void setPersona(PersonaDO persona) {
		this.persona = persona;
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
	public String getDescription() {
		return getDescription;
	}
	public void setGetDescription(String getDescription) {
		this.getDescription = getDescription;
	}
	public Integer getGollowersCount() {
		return gollowersCount;
	}
	public void setGollowersCount(Integer gollowersCount) {
		this.gollowersCount = gollowersCount;
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
	

}
