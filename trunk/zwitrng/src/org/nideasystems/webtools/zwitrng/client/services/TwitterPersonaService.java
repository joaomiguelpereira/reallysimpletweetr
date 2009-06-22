package org.nideasystems.webtools.zwitrng.client.services;




import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("personaService")
public interface TwitterPersonaService extends RemoteService {
	PersonaDTO createPersona(PersonaDTO persona) throws Exception;
	PersonaDTOList getPersonas()  throws Exception;
	String deletePersona(String persona)  throws Exception;
	List<FilterCriteriaDTO> getPersonaFilters(String  personaKey)  throws Exception;
	TemplateDTOList getTemplates(String name) throws Exception;
	TemplateDTO createTemplate(PersonaDTO model, TemplateDTO template) throws Exception;
	
}
