package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TemplateDTO implements IModel {

	private long id = -1;
	private Date created;
	private Date modified;
	private long usedTimes = 0;

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


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Template:");
		sb.append("\nTemplate Text: " + templateText);
		if (tags != null) {
			for (String tag : tags) {
				sb.append("\nTag: " + tag);
			}
		} else {
			sb.append("\nTags are null");
		}

		if (created != null) {
			sb.append("\nCreated at" + created);
		} else {
			sb.append("\nNo creation date");
		}
		if (modified != null) {
			sb.append("\nmodified at" + modified);
		} else {
			sb.append("\nNo modified date");
		}

		return sb.toString();

	}

	public String getTagsAsText() {
		StringBuffer sb = new StringBuffer();
		if (tags != null) {
			for (String tag : tags) {
				sb.append(tag);
				sb.append(" ");
			}

		}
		return sb.toString();

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

	public void setUsedTimes(long usedTimes) {
		this.usedTimes = usedTimes;
	}

	public long getUsedTimes() {
		return usedTimes;
	}
}
