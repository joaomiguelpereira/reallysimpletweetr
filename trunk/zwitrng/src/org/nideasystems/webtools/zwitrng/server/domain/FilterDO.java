package org.nideasystems.webtools.zwitrng.server.domain;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

//Each persona has a n filters

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FilterDO {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String name;
	
	@Persistent
	private PersonaDO persona;

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

	public PersonaDO getPersona() {
		return persona;
	}

	public void setPersona(PersonaDO persona) {
		this.persona = persona;
	}
	
		

}
