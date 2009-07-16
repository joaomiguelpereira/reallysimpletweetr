package org.nideasystems.webtools.zwitrng.server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TemplateDO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2090679994164138030L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private Text text;

	
	//Parent Persona
	@Persistent
	private PersonaDO persona;
	
	@Persistent
	private String name;
	
	@Persistent
	private Date created;
	
	@Persistent
	private Date modified;
	
	@Persistent
	private Long usedTimes;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public PersonaDO getPersona() {
		return persona;
	}

	public void setPersona(PersonaDO persona) {
		this.persona = persona;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public void setUsedTimes(Long usedTimes) {
		this.usedTimes = usedTimes;
	}

	public Long getUsedTimes() {
		return usedTimes;
	}
	
	
	
	



}
