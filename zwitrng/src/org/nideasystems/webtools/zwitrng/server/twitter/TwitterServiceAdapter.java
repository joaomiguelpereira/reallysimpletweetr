package org.nideasystems.webtools.zwitrng.server.twitter;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.ExtendedTwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserType;

import twitter4j.DirectMessage;
import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class TwitterServiceAdapter {

	private static final String CONSUMER_KEY = "Th4VbjuSXf8PAf2eQuiJ2Q";
	private static final String CONSUMER_SECRET = "HZu9vhygeXPShOr7jsvidkiWkTNZmQJRYkQZfYwc";

	private static final Logger log = Logger
			.getLogger(TwitterServiceAdapter.class.getName());

	/**
	 * This class has it's own object factory. Don't create it manually private
	 * TwitterServiceAdapter() {
	 * 
	 * }
	 * 
	 * /** Get a new instance of the adapter
	 * 
	 * @return
	 */
	public static TwitterServiceAdapter get() {
		return new TwitterServiceAdapter();
	}

	/*
	 * public List<TwitterUpdateDTO> searchStatus( TwitterAccountDTO
	 * twitterAccount, FilterCriteriaDTO filter) throws Exception { Twitter
	 * twitter = new Twitter(twitterAccount.getTwitterScreenName(),
	 * twitterAccount.getTwitterPassword()); assert (twitter != null);
	 * List<Status> listStList = twitter.getFriendsTimeline();
	 * 
	 * assert (listStList != null); for (Status stat : listStList) {
	 * 
	 * } QueryResult queryResult = null; Query query = new Query();
	 * query.setQuery(""); try { queryResult = twitter.search(query); } catch
	 * (TwitterException e) { log.warning("Error calling twitter searche API: "
	 * + e.getMessage()); log.severe(e.getLocalizedMessage());
	 * e.printStackTrace(); throw e; } assert (queryResult != null);
	 * 
	 * List<Tweet> tweets = queryResult.getTweets();
	 * 
	 * if (tweets != null && tweets.size() > 0) { for (Tweet tweet : tweets) {
	 * System.out.println("getFromUser " + tweet.getFromUser());
	 * System.out.println("getProfileImageUrl " + tweet.getProfileImageUrl());
	 * System.out.println("getRateLimitLimit " + tweet.getRateLimitLimit());
	 * System.out.println("getRateLimitRemaining " +
	 * tweet.getRateLimitRemaining()); System.out.println("getRateLimitReset " +
	 * tweet.getRateLimitReset()); System.out.println("getSource " +
	 * tweet.getSource()); System.out.println("getText " + tweet.getText());
	 * System.out.println("getToUser " + tweet.getToUser());
	 * System.out.println("getCreatedAt " + tweet.getCreatedAt());
	 * 
	 * } } return new ArrayList<TwitterUpdateDTO>();
	 * 
	 * }
	 */
	/**
	 * Get a Twitter User information
	 * 
	 * @param twitterName
	 * @param twitterPass
	 * @param authenticationNeeded
	 * @return
	 * @throws Exception
	 */
	public User getExtendedUser(TwitterAccountDTO authenticatedTwitterAccount)
			throws Exception {

		log.info("Calling TWITTER API: "
				+ authenticatedTwitterAccount.getTwitterScreenName());
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(
				authenticatedTwitterAccount.getOAuthToken(),
				authenticatedTwitterAccount.getOAuthTokenSecret());
		User extendedUser = twitter.verifyCredentials();

		return extendedUser;

	}

	public TwitterUpdateDTOList getUpdates(TwitterAccountDTO twitterAccount,
			FilterCriteriaDTO filter) throws Exception {

		log.fine("Loading Updates.");
		log.fine("Max Id " + filter.getMaxId());
		log.fine("Since Id " + filter.getSinceId());
		log.fine("Page " + filter.getPage());
		log.fine("RPP " + filter.getResultsPerPage());
		log.fine("SearchText " + filter.getSearchText());
		log.fine("Status ID" + filter.getStatusId());

		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(twitterAccount.getOAuthToken(),
				twitterAccount.getOAuthTokenSecret());
		// ExtendedUser extendedUser = twitter.verifyCredentials();

		// Create new data structure
		TwitterUpdateDTOList returnList = new TwitterUpdateDTOList();
		FilterCriteriaDTO newFilter = new FilterCriteriaDTO();

		returnList.setFilter(filter);
		// if twitter is null, something went wrong
		assert (twitter != null);

		// CAll twitter API
		List<Status> statusList = null;

		Paging paging = new Paging();

		paging.setCount(filter.getResultsPerPage());
		paging.setPage(filter.getPage());

		if (filter.getMaxId() > 1) {
			paging.setMaxId(filter.getMaxId());
		} else {
			paging.setSinceId(filter.getSinceId());
		}

		log.fine("maxId=" + paging.getMaxId() + "&page=" + paging.getPage()
				+ "&sinceid=" + paging.getSinceId() + "&count="
				+ paging.getCount());
		// paging.setMaxId(filter.getMaxId());

		if (filter.getUpdatesType() == UpdatesType.FRIENDS) {
			// CAll twitter API
			statusList = twitter.getFriendsTimeline(paging);
			for (Status status : statusList) {
				returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto(
						status, true));

			}
		} else if (filter.getUpdatesType() == UpdatesType.MENTIONS) {
			statusList = twitter.getMentions(paging);
			for (Status status : statusList) {
				returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto(
						status, true));

			}
		} else if (filter.getUpdatesType() == UpdatesType.SEARCHES) {
			log.fine("Doing search for text: " + filter.getSearchText());
			log.fine("Doing search sinceId: " + filter.getSinceId());
			log.fine("Doing search maxId: " + filter.getMaxId());
			log.fine("Doing search page: " + filter.getPage());
			log.fine("Doing search results per page: "
					+ filter.getResultsPerPage());
			log.fine("Doing search results for page: " + filter.getPage());

			Query query = new Query();

			query.setRpp(filter.getResultsPerPage());
			query.setPage(filter.getPage());

			/*
			 * if (filter.getSearchText().startsWith("from:")) {
			 * 
			 * query.setSinceId(-1); } else {
			 * query.setSinceId(filter.getSinceId()); }
			 */

			// cannot use 1 when doing a search with from:operator
			if (filter.getSinceId() == 1) {
				filter.setSinceId(-1);
			}
			query.setSinceId(filter.getSinceId());
			query.setQuery(filter.getSearchText().trim());

			QueryResult qResult = twitter.search(query);

			// TODO: remove these from here.
			newFilter.setRefreshUrl(qResult.getRefreshUrl());
			newFilter.setCompletedIn(qResult.getCompletedIn());

			newFilter.setMaxId(qResult.getMaxId());
			newFilter.setPage(qResult.getPage());
			newFilter.setResultsPerPage(qResult.getResultsPerPage());
			newFilter.setSinceId(qResult.getSinceId());
			/*
			 * 
			 * //log.fine("qResult.getCompletedIn " + qResult.getCompletedIn());
			 * //log.fine("qResult.getgetMaxId() " + qResult.getMaxId());
			 * 
			 * //log.fine("qResult.qResult.getPage() " + qResult.getPage());
			 * //log.fine("qResult.getQuery() " + qResult.getQuery());
			 * //log.fine("qResult.getRateLimitLimit() " +
			 * qResult.getRateLimitLimit());
			 * //log.fine("qResult.getRateLimitRemaining() " +
			 * qResult.getRateLimitRemaining());
			 * log.fine("qResult.getResultsPerPage() " +
			 * qResult.getResultsPerPage()); log.fine("qResult.getSinceId() " +
			 * qResult.getSinceId()); log.fine("qResult.getWarning() " +
			 * qResult.getWarning());
			 */
			List<Tweet> tuites = qResult.getTweets();
			log.fine("returned: " + tuites.size() + " tuites");
			for (Tweet tuite : tuites) {
				returnList.addTwitterUpdate(DataUtils
						.createTwitterUpdateDto(tuite));
			}

		} else if (filter.getUpdatesType() == UpdatesType.SINGLE) {
			Status status = twitter.showStatus(filter.getStatusId());
			returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto(
					status, true));

		} else if (filter.getUpdatesType() == UpdatesType.CONVERSATION) {
			// Load the status

			long nextId = filter.getStatusId();
			while (nextId > 0) {
				Status status = twitter.showStatus(nextId);
				returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto(
						status, true));

				if (status.getInReplyToStatusId() > 0) {
					nextId = status.getInReplyToStatusId();
				} else {
					nextId = -1;
				}
			}
		} else if (filter.getUpdatesType() == UpdatesType.DIRECT_RECEIVED) {
			List<DirectMessage> dms = twitter.getDirectMessages(paging);

			for (DirectMessage dm : dms) {
				returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto(
						dm, true, filter.getUpdatesType()));
			}

		} else if (filter.getUpdatesType() == UpdatesType.DIRECT_SENT) {
			List<DirectMessage> dms = twitter.getSentDirectMessages(paging);

			for (DirectMessage dm : dms) {
				returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto(
						dm, true, filter.getUpdatesType()));
			}

		}

		// CAll twitter API
		// List<Status> statusList = twitter.getFriendsTimeline();

		// Is something wrong?
		assert (statusList != null);

		return returnList;
	}

	public Status postUpdate(TwitterUpdateDTO update) throws Exception {
		log.info("Calling TWITTER API: "
				+ update.getTwitterAccount().getTwitterScreenName());
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(update.getTwitterAccount().getOAuthToken(),
				update.getTwitterAccount().getOAuthTokenSecret());

		// twitter.updateStatus(update.getText());
		// twitter.setSource("web");
		// ExtendedUser user = null;
		Status latestStatus = null;
		log.fine("Updating status: " + update.getText());

		try {
			if (update.getInReplyToUserId() > -1) {

				// TODO: if you need to create a list of sent messages, return
				// this one (major refactor needed :))
				DirectMessage dm = twitter.sendDirectMessage(Long
						.toString(update.getInReplyToUserId()), update
						.getText());

			} else {
				latestStatus = twitter.updateStatus(update.getText(), update
						.getInReplyToStatusId());
			}

		} catch (TwitterException e) {
			log.severe(e.getLocalizedMessage());
			e.printStackTrace();
			throw new Exception(e);
		}
		return latestStatus;
	}

	public TwitterAccountDTO getPreAuthorizedTwitterAccount() throws Exception {
		TwitterAccountDTO preAutwitterAccount = new TwitterAccountDTO();
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

		// must save this request token for later use
		RequestToken requestToken = null;

		try {
			requestToken = twitter.getOAuthRequestToken();

		} catch (TwitterException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

		// requestToken.getAccessToken();

		preAutwitterAccount
				.setOAuthLoginUrl(requestToken.getAuthorizationURL());
		preAutwitterAccount.setOAuthToken(requestToken.getToken());
		preAutwitterAccount.setOAuthTokenSecret(requestToken.getTokenSecret());

		return preAutwitterAccount;
	}

	public TwitterAccountDTO authorizeAccount(
			TwitterAccountDTO preAuthorizedTwitterAccount, String pinCode)
			throws Exception {
		TwitterAccountDTO authorizedTwitterAccount = new TwitterAccountDTO();

		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		AccessToken accessToken = null;
		User twitterUser = null;
		try {

			accessToken = twitter.getOAuthAccessToken(
					preAuthorizedTwitterAccount.getOAuthToken(),
					preAuthorizedTwitterAccount.getOAuthTokenSecret(), pinCode);

			twitter.setOAuthAccessToken(accessToken);

			twitterUser = twitter.verifyCredentials();

		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}

		if (twitterUser == null) {
			throw new Exception("Could not authenticate");
		}

		authorizedTwitterAccount.setIsOAuthenticated(true);
		authorizedTwitterAccount.setOAuthToken(accessToken.getToken());
		authorizedTwitterAccount.setOAuthTokenSecret(accessToken
				.getTokenSecret());
		return authorizedTwitterAccount;

	}

	public User getAuthenticatedUser(TwitterAccountDTO twitterAccount)
			throws Exception {

		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		AccessToken accessToken = null;
		User twitterUser = null;
		try {

			accessToken = twitter.getOAuthAccessToken(twitterAccount
					.getOAuthToken(), twitterAccount.getOAuthTokenSecret());

			twitter.setOAuthAccessToken(accessToken);

			twitterUser = twitter.verifyCredentials();

			// twitter2.updateStatus("Setting new Status...");
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}

		twitterAccount.setIsOAuthenticated(true);
		twitterAccount.setOAuthToken(accessToken.getToken());
		twitterAccount.setOAuthTokenSecret(accessToken.getTokenSecret());

		return twitterUser;
	}

	public User getAuthenticatedUser(TwitterAccountDO twitterAccount)
			throws Exception {
		String oAuthToken = twitterAccount.getOAuthToken();
		String oAuthTokenSecret = twitterAccount.getOAuthTokenSecret();
		if (oAuthToken == null || oAuthTokenSecret == null) {
			throw new Exception("The user has no configured twitter account");
		}

		try {
			Twitter twitter = new Twitter();
			AccessToken accessToken = new AccessToken(oAuthToken,
					oAuthTokenSecret);
			twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			twitter.setOAuthAccessToken(accessToken);

			User twitterUser = twitter.verifyCredentials();
			return twitterUser;
		} catch (Exception e) {
			log.severe("Error: " + e.getLocalizedMessage());
			throw e;
		}
	}

	/**
	 * Get extended information regarding the user identified by
	 * userIdOrScreenName
	 * 
	 * @param authenticatedTwitterAccount
	 * @param userIdOrScreenName
	 * @return
	 * @throws Exception
	 */
	public TwitterAccountDTO getExtendedUser(
			TwitterAccountDTO authenticatedTwitterAccount,
			String userIdOrScreenName) throws Exception {
		log.info("Calling TWITTER API: "
				+ authenticatedTwitterAccount.getTwitterScreenName());
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(
				authenticatedTwitterAccount.getOAuthToken(),
				authenticatedTwitterAccount.getOAuthTokenSecret());
		User extUser = null;
		try {
			extUser = twitter.getUserDetail(userIdOrScreenName);
		} catch (TwitterException e) {
			log.severe("error calling twitter" + e.getMessage());
			throw new Exception(e);
		}

		TwitterAccountDTO twitterAccountDto = new TwitterAccountDTO();

		twitterAccountDto = DataUtils.createTwitterAccountDto(extUser);

		ExtendedTwitterAccountDTO extendedDto = new ExtendedTwitterAccountDTO();

		extendedDto.setImBlocking(twitter.existsBlock(userIdOrScreenName));
		extendedDto.setImFollowing(twitter.existsFriendship(
				authenticatedTwitterAccount.getId().toString(),
				userIdOrScreenName));
		extendedDto.setMutualFriendShip(twitter.existsFriendship(
				userIdOrScreenName, authenticatedTwitterAccount.getId()
						.toString()));
		twitterAccountDto.setExtendedUserAccount(extendedDto);
		return twitterAccountDto;

	}

	/**
	 * Follow/unfollow user
	 * 
	 * @param account
	 * @param follow
	 * @param userId
	 * @return True if following/false otherwise
	 * @throws Exception
	 */
	public void followUser(TwitterAccountDTO account, boolean follow,
			Integer userId) throws Exception {
		log.info("Calling TWITTER API: " + account.getTwitterScreenName());
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(account.getOAuthToken(), account
				.getOAuthTokenSecret());

		User user = null;
		try {
			if (follow) {
				user = twitter.createFriendship(userId.toString());

			} else {
				user = twitter.destroyFriendship(userId.toString());

			}
		} catch (Exception e) {
			log.severe("Error while following/unfollowing user"
					+ e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

		if (user == null || user.getScreenName().isEmpty()) {
			throw new Exception("Unable to follow/unfollow");
		}

	}

	public void blockUser(TwitterAccountDTO account, boolean block,
			Integer userId) throws Exception {
		log.info("Calling TWITTER API: " + account.getTwitterScreenName());
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(account.getOAuthToken(), account
				.getOAuthTokenSecret());

		User user = null;
		try {
			if (block) {
				user = twitter.createBlock(userId.toString());
			} else {
				user = twitter.destroyBlock(userId.toString());
			}
		} catch (Exception e) {
			log
					.severe("Error while blocking/unblocking user "
							+ e.getMessage());
			throw e;
		}
		if (user == null || user.getScreenName() == null) {
			throw new Exception("unable to block the user");
		}
	}

	private TwitterUserDTOList getUsersFromCache(
			TwitterUserFilterDTO currentFilter) {
		Cache cache = null;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(
					Collections.emptyMap());
		} catch (CacheException e1) {
			// TODO Auto-generated catch block
			log.warning("Could not get a Cache instance " + cache);
			e1.printStackTrace();
			// ignore and just return null. Cache service not available
		}

		// if in the cache, just return the requested elements
		TwitterUserDTOList cachedList = (TwitterUserDTOList) cache
				.get(currentFilter.getTwitterUserScreenName() + "_"
						+ currentFilter.getType().name());

		TwitterUserDTOList retList = null;
		if (cachedList != null) {
			retList = new TwitterUserDTOList();
			int start = currentFilter.getCount() * currentFilter.getPage()
					- currentFilter.getCount();
			int end = start + currentFilter.getCount();

			if (end <= cachedList.getAccounts().size()) {
				for (int i = start; i < end; i++) {
					retList.add(cachedList.getAccounts().get(i));
					// retList.add(twAccount)
				}

			}
		}

		return retList;
	}

	private void addToCache(TwitterUserFilterDTO currentFilter,
			TwitterUserDTOList ret) throws Exception {
		Cache cache = null;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(
					Collections.emptyMap());

		} catch (CacheException e1) {
			// TODO Auto-generated catch block
			log.warning("Could not get a Cache instance " + cache);
			throw e1;
			// ignore and just return null. Cache service not available
		}

		if (cache != null) {

			// Get the actual value
			TwitterUserDTOList currentList = (TwitterUserDTOList) cache
					.get(currentFilter.getTwitterUserScreenName() + "_"
							+ currentFilter.getType().name());

			
			TwitterUserDTOList newList = null;
			
			if (currentList != null) {
				
				log.fine("Adding "+ret.getAccounts().size()+" elements to the lisr");
				
				log.fine("List size: "+currentList.getAccounts().size());
				currentList.getAccounts().addAll(ret.getAccounts());
				log.fine("New List size: "+currentList.getAccounts().size());
			
				cache.put(currentFilter.getTwitterUserScreenName() + "_"
						+ currentFilter.getType().name(),currentList);
			} else {
				cache.put(currentFilter.getTwitterUserScreenName() + "_"
						+ currentFilter.getType().name(), ret);
			}
		}

	}

	public List<User> getUsers(TwitterAccountDTO account,
			TwitterUserFilterDTO currentFilter) throws Exception {
		List<User> ret = null;
		// Check the cache
		log.info("Chechink cache...");
//		//ret = getUsersFromCache(currentFilter);
//		// if the requested page has no data
//		if (ret == null
//				|| ret.getAccounts().size() < (currentFilter.getPage() * currentFilter
//						.getCount())) {
//			log
//					.info("Could not get the users list in cache. Retrieving the users from Twitter API");
//			ret = getFriends(account, currentFilter);
//			//addToCache(currentFilter, ret);
//
//		}
		
		if ( currentFilter.getType().equals(TwitterUserType.FRIENDS)) {
			ret = getFriends(account, currentFilter);
		} else {
			ret = getFollowers(account, currentFilter);
		}
				return ret;

	}

	private List<User> getFollowers(
			TwitterAccountDTO account, TwitterUserFilterDTO currentFilter) throws Exception {
	
		//TwitterUserDTOList ret = new TwitterUserDTOList();
		log.info("Calling TWITTER API: " + account.getTwitterScreenName());
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(account.getOAuthToken(), account
				.getOAuthTokenSecret());

		Paging paging = new Paging();
		paging.count(currentFilter.getCount());
		paging.setCount(currentFilter.getCount());
		paging.setPage(currentFilter.getPage());

		List<User> users = null;
		try {
			users = twitter.getFollowers(currentFilter.getTwitterUserScreenName(), paging);
			
			
		} catch (TwitterException e) {
			e.printStackTrace();
			log.severe("Error: " + e.getMessage());
			throw new Exception(e);
		}
		log.fine("returned " + users.size() + " users from twitter");

/*		if (users != null) {
			for (User user : users) {
				
				//ExtendedTwitterAccountDTO extendedUser = getExtendedTwitterAccountDTO(user, twitter);
				
				TwitterAccountDTO twAccount = DataUtils
						.createTwitterAccountDto(user);
				ret.add(twAccount);
			}
		}*/

		return users;

	}
	
	private ExtendedTwitterAccountDTO getExtendedTwitterAccountDTO(User user, Twitter twitter) {
		ExtendedTwitterAccountDTO ret = new ExtendedTwitterAccountDTO();
		ret.setImBlocking(existsBlocking(user,twitter));
		ret.setImFollowing(existsFollowing(user,twitter));
		ret.setMutualFriendShip(existsMutualFriendShip(user,twitter));
		return ret;
		
	}
	private boolean existsMutualFriendShip(User user, Twitter twitter) {
 
		return false;
	}

	private boolean existsFollowing(User user, Twitter twitter) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean existsBlocking(User user, Twitter twitter) {
		// TODO Auto-generated method stub
		return false;
	}

	private List<User> getFriends(
			TwitterAccountDTO account, TwitterUserFilterDTO currentFilter)
			throws Exception {
		//TwitterUserDTOList ret = new TwitterUserDTOList();
		log.info("Calling TWITTER API: " + account.getTwitterScreenName());
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(account.getOAuthToken(), account
				.getOAuthTokenSecret());

		Paging paging = new Paging();
		paging.count(currentFilter.getCount());
		paging.setCount(currentFilter.getCount());
		paging.setPage(currentFilter.getPage());

		List<User> users = null;
		try {
			users = twitter.getFriends(
					currentFilter.getTwitterUserScreenName(), paging);
		} catch (TwitterException e) {
			e.printStackTrace();
			log.severe("Error: " + e.getMessage());
			throw new Exception(e);
		}
		log.fine("returned " + users.size() + " users from twitter");

		/*if (users != null) {
			for (User user : users) {
				TwitterAccountDTO twAccount = DataUtils
						.createTwitterAccountDto(user);
				ret.add(twAccount);
			}
		}*/

		return users;
	}

	public int[] getFollowersIds(TwitterAccountDTO account) throws Exception {
		log.info("Calling TWITTER API: " + account.getTwitterScreenName());
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(account.getOAuthToken(), account
				.getOAuthTokenSecret());

		Paging paging = new Paging();
		//paging.count(currentFilter.getCount());
		//paging.setCount(currentFilter.getCount());
		//paging.setPage();
		int[] retIds;
		try {
			IDs ids = twitter.getFollowersIDs();
			retIds = ids.getIDs();
		} catch (TwitterException e) {
			log.severe("Error calling Twitter API: "+e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

		return retIds;
		
	}

	public int[] getFollowingIds(TwitterAccountDTO account) throws Exception{
		log.info("Calling TWITTER API: " + account.getTwitterScreenName());
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(account.getOAuthToken(), account
				.getOAuthTokenSecret());

		Paging paging = new Paging();
		//paging.count(currentFilter.getCount());
		//paging.setCount(currentFilter.getCount());
		//paging.setPage();
		int[] retIds;
		try {
			IDs ids = twitter.getFriendsIDs();
			retIds = ids.getIDs();
		} catch (TwitterException e) {
			log.severe("Error calling Twitter API: "+e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

		return retIds;	}

	public int[] getBlockingIds(TwitterAccountDTO account) throws Exception {
		log.info("Calling TWITTER API: " + account.getTwitterScreenName());
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(account.getOAuthToken(), account
				.getOAuthTokenSecret());

		Paging paging = new Paging();
		//paging.count(currentFilter.getCount());
		//paging.setCount(currentFilter.getCount());
		//paging.setPage();
		int[] retIds;
		try {
			IDs ids = twitter.getBlockingUsersIDs();
			retIds = ids.getIDs();
		} catch (TwitterException e) {
			log.severe("Error calling Twitter API: "+e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

		return retIds;	}	

}
