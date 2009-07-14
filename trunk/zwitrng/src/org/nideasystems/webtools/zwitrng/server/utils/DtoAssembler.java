package org.nideasystems.webtools.zwitrng.server.utils;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class DtoAssembler {

	public static PersonaDTO assemble(PersonaDO personaDo) {
		
		PersonaDTO returnPersona = new PersonaDTO();
		
		
		returnPersona.setCreated(personaDo.getCreated());
		returnPersona.setModified(personaDo.getModified());
		returnPersona.setName(personaDo.getName());
		returnPersona.setUserEmail(personaDo.getUserEmail());
		returnPersona.setTwitterAccount(DtoAssembler.assemble(personaDo.getTwitterAccount()));
		
		
		
		return returnPersona;	}

	public static TwitterAccountDTO assemble(TwitterAccountDO twitterAccount) {
		// TODO Auto-generated method stub
		TwitterAccountDTO dto = new TwitterAccountDTO();
		dto.setOAuthLoginUrl(twitterAccount.getOAuthLoginUrl());
		dto.setOAuthToken(twitterAccount.getOAuthToken());
		dto.setOAuthTokenSecret(twitterAccount.getOAuthTokenSecret());
		dto.setId(twitterAccount.getId());
		
		return dto;
		
		
	}

}
