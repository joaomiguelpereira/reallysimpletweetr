package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TemplateFragmentDTO implements IModel {

	
	private String name = null;
	private String list = null;
	private List<String> tags = null;
	private long id = -1;
	private Date created;
	private Date modified;
	private boolean maintainOrder;
	private boolean repeatInCampaignAndTemplate;
	
	
	public TemplateFragmentDTO() {
		tags = new ArrayList<String>();
	}
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getTagsAsString() {
		StringBuffer sb = new StringBuffer();
		for (String tag:tags) {
			sb.append(tag);
			sb.append(" ");
			
		}
		return sb.toString();
	}
	public boolean getMaintainOrder() {
		return this.maintainOrder;
	}
	public boolean getRepeatInCampaignAndTemplate() {
		return this.repeatInCampaignAndTemplate;
	}
	public void setMaintainOrder(boolean maintainOrder) {
		this.maintainOrder = maintainOrder;
	}
	public void setRepeatInCampaignAndTemplate(boolean repeatInCampaignAndTemplate) {
		this.repeatInCampaignAndTemplate = repeatInCampaignAndTemplate;
	}
	
	
	
}
