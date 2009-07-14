package org.nideasystems.webtools.zwitrng.server.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.ExtendedTwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
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

	private static ThreadLocal<Map<Integer, TwitterServiceAdapter>> instances = new ThreadLocal<Map<Integer, TwitterServiceAdapter>>() {

		@Override
		protected Map<Integer, TwitterServiceAdapter> initialValue() {
			return new HashMap<Integer, TwitterServiceAdapter>();
		}

	};
	private TwitterAccountDTO twitterAccount = null;
	private int rateLimitLimit = 0;
	private int rateLimitRemaining = 0;
	private long rateLimitReset = 0;
	private Twitter twitter = null;

	/**
	 * Constructor
	 * 
	 * @param account
	 */
	private TwitterServiceAdapter(TwitterAccountDTO account) {
		this.twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(account.getOAuthToken(), account
				.getOAuthTokenSecret());
		this.setTwitterAccount(account);
	}

	public static TwitterServiceAdapter get(TwitterAccountDTO account) {
		// The same thread can use different user accounts and thus different
		// TwitterServiceAdapters.
		// The instances map maintains a map of used twitter accounts in the
		// thread
		TwitterServiceAdapter instance = instances.get().get(account.getId());

		if (instance == null) {
			instance = new TwitterServiceAdapter(account);
			instances.get().put(account.getId(), instance);
		}

		return instance;
	}

	// public static TwitterServiceAdapter get() {
	// return new TwitterServiceAdapter();
	// }

	/**
	 * Get a Twitter User information
	 * 
	 * @param twitterName
	 * @param twitterPass
	 * @param authenticationNeeded
	 * @return
	 * @throws Exception
	 */
	public User getExtendedUser(/* TwitterAccountDTO authenticatedTwitterAccount */)
			throws Exception {

		/*
		 * log.info("Calling TWITTER API: " +
		 * authenticatedTwitterAccount.getTwitterScreenName()); Twitter twitter
		 * = new Twitter(); twitter.setOAuthConsumer(CONSUMER_KEY,
		 * CONSUMER_SECRET); twitter.setOAuthAccessToken(
		 * authenticatedTwitterAccount.getOAuthToken(),
		 * authenticatedTwitterAccount.getOAuthTokenSecret());
		 */

		User extendedUser = twitter.verifyCredentials();
		extendedUser = twitter.getUserDetail(extendedUser.getScreenName());
		// Not rate limits in this call
		return extendedUser;

	}

	private Paging createPaging(FilterCriteriaDTO filter) {

		Paging paging = new Paging();
		paging.setCount(filter.getResultsPerPage());
		paging.setPage(filter.getPage());
		if (filter.getMaxId() > 1) {
			paging.setMaxId(filter.getMaxId());
		} else {
			paging.setSinceId(filter.getSinceId());
		}

		return paging;
	}

	/**
	 * Get friends Timeline
	 * 
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<Status> getFriendsTimeLine(FilterCriteriaDTO filter)
			throws Exception {

		List<Status> statusList = null;

		try {
			statusList = twitter.getFriendsTimeline(createPaging(filter));

			// Update rateLimits
			if (statusList.size() > 0) {
				this.rateLimitLimit = statusList.get(0).getRateLimitLimit();
				this.rateLimitRemaining = statusList.get(0)
						.getRateLimitRemaining();
				this.rateLimitReset = statusList.get(0).getRateLimitReset();
			}

		} catch (TwitterException e) {
			log.severe("Error Calling Twitter API: " + e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

		return statusList;

	}

	/**
	 * Get Mentions
	 * 
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<Status> getMentions(FilterCriteriaDTO filter) throws Exception {
		List<Status> statusList = null;

		try {
			statusList = twitter.getMentions(createPaging(filter));
			// Update rateLimits
			if (statusList.size() > 0) {
				this.rateLimitLimit = statusList.get(0).getRateLimitLimit();
				this.rateLimitRemaining = statusList.get(0)
						.getRateLimitRemaining();
				this.rateLimitReset = statusList.get(0).getRateLimitReset();
			}
		} catch (TwitterException e) {
			log.severe("Error Calling Twitter API: " + e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}
		return statusList;
	}

	/**
	 * Perform a search
	 * 
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<Tweet> search(FilterCriteriaDTO filter) throws Exception {

		Query query = new Query();
		query.setRpp(filter.getResultsPerPage());
		query.setPage(filter.getPage());

		// cannot use 1 when doing a search with from:operator
		if (filter.getSinceId() == 1) {
			filter.setSinceId(-1);
		}
		query.setSinceId(filter.getSinceId());
		query.setQuery(filter.getSearchText().trim());

		QueryResult qResult = null;

		try {
			qResult = twitter.search(query);
			// Update rateLimits
			this.rateLimitLimit = qResult.getRateLimitLimit();
			this.rateLimitRemaining = qResult.getRateLimitRemaining();
			this.rateLimitReset = qResult.getRateLimitReset();

		} catch (Exception e) {
			log.severe("Error performing Twitter Search");
			e.printStackTrace();
			throw new Exception(e);
		}
		return qResult.getTweets();
	}

	public Status getStatus(long statusId) throws Exception {
		Status status = null;

		try {
			status = twitter.showStatus(statusId);
			this.rateLimitLimit = status.getRateLimitLimit();
			this.rateLimitRemaining = status.getRateLimitRemaining();
			this.rateLimitReset = status.getRateLimitReset();

		} catch (Exception e) {
			log.severe("Error getting the status from Twitter "
					+ e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

		return status;
	}

	public List<Status> getConversation(long statusId) throws Exception {

		List<Status> retList = new ArrayList<Status>();
		long nextId = statusId;

		try {
			while (nextId > 0) {
				Status status = twitter.showStatus(nextId);

				retList.add(status);

				if (status.getInReplyToStatusId() > 0) {
					nextId = status.getInReplyToStatusId();
				} else {
					nextId = -1;
				}
				this.rateLimitLimit = status.getRateLimitLimit();
				this.rateLimitRemaining = status.getRateLimitRemaining();
				this.rateLimitReset = status.getRateLimitReset();

			}
		} catch (Exception e) {
			log.severe("Error while constructing the conversation "
					+ e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}
		return retList;
	}

	public List<DirectMessage> getReceivedDirectMessages(
			FilterCriteriaDTO filter) throws Exception {

		List<DirectMessage> dms = null;

		try {
			dms = twitter.getDirectMessages(createPaging(filter));

			if (dms.size() > 0) {
				this.rateLimitLimit = dms.get(0).getRateLimitLimit();
				this.rateLimitRemaining = dms.get(0).getRateLimitRemaining();
				this.rateLimitReset = dms.get(0).getRateLimitReset();
			}

		} catch (Exception e) {
			log.severe("Error Getting the received Direct Messages");
			e.printStackTrace();
			throw new Exception(e);
		}

		return dms;
	}

	public List<DirectMessage> getSentDirectMessages(FilterCriteriaDTO filter)
			throws Exception {
		List<DirectMessage> dms = null;

		try {
			dms = twitter.getSentDirectMessages(createPaging(filter));
			if (dms.size() > 0) {
				this.rateLimitLimit = dms.get(0).getRateLimitLimit();
				this.rateLimitRemaining = dms.get(0).getRateLimitRemaining();
				this.rateLimitReset = dms.get(0).getRateLimitReset();
			}
		} catch (Exception e) {
			log.severe("Error Getting the received Direct Messages");
			e.printStackTrace();
			throw new Exception(e);
		}
		return dms;

	}

	/*
	 * public TwitterUpdateDTOList getUpdates( TwitterAccountDTO twitterAccount,
	 * FilterCriteriaDTO filter) throws Exception {
	 * 
	 * Twitter twitter = new Twitter(); twitter.setOAuthConsumer(CONSUMER_KEY,
	 * CONSUMER_SECRET);
	 * twitter.setOAuthAccessToken(twitterAccount.getOAuthToken(),
	 * twitterAccount.getOAuthTokenSecret()); // ExtendedUser extendedUser =
	 * twitter.verifyCredentials();
	 * 
	 * // Create new data structure TwitterUpdateDTOList returnList = new
	 * TwitterUpdateDTOList(); FilterCriteriaDTO newFilter = new
	 * FilterCriteriaDTO();
	 * 
	 * returnList.setFilter(filter); // if twitter is null, something went wrong
	 * assert (twitter != null);
	 * 
	 * // CAll twitter API List<Status> statusList = null;
	 * 
	 * Paging paging = new Paging();
	 * 
	 * paging.setCount(filter.getResultsPerPage());
	 * paging.setPage(filter.getPage()); Status controlStatus = null;
	 * DirectMessage controlDm = null; Tweet controlTweet = null;
	 * 
	 * if (filter.getMaxId() > 1) { paging.setMaxId(filter.getMaxId()); } else {
	 * paging.setSinceId(filter.getSinceId()); }
	 * 
	 * log.fine("maxId=" + paging.getMaxId() + "&page=" + paging.getPage() +
	 * "&sinceid=" + paging.getSinceId() + "&count=" + paging.getCount()); //
	 * paging.setMaxId(filter.getMaxId());
	 * 
	 * if (filter.getUpdatesType() == UpdatesType.FRIENDS) { // CAll twitter API
	 * 
	 * statusList = twitter.getFriendsTimeline(paging);
	 * 
	 * for (Status status : statusList) {
	 * returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto( status,
	 * true)); controlStatus = status;
	 * 
	 * }
	 * 
	 * // updateRateLimits(twitterAccount.getId(), ); } else if
	 * (filter.getUpdatesType() == UpdatesType.MENTIONS) { statusList =
	 * twitter.getMentions(paging); for (Status status : statusList) {
	 * controlStatus = status;
	 * returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto( status,
	 * true));
	 * 
	 * } } else if (filter.getUpdatesType() == UpdatesType.SEARCHES) {
	 * log.fine("Doing search for text: " + filter.getSearchText());
	 * log.fine("Doing search sinceId: " + filter.getSinceId());
	 * log.fine("Doing search maxId: " + filter.getMaxId());
	 * log.fine("Doing search page: " + filter.getPage());
	 * log.fine("Doing search results per page: " + filter.getResultsPerPage());
	 * log.fine("Doing search results for page: " + filter.getPage());
	 * 
	 * Query query = new Query();
	 * 
	 * query.setRpp(filter.getResultsPerPage());
	 * query.setPage(filter.getPage());
	 * 
	 * 
	 * if (filter.getSearchText().startsWith("from:")) {
	 * 
	 * query.setSinceId(-1); } else { query.setSinceId(filter.getSinceId()); }
	 * 
	 * 
	 * // cannot use 1 when doing a search with from:operator if
	 * (filter.getSinceId() == 1) { filter.setSinceId(-1); }
	 * query.setSinceId(filter.getSinceId());
	 * query.setQuery(filter.getSearchText().trim());
	 * 
	 * QueryResult qResult = twitter.search(query);
	 * 
	 * // TODO: remove these from here.
	 * newFilter.setRefreshUrl(qResult.getRefreshUrl());
	 * newFilter.setCompletedIn(qResult.getCompletedIn());
	 * 
	 * newFilter.setMaxId(qResult.getMaxId());
	 * newFilter.setPage(qResult.getPage());
	 * newFilter.setResultsPerPage(qResult.getResultsPerPage());
	 * newFilter.setSinceId(qResult.getSinceId());
	 * 
	 * List<Tweet> tuites = qResult.getTweets(); log.fine("returned: " +
	 * tuites.size() + " tuites");
	 * 
	 * for (Tweet tuite : tuites) { controlTweet = tuite;
	 * 
	 * returnList.addTwitterUpdate(DataUtils .createTwitterUpdateDto(tuite)); }
	 * 
	 * } else if (filter.getUpdatesType() == UpdatesType.SINGLE) { Status status
	 * = twitter.showStatus(filter.getStatusId()); controlStatus = status;
	 * returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto( status,
	 * true));
	 * 
	 * } else if (filter.getUpdatesType() == UpdatesType.CONVERSATION) { // Load
	 * the status
	 * 
	 * long nextId = filter.getStatusId(); while (nextId > 0) { Status status =
	 * twitter.showStatus(nextId); controlStatus = status;
	 * returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto( status,
	 * true));
	 * 
	 * if (status.getInReplyToStatusId() > 0) { nextId =
	 * status.getInReplyToStatusId(); } else { nextId = -1; } } } else if
	 * (filter.getUpdatesType() == UpdatesType.DIRECT_RECEIVED) {
	 * List<DirectMessage> dms = twitter.getDirectMessages(paging);
	 * 
	 * for (DirectMessage dm : dms) { controlDm = dm;
	 * 
	 * returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto( dm, true,
	 * filter.getUpdatesType())); }
	 * 
	 * } else if (filter.getUpdatesType() == UpdatesType.DIRECT_SENT) {
	 * List<DirectMessage> dms = twitter.getSentDirectMessages(paging);
	 * 
	 * for (DirectMessage dm : dms) { controlDm = dm;
	 * returnList.addTwitterUpdate(DataUtils.createTwitterUpdateDto( dm, true,
	 * filter.getUpdatesType())); }
	 * 
	 * }
	 * 
	 * // CAll twitter API // List<Status> statusList =
	 * twitter.getFriendsTimeline();
	 * 
	 * // Is something wrong? assert (statusList != null);
	 * 
	 * return returnList; }
	 */
	public DirectMessage sendDirectMessage(TwitterUpdateDTO update) throws Exception {

		DirectMessage dm = twitter.sendDirectMessage(Long
				.toString(update.getInReplyToUserId()), update
				.getText());

		this.rateLimitLimit = dm.getRateLimitLimit();
		this.rateLimitRemaining = dm.getRateLimitRemaining();
		this.rateLimitReset = dm.getRateLimitReset();

		return dm;
	}
	public Status postUpdate(TwitterUpdateDTO update) throws Exception {

		/*
		 * log.info("Calling TWITTER API: " +
		 * update.getTwitterAccount().getTwitterScreenName()); Twitter twitter =
		 * new Twitter(); twitter.setOAuthConsumer(CONSUMER_KEY,
		 * CONSUMER_SECRET);
		 * twitter.setOAuthAccessToken(update.getTwitterAccount
		 * ().getOAuthToken(),
		 * update.getTwitterAccount().getOAuthTokenSecret());
		 */
		// twitter.updateStatus(update.getText());
		// twitter.setSource("web");
		// ExtendedUser user = null;
		Status latestStatus = null;
		log.fine("Updating status: " + update.getText());

		try {
			//if (update.getInReplyToUserId() > -1) {

				/*// TODO: if you need to create a list of sent messages, return
				// this one (major refactor needed :))
				DirectMessage dm = twitter.sendDirectMessage(Long
						.toString(update.getInReplyToUserId()), update
						.getText());

				this.rateLimitLimit = dm.getRateLimitLimit();
				this.rateLimitRemaining = dm.getRateLimitRemaining();
				this.rateLimitReset = dm.getRateLimitReset();*/

			//} else {
				latestStatus = twitter.updateStatus(update.getText(), update
						.getInReplyToStatusId());
				this.rateLimitLimit = latestStatus.getRateLimitLimit();
				this.rateLimitRemaining = latestStatus.getRateLimitRemaining();
				this.rateLimitReset = latestStatus.getRateLimitReset();

			//}

		} catch (TwitterException e) {
			throw new Exception(e);
		}

		return latestStatus;
	}

	/**
	 * Create a preAuthorized Twitter account. It contains the URL so the user
	 * can login using OAuth
	 * 
	 * @return
	 * @throws Exception
	 */
	public static TwitterAccountDTO createPreAuthorizedTwitterAccount()
			throws Exception {
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


		preAutwitterAccount
				.setOAuthLoginUrl(requestToken.getAuthorizationURL());
		preAutwitterAccount.setOAuthToken(requestToken.getToken());
		preAutwitterAccount.setOAuthTokenSecret(requestToken.getTokenSecret());

		return preAutwitterAccount;
	}

	/**
	 * Authorize a given account
	 * 
	 * @param preAuthorizedTwitterAccount
	 * @param pinCode
	 * @return
	 * @throws Exception
	 */
	public static TwitterAccountDTO authorizeAccount(
			TwitterAccountDTO preAuthorizedTwitterAccount, String pinCode)
			throws Exception {
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

		TwitterAccountDTO authorizedTwitterAccount = new TwitterAccountDTO();
		authorizedTwitterAccount.setIsOAuthenticated(true);
		authorizedTwitterAccount.setOAuthToken(accessToken.getToken());
		authorizedTwitterAccount.setOAuthTokenSecret(accessToken
				.getTokenSecret());
		authorizedTwitterAccount.setId(twitterUser.getId());

		return DataUtils.createAutenticatedTwitterAccountDto(twitterUser,
				authorizedTwitterAccount);

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

	public User getAuthenticatedUser(/* TwitterAccountDO twitterAccount */)
			throws Exception {
		String oAuthToken = twitterAccount.getOAuthToken();
		String oAuthTokenSecret = twitterAccount.getOAuthTokenSecret();
		if (oAuthToken == null || oAuthTokenSecret == null) {
			throw new Exception("The user has no configured twitter account");
		}

		User twitterUser = null;
		try {
			Twitter twitter = new Twitter();
			AccessToken accessToken = new AccessToken(oAuthToken,
					oAuthTokenSecret);
			twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			twitter.setOAuthAccessToken(accessToken);

			twitterUser = twitter.verifyCredentials();

		} catch (Exception e) {
			log.severe("Error: " + e.getLocalizedMessage());
			throw e;
		}
		return twitterUser;
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
	public User getUserInfo(/*
							 * TwitterAccountDTO authenticatedTwitterAccount,
							 */
	String userIdOrScreenName) throws Exception {

		/*
		 * log.info("Calling TWITTER API: " +
		 * authenticatedTwitterAccount.getTwitterScreenName()); Twitter twitter
		 * = new Twitter(); twitter.setOAuthConsumer(CONSUMER_KEY,
		 * CONSUMER_SECRET); twitter.setOAuthAccessToken(
		 * authenticatedTwitterAccount.getOAuthToken(),
		 * authenticatedTwitterAccount.getOAuthTokenSecret());
		 */
		User extUser = null;
		try {
			extUser = twitter.getUserDetail(userIdOrScreenName);
			this.rateLimitLimit = extUser.getRateLimitLimit();
			this.rateLimitRemaining = extUser.getRateLimitRemaining();
			this.rateLimitReset = extUser.getRateLimitReset();

		} catch (TwitterException e) {
			log.severe("error calling twitter" + e.getMessage());
			throw new Exception(e);
		}

		/*
		 * TwitterAccountDTO twitterAccountDto = new TwitterAccountDTO();
		 * 
		 * twitterAccountDto = DataUtils.createTwitterAccountDto(extUser);
		 * 
		 * ExtendedTwitterAccountDTO extendedDto = new
		 * ExtendedTwitterAccountDTO();
		 * 
		 * extendedDto.setImBlocking(twitter.existsBlock(userIdOrScreenName));
		 * extendedDto.setImFollowing(twitter.existsFriendship(
		 * authenticatedTwitterAccount.getId().toString(), userIdOrScreenName));
		 * extendedDto.setMutualFriendShip(twitter.existsFriendship(
		 * userIdOrScreenName, authenticatedTwitterAccount.getId()
		 * .toString())); twitterAccountDto.setExtendedUserAccount(extendedDto);
		 * return twitterAccountDto;
		 */
		return extUser;

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
	public void followUser(/* TwitterAccountDTO account, */boolean follow,
			Integer userId) throws Exception {

		User user = null;
		try {
			if (follow) {
				user = twitter.createFriendship(userId.toString(), true);

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

		this.rateLimitLimit = user.getRateLimitLimit();
		this.rateLimitRemaining = user.getRateLimitRemaining();
		this.rateLimitReset = user.getRateLimitReset();

	}

	public void blockUser(/* TwitterAccountDTO account, */boolean block,
			Integer userId) throws Exception {
		/*
		 * log.info("Calling TWITTER API: " + account.getTwitterScreenName());
		 * Twitter twitter = new Twitter();
		 * twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		 * twitter.setOAuthAccessToken(account.getOAuthToken(), account
		 * .getOAuthTokenSecret());
		 */
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

		this.rateLimitLimit = user.getRateLimitLimit();
		this.rateLimitRemaining = user.getRateLimitRemaining();
		this.rateLimitReset = user.getRateLimitReset();

	}

	public List<User> getUsers(/* TwitterAccountDTO account, */
	TwitterUserFilterDTO currentFilter) throws Exception {
		List<User> ret = null;

		try {
			if (currentFilter.getType().equals(TwitterUserType.FRIENDS)) {
				ret = getFriends(currentFilter);
			} else {
				ret = getFollowers(currentFilter);
			}
		} catch (Exception e) {
			log.severe("Error calling Twitter API: " + e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

		if (ret.size() > 0) {
			this.rateLimitLimit = ret.get(0).getRateLimitLimit();
			this.rateLimitRemaining = ret.get(0).getRateLimitRemaining();
			this.rateLimitReset = ret.get(0).getRateLimitReset();
		}

		return ret;

	}

	private List<User> getFollowers(TwitterUserFilterDTO currentFilter)
			throws Exception {

		Paging paging = new Paging();
		paging.count(currentFilter.getCount());
		paging.setCount(currentFilter.getCount());
		paging.setPage(currentFilter.getPage());

		List<User> users = null;

		users = twitter.getFollowers(currentFilter.getTwitterUserScreenName(),
				paging);

		return users;

	}

	private List<User> getFriends(TwitterUserFilterDTO currentFilter)
			throws Exception {
		Paging paging = new Paging();
		paging.count(currentFilter.getCount());
		paging.setCount(currentFilter.getCount());
		paging.setPage(currentFilter.getPage());

		List<User> users = null;
		users = twitter.getFriends(currentFilter.getTwitterUserScreenName(),
				paging);
		return users;
	}

	public int[] getFollowersIds(/* TwitterAccountDTO account */)
			throws Exception {
		int[] retIds;
		IDs ids;

		try {
			ids = twitter.getFollowersIDs();
			retIds = ids.getIDs();

			this.rateLimitLimit = ids.getRateLimitLimit();
			this.rateLimitRemaining = ids.getRateLimitRemaining();
			this.rateLimitReset = ids.getRateLimitReset();
		} catch (TwitterException e) {
			log.severe("Error calling Twitter API: " + e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

		return retIds;

	}

	public int[] getFollowingIds() throws Exception {
		int[] retIds;
		IDs ids;
		try {
			ids = twitter.getFriendsIDs();
			retIds = ids.getIDs();
			this.rateLimitLimit = ids.getRateLimitLimit();
			this.rateLimitRemaining = ids.getRateLimitRemaining();
			this.rateLimitReset = ids.getRateLimitReset();

		} catch (TwitterException e) {
			log.severe("Error calling Twitter API: " + e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}
		return retIds;
	}

	public int[] getBlockingIds() throws Exception {
		int[] retIds;
		IDs ids;
		try {
			ids = twitter.getBlockingUsersIDs();
			retIds = ids.getIDs();
			this.rateLimitLimit = ids.getRateLimitLimit();
			this.rateLimitRemaining = ids.getRateLimitRemaining();
			this.rateLimitReset = ids.getRateLimitReset();

		} catch (TwitterException e) {
			log.severe("Error calling Twitter API: " + e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

		return retIds;
	}

	public void setTwitterAccount(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountDTO getTwitterAccount() {
		return twitterAccount;
	}

	public void setRateLimitLimit(int rateLimitLimit) {
		this.rateLimitLimit = rateLimitLimit;
	}

	public int getRateLimitLimit() {
		return rateLimitLimit;
	}

	public void setRateLimitRemaining(int rateLimitRemaining) {
		this.rateLimitRemaining = rateLimitRemaining;
	}

	public int getRateLimitRemaining() {
		return rateLimitRemaining;
	}

	public void setRateLimitReset(long rateLimitReset) {
		this.rateLimitReset = rateLimitReset;
	}

	public long getRateLimitReset() {
		return rateLimitReset;
	}

}
