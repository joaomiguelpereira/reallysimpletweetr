package org.nideasystems.webtools.zwitrng.server.utils;

import java.util.Date;

import org.nideasystems.webtools.zwitrng.server.domain.FilterDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import twitter4j.ExtendedUser;
import twitter4j.Status;
import twitter4j.User;

public class DataUtils {

	public static PersonaDTO createPersonaDto(PersonaDO personaDo,
			ExtendedUser twitterUser) {
		
		PersonaDTO returnPersona = new PersonaDTO();
		returnPersona.setCreationDate(personaDo.getCreationDate());
		returnPersona.setName(personaDo.getName());
		returnPersona.setUserEmail(personaDo.getUserEmail());
		
		
		TwitterAccountDTO twitterAcount = createTwitterAccountDto(twitterUser);
		
		/*twitterAcount.setTwitterDescription(twitterUser.getDescription());
		twitterAcount.setTwitterFollowers(twitterUser.getFollowersCount());
		twitterAcount.setTwitterFollowing(twitterUser.getFriendsCount());
		twitterAcount.setTwitterImageUrl(twitterUser.getProfileImageURL().toExternalForm());
		twitterAcount.setTwitterScreenName(twitterUser.getScreenName());
		twitterAcount.setTwitterUpdates(twitterUser.getStatusesCount());
		twitterAcount.setTwitterUserName(twitterUser.getName());*/
		

		//Handle correcly the pass
		twitterAcount.setTwitterPassword(personaDo.getTwitterAccount().getTwitterPass());

		
		returnPersona.setTwitterAccount(twitterAcount);
		
		return returnPersona;
		
	}

	/**
	 * 
	 * @param twitterUser
	 * @return
	 */
	public static TwitterAccountDTO createTwitterAccountDto(User twitterUser) {
		
		TwitterAccountDTO twitterAcount = new TwitterAccountDTO();
		twitterAcount.setTwitterDescription(twitterUser.getDescription());
		
		twitterAcount.setTwitterFollowers(twitterUser.getFollowersCount());
		//twitterAcount.setTwitterFollowing(twitterUser.getFriendsCount());
		twitterAcount.setTwitterImageUrl(twitterUser.getProfileImageURL().toExternalForm());
		twitterAcount.setTwitterScreenName(twitterUser.getScreenName());
		//twitterAcount.setTwitterUpdates(twitterUser.getStatusesCount());
		twitterAcount.setTwitterUserName(twitterUser.getName());
		
		return twitterAcount;
	}

	/**
	 * 
	 * @param twitterUser
	 * @return
	 */
	public static TwitterAccountDTO createTwitterAccountDto(ExtendedUser twitterUser) {
		
		assert(twitterUser!=null);
		TwitterAccountDTO twitterAcount = new TwitterAccountDTO();
		
		twitterAcount.setTwitterDescription(twitterUser.getDescription());
		twitterAcount.setTwitterFollowers(twitterUser.getFollowersCount());
		twitterAcount.setTwitterFollowing(twitterUser.getFriendsCount());
		twitterAcount.setTwitterImageUrl(twitterUser.getProfileImageURL().toExternalForm());
		twitterAcount.setTwitterScreenName(twitterUser.getScreenName());
		twitterAcount.setTwitterUpdates(twitterUser.getStatusesCount());
		twitterAcount.setTwitterUserName(twitterUser.getName());
		
		return twitterAcount;
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

	public static TwitterUpdateDTO createTwitterUpdateDto(Status status) {
		TwitterUpdateDTO twitterUpdate = new TwitterUpdateDTO();
		
		
		twitterUpdate.setCreatedAt(status.getCreatedAt());
		twitterUpdate.setId(status.getId());
		twitterUpdate.setInReplyToStatusId(status.getInReplyToStatusId());
		twitterUpdate.setInReplyToUserId(status.getInReplyToUserId());
		
		twitterUpdate.setRateLimitLimit(status.getRateLimitLimit());
		twitterUpdate.setRateLimitRemaining(status.getRateLimitRemaining());
		twitterUpdate.setRateLimitReset(status.getRateLimitReset());
		twitterUpdate.setSource(status.getSource());
		twitterUpdate.setText(status.getText());
		
		//Create a twitter Account
		TwitterAccountDTO twitterAccount =  DataUtils.createTwitterAccountDto(status.getUser());
		
		twitterUpdate.setTwitterAccount(twitterAccount);
		
		
		return twitterUpdate;
		
	}

}
