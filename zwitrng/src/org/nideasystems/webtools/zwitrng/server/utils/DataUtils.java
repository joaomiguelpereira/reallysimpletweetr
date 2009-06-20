package org.nideasystems.webtools.zwitrng.server.utils;

import java.util.Date;

import org.nideasystems.webtools.zwitrng.server.domain.FilterDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.User;

public class DataUtils {

	public static PersonaDTO createPersonaDto(PersonaDO personaDo,
			TwitterAccountDTO authorizedTwitterAccount) {

		PersonaDTO returnPersona = new PersonaDTO();
		returnPersona.setCreationDate(personaDo.getCreationDate());
		returnPersona.setName(personaDo.getName());
		returnPersona.setUserEmail(personaDo.getUserEmail());
		returnPersona.setTwitterAccount(authorizedTwitterAccount);

		return returnPersona;

	}

	/**
	 * 
	 * @param twitterUser
	 * @return
	 */
	/*public static TwitterAccountDTO createTwitterAccountDto(User twitterUser) {

		TwitterAccountDTO twitterAcount = new TwitterAccountDTO();

		if (twitterUser != null) {
			twitterAcount.setTwitterDescription(twitterUser.getDescription());
			twitterAcount.setTwitterFollowers(twitterUser.getFollowersCount());

			twitterAcount.setTwitterImageUrl(twitterUser.getProfileImageURL()
					.toExternalForm());
			twitterAcount.setId(twitterUser.getId());

			twitterAcount.setTwitterScreenName(twitterUser.getScreenName());
			twitterAcount.setTwitterName(twitterUser.getName());
			twitterAcount.setTwitterLocation(twitterUser.getLocation());
			twitterAcount
					.setTwitterWeb(twitterUser.getURL() != null ? twitterUser
							.getURL().toExternalForm() : "");

		}

		return twitterAcount;
	}*/

	/**
	 * 
	 * @param twitterUser
	 * @return
	 */
	public static TwitterAccountDTO createTwitterAccountDto(
			User twitterUser) {

		assert (twitterUser != null);
		TwitterAccountDTO twitterAcount = new TwitterAccountDTO();

		twitterAcount.setTwitterDescription(twitterUser.getDescription());
		twitterAcount.setTwitterFollowers(twitterUser.getFollowersCount());
		twitterAcount.setTwitterFriends(twitterUser.getFriendsCount());
		twitterAcount.setTwitterImageUrl(twitterUser.getProfileImageURL()
				.toExternalForm());
		twitterAcount.setTwitterScreenName(twitterUser.getScreenName());
		twitterAcount.setTwitterUpdates(twitterUser.getStatusesCount());
		twitterAcount.setTwitterName(twitterUser.getName());
		twitterAcount.setTwitterLocation(twitterUser.getLocation());
		twitterAcount.setId(twitterUser.getId());
		twitterAcount.setTwitterWeb(twitterUser.getURL() != null ? twitterUser
				.getURL().toExternalForm() : "");

		return twitterAcount;
	}

	public static PersonaDO personaDofromDto(PersonaDTO personaDto, String email) {
		PersonaDO returnPersona = new PersonaDO();

		returnPersona.setCreationDate(new Date());
		returnPersona.setName(personaDto.getName());
		returnPersona.setUserEmail(email);

		// Copy the TwitterAccountDTO
		TwitterAccountDO twitterAccountDo = twitterAccountDofromDto(personaDto
				.getTwitterAccount());
		returnPersona.setTwitterAccount(twitterAccountDo);
		return returnPersona;

	}

	private static TwitterAccountDO twitterAccountDofromDto(
			TwitterAccountDTO twitterAccount) {
		TwitterAccountDO twitterAccountDo = new TwitterAccountDO();

		twitterAccountDo.setOAuthLoginUrl(twitterAccount.getOAuthLoginUrl());
		twitterAccountDo.setOAuthTokenSecret(twitterAccount
				.getOAuthTokenSecret());
		twitterAccountDo.setOAuthToken(twitterAccount.getOAuthToken());

		return twitterAccountDo;

	}

