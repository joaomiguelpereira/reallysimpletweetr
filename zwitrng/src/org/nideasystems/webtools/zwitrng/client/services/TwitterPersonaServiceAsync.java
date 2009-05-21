package org.nideasystems.webtools.zwitrng.client.services;


import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterPersonaServiceAsync {
	void createPersona(PersonaDTO persona, AsyncCallback<PersonaDTO> callback) throws Exception;
	void deletePersona(String input, AsyncCallback<String> callback);
	void getPersonas(AsyncCallback<List<PersonaDTO>> callback);
	void getPersonaFilters(String personaKey,
			AsyncCallback<List<FilterCriteriaDTO>> asyncCallback);
	
}
