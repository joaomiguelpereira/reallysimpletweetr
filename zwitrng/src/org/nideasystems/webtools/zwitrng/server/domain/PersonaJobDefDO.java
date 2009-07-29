package org.nideasystems.webtools.zwitrng.server.domain;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;



import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersonaJobDefDO{
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String name;
	@Persistent
	private long lastRun;
	@Persistent
	private long waitTime;
	@Persistent
	private String jobClass;
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getLastRun() {
		return lastRun;
	}
	public void setLastRun(long lastRun) {
		this.lastRun = lastRun;
	}
	public long getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	public String getJobClass() {
		return jobClass;
	}
	

}
	