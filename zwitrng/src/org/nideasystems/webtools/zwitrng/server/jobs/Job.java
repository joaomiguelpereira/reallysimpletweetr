package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.Serializable;



import com.google.appengine.api.datastore.Key;


public class Job implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7108881661958549657L;
	private String jobClassName;
	private Key personaKey;
	public String getJobClassName() {
		return jobClassName;
	}
	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}
	public Key getPersonaKey() {
		return personaKey;
	}
	public void setPersonaKey(Key personaKey) {
		this.personaKey = personaKey;
	}


}
