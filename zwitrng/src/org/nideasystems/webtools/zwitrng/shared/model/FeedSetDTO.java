package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FeedSetDTO implements IDTO {
	
	private long id = -1;
	
	private String name;
	private List<String> feedUrls;
	private Date modified;
	private Date created;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public void setFeedUrls(List<String> feedUrls) {
		this.feedUrls = feedUrls;
	}
	public List<String> getFeedUrls() {
		return feedUrls;
	}

	public void addFeedUrl(String feedUrl) {
		if ( this.feedUrls == null) {
			this.feedUrls = new ArrayList<String>();
		}
		this.feedUrls.add(feedUrl);
	}


}
