package org.nideasystems.webtools.zwitrng.server.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.FeedSetDO;
import org.nideasystems.webtools.zwitrng.server.domain.FilterDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateFragmentDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitteUserDTO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterUserDO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;

import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.RateLimitsDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateListDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.User;

public class DataUtils {

	private final static Logger log = Logger.getLogger(DataUtils.class.getName());
	public static PersonaDTO createPersonaDto(PersonaDO personaDo,
			TwitterAccountDTO authorizedTwitterAccount) {

		PersonaDTO returnPersona = new PersonaDTO();
		returnPersona.setCreated(personaDo.getCreated());
		returnPersona.setName(personaDo.getName());
		returnPersona.setUserEmail(personaDo.getUserEmail());
		returnPersona.setTwitterAccount(authorizedTwitterAccount);
		returnPersona.setId(personaDo.getKey().getId());

		return returnPersona;

	}

	/**
	 * 
	 * @param theTwitterUser
	 * @return
	 */
	public static TwitterUserDTO createTwitterAccountDto(User theTwitterUser) {

		assert (theTwitterUser != null);
		TwitterUserDTO twitterUser = new TwitterUserDTO();

		twitterUser.setTwitterDescription(theTwitterUser.getDescription());
		twitterUser.setTwitterFollowers(theTwitterUser.getFollowersCount());
		twitterUser.setTwitterFriends(theTwitterUser.getFriendsCount());
		twitterUser.setTwitterImageUrl(theTwitterUser.getProfileImageURL()
				.toExternalForm());
		twitterUser.setTwitterScreenName(theTwitterUser.getScreenName());
		twitterUser.setTwitterUpdates(theTwitterUser.getStatusesCount());
		twitterUser.setTwitterName(theTwitterUser.getName());
		twitterUser.setTwitterLocation(theTwitterUser.getLocation());
		twitterUser.setTwitterWeb(theTwitterUser.getURL() != null ? theTwitterUser
				.getURL().toExternalForm() : "");
		twitterUser.setTwitterStatusText(theTwitterUser.getStatusText());
		twitterUser.setId(theTwitterUser.getId());

		return twitterUser;
	}

	/*public static PersonaDO personaDofromDto(PersonaDTO personaDto, String email) {
		PersonaDO returnPersona = new PersonaDO();

		returnPersona.setCreationDate(new Date());
		returnPersona.setName(personaDto.getName());
		returnPersona.setUserEmail(email);

		// Copy the TwitterAccountDTO
		TwitterAccountDO twitterAccountDo = twitterAccountDofromDto(personaDto
				.getTwitterAccount());
		returnPersona.setTwitterAccount(twitterAccountDo);
		return returnPersona;

	}*/

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

	public static TwitterUpdateDTO createTwitterUpdateDto(DirectMessage dm,
			boolean copyUserForTwitterAccount, UpdatesType type) {

		TwitterUpdateDTO twitterUpdate = new TwitterUpdateDTO();

		twitterUpdate.setCreatedAt(dm.getCreatedAt());
		twitterUpdate.setId(dm.getId());

		// twitterUpdate.setInReplyToStatusId();

		twitterUpdate.setInReplyToUserId(dm.getRecipientId());
		twitterUpdate.setInReplyToScreenName(dm.getRecipientScreenName());

		twitterUpdate.setSource("");
		twitterUpdate.setText(dm.getText());
		twitterUpdate.setType(type);
		TwitterUserDTO twitterAccount = new TwitterUserDTO();

		if (type.equals(UpdatesType.DIRECT_SENT)) {
			twitterAccount.setTwitterScreenName(dm.getRecipientScreenName());
			twitterAccount.setId(dm.getRecipientId());
			// twitterAccount.setTwitterImageUrl("https://s3.amazonaws.com/twitter_production/profile_images/"+dm.getSenderId()+"/twitter_normal.jpg");

			twitterAccount.setTwitterImageUrl(dm.getRecipient()
					.getProfileImageURL().toExternalForm());

		} else {
			twitterAccount.setTwitterScreenName(dm.getSenderScreenName());
			twitterAccount.setId(dm.getSenderId());
			// twitterAccount.setTwitterImageUrl("https://s3.amazonaws.com/twitter_production/profile_images/"+dm.getSenderId()+"/twitter_normal.jpg");

			twitterAccount.setTwitterImageUrl(dm.getSender()
					.getProfileImageURL().toExternalForm());

		}

		twitterUpdate.setTwitterUser(twitterAccount);
		//twitterUpdate.setTwitterAccount(twitterAccount);

		return twitterUpdate;

	}

