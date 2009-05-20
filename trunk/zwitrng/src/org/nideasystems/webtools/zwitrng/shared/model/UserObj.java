package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.List;

public class UserObj {
	
	private String userName = null;
	
	private List<PersonaObj> personas;

	public void setPersonas(List<PersonaObj> personas) {
		this.personas = personas;
	}

	public List<PersonaObj> getPersonas() {
		return personas;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
	
	
	

}
