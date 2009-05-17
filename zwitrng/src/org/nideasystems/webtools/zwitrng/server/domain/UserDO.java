package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserDO {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private User user;
	
	@Persistent
	private Date creationDate;
	
	
	@Persistent(mappedBy="parentUser")
	private Collection<PersonaDO> personas = null;
	
	public UserDO(User user, Date date ) {
		this.setCreationDate(date);
		this.setUser(user);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setPersonas(List<PersonaDO> personas) {
		this.personas = personas;
	}

	public Collection<PersonaDO> getPersonas() {
		return personas;
	}
	

}
