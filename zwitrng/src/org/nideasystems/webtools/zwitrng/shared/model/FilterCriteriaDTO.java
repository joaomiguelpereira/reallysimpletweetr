package org.nideasystems.webtools.zwitrng.shared.model;

import org.nideasystems.webtools.zwitrng.shared.UpdatesType;



public class FilterCriteriaDTO implements IModel{
	
	private String name = null;
	private String key = null;
	private long sinceId = 1;
	private UpdatesType updatesType = null;

	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	

	public void setUpdatesType(UpdatesType updatesType) {
		this.updatesType = updatesType;
	}

	public UpdatesType getUpdatesType() {
		return updatesType;
	}

	public void setSinceId(long sinceId) {
		this.sinceId = sinceId;
	}

	public long getSinceId() {
		return sinceId;
	}

	
	

}
