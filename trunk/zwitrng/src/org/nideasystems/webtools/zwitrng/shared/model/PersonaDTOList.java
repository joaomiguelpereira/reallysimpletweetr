package org.nideasystems.webtools.zwitrng.shared.model;


import java.util.ArrayList;
import java.util.List;

public class PersonaDTOList implements IDTO {
	
	private List<PersonaDTO> personaList = new ArrayList<PersonaDTO>();
	public void addPersona(PersonaDTO personaDto) {
		personaList.add(personaDto);
	}
	public List<PersonaDTO> getPersonaList() {
		return this.personaList;
	}

}
