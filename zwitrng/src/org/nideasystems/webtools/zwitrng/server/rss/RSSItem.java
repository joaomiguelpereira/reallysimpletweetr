package org.nideasystems.webtools.zwitrng.server.rss;

import java.io.Serializable;

public class RSSItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8840846357058535611L;
	private String title;
	private String pubDate;
	private String link;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

}
