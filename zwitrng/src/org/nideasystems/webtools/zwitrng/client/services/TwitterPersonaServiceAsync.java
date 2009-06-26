package org.nideasystems.webtools.zwitrng.client.services;


import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterPersonaServiceAsync {
	void createPersona(PersonaDTO persona, AsyncCallback<PersonaDTO> callback) throws Exception;
	void deletePersona(String input, AsyncCallback<String> callback) throws Exception;
	void getPersonas(AsyncCallback<PersonaDTOList> callback) throws Exception;
	void getPersonaFilters(String personaKey,
			AsyncCallback<List<FilterCriteriaDTO>> asyncCallback) throws Exception;
	void getTemplates(String name, AsyncCallback<TemplateDTOList> callback) throws Exception;
	void createTemplate(PersonaDTO model, TemplateDTO template,
			AsyncCallback<TemplateDTO> asyncCallback) throws Exception;
	void deleteTemplate(PersonaDTO model, TemplateDTO template,
			AsyncCallback<TemplateDTO> asyncCallback) throws Exception;
	void saveTemplate(PersonaDTO model, TemplateDTO template,
			AsyncCallback<TemplateDTO> asyncCallback);
	

	

}
