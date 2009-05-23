package org.nideasystems.webtools.zwitrng.server.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import twitter4j.DirectMessage;
import twitter4j.ExtendedUser;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterServiceAdapter {

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
	public ExtendedUser getExtendedUser(String twitterName, String twitterPass,
			boolean authenticationNeeded) throws Exception {

		Twitter twitter = new Twitter(twitterName, twitterPass);

		
		if (authenticationNeeded) {

			// Try an operation to login
			try {
				// Dummy call
				List<DirectMessage> msgs = twitter.getDirectMessages();

			} catch (TwitterException e1) {
				throw new Exception(e1);
			}
		}

		ExtendedUser userDetails = null;

		if (twitter != null) {
			String userId = twitter.getUserId();
			if (userId != null && !userId.isEmpty()) {
				try {
					userDetails = twitter.getUserDetail(userId);

				} catch (TwitterException e) {
					e.printStackTrace();
					throw new Exception(e);
				}

			}
		}
		return userDetails;

	}

	public List<TwitterUpdateDTO> getUpdates(TwitterAccountDTO twitterAccount,
			FilterCriteriaDTO filter) throws Exception {

		// Get the user twitter account
		Twitter twitter = new Twitter(twitterAccount.getTwitterScreenName(),
				twitterAccount.getTwitterPassword());
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
			returnList.add(DataUtils.createTwitterUpdateDto(status));

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
}
