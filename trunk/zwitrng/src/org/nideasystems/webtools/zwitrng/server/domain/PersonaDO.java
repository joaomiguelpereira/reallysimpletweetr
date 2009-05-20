package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;



import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersonaDO {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String userEmail ;

	@Persistent
	private String name;
	
	
	@Persistent
	private Date creationDate;
	

	@Persistent
	private TwitterAccountDO twitterAccount;
	
	@Persistent(mappedBy="persona",defaultFetchGroup="true")
	private List<FilterDO> filters;

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




	public void setTwitterAccount(TwitterAccountDO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}


	public TwitterAccountDO getTwitterAccount() {
		return twitterAccount;
	}


	



	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public String getUserEmail() {
		return userEmail;
	}


	


	public void setFilters(List<FilterDO> filters) {
		this.filters = filters;
	}


	public List<FilterDO> getFilters() {
		return filters;
	}

	public void addFilter(FilterDO filter) {
		if (this.filters == null )
			this.filters = new ArrayList<FilterDO>();
		this.filters.add(filter);
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}
	

	


	


}
