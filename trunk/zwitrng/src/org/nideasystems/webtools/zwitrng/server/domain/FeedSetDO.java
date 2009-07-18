package org.nideasystems.webtools.zwitrng.server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FeedSetDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6219678770474223539L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private String name;

	@Persistent
	private PersonaDO persona;

	@Persistent
	private Date created;
	@Persistent
	private Date modified;

	@Persistent
	private Boolean includeLink;
	public Boolean getIncludeLink() {
		return includeLink;
	}

	public void setIncludeLink(Boolean includeLink) {
		this.includeLink = includeLink;
	}

	public Boolean getUseLinkAtBegining() {
		return useLinkAtBegining;
	}

	public void setUseLinkAtBegining(Boolean useLinkAtBegining) {
		this.useLinkAtBegining = useLinkAtBegining;
	}

	public Boolean getIncludeTitle() {
		return includeTitle;
	}

	public void setIncludeTitle(Boolean includeTitle) {
		this.includeTitle = includeTitle;
	}

	@Persistent
	private Boolean useLinkAtBegining;
	@Persistent
	private Boolean includeTitle;

	/*@Persistent
	private List<String> usedTitles;*/

	@Persistent
	private List<String> feedUrls;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public List<String> getFeedUrls() {
		return feedUrls;
	}

	public void setFeedUrls(List<String> feedUrls) {
		this.feedUrls = feedUrls;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void addFeedUrl(String url) {
		if (this.feedUrls == null) {
			this.feedUrls = new ArrayList<String>();
		}
		this.feedUrls.add(url);
	}

	public void setPersona(PersonaDO persona) {
		this.persona = persona;
	}

	public PersonaDO getPersona() {
		return persona;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return created;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getModified() {
		return modified;
	}

/*	public void setUsedTitles(List<String> usedTitles) {
		this.usedTitles = usedTitles;
	}

	public List<String> getUsedTitles() {
		return usedTitles;
	}*/

}
