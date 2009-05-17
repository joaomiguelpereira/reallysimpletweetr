package org.nideasystems.webtools.zwitrng.client.objects;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;


public class PersonaObj implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = null;
	private Date creationDate =null;
	

	private TwitterAccountObj twitterAccount = null;
	

	
	
	public PersonaObj() {
	
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setTwitterAccount(TwitterAccountObj twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountObj getTwitterAccount() {
		return twitterAccount;
	}

	public JSONObject toJson() {
		//creat jsonObj
		JSONObject personaObj = new JSONObject();
		personaObj.put("personaName", new JSONString(this.getName()));
		
		JSONObject twitterAccount = new JSONObject();
		twitterAccount.put("twitterScreenName", new JSONString(this.getTwitterAccount().getTwitterScreenName()));
		twitterAccount.put("twitterPassword", new JSONString(this.getTwitterAccount().getTwitterPassword()));
		
		personaObj.put("twitterAccount", twitterAccount);
		
		return personaObj;
	}

	public static PersonaObj fromJson(JSONValue jsonObj) {
		PersonaObj persona = new PersonaObj();
		
		String personaName = jsonObj.isObject().get("personaName").isString().stringValue();
		
		persona.setName(personaName);
		
		JSONObject jsonTwitterAccount = jsonObj.isObject().get("twitterAccount").isObject();
		
		
		TwitterAccountObj twitterAccount = new TwitterAccountObj();
		
		
		twitterAccount.setTwitterScreenName(jsonTwitterAccount.get("twitterScreenName").isString().stringValue());
		twitterAccount.setTwitterUserName(jsonTwitterAccount.get("twitterUserName").isString().stringValue());
		twitterAccount.setTwitterDescription(jsonTwitterAccount.get("twitterDescription").isString().stringValue());
		twitterAccount.setTwitterImageUrl(jsonTwitterAccount.get("twitterImageUrl").isString().stringValue());
		persona.setTwitterAccount(twitterAccount);
		return persona;
	}
	
}