	public static TwitterUpdateDTO createTwitterUpdateDto(Tweet status) {
		TwitterUpdateDTO twitterUpdate = new TwitterUpdateDTO();

		twitterUpdate.setCreatedAt(status.getCreatedAt());
		twitterUpdate.setId(status.getId());

		// twitterUpdate.setInReplyToStatusId();
		twitterUpdate.setInReplyToUserId(status.getToUserId());

		
		twitterUpdate.setSource(status.getSource());
		twitterUpdate.setText(status.getText());
		TwitterUserDTO twitterUserDto = new TwitterAccountDTO();
		twitterUserDto.setTwitterScreenName(status.getFromUser());
		twitterUserDto.setId(status.getFromUserId());
		twitterUserDto.setTwitterImageUrl(status.getProfileImageUrl());
		//twitterUpdate.setInReplyToScreenName(status.getInReplyToScreenName());
		
		twitterUpdate.setTwitterUser(twitterUserDto);
		
		twitterUpdate.setType(UpdatesType.TWEET);

		return twitterUpdate;

	}

	public static TwitterUpdateDTO createTwitterUpdateDto(Status status,
			boolean copyUserForTwitterAccount) {
		TwitterUpdateDTO twitterUpdate = new TwitterUpdateDTO();

		twitterUpdate.setCreatedAt(status.getCreatedAt());
		twitterUpdate.setId(status.getId());
		twitterUpdate.setInReplyToStatusId(status.getInReplyToStatusId());
		twitterUpdate.setInReplyToUserId(status.getInReplyToUserId());

		twitterUpdate.setSource(status.getSource());
		twitterUpdate.setText(status.getText());
		twitterUpdate.setInReplyToScreenName(status.getInReplyToScreenName());
		if (copyUserForTwitterAccount) {
			// Create a twitter Account
			TwitterUserDTO twitterAccount = DataUtils
					.createTwitterAccountDto(status.getUser());

			twitterUpdate.setTwitterUser(twitterAccount);

		}
		twitterUpdate.setType(UpdatesType.STATUS);

		return twitterUpdate;

	}

	/*public static PersonaDTO personaDtoFromDo(PersonaDO personaDo) {

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
	}*/

	/*public static TwitterAccountDTO twitterAccountDtoFromDo(
			TwitterAccountDO twitterAccount) {
		TwitterAccountDTO dto = new TwitterAccountDTO();
		dto.setOAuthLoginUrl(twitterAccount.getOAuthLoginUrl());
		dto.setOAuthToken(twitterAccount.getOAuthToken());
		dto.setOAuthTokenSecret(twitterAccount.getOAuthTokenSecret());
		return dto;
	}*/

