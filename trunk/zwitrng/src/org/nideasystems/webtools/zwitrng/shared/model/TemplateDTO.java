package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.List;

public class TemplateDTO implements IModel {

	private long id;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTemplateText() {
		return templateText;
	}
	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}
	public List<String> getTags() {
		return tags;
	}
	public void addTags(String tag) {
		this.tags.add(tag);
	}
	private String templateText;
	private List<String> tags = new ArrayList<String>();
	private String tagsAsString = null;
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Template:");
		sb.append("\nTemplate Text: "+templateText);
		if (tags != null) {
			for (String tag: tags) {
				sb.append("\nTag: "+tag);
			}
		} else {
			sb.append("\nTags are null");
		}
		return sb.toString();
		
	}
	public String getTagsAsText() {
		StringBuffer sb = new StringBuffer();
		if ( tagsAsString == null && tags != null ) {
			for (String tag: tags) {
				sb.append("\nTag: "+tag);
			}
		}
		tagsAsString = sb.toString();
		return tagsAsString;
		
	}
}
