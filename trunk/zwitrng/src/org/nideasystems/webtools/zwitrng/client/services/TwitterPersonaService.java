package org.nideasystems.webtools.zwitrng.client.services;




import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("personaService")
public interface TwitterPersonaService extends RemoteService {
	PersonaDTO createPersona(PersonaDTO persona) throws Exception;
	List<PersonaDTO> getPersonas()  throws Exception;
	String deletePersona(String persona)  throws Exception;
	List<FilterCriteriaDTO> getPersonaFilters(String  personaKey)  throws Exception;
	
}
