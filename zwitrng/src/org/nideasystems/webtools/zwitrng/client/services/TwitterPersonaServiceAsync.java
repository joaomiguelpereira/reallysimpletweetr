package org.nideasystems.webtools.zwitrng.client.services;


import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.OAuthInfoDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterPersonaServiceAsync {
	void createPersona(PersonaDTO persona, AsyncCallback<PersonaDTO> callback) throws Exception;
	void deletePersona(String input, AsyncCallback<String> callback) throws Exception;
	void getPersonas(AsyncCallback<List<PersonaDTO>> callback) throws Exception;
	void getPersonaFilters(String personaKey,
			AsyncCallback<List<FilterCriteriaDTO>> asyncCallback) throws Exception;
	

	

}