	/**
	 * Take a DOM to DTO
	 * 
	 * @param filterDo
	 * @return
	 */
	public static FilterCriteriaDTO createFilterCriteriaDto(FilterDO filterDo) {

		FilterCriteriaDTO returnObj = new FilterCriteriaDTO();
		returnObj.setSearchText(filterDo.getName());
		returnObj.setKey(filterDo.getKey().toString());

		return returnObj;

	}

	public static TwitterUpdateDTO createTwitterUpdateDto(Tweet status) {
		TwitterUpdateDTO twitterUpdate = new TwitterUpdateDTO();

		twitterUpdate.setCreatedAt(status.getCreatedAt());
		twitterUpdate.setId(status.getId());
		
		//twitterUpdate.setInReplyToStatusId();
		twitterUpdate.setInReplyToUserId(status.getToUserId());

		twitterUpdate.setRateLimitLimit(status.getRateLimitLimit());
		twitterUpdate.setRateLimitRemaining(status.getRateLimitRemaining());
		twitterUpdate.setRateLimitReset(status.getRateLimitReset());
		twitterUpdate.setSource(status.getSource());
		twitterUpdate.setText(status.getText());
		TwitterAccountDTO twitterAccount = new TwitterAccountDTO();
		twitterAccount.setTwitterScreenName(status.getFromUser());
		twitterAccount.setId(status.getFromUserId());
		twitterAccount.setTwitterImageUrl(status.getProfileImageUrl());
		//twitterUpdate.setInReplyToScreenName(status.getInReplyToScreenName());
		twitterUpdate.setTwitterAccount(twitterAccount);


		return twitterUpdate;

	}

	public static TwitterUpdateDTO createTwitterUpdateDto(Status status,
			boolean copyUserForTwitterAccount) {
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
		twitterUpdate.setInReplyToScreenName(status.getInReplyToScreenName());
		if (copyUserForTwitterAccount) {
			// Create a twitter Account
			TwitterAccountDTO twitterAccount = DataUtils
					.createTwitterAccountDto(status.getUser());

			twitterUpdate.setTwitterAccount(twitterAccount);

		}

		return twitterUpdate;

	}

	public static PersonaDTO personaDtoFromDo(PersonaDO personaDo) {

		// Create new PersonaDTO
		PersonaDTO returnPersona = new PersonaDTO();
		returnPersona.setCreationDate(personaDo.getCreationDate());
		returnPersona.setName(personaDo.getName());
		returnPersona.setUserEmail(personaDo.getUserEmail());

		// Create associated Twitter Account
		TwitterAccountDTO twitterAccount = twitterAccountDtoFromDo(personaDo
				.getTwitterAccount());

		returnPersona.setTwitterAccount(twitterAccount);

		return returnPersona;
	}

	public static TwitterAccountDTO twitterAccountDtoFromDo(
			TwitterAccountDO twitterAccount) {
		TwitterAccountDTO dto = new TwitterAccountDTO();
		dto.setOAuthLoginUrl(twitterAccount.getOAuthLoginUrl());
		dto.setOAuthToken(twitterAccount.getOAuthToken());
		dto.setOAuthTokenSecret(twitterAccount.getOAuthTokenSecret());
		return dto;
	}

