package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.Date;



import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersonaDO {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String userEmail ;

	@Persistent
	private String name;
	
	
	@Persistent
	private Date creationDate;
	

	
	@Persistent
	private TwitterAccountDO twitterAccount;
	



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


	public void setId(Long id) {
		this.id = id;
	}


	public Long getId() {
		return id;
	}


	


	


}
