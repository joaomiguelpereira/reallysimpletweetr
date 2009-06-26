package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TemplateDTO implements IModel {

	private long id;
	private Date created;
	private Date modified;
	
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
		
		if ( created != null ) {
			sb.append("\nCreated at"+created);
		} else {
			sb.append("\nNo creation date");
		}
		if ( modified != null ) {
			sb.append("\nmodified at"+modified);
		} else {
			sb.append("\nNo modified date");
		}

		return sb.toString();
		
	}
	public String getTagsAsText() {
		StringBuffer sb = new StringBuffer();
		if ( tagsAsString == null && tags != null ) {
			for (String tag: tags) {
				sb.append(tag);
				sb.append(" ");
			}
		}
		tagsAsString = sb.toString();
		return tagsAsString;
		
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
}