	/**
	 * Take an authenticated twitter user and merge with the given twitter
	 * account
	 * 
	 * @param authenticatedTwitterUser
	 * @param twitterAccount
	 * @return
	 */
	public static TwitterAccountDTO mergeTwitterAccount(
			User authenticatedTwitterUser,
			TwitterAccountDTO twitterAccount) {
		TwitterAccountDTO authenticatedTwitterAccount = new TwitterAccountDTO();

		authenticatedTwitterAccount.setIsOAuthenticated(twitterAccount
				.getIsOAuthenticated());
		// Copy access info
		authenticatedTwitterAccount.setOAuthToken(twitterAccount
				.getOAuthToken());
		authenticatedTwitterAccount.setOAuthTokenSecret(twitterAccount
				.getOAuthTokenSecret());

		// Set twitter account details
		authenticatedTwitterAccount
				.setTwitterDescription(authenticatedTwitterUser
						.getDescription());
		authenticatedTwitterAccount
				.setTwitterFollowers(authenticatedTwitterUser
						.getFollowersCount());
		authenticatedTwitterAccount.setTwitterImageUrl(authenticatedTwitterUser
				.getProfileImageURL().toExternalForm());
		authenticatedTwitterAccount
				.setTwitterScreenName(authenticatedTwitterUser.getScreenName());
		authenticatedTwitterAccount.setTwitterName(authenticatedTwitterUser
				.getName());
		authenticatedTwitterAccount.setId(authenticatedTwitterUser.getId());
		authenticatedTwitterAccount.setLocation(authenticatedTwitterUser
				.getLocation());

		authenticatedTwitterAccount
				.setTwitterFollowers(authenticatedTwitterUser
						.getFollowersCount());
		authenticatedTwitterAccount.setTwitterFriends(authenticatedTwitterUser
				.getFriendsCount());
		authenticatedTwitterAccount.setTwitterUpdates(authenticatedTwitterUser
				.getStatusesCount());

		// Set the last status
		TwitterUpdateDTO twitterUpdateDto = new TwitterUpdateDTO();

		twitterUpdateDto.setCreatedAt(authenticatedTwitterUser
				.getStatusCreatedAt());
		twitterUpdateDto.setId(authenticatedTwitterUser.getStatusId());
		twitterUpdateDto.setInReplyToStatusId(authenticatedTwitterUser
				.getStatusInReplyToStatusId());
		twitterUpdateDto.setInReplyToUserId(authenticatedTwitterUser
				.getStatusInReplyToUserId());
		
		twitterUpdateDto.setInReplyToScreenName(authenticatedTwitterUser.getStatusInReplyToScreenName());
		
		twitterUpdateDto.setRateLimitLimit(authenticatedTwitterUser
				.getRateLimitLimit());
		twitterUpdateDto.setRateLimitRemaining(authenticatedTwitterUser
				.getRateLimitRemaining());
		twitterUpdateDto.setRateLimitReset(authenticatedTwitterUser
				.getRateLimitReset());
		twitterUpdateDto.setSource(authenticatedTwitterUser.getStatusSource());
		twitterUpdateDto.setText(authenticatedTwitterUser.getStatusText());

		authenticatedTwitterAccount.setTwitterUpdateDto(twitterUpdateDto);

		return authenticatedTwitterAccount;
	}

	/**
	 * 
	 * @param exUser
	 * @param authenticatedAccount
	 * @deprecated
	 */
	public static void mergeExtendedUserAccount(User exUser,
			TwitterAccountDTO authenticatedAccount) {
		// TODO Auto-generated method stub
		authenticatedAccount.setTwitterFollowers(exUser.getFollowersCount());
		authenticatedAccount.setTwitterFriends(exUser.getFriendsCount());
		authenticatedAccount.setTwitterUpdates(exUser.getStatusesCount());
	}

	/**
	 * 
	 * @param fullAuthorizeddAccount
	 * @return
	 */
	public static TwitterAccountDO twitterAccountDoFromDto(
			TwitterAccountDTO fullAuthorizeddAccount) {
		TwitterAccountDO accountDo = new TwitterAccountDO();
		accountDo.setOAuthLoginUrl(fullAuthorizeddAccount.getOAuthLoginUrl());
		accountDo.setOAuthToken(fullAuthorizeddAccount.getOAuthToken());
		accountDo.setOAuthTokenSecret(fullAuthorizeddAccount
				.getOAuthTokenSecret());
		return accountDo;

	}

}
