package org.nideasystems.webtools.zwitrng.shared.model;

public enum TwitterUserType {
	
	FRIENDS("Friends"),
	FOLLOWERS("Followers");
	
	private final String type;

	

	TwitterUserType(String type) {
		this.type = type;
	}



	public String type() {
		return type;
	}
	
	
}
