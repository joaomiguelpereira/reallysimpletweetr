package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TemplateFragmentDTO implements IModel {

	
	private String name = null;
	private String list = null;
	private List<String> tags = null;
	private Long id;
	private Date created;
	private Date modified;
	
	
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	
	
	
}
