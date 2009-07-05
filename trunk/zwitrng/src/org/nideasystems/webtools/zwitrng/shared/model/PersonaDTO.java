package org.nideasystems.webtools.zwitrng.shared.model;


import java.util.Date;
import java.util.List;


import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;



public class PersonaDTO implements IDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = null;
	private Date creationDate =null;
	private String userEmail = null;
	private List<FilterCriteriaDTO> filters;
	private long id = 0;
	

	
	

	private TwitterAccountDTO twitterAccount = null;
	

	
	
	public PersonaDTO() {
	
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

	public void setTwitterAccount(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountDTO getTwitterAccount() {
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

	public static PersonaDTO fromJson(JSONValue jsonObj) {
		PersonaDTO persona = new PersonaDTO();
		
		String personaName = jsonObj.isObject().get("personaName").isString().stringValue();
		
		persona.setName(personaName);
		
		JSONObject jsonTwitterAccount = jsonObj.isObject().get("twitterAccount").isObject();
		
		
		TwitterAccountDTO twitterAccount = new TwitterAccountDTO();
		
		
		twitterAccount.setTwitterScreenName(jsonTwitterAccount.get("twitterScreenName").isString().stringValue());
		twitterAccount.setTwitterName(jsonTwitterAccount.get("twitterUserName").isString().stringValue());
		twitterAccount.setTwitterDescription(jsonTwitterAccount.get("twitterDescription").isString().stringValue());
		twitterAccount.setTwitterImageUrl(jsonTwitterAccount.get("twitterImageUrl").isString().stringValue());
		persona.setTwitterAccount(twitterAccount);
		return persona;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setFilters(List<FilterCriteriaDTO> filters) {
		this.filters = filters;
	}

	public List<FilterCriteriaDTO> getFilters() {
		return filters;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}



	
	
}
