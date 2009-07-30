package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class JobsQueueDO {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Integer lastUsedJobIndex;
	
	@Persistent
	private Integer lastPersonaUsedIndex;
	@Persistent
	private List<JobDO> jobs;


	public void setKey(Key key) {
		this.key = key;
	}


	public Key getKey() {
		return key;
	}


	public void setJobs(List<JobDO> jobs) {
		this.jobs = jobs;
	}


	public List<JobDO> getJobs() {
		return jobs;
	}


	public void setLastUsedJobIndex(Integer lastUsedJobIndex) {
		this.lastUsedJobIndex = lastUsedJobIndex;
	}


	public Integer getLastUsedJobIndex() {
		return lastUsedJobIndex;
	}


	public void setLastPersonaUsedIndex(Integer lastPersonaUsedIndex) {
		this.lastPersonaUsedIndex = lastPersonaUsedIndex;
	}


	public Integer getLastPersonaUsedIndex() {
		return lastPersonaUsedIndex;
	}




}
