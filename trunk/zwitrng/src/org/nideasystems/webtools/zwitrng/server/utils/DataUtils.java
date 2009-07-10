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
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;

import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
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
		returnPersona.setCreationDate(personaDo.getCreationDate());
		returnPersona.setName(personaDo.getName());
		returnPersona.setUserEmail(personaDo.getUserEmail());
		returnPersona.setTwitterAccount(authorizedTwitterAccount);
		returnPersona.setId(personaDo.getKey().getId());

		return returnPersona;

	}

	/**
	 * 
	 * @param twitterUser
	 * @return
	 */
	public static TwitterAccountDTO createTwitterAccountDto(User twitterUser) {

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
		twitterAcount.setTwitterStatusText(twitterUser.getStatusText());

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

	public static TwitterUpdateDTO createTwitterUpdateDto(DirectMessage dm,
			boolean copyUserForTwitterAccount, UpdatesType type) {

		TwitterUpdateDTO twitterUpdate = new TwitterUpdateDTO();

		twitterUpdate.setCreatedAt(dm.getCreatedAt());
		twitterUpdate.setId(dm.getId());

		// twitterUpdate.setInReplyToStatusId();

		twitterUpdate.setInReplyToUserId(dm.getRecipientId());
		twitterUpdate.setInReplyToScreenName(dm.getRecipientScreenName());

		twitterUpdate.setRateLimitLimit(dm.getRateLimitLimit());
		twitterUpdate.setRateLimitRemaining(dm.getRateLimitRemaining());
		twitterUpdate.setRateLimitReset(dm.getRateLimitReset());

		twitterUpdate.setSource("");
		twitterUpdate.setText(dm.getText());
		twitterUpdate.setType(type);
		TwitterAccountDTO twitterAccount = new TwitterAccountDTO();

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

		twitterUpdate.setTwitterAccount(twitterAccount);

		return twitterUpdate;

	}

	public static TwitterUpdateDTO createTwitterUpdateDto(Tweet status) {
		TwitterUpdateDTO twitterUpdate = new TwitterUpdateDTO();

		twitterUpdate.setCreatedAt(status.getCreatedAt());
		twitterUpdate.setId(status.getId());

		// twitterUpdate.setInReplyToStatusId();
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
		// twitterUpdate.setInReplyToScreenName(status.getInReplyToScreenName());
		twitterUpdate.setTwitterAccount(twitterAccount);
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
		twitterUpdate.setType(UpdatesType.STATUS);

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
			User authenticatedTwitterUser, TwitterAccountDTO twitterAccount) {
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

		twitterUpdateDto.setInReplyToScreenName(authenticatedTwitterUser
				.getStatusInReplyToScreenName());

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

	public static TemplateDTO templateDtoFromDom(TemplateDO templateDom) {
		TemplateDTO templateDto = new TemplateDTO();
		templateDto.setTemplateText(templateDom.getText());
		templateDto.setCreated(templateDom.getCreated());
		templateDto.setModified(templateDom.getModified());
		
		
		if (templateDom.getTags() != null) {
			
			for (String tag : templateDom.getTags()) {
				templateDto.addTags(tag);
			}
		} else {
			templateDto.addTags("");
		}

		templateDto.setId(templateDom.getKey().getId());
		if ( templateDom.getUsedTimes()!= null ) {
			templateDto.setUsedTimes(templateDom.getUsedTimes());
		} else { 
			templateDto.setUsedTimes(0);
		}
		
		return templateDto;
	}

	public static TemplateFragmentDTO templateFragmentDtoFromDom(
			TemplateFragmentDO fragDo) {
		TemplateFragmentDTO fragDto = new TemplateFragmentDTO();
		fragDto.setCreated(fragDo.getCreated());
		fragDto.setModified(fragDo.getModified());
		fragDto.setName(fragDo.getName());
		fragDto.setList(fragDo.getText());
		if ( fragDo.getTags() != null ) {
			for (String tag: fragDo.getTags()) {
				fragDto.addTag(tag);
			}
		} else {
			fragDto.addTag("");
		}
		
		if (fragDo.getMaintainOrder()!=null) {
			fragDto.setMaintainOrder(fragDo.getMaintainOrder());
		}
		if (fragDo.getRepeatInCampaignAndTemplate()!=null) {
			fragDto.setRepeatInCampaignAndTemplate(fragDo.getRepeatInCampaignAndTemplate());
		}
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
		log.fine("Adding filters");
		if ( dom.getFilter()!= null) {
			for (String filter: dom.getFilter()) {
				dto.addFilter(filter);
				
			}

		}
		
		
		log.fine("Adding ID");
		dto.setId(dom.getKey().getId());
		
		log.fine("Adding modified");
		dto.setModified(dom.getModified());
		log.fine("Adding created");
		dto.setCreated(dom.getCreated());
		return dto;
	}

	public static TwitterUserDTOList twitterUserDtoFromDo(
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

}
