package org.nideasystems.webtools.zwitrng.server;

import java.util.ArrayList;
import java.util.List;

import org.nideasystems.webtools.zwitrng.client.services.TwitterService;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;

import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TwitterServiceImpl extends RemoteServiceServlet implements
		TwitterService {

	private static final List<String> whiteListEmails = new ArrayList<String>();
	static {
		whiteListEmails.add("joao");
		whiteListEmails.add("joaomiguel.pereira@gmail.com");

	}
	private UserService userService = UserServiceFactory.getUserService();;
	/**
	 * 
	 */
	private static final long serialVersionUID = -481643127871478064L;

	@Override
	public List<TwitterUpdateDTO> search(TwitterAccountDTO twitterAccount,
			FilterCriteriaDTO filter) throws Exception {

		// Check if is logged in
		User currentUser = userService.getCurrentUser();

		// do some validations...
		if (currentUser == null
				|| !whiteListEmails.contains(currentUser.getEmail())) {
			throw new Exception("You must be logged in");

		}

		return TwitterServiceAdapter.get().searchStatus(twitterAccount, filter);

	}

	@Override
	public List<TwitterUpdateDTO> getTwitterUpdates(
			TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter)
			throws Exception {
		// Check if is logged in
		User currentUser = userService.getCurrentUser();

		// do some validations...
		if (currentUser == null
				|| !whiteListEmails.contains(currentUser.getEmail())) {
			throw new Exception("You must be logged in");

		}

		return TwitterServiceAdapter.get().getUpdates(twitterAccount, filter);

	}

}
