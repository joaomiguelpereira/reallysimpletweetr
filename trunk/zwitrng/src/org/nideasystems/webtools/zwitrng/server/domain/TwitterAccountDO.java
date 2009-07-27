package org.nideasystems.webtools.zwitrng.server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TwitterAccountDO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6651739528025255161L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String twitterName;
	
	@Persistent
	private String oAuthToken;
	
	@Persistent
	private String oAuthTokenSecret;

	@Persistent
	private String oAuthLoginUrl;

	@Persistent 
	private RateLimitsDO rateLimits;
	
	@Persistent
	private Set<Integer> followersIds;

	@Persistent
	private Set<Integer> followingIds;

	@Persistent
	private Set<Integer> blockingIds;
	
	@Persistent
	private List<Integer> autoFollowBackIdsQueue;

	@Persistent
	private List<Integer> autoUnFollowBackIdsQueue;


	@Persistent
	private Set<Integer> ignoreUsersIds;;
	@Persistent
	private Set<Integer> autoUnfollowedIds;
	
	

	@Persistent
	private Integer id;

	@Persistent
	private Integer autoFollowedCount;

	
	public void setTwitterName(String twitterName) {
		this.twitterName = twitterName;
	}

	public String getTwitterName() {
		return twitterName;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public void setOAuthTokenSecret(String oAuthTokenSecret) {
		this.oAuthTokenSecret = oAuthTokenSecret;
	}

	public String getOAuthTokenSecret() {
		return oAuthTokenSecret;
	}

	public void setOAuthToken(String oAuthToken) {
		this.oAuthToken = oAuthToken;
	}

	public String getOAuthToken() {
		return oAuthToken;
	}

	public void setOAuthLoginUrl(String oAuthLoginUrl) {
		this.oAuthLoginUrl = oAuthLoginUrl;
	}

	public String getOAuthLoginUrl() {
		return oAuthLoginUrl;
	}

	public void setRateLimits(RateLimitsDO rateLimits) {
		this.rateLimits = rateLimits;
	}

	public RateLimitsDO getRateLimits() {
		return rateLimits;
	}

	public void setFollowersIds(Set<Integer> followersIds) {
		this.followersIds = followersIds;
	}

	public Set<Integer> getFollowersIds() {
		return followersIds;
	}

	public void setFollowingIds(Set<Integer> followingIds) {
		this.followingIds = followingIds;
	}

	public Set<Integer> getFollowingIds() {
		return followingIds;
	}

	public void setBlockingIds(Set<Integer> blockingIds) {
		this.blockingIds = blockingIds;
	}

	public Set<Integer> getBlockingIds() {
		return blockingIds;
	}



	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void addFollowingId(Integer integer) {
		this.followingIds.add(integer);
		
	}

	public void setAutoFollowBackIdsQueue(List<Integer> autoFollowBackIdsQueue) {
		this.autoFollowBackIdsQueue = autoFollowBackIdsQueue;
	}

	public List<Integer> getAutoFollowBackIdsQueue() {
		return autoFollowBackIdsQueue;
	}
	
	public void addFollowBackId(Integer id) {
		if (this.autoFollowBackIdsQueue==null) {
			autoFollowBackIdsQueue = new ArrayList<Integer>();
		}
		this.autoFollowBackIdsQueue.add(id);
	}

	public void setIgnoreUsersIds(Set<Integer> ignoreUsersIds) {
		this.ignoreUsersIds = ignoreUsersIds;
	}

	public Set<Integer> getIgnoreUsersIds() {
		return ignoreUsersIds;
	}

	public void setAutoFollowedCount(Integer autoFollowedCount) {
		this.autoFollowedCount = autoFollowedCount;
	}

	public Integer getAutoFollowedCount() {
		return autoFollowedCount;
	}

	public void setAutoUnFollowBackIdsQueue(List<Integer> autoUnFollowBackIdsQueue) {
		this.autoUnFollowBackIdsQueue = autoUnFollowBackIdsQueue;
	}

	public List<Integer> getAutoUnFollowBackIdsQueue() {
		return autoUnFollowBackIdsQueue;
	}


	public void setAutoUnfollowedIds(Set<Integer> autoUnfollowedIds) {
		this.autoUnfollowedIds = autoUnfollowedIds;
	}

	public Set<Integer> getAutoUnfollowedIds() {
		return autoUnfollowedIds;
	}

	

	

	
}
