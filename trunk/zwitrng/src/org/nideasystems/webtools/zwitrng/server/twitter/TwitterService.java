package org.nideasystems.webtools.zwitrng.server.twitter;

import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.ExtendedUser;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterService {

	private TwitterService() {

	}

	public static TwitterService get() {
		return new TwitterService();
	}

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
					System.out.println(userDetails.getCreatedAt());
					System.out.println(userDetails.getDescription());
					System.out.println(userDetails.getFavouritesCount());
					System.out.println(userDetails.getFollowersCount());
					System.out.println(userDetails.getFriendsCount());
					System.out.println(userDetails.getLocation());
					System.out.println(userDetails.getScreenName());
					System.out.println(userDetails.getStatusesCount());
					System.out.println(userDetails.getProfileImageURL());

				} catch (TwitterException e) {
					throw new Exception(e);
				}

			}
		}
		return userDetails;

	}
}
