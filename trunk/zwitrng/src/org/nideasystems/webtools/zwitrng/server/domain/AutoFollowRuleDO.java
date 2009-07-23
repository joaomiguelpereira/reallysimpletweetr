package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;


import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class AutoFollowRuleDO {


	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private boolean enabled;
	@Persistent
	private int maxRatio;
	@Persistent
	private int minUpdates;
	@Persistent
	private boolean sendDirectMessage;
	@Persistent
	private java.util.List<String> excludedWordsInNames;
	@Persistent
	private AutoFollowTriggerType triggerType;
	@Persistent
	private PersonaDO persona;
	@Persistent
	private Date created;
	@Persistent
	private Date modified;
	@Persistent
	private String templateName;

	@Persistent
	private Boolean sendDirectMessageOnIgnore;
	@Persistent
	private String ignoreTemplate;
	
	public PersonaDO getPersona() {
		return persona;
	}
	public void setPersona(PersonaDO persona) {
		this.persona = persona;
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
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public int getMaxRatio() {
		return maxRatio;
	}
	public void setMaxRatio(int maxRatio) {
		this.maxRatio = maxRatio;
	}
	public int getMinUpdates() {
		return minUpdates;
	}
	public void setMinUpdates(int minUpdates) {
		this.minUpdates = minUpdates;
	}
	public boolean isSendDirectMessage() {
		return sendDirectMessage;
	}
	public void setSendDirectMessage(boolean sendDirectMessage) {
		this.sendDirectMessage = sendDirectMessage;
	}
	public java.util.List<String> getExcludedWordsInNames() {
		return excludedWordsInNames;
	}
	public void setExcludedWordsInNames(java.util.List<String> excludedWordsInNames) {
		this.excludedWordsInNames = excludedWordsInNames;
	}
	public AutoFollowTriggerType getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(AutoFollowTriggerType triggerType) {
		this.triggerType = triggerType;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
		
	}
	public String getTemplateName() {
		return templateName;
	}
	
	public void setIgnoreTemplate(String ignoreTemplate) {
		this.ignoreTemplate = ignoreTemplate;
	}
	public String getIgnoreTemplate() {
		return ignoreTemplate;
	}
	public void setSendDirectMessageOnIgnore(Boolean sendDirectMessageOnIgnore) {
		this.sendDirectMessageOnIgnore = sendDirectMessageOnIgnore;
	}
	public Boolean getSendDirectMessageOnIgnore() {
		return sendDirectMessageOnIgnore;
	}
	
}
