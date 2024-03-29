package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.Configuration;
import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.RateLimitsDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;

import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;

import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;

import com.google.appengine.api.datastore.Blob;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.User;

public class TwitterPojo extends AbstractPojo {

	private TwitterServiceAdapter twitterService = null;
	private long twitterStartTime = 0;

	private final static Logger log = Logger.getLogger(TwitterPojo.class
			.getName());

	/**
	 * Get Users from Twitter
	 * 
	 * @param thePersonaDto
	 *            - The persona
	 * @param currentFilter
	 *            - The filter
	 * @return
	 * @throws Exception
	 */
	public TwitterUserDTOList getUsers(PersonaDTO thePersonaDto,
			TwitterUserFilterDTO currentFilter) throws Exception {

		// Get the users from the accoun
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(thePersonaDto.getName(),
						thePersonaDto.getUserEmail());

		if (persona == null) {
			throw new Exception("Could not find the persona");
		}

		startTwitterTransaction(thePersonaDto.getTwitterAccount());

		if (persona.getTwitterAccount().getFollowersIdsBlob() == null
				|| persona.getTwitterAccount().getBlockingIds() == null
				|| persona.getTwitterAccount().getFollowingIdsBlob() == null) {

			synchronize(persona, thePersonaDto.getTwitterAccount());
		}

		// Get the users from twitter according to the filter
		List<User> twitterUserList = twitterService.getUsers(/*
															 * thePersonaDto.getTwitterAccount
															 * (),
															 */currentFilter);
		endTwitterTransaction();

		TwitterUserDTOList ret = new TwitterUserDTOList();
		ret.setFilter(currentFilter);

		Set<Integer> followers = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount().getFollowersIdsBlob());
		Set<Integer> followings = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount().getFollowingIdsBlob());
		
		for (User twitterUser : twitterUserList) {

			TwitterUserDTO twitterAccount = DataUtils
					.createTwitterAccountDto(twitterUser);
			// Create extension
			// Set is new
			boolean isNew = !followers.contains(new Integer(twitterAccount.getId()))
					&& !followings.contains(
							new Integer(twitterAccount.getId()));
			twitterAccount.setNew(isNew);

			// ExtendedTwitterAccountDTO extUser = new
			// ExtendedTwitterAccountDTO();

			twitterAccount.setImBlocking(persona.getTwitterAccount()
					.getBlockingIds().contains(
							new Integer(twitterAccount.getId())));
			twitterAccount.setImFollowing(followings.contains(
							new Integer(twitterAccount.getId())));
			twitterAccount.setMutualFriendShip(twitterAccount.isImFollowing()
					&& followers.contains(
							new Integer(twitterAccount.getId())));

			ret.add(twitterAccount);

		}
		updateRateLimist(persona);
		return ret;

	}

	/**
	 * End a trasaction with twitter
	 */
	private void endTwitterTransaction() {

		long endTime = new Date().getTime();
		long elapsedTime = endTime - twitterStartTime;
		log.info("Ending Twitter Transaction for User: "
				+ this.twitterService.getTwitterAccount()
						.getTwitterScreenName() + " in " + elapsedTime);

	}

	/**
	 * Start a transaction with twitter
	 * 
	 * @param twitterAccount
	 */
	private void startTwitterTransaction(TwitterAccountDTO twitterAccount) {
		log.info("Starting Twitter Transaction for User: "
				+ twitterAccount.getTwitterScreenName());

		this.twitterService = TwitterServiceAdapter.get(twitterAccount);
		this.twitterStartTime = new Date().getTime();

	}

	private void synchronize(PersonaDO persona, TwitterAccountDTO account)
			throws Exception {

		// get from twitter the followers

		int[] newFollowersIds;
		try {
			newFollowersIds = twitterService.getFollowersIds();
		} catch (Exception e) {
			log.severe("Error:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		/*if (persona.getTwitterAccount().getFollowersIds() != null) {
			// Clear current array
			persona.getTwitterAccount().setFollowersIds(new Blob(new byte[0]));

		}*/

		Set<Integer> followersIds = new HashSet<Integer>();
		for (int id : newFollowersIds) {
			followersIds.add(id);
		}
		
		persona.getTwitterAccount().setFollowersIdsBlob(new Blob(TwitterAccountDAO.toByteArray(followersIds)));

		// Get the following
		int[] newFollowingIds;
		try {
			newFollowingIds = twitterService.getFollowingIds();
		} catch (Exception e) {
			log.severe("Error:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		
		/*if (persona.getTwitterAccount().getFollowingIds() != null) {
			// Clear current array
			persona.getTwitterAccount().getFollowingIds().clear();

		}*/

		Set<Integer> followingIds = new HashSet<Integer>();
		for (int id : newFollowingIds) {
			followingIds.add(id);
		}
		persona.getTwitterAccount().setFollowingIdsBlob(new Blob(TwitterAccountDAO.toByteArray(followingIds)));
		//persona.getTwitterAccount().setFollowingIds(followingIds);

		// Get the blocinkg ids

		int[] newBlockingIds;
		try {
			newBlockingIds = twitterService.getBlockingIds();
		} catch (Exception e) {
			log.severe("Error:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		if (persona.getTwitterAccount().getBlockingIds() != null) {
			// Clear current array
			persona.getTwitterAccount().getBlockingIds().clear();
		}

		Set<Integer> blockingIds = new HashSet<Integer>();
		for (int id : newBlockingIds) {
			blockingIds.add(new Integer(id));
		}
		persona.getTwitterAccount().setBlockingIds(blockingIds);

	}

	public void synchronize(PersonaDTO thePersonaDto) throws Exception {
		// Get the users from the accoun
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(thePersonaDto.getName(),
						thePersonaDto.getUserEmail());

		startTwitterTransaction(thePersonaDto.getTwitterAccount());
		if (persona == null) {
			throw new Exception("Could not find the persona");
		}

		synchronize(persona, thePersonaDto.getTwitterAccount());
		
		if (persona.getTwitterAccount().getAutoFollowBackIdsQueue() != null) {
			persona.getTwitterAccount().getAutoFollowBackIdsQueue().clear();
		}
		
		if (persona.getTwitterAccount().getIgnoreUsersIds() != null) {
			persona.getTwitterAccount().getIgnoreUsersIds().clear();
		}
		if (persona.getTwitterAccount().getAutoFollowScreenNamesQueue() != null ) {
			persona.getTwitterAccount().getAutoFollowScreenNamesQueue().clear();
		}
		if (persona.getTwitterAccount().getAutoUnFollowBackIdsQueue() != null ) {
			persona.getTwitterAccount().getAutoUnFollowBackIdsQueue().clear();
		}
		

		endTwitterTransaction();
		updateRateLimist(persona);

	}

	public void followUser(PersonaDTO currentPersona, TwitterUserDTO user,
			boolean synch) throws Exception {
		log.fine("Start following user: " + user.getId());
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(currentPersona.getName(),
						currentPersona.getUserEmail());

		startTwitterTransaction(currentPersona.getTwitterAccount());

		Set<Integer> followings = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount().getFollowingIdsBlob());
		
		if (persona == null) {
			throw new Exception("Could not find the persona");
		}
		// Try to call Twitter service
		try {
			twitterService.followUser(true, user.getId());
		} catch (Exception e) {
			log.severe("Could not follow the User" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			endTwitterTransaction();
		}

		if (followings != null) {
						followings.add(
					new Integer(user.getId()));
			
			
		}
		
		persona.getTwitterAccount().setFollowersIdsBlob(new Blob(TwitterAccountDAO.toByteArray(followings)));
		
		Set<Integer> followers = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount().getFollowersIdsBlob());
		// if is to synch
		if (synch) {
			if (followers != null) {
				followers.add(new Integer(user.getId()));
			}
		}
		persona.getTwitterAccount().setFollowersIdsBlob(new Blob(TwitterAccountDAO.toByteArray(followers)));
		updateRateLimist(persona);

	}

	public void unfollowUser(PersonaDTO currentPersona, TwitterUserDTO user)
			throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(currentPersona.getName(),
						currentPersona.getUserEmail());

		if (persona == null) {
			throw new Exception("Could not find the persona");
		}
		startTwitterTransaction(currentPersona.getTwitterAccount());
		// Try to call Twitter service
		try {
			twitterService.followUser(false, user.getId());
		} catch (Exception e) {
			log.severe("Could not follow the User" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			endTwitterTransaction();
		}

		Set<Integer> followings = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount().getFollowingIdsBlob());
		if ( followings != null) {
			followings.remove(user.getId());
		}

		updateRateLimist(persona);
	}

	public void blockUser(PersonaDTO currentPersona, TwitterUserDTO user)
			throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(currentPersona.getName(),
						currentPersona.getUserEmail());
		log.fine("Block  user: " + user.getTwitterScreenName());
		log.fine("Block user: " + user.getId());

		if (persona == null) {
			throw new Exception("Could not find the persona");
		}
		startTwitterTransaction(currentPersona.getTwitterAccount());
		// Try to call Twitter service
		try {
			twitterService.blockUser(true, user.getId());
		} catch (Exception e) {
			log.severe("Could not block the User" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			endTwitterTransaction();
		}

		if (persona.getTwitterAccount().getBlockingIds() != null) {
			persona.getTwitterAccount().getBlockingIds().add(new Integer(user.getId()));
		}

		updateRateLimist(persona);

	}

	public void unblockUser(PersonaDTO currentPersona, TwitterUserDTO user)
			throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(currentPersona.getName(),
						currentPersona.getUserEmail());
		log.fine("unBlock  user: " + user.getTwitterScreenName());
		log.fine("unBlock user: " + user.getId());

		if (persona == null) {
			throw new Exception("Could not find the persona");
		}
		startTwitterTransaction(currentPersona.getTwitterAccount());
		// Try to call Twitter service
		try {
			twitterService.blockUser(false, user.getId());
		} catch (Exception e) {
			log.severe("Could not unblock the User" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			endTwitterTransaction();
		}

		if (persona.getTwitterAccount().getBlockingIds() != null) {
			persona.getTwitterAccount().getBlockingIds().remove(user.getId());
		}

		updateRateLimist(persona);

	}

	public TwitterUpdateDTO postUpdate(PersonaDTO personaDto,
			TwitterUpdateDTO update) throws Exception {

		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(personaDto.getName(),
						personaDto.getUserEmail());

		if (persona == null) {
			throw new Exception("Persona not found");
		}
		startTwitterTransaction(personaDto.getTwitterAccount());
		Status status = null;

		try {
			status = twitterService.postUpdate(update);
		} catch (Exception e) {
			log.severe("Error Posting the update:" + e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			endTwitterTransaction();
		}

		updateRateLimist(persona);
		return DataUtils.createTwitterUpdateDto(status, true);
	}

	/**
	 * Get a lis of updates
	 * 
	 * @param personaDto
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public TwitterUpdateDTOList getUpdates(PersonaDTO personaDto,
			FilterCriteriaDTO filter) throws Exception {

		startTwitterTransaction(personaDto.getTwitterAccount());
		PersonaDO personaDo = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(personaDto.getName(),
						personaDto.getUserEmail());
		if (personaDo == null) {
			throw new Exception("Persona Not found");
		}

		// Start the transaction

		TwitterUpdateDTOList returnList = null;
		// get friends

		try {
			if (filter.getUpdatesType().equals(UpdatesType.FRIENDS)) {
				returnList = DataUtils
						.createTwitterUpdateDtoListFromStatuses(twitterService
								.getFriendsTimeLine(filter));
			} else if (filter.getUpdatesType().equals(UpdatesType.MENTIONS)) {
				returnList = DataUtils
						.createTwitterUpdateDtoListFromStatuses(twitterService
								.getMentions(filter));
			} else if (filter.getUpdatesType().equals(UpdatesType.SEARCHES)) {
				returnList = DataUtils
						.createTwitterUpdateDtoListFromTweets(twitterService
								.search(filter));
			} else if (filter.getUpdatesType().equals(UpdatesType.SEARCHES)) {
				returnList = DataUtils
						.createTwitterUpdateDtoListFromStatus(twitterService
								.getStatus(filter.getStatusId()));
			} else if (filter.getUpdatesType().equals(UpdatesType.CONVERSATION)) {
				returnList = DataUtils
						.createTwitterUpdateDtoListFromStatuses(twitterService
								.getConversation(filter.getStatusId()));
			} else if (filter.getUpdatesType().equals(
					UpdatesType.DIRECT_RECEIVED)) {
				returnList = DataUtils.createTwitterUpdateDtoListFromDM(
						twitterService.getReceivedDirectMessages(filter),
						UpdatesType.DIRECT_RECEIVED);
			} else if (filter.getUpdatesType().equals(UpdatesType.DIRECT_SENT)) {
				returnList = DataUtils.createTwitterUpdateDtoListFromDM(
						twitterService.getSentDirectMessages(filter),
						UpdatesType.DIRECT_SENT);
			}
		} catch (Exception e) {
			log.severe("Error:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			endTwitterTransaction();
		}

		updateRateLimist(personaDo);

		// Update rate limis

		return returnList;
	}

	private void updateRateLimist(PersonaDO personaDo) {

		if (personaDo.getTwitterAccount().getRateLimits() != null) {
			if (twitterService.getRateLimitLimit() >= 0) {
				personaDo.getTwitterAccount().getRateLimits()
						.setRateLimitLimit(twitterService.getRateLimitLimit());
				personaDo.getTwitterAccount().getRateLimits()
						.setRateLimitRemaining(
								twitterService.getRateLimitRemaining());
				personaDo.getTwitterAccount().getRateLimits()
						.setRateLimitReset(twitterService.getRateLimitReset());

			}

		} else {
			RateLimitsDO limitsDo = new RateLimitsDO();
			limitsDo.setRateLimitLimit(twitterService.getRateLimitLimit());
			limitsDo.setRateLimitRemaining(twitterService
					.getRateLimitRemaining());
			limitsDo.setRateLimitReset(twitterService.getRateLimitReset());
			limitsDo.setTwitterAccount(personaDo.getTwitterAccount());
			personaDo.getTwitterAccount().setRateLimits(limitsDo);

		}
	}

	public void updateAuthorizedTwitterAccount(PersonaDTO personaDto,
			TwitterAccountDTO authorizedTwitterAccount) throws Exception {

		// find the persona
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(personaDto.getName(),
						personaDto.getUserEmail());

		if (persona == null) {
			throw new Exception("Persona not found");
		}

		// Try to get the twitter account
		TwitterAccountDO twitterAccount = persona.getTwitterAccount();

		/*
		 * if (twitterAccount == null) { twitterAccount = new
		 * TwitterAccountDO(); //twitterAccount.setPersona(persona);
		 * persona.setTwitterAccount(twitterAccount); }
		 */
		twitterAccount.setOAuthToken(authorizedTwitterAccount.getOAuthToken());
		twitterAccount.setOAuthTokenSecret(authorizedTwitterAccount.getOAuthTokenSecret());
		twitterAccount.setId(authorizedTwitterAccount.getId());

	}

	public TwitterUserDTO getUserInfo(PersonaDTO personaDto,
			String userIdOrScreenName) throws Exception {

		// Try to find the persona
		// find the persona
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(personaDto.getName(),
						personaDto.getUserEmail());

		if (persona == null) {
			throw new Exception("Persona not found");
		}

		Set<Integer> followers = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount().getFollowersIdsBlob());
		Set<Integer> followings = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount().getFollowingIdsBlob());
		
		startTwitterTransaction(personaDto.getTwitterAccount());

		User user = twitterService.getUserInfo(userIdOrScreenName);

		TwitterUserDTO dto = DataUtils.createTwitterAccountDto(user);

		// ExtendedTwitterAccountDTO extendedTwitterAccount = new
		// ExtendedTwitterAccountDTO();

		if (persona.getTwitterAccount().getBlockingIds() != null
				&& persona.getTwitterAccount().getBlockingIds().contains(
						dto.getId())) {
			dto.setImBlocking(true);
		} else {
			dto.setImBlocking(false);
		}

		if (followings != null
				&& followings.contains(
						dto.getId())) {
			dto.setImFollowing(true);
		} else {
			dto.setImFollowing(false);
		}

		if (followers != null
				&& followers.contains(
						dto.getId()) && dto.isImFollowing()) {
			dto.setMutualFriendShip(true);

		} else {
			dto.setMutualFriendShip(false);
		}
		// dto.setExtendedUserAccount(extendedTwitterAccount);

		updateRateLimist(persona);
		return dto;
	}

	public User getAuthenticatedUser(TwitterAccountDTO authorizedTwitterAccount) {

		User authenticatedUser = null;
		startTwitterTransaction(authorizedTwitterAccount);

		try {
			authenticatedUser = twitterService.getAuthenticatedUser();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		endTwitterTransaction();
		return authenticatedUser;

	}

	public int[] getFollowingIds(TwitterAccountDTO twAccount) throws Exception {

		startTwitterTransaction(twAccount);
		int[] ret;

		try {
			ret = twitterService.getFollowingIds();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			endTwitterTransaction();
		}
		return ret;
	}

	public int[] getFollowersIds(TwitterAccountDTO twAccount) throws Exception {
		startTwitterTransaction(twAccount);
		int[] ret;

		try {
			ret = twitterService.getFollowersIds();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			endTwitterTransaction();
		}
		return ret;
	}

	public int[] getBlockingIds(TwitterAccountDTO twAccount) throws Exception {
		startTwitterTransaction(twAccount);
		int[] ret;

		try {
			ret = twitterService.getBlockingIds();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			endTwitterTransaction();
		}
		return ret;
	}

	public TwitterUpdateDTO postUpdate(TwitterUpdateDTO update)
			throws Exception {

		startTwitterTransaction(update.getSendingTwitterAccount());
		Status status = null;

		try {
			status = twitterService.postUpdate(update);
		} catch (Exception e) {
			log.severe("Error Posting the update:" + e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			endTwitterTransaction();
		}

		return DataUtils.createTwitterUpdateDto(status, true);

	}

	public TwitterUpdateDTO sendDM(PersonaDTO currentPersona,
			TwitterUpdateDTO dm) throws Exception {

		startTwitterTransaction(currentPersona.getTwitterAccount());
		DirectMessage dmessage = null;

		try {
			dmessage = twitterService.sendDirectMessage(dm);
		} catch (Exception e) {
			log.severe("Error Posting the update:" + e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			endTwitterTransaction();
		}

		return DataUtils.createTwitterUpdateDto(dmessage, true,
				UpdatesType.DIRECT_SENT);
	}

	public void updateFollowBackUsersIdQueue(TwitterAccountDO twitterAccount,
			TwitterAccountDTO authorizedTwitterAccount) throws Exception {
		int followers[] = businessHelper.getTwitterPojo().getFollowersIds(
				authorizedTwitterAccount);
		
		int following[] = businessHelper.getTwitterPojo().getFollowingIds(authorizedTwitterAccount);
		
		//Get the ignore list
		Set<Integer> ignoreList = twitterAccount.getIgnoreUsersIds();
		if ( ignoreList == null ) {
			ignoreList = new HashSet<Integer>();
		}
		
		List<Integer> followBackUserIdList = new ArrayList<Integer>();
		//Create a reverse list of myFriends
		List<Integer> followingList = new ArrayList<Integer>(following.length);
		for ( int i=following.length-1; i>=0; i--) {
			followingList.add(following[i]);
		}
		
		
		//For each of my followers, check if I'm following
		for (int i=followers.length-1; i>=0; i-- ) {
			//Check if is in the ignore list
			
			//if the followers is in the ignore list, don't bother
			if ( ignoreList.contains(followers[i])) {
				continue;
			}
			//If I'm not following, then add to the followback list
			if ( !followingList.contains(followers[i])) {
				followBackUserIdList.add(followers[i]);
			}
		}
		
		log.fine("FollowBackSize: "+followBackUserIdList.size());
		twitterAccount.setAutoFollowBackIdsQueue(followBackUserIdList);
		
		

	}

	public void updateUnFollowBackUsersIdQueue(TwitterAccountDO twitterAccount,
			TwitterAccountDTO authorizedTwitterAccount) throws Exception{
		
		int followers[] = businessHelper.getTwitterPojo().getFollowersIds(
				authorizedTwitterAccount);
		
		int following[] = businessHelper.getTwitterPojo().getFollowingIds(authorizedTwitterAccount);
		
		log.fine("Following Size: "+following.length);
		log.fine("Followers Size: "+followers.length);
		Set<Integer> unfollowedIds = twitterAccount.getAutoUnfollowedIds()!=null?twitterAccount.getAutoUnfollowedIds():new HashSet<Integer>();
		List<Integer> unfollowBackUserIdList = new ArrayList<Integer>();
		//Create a reverse list of myFriends
		List<Integer> followersList = new ArrayList<Integer>(followers.length);
		
		for ( int i=followers.length-1; i>=0; i--) {
			followersList.add(followers[i]);
		}
		
		
		//For each of my friends, check if he's following
		for (int i=following.length-1; i>=0; i-- ) {
			//Check if is in the ignore list
			
			//if is in the unfollowed list, then ignore
			if ( unfollowedIds.contains(following[i])) {
				continue;
			}
			if (!followersList.contains(following[i])) {
				unfollowBackUserIdList.add(following[i]);
			}
			
		}
		
		log.fine("FollowBackSize: "+unfollowBackUserIdList.size());
		twitterAccount.setAutoUnFollowBackIdsQueue(unfollowBackUserIdList);
		
	}

	public void updateFollowUsersScreenNamesQueue(TwitterAccountDO twitterAccount,
			AutoFollowRuleDO autofollowrule,
			TwitterAccountDTO authorizedTwitterAccount) throws Exception {
		//Get the list of current queu
		Set<String> followedUsers = twitterAccount.getAutoFollowedScreenNames();
		if ( followedUsers == null ){
			followedUsers = new HashSet<String>();
		}
		List<String> queue = twitterAccount.getAutoFollowScreenNamesQueue();
		if (queue==null) {
			queue = new ArrayList<String>();
		}//Execute the search
		
		if (queue.size()>150) {
			queue.clear();
		}
		TwitterAccountDTO authorizedAccount = TwitterAccountDAO.createAuthorizedAccountDto(twitterAccount);
		
		startTwitterTransaction( authorizedAccount );
		
		List<Tweet> tweets = twitterService.search(autofollowrule.getSearchTerm(),Configuration.MAX_TWEETS_ON_SEARCH_FOLLOW);
		endTwitterTransaction();
		//Get last retweet Timestamo
		Long lastRtTimeStamp = twitterAccount.getLastRTTimeStamp();
		if (lastRtTimeStamp == null) {
			lastRtTimeStamp = new Date().getTime();
		}
		int minMinutesBetweenRts = 5;

		for ( Tweet tweet: tweets ) {
			log.fine("Cheching user to follow: "+tweet.getFromUser());
			//Add the user to the queue
			//Check first if exists
			if ( !queue.contains(tweet.getFromUser()) && !followedUsers.contains(tweet.getFromUser())) {
				
				
				//Ignore the ones that com from an excluded source
				boolean add = true;
				
				if ( autofollowrule.getExcludeWordsInClients()!= null) {
					//Check if last status is from a exlcuded source
					for ( String name: autofollowrule.getExcludeWordsInClients() ) {
						log.fine("Cheching source: "+tweet.getSource());
						if (tweet.getSource().toLowerCase().contains(name.toLowerCase().trim())) {
							add = false;
							break;
										
						}
					}
						
				}
				
				if (autofollowrule.getExcludedWordsInNames() != null) {
					//Check if last status is from a exlcuded source
					for ( String str: autofollowrule.getExcludedWordsInNames() ) {
						log.fine("Cheching Name: "+tweet.getFromUser());
						if (tweet.getFromUser().toLowerCase().contains(str.toLowerCase())) {
							add = false;
							break;
										
						}
					}
						
				}
				
				if ( add ) {
					//Check if is to send a rt as a teaser
					
					if (autofollowrule.getSendTeaserTweet()!=null && autofollowrule.getSendTeaserTweet() ) {
						long now = new Date().getTime();
						if ( now-lastRtTimeStamp > minMinutesBetweenRts*1000*60) {
							
							lastRtTimeStamp = now;
							TwitterUpdateDTO update = new TwitterUpdateDTO();
							update.setInReplyToScreenName(tweet.getFromUser());
							update.setInReplyToStatusId(tweet.getId());
							update.setText("RT @"+tweet.getFromUser()+":"+tweet.getText());
							log.fine("Sending: "+update.getText());
							
							try {
								twitterService.postUpdate(update);
							} catch (Exception e) {
								log.severe("Could not do the RT "+e.getMessage());
								e.printStackTrace();
							}	
						}
						
					}
					log.fine("Adding user to follow to the queue: "+tweet.getFromUser());
					queue.add(tweet.getFromUser());	
				} else {
					log.fine("Ignoring user: "+tweet.getFromUser());
				}
				
			}
		}
		twitterAccount.setLastRTTimeStamp(lastRtTimeStamp);
		twitterAccount.setAutoFollowScreenNamesQueue(queue);
	}
	
	
	

}
