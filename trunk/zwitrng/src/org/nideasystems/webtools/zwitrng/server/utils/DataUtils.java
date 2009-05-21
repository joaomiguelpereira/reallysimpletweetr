package org.nideasystems.webtools.zwitrng.server.utils;

import java.util.Date;

import org.nideasystems.webtools.zwitrng.server.domain.FilterDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import twitter4j.ExtendedUser;

public class DataUtils {

	public static PersonaDTO createPersonaDto(PersonaDO personaDo,
			ExtendedUser twitterUser) {
		
		PersonaDTO returnPersona = new PersonaDTO();
		returnPersona.setCreationDate(personaDo.getCreationDate());
		returnPersona.setName(personaDo.getName());
		returnPersona.setUserEmail(personaDo.getUserEmail());
		
		//WORKE HERE
/*		Long id = personaDo.getKey().getId();
		String kind = personaDo.getKey().getKind();
		
		String name= personaDo.getKey().getName();
		
		
		
		String keyAsAsString = personaDo.getKey().toString();
*/		
		
		//returnPersona.setKey(keyAsAsString);
		
		TwitterAccountDTO twitterAcount = new TwitterAccountDTO();
		twitterAcount.setTwitterDescription(twitterUser.getDescription());
		twitterAcount.setTwitterFollowers(twitterUser.getFollowersCount());
		twitterAcount.setTwitterFollowing(twitterUser.getFriendsCount());
		twitterAcount.setTwitterImageUrl(twitterUser.getProfileImageURL().toExternalForm());
		twitterAcount.setTwitterScreenName(twitterUser.getScreenName());
		twitterAcount.setTwitterUpdates(twitterUser.getStatusesCount());
		twitterAcount.setTwitterUserName(twitterUser.getName());
		
		returnPersona.setTwitterAccount(twitterAcount);
		
		return returnPersona;
		
	}
	
	public static PersonaDO fromDto(PersonaDTO personaDto, String email) {
		PersonaDO returnPersona = new PersonaDO();

		returnPersona.setCreationDate(new Date());
		returnPersona.setName(personaDto.getName());
		returnPersona.setUserEmail(email);

		TwitterAccountDO twitterAccount = new TwitterAccountDO();
		twitterAccount.setTwitterName(personaDto.getTwitterAccount()
				.getTwitterScreenName());
		twitterAccount.setTwitterPass(personaDto.getTwitterAccount()
				.getTwitterPassword());
		returnPersona.setTwitterAccount(twitterAccount);
		// Create the default filter
		FilterDO defaultFilter = new FilterDO();
		defaultFilter.setName("Home");
		returnPersona.addFilter(defaultFilter);
		return returnPersona;

	}

	/**
	 * Take a DOM to DTO
	 * @param filterDo
	 * @return
	 */
	public static FilterCriteriaDTO createFilterCriteriaDto(FilterDO filterDo) {
		
		FilterCriteriaDTO returnObj = new FilterCriteriaDTO();
		returnObj.setName(filterDo.getName());
		returnObj.setKey(filterDo.getKey().toString());
		
		return returnObj;
		
		
	}

}
