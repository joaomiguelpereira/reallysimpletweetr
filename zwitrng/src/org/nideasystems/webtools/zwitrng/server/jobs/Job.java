package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;



import com.google.appengine.api.datastore.Key;


public class Job implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7108881661958549657L;
	private Map<String,Object> parameters = new HashMap<String, Object>();
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
	public void addParameter(String string, TwitterUpdateDTO update) {
		parameters.put(string, update);
		
	}
	public Map<String, Object> getParameters() {
		return this.parameters;
		
		
	}
	public void setParameters(Map<String, Object> parameters2) {
		// TODO Auto-generated method stub
		
	}


}