	/**
	 * Take an authenticated twitter user and merge with the given twitter
	 * account
	 * 
	 * @param twitterUser
	 * @param twitterAccount
	 * @return
	 */
	public static TwitterAccountDTO createAutenticatedTwitterAccountDto(
			User twitterUser, TwitterAccountDTO twitterAccount) {
		
		
		TwitterAccountDTO returnAuthenticatedTwitterAccount = new TwitterAccountDTO();
		
		
		returnAuthenticatedTwitterAccount.setIsOAuthenticated(twitterAccount
				.getIsOAuthenticated());
		// Copy access info
		returnAuthenticatedTwitterAccount.setOAuthToken(twitterAccount
				.getOAuthToken());
		returnAuthenticatedTwitterAccount.setOAuthTokenSecret(twitterAccount
				.getOAuthTokenSecret());

		// Set twitter account details
		returnAuthenticatedTwitterAccount
				.setTwitterDescription(twitterUser
						.getDescription());
		returnAuthenticatedTwitterAccount
				.setTwitterFollowers(twitterUser
						.getFollowersCount());
		returnAuthenticatedTwitterAccount.setTwitterImageUrl(twitterUser
				.getProfileImageURL().toExternalForm());
		returnAuthenticatedTwitterAccount
				.setTwitterScreenName(twitterUser.getScreenName());
		returnAuthenticatedTwitterAccount.setTwitterName(twitterUser
				.getName());
		returnAuthenticatedTwitterAccount.setId(twitterUser.getId());
		returnAuthenticatedTwitterAccount.setTwitterLocation(twitterUser
				.getLocation());

		returnAuthenticatedTwitterAccount
				.setTwitterFollowers(twitterUser
						.getFollowersCount());
		returnAuthenticatedTwitterAccount.setTwitterFriends(twitterUser
				.getFriendsCount());
		returnAuthenticatedTwitterAccount.setTwitterUpdates(twitterUser
				.getStatusesCount());
		if ( twitterUser.getURL()!= null ) {
			returnAuthenticatedTwitterAccount.setTwitterWeb(twitterUser.getURL().toExternalForm());
		} else {
			returnAuthenticatedTwitterAccount.setTwitterWeb("");
		}
		
		
		
		// Set the last status
		TwitterUpdateDTO twitterUpdateDto = new TwitterUpdateDTO();

		twitterUpdateDto.setCreatedAt(twitterUser
				.getStatusCreatedAt());
		twitterUpdateDto.setId(twitterUser.getStatusId());
		twitterUpdateDto.setInReplyToStatusId(twitterUser
				.getStatusInReplyToStatusId());
		twitterUpdateDto.setInReplyToUserId(twitterUser
				.getStatusInReplyToUserId());

		twitterUpdateDto.setInReplyToScreenName(twitterUser
				.getStatusInReplyToScreenName());

		
		
		
		twitterUpdateDto.setSource(twitterUser.getStatusSource());
		twitterUpdateDto.setText(twitterUser.getStatusText());

		
		returnAuthenticatedTwitterAccount.setTwitterUpdateDto(twitterUpdateDto);
		returnAuthenticatedTwitterAccount.setTwitterStatusText(twitterUpdateDto.getText());
		

		return returnAuthenticatedTwitterAccount;
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

	public static TemplateDTO templateDtoFromDom(TemplateDO templateDom) {
		TemplateDTO templateDto = new TemplateDTO();
		templateDto.setTemplateText(templateDom.getText().getValue());
		templateDto.setCreated(templateDom.getCreated());
		templateDto.setModified(templateDom.getModified());
		templateDto.setName(templateDom.getName());
		
		templateDto.setId(templateDom.getKey().getId());
		if ( templateDom.getUsedTimes()!= null ) {
			templateDto.setUsedTimes(templateDom.getUsedTimes());
		} else { 
			templateDto.setUsedTimes(0);
		}
		
		return templateDto;
	}

	public static TemplateListDTO templateFragmentDtoFromDom(
			TemplateFragmentDO fragDo) {
		TemplateListDTO fragDto = new TemplateListDTO();
		fragDto.setCreated(fragDo.getCreated());
		fragDto.setModified(fragDo.getModified());
		fragDto.setName(fragDo.getName());
		fragDto.setList(fragDo.getText());
		
		fragDto.setId(fragDo.getKey().getId());
		
		return fragDto;
	}

	public static String encodeString(String str) {
		
		String encoded = str;
		try {
			encoded = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		
		return encoded;
	}

	public static CampaignDTO campaignDtoFromDo(CampaignDO dom) {
		CampaignDTO  dto = new CampaignDTO();
		dto.setId(dom.getKey().getId());
		dto.setCreated(dom.getCreated());
		dto.setEndDate(dom.getEndDate());
		dto.setFilterByTemplateTags(dom.getFilterByTemplateTags());
		//dto.setFilterByTemplateText(dom.getFilterByTemplateText());
		//dto.setFilterOperator(dom.getFilterOperator());
		if ( dom.getMaxTweets() != null ) {
			dto.setMaxTweets(dom.getMaxTweets());
		}
		
		dto.setModified(dom.getModified());
		dto.setName(dom.getName());
		dto.setStartDate(dom.getStartDate());
		dto.setTimeBetweenTweets(dom.getTimeBetweenTweets());
		dto.setTimeUnit(dom.getTimeUnit());
		dto.setStatus(dom.getStatus());
		dto.setTweetsSent(dom.getTweetsSent());
		dto.setNextRun(dom.getNextRun());
		dto.setLastRun(dom.getLastRun());
		dto.setStartHourOfTheDay(dom.getStartHourOfTheDay()!=null?dom.getStartHourOfTheDay():0);
		dto.setEndHourOfTheDay(dom.getEndHourOfTheDay()!=null?dom.getEndHourOfTheDay():22);

		
		
		return dto;
	}

	public static FeedSetDTO feedSetDtoFromDo(FeedSetDO dom) {
		FeedSetDTO dto = new FeedSetDTO();
		log.fine("Addign name");
		dto.setName(dom.getName());
		
		log.fine("Adding feedsUrls");
		for (String feed: dom.getFeedUrls()) {
			dto.addFeedUrl(feed);
			
		}
		
		
		
		log.fine("Adding ID");
		dto.setId(dom.getKey().getId());
		
		log.fine("Adding modified");
		dto.setModified(dom.getModified());
		log.fine("Adding created");
		dto.setCreated(dom.getCreated());
		return dto;
	}

	@Deprecated
	private static TwitterUserDTOList twitterUserDtoFromDo(
			List<TwitterUserDO> followers) {

		List<TwitterUserDO> list = new ArrayList<TwitterUserDO>();
		for (TwitterUserDO dom: followers ) {
			TwitteUserDTO dto = new TwitteUserDTO();
			dto.setCreated(dom.getCreated());
			dto.setFollowersCount(dom.getGollowersCount());
			dto.setFriendsCount(dom.getFriendsCount());
			dto.setDescription(dom.getDescription());
			dto.setId(dom.getId());
			dto.setLocation(dom.getLocation());
			dto.setModified(dom.getModified());
			dto.setName(dom.getName());
			dto.setProfileImageURL(dom.getProfileImageURL());
			dto.setScreenName(dom.getScreenName());
			dto.setStatusesCount(dom.getStatusesCount());
			dto.setUrl(dom.getUrl());
			
			
			
			
		}
		
		
		
		return null;
	}
	

	/**
	 * From a list of Status, create a list o TwitterUpdates
	 * @param friendsTimeLine
	 * @return
	 */
	public static TwitterUpdateDTOList createTwitterUpdateDtoListFromStatuses(
			List<Status> statuses) {
		
		TwitterUpdateDTOList list = new TwitterUpdateDTOList();
		
		for ( Status status: statuses ) {
			list.addTwitterUpdate(DataUtils.createTwitterUpdateDto(
					status, true));
		}	
		return list;
	}

	/**
	 * From a list of Tweets, create a list of Updates
	 * @param search
	 * @return
	 */
	public static TwitterUpdateDTOList createTwitterUpdateDtoListFromTweets(
			List<Tweet> search) {
		TwitterUpdateDTOList list = new TwitterUpdateDTOList();
		
		for (Tweet tuit: search) {
			list.addTwitterUpdate(DataUtils
					.createTwitterUpdateDto(tuit));
		}
		return list;
		
		
	}

	/**
	 * From a single status, create a list with one element
	 * @param status
	 * @return
	 */
	public static TwitterUpdateDTOList createTwitterUpdateDtoListFromStatus(
			Status status) {
		TwitterUpdateDTOList list = new TwitterUpdateDTOList();
		list.addTwitterUpdate(DataUtils.createTwitterUpdateDto(
					status, true));
		return list;
	}

	/**
	 * From a list of direct messages, create a list of updates
	 * @param receivedDirectMessages
	 * @return
	 */
	public static TwitterUpdateDTOList createTwitterUpdateDtoListFromDM(
			List<DirectMessage> dms, UpdatesType type) {
		
		
		TwitterUpdateDTOList list = new TwitterUpdateDTOList();
		
		for (DirectMessage dm : dms) {
			list.addTwitterUpdate(DataUtils.createTwitterUpdateDto(
					dm, true, type));
		}
		return list;

		
	}

	

}
