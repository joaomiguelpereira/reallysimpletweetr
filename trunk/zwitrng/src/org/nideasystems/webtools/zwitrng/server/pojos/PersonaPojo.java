package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.List;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;

public class PersonaPojo extends AbstractPojo {

	public PersonaDO createPersona(PersonaDTO persona, String email) throws Exception {
		
		return getPersonaDao().createPersona(persona, email);
	}

	public void deletePersona(String persona, String email) throws Exception{
		getPersonaDao().deletePersona(persona, email);
		
	}

	public PersonaDTOList getAssPersonas(String email) throws Exception {
		return  getPersonaDao().findAllPersonas(email);

	}

	public List<FilterCriteriaDTO> getAllFilters(String personaName,
			String email) throws Exception{

		return getPersonaDao().findAllPersonaFilters(personaName, email);
	}

	public void updatePersonaTwitterAccount(PersonaDTO personaDto,
			TwitterAccountDO twitterAccountDo) throws Exception{
		getPersonaDao().updatePersonaTwitterAccount(personaDto,
				twitterAccountDo);
		
	}

}
