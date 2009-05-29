package org.nideasystems.webtools.zwitrng.server.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import twitter4j.ExtendedUser;
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
	 * This class has it's own object factory. Don't create it manually
	 */
	private TwitterServiceAdapter() {

	}

	/**
	 * Get a new instance of the adapter
	 * 
	 * @return
	 */
	public static TwitterServiceAdapter get() {
		return new TwitterServiceAdapter();
	}

	public List<TwitterUpdateDTO> searchStatus(
			TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter)
			throws Exception {
		Twitter twitter = new Twitter(twitterAccount.getTwitterScreenName(),
				twitterAccount.getTwitterPassword());
		assert (twitter != null);
		List<Status> listStList = twitter.getFriendsTimeline();

		assert (listStList != null);
		for (Status stat : listStList) {
			System.out.println("getInReplyToStatusId "
					+ stat.getInReplyToStatusId());
			System.out.println("getId() " + stat.getId());
			System.out.println("getInReplyToUserId() "
					+ stat.getInReplyToUserId());
			System.out.println("getRateLimitLimit() "
					+ stat.getRateLimitLimit());
			System.out.println("getRateLimitRemaining() "
					+ stat.getRateLimitRemaining());
			System.out.println("getRateLimitReset() "
					+ stat.getRateLimitReset());
			System.out.println("getSource() " + stat.getSource());
			System.out.println("stat.getText() " + stat.getText());
			System.out.println("getCreatedAt() " + stat.getCreatedAt());
			System.out.println("getUser().getScreenName() "
					+ stat.getUser().getScreenName());
		}
		QueryResult queryResult = null;
		Query query = new Query();
		query.setQuery("");
		try {
			queryResult = twitter.search(query);
		} catch (TwitterException e) {
			log.warning("Error calling twitter searche API: " + e.getMessage());
			log.severe(e.getLocalizedMessage());
			e.printStackTrace();
			throw e;
		}
		assert (queryResult != null);

		List<Tweet> tweets = queryResult.getTweets();

		if (tweets != null && tweets.size() > 0) {
			for (Tweet tweet : tweets) {
				System.out.println("getFromUser " + tweet.getFromUser());
				System.out.println("getProfileImageUrl "
						+ tweet.getProfileImageUrl());
				System.out.println("getRateLimitLimit "
						+ tweet.getRateLimitLimit());
				System.out.println("getRateLimitRemaining "
						+ tweet.getRateLimitRemaining());
				System.out.println("getRateLimitReset "
						+ tweet.getRateLimitReset());
				System.out.println("getSource " + tweet.getSource());
				System.out.println("getText " + tweet.getText());
				System.out.println("getToUser " + tweet.getToUser());
				System.out.println("getCreatedAt " + tweet.getCreatedAt());

			}
		}
		return new ArrayList<TwitterUpdateDTO>();

	}

	/**
	 * Get a Twitter User information
	 * 
	 * @param twitterName
	 * @param twitterPass
	 * @param authenticationNeeded
	 * @return
	 * @throws Exception
	 */
	public ExtendedUser getExtendedUser(TwitterAccountDTO authenticatedTwitterAccount) throws Exception {

		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(authenticatedTwitterAccount.getOAuthToken(), authenticatedTwitterAccount.getOAuthTokenSecret());
		ExtendedUser extendedUser = twitter.verifyCredentials();
		/*
		String currentUserId = null;
		ExtendedUser extendedUser = null;
		
		
		try {
			currentUserId = twitter.getUserId();
			extendedUser =twitter.getUserDetail(currentUserId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
*/		
		return extendedUser;

	}

	public List<TwitterUpdateDTO> getUpdates(TwitterAccountDTO twitterAccount,
			FilterCriteriaDTO filter) throws Exception {

		// Get the user twitter account
		/*Twitter twitter = new Twitter(twitterAccount.getTw
				itterScreenName(),
				twitterAccount.getTwitterPassword());*/
		
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(twitterAccount.getOAuthToken(), twitterAccount.getOAuthTokenSecret());
		//ExtendedUser extendedUser = twitter.verifyCredentials();
		
		// Create new data structure
		List<TwitterUpdateDTO> returnList = new ArrayList<TwitterUpdateDTO>();

		// if twitter is null, something went wrong
		assert (twitter != null);

		// CAll twitter API
		List<Status> statusList = null;

		Paging paging = new Paging();
		paging.setSinceId(filter.getSinceId());
		// CAll twitter API
		statusList = twitter.getFriendsTimeline(paging);

		// CAll twitter API
		// List<Status> statusList = twitter.getFriendsTimeline();

		// Is something wrong?
		assert (statusList != null);

		// Populate data structure
		for (Status status : statusList) {
			returnList.add(DataUtils.createTwitterUpdateDto(status,true));

			System.out.println("getInReplyToStatusId "
					+ status.getInReplyToStatusId());
			System.out.println("getId() " + status.getId());
			System.out.println("getInReplyToUserId() "
					+ status.getInReplyToUserId());
			System.out.println("getRateLimitLimit() "
					+ status.getRateLimitLimit());
			System.out.println("getRateLimitRemaining() "
					+ status.getRateLimitRemaining());
			System.out.println("getRateLimitReset() "
					+ status.getRateLimitReset());
			System.out.println("getSource() " + status.getSource());
			System.out.println("stat.getText() " + status.getText());
			System.out.println("getCreatedAt() " + status.getCreatedAt());
			System.out.println("getUser().getScreenName() "
					+ status.getUser().getScreenName());
		}
		return returnList;
	}

	public Status postUpdate(TwitterUpdateDTO update) throws Exception {

		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(update.getTwitterAccount().getOAuthToken(), update.getTwitterAccount().getOAuthTokenSecret());
		
		//twitter.updateStatus(update.getText());
		
		
		//ExtendedUser user = null;
		Status latestStatus = null;
		log.fine("Updating status: "+update.getText());
		
		try {
				latestStatus = twitter.updateStatus(update.getText(), update.getInReplyToStatusId());
				
				
		} catch (TwitterException e) {
			log.severe(e.getLocalizedMessage());
			e.printStackTrace();
			throw new Exception(e);
		}
		return latestStatus;
	}
	

	public TwitterAccountDTO getPreAuthorizedTwitterAccount()
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
		
		//requestToken.getAccessToken();


		preAutwitterAccount.setOAuthLoginUrl(requestToken.getAuthorizationURL());
		preAutwitterAccount.setOAuthToken(requestToken.getToken());
		preAutwitterAccount.setOAuthTokenSecret(requestToken.getTokenSecret());
	
		return preAutwitterAccount;
	}

	
	public TwitterAccountDTO authorizeAccount(TwitterAccountDTO preAuthorizedTwitterAccount) throws Exception {
		TwitterAccountDTO authorizedTwitterAccount = new TwitterAccountDTO();
		
		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		AccessToken accessToken = null;
		User twitterUser = null;
		try {
			
			
			accessToken = twitter.getOAuthAccessToken(preAuthorizedTwitterAccount.getOAuthToken(),preAuthorizedTwitterAccount.getOAuthTokenSecret());
			
			twitter.setOAuthAccessToken(accessToken);
			
			
			twitterUser = twitter.verifyCredentials();
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw e;
		}

		if ( twitterUser == null ) {
			throw new Exception("Could not authenticate");
		}
		
		authorizedTwitterAccount.setIsOAuthenticated(true);
		authorizedTwitterAccount.setOAuthToken(accessToken.getToken());
		authorizedTwitterAccount.setOAuthTokenSecret(accessToken.getTokenSecret());
		return authorizedTwitterAccount;
		
	}
	public User getAuthenticatedUser(
			TwitterAccountDTO twitterAccount)
			throws Exception {

		Twitter twitter = new Twitter();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		AccessToken accessToken = null;
		User twitterUser = null;
		try {
			
			
			accessToken = twitter.getOAuthAccessToken(twitterAccount.getOAuthToken(),twitterAccount.getOAuthTokenSecret());
			
			twitter.setOAuthAccessToken(accessToken);
			
			
			twitterUser = twitter.verifyCredentials();
			
			//twitter2.updateStatus("Setting new Status...");
		} catch (Exception e) {
			
			e.printStackTrace();
			throw e;
		}

		twitterAccount.setIsOAuthenticated(true);
		twitterAccount.setOAuthToken(accessToken.getToken());
		twitterAccount.setOAuthTokenSecret(accessToken.getTokenSecret());
		
		return twitterUser;
	}

	public User getAuthenticatedUser(TwitterAccountDO twitterAccount) throws Exception {
		String oAuthToken = twitterAccount.getOAuthToken();
		String oAuthTokenSecret = twitterAccount.getOAuthTokenSecret();
		if (oAuthToken==null || oAuthTokenSecret==null ) {
			throw new Exception("The user has no configured twitter account");
		}
		
		try {
			Twitter twitter = new Twitter();
			AccessToken accessToken = new AccessToken(oAuthToken,oAuthTokenSecret);
			twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			twitter.setOAuthAccessToken(accessToken);
			
			User twitterUser = twitter.verifyCredentials();
			return twitterUser;
		} catch (Exception e) {
			log.severe("Error: "+e.getLocalizedMessage());
			throw e;
		}		
	}
	
}
