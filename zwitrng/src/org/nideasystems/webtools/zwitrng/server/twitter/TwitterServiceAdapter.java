package org.nideasystems.webtools.zwitrng.server.twitter;

import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.ExtendedTwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;

import twitter4j.DirectMessage;
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
	public User getExtendedUser(
			TwitterAccountDTO authenticatedTwitterAccount) throws Exception {

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

		// Get the user twitter account
		/*
		 * Twitter twitter = new Twitter(twitterAccount.getTw itterScreenName(),
		 * twitterAccount.getTwitterPassword());
		 */

		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(twitterAccount.getOAuthToken(),
				twitterAccount.getOAuthTokenSecret());
		// ExtendedUser extendedUser = twitter.verifyCredentials();

		// Create new data structure
		TwitterUpdateDTOList returnList = new TwitterUpdateDTOList();
		FilterCriteriaDTO newFilter = new FilterCriteriaDTO();
		returnList.setFilter(newFilter);
		
		
		
		
		
		returnList.setFilter(filter);
		// if twitter is null, something went wrong
		assert (twitter != null);

		// CAll twitter API
		List<Status> statusList = null;

		Paging paging = new Paging();
		paging.setSinceId(filter.getSinceId());
		paging.setCount(filter.getResultsPerPage());

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
			Query query = new Query();
			query.setRpp(filter.getResultsPerPage());
			query.setSinceId(filter.getSinceId());
			query.setQuery(filter.getSearchText());
			QueryResult qResult = twitter.search(query);
			filter.setRefreshUrl(qResult.getRefreshUrl());
			filter.setCompletedIn(qResult.getCompletedIn());
			
			newFilter.setMaxId(qResult.getMaxId());
			newFilter.setPage(qResult.getPage());
			newFilter.setResultsPerPage(qResult.getResultsPerPage());
			newFilter.setSinceId(qResult.getSinceId());
			/*
			
			//log.fine("qResult.getCompletedIn " + qResult.getCompletedIn());
			//log.fine("qResult.getgetMaxId() " + qResult.getMaxId());
			
			//log.fine("qResult.qResult.getPage() " + qResult.getPage());
			//log.fine("qResult.getQuery() " + qResult.getQuery());
			//log.fine("qResult.getRateLimitLimit() "
					+ qResult.getRateLimitLimit());
			//log.fine("qResult.getRateLimitRemaining() "
					+ qResult.getRateLimitRemaining());
			log.fine("qResult.getResultsPerPage() "
					+ qResult.getResultsPerPage());
			log.fine("qResult.getSinceId() " + qResult.getSinceId());
			log.fine("qResult.getWarning() " + qResult.getWarning());
			*/
			List<Tweet> tuites = qResult.getTweets();

			for (Tweet tuite : tuites) {
				returnList.addTwitterUpdate(DataUtils
						.createTwitterUpdateDto(tuite));
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
			TwitterAccountDTO preAuthorizedTwitterAccount) throws Exception {
		TwitterAccountDTO authorizedTwitterAccount = new TwitterAccountDTO();

		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		AccessToken accessToken = null;
		User twitterUser = null;
		try {

			accessToken = twitter.getOAuthAccessToken(
					preAuthorizedTwitterAccount.getOAuthToken(),
					preAuthorizedTwitterAccount.getOAuthTokenSecret());

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

}
