package org.nideasystems.webtools.zwitrng.shared.model;


import java.util.Date;



public class TemplateDTO implements IDTO {

	private long id = -1;
	private Date created;
	private Date modified;
	private long usedTimes = 0;
	private String name;
	private String templateText;

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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
