package org.nideasystems.webtools.zwitrng.shared.model;
import java.util.Date;

public class TemplateListDTO implements IDTO {

	
	private String name = null;
	private String list = null;
	private long id = -1;
	private Date created;
	private Date modified;
	
	
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
		
}
