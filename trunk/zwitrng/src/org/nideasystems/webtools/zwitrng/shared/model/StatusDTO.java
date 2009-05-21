package org.nideasystems.webtools.zwitrng.shared.model;


public class StatusDTO implements IModel{

	private String update = "";
	private String timeUpdate ="";
	public void setUpdate(String update) {
		this.update = update;
	}

	public String getUpdate() {
		return update;
	}

	
	public void setTimeUpdate(String timeUpdate) {
		this.timeUpdate = timeUpdate;
	}

	public String getTimeUpdate() {
		return timeUpdate;
	}
	
}
