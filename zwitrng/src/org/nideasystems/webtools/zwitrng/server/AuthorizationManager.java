package org.nideasystems.webtools.zwitrng.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AuthorizationManager {

	private static final Logger log = Logger
			.getLogger(AuthorizationManager.class.getName());

	private static final List<String> whiteListEmails = new ArrayList<String>();
	static {
		whiteListEmails.add("joao");
		whiteListEmails.add("joaomiguel.pereira@gmail.com");
		whiteListEmails.add("marco.pais@gmail.com");
		whiteListEmails.add("vitoratavares@gmail.com");
		whiteListEmails.add("amalheiro@gmail.com");
		whiteListEmails.add("cmsrodrigues@gmail.com");
		whiteListEmails.add("jpearson.us@googlemail.com");

		// whiteListEmails.add("marco.pais@gmail.com");

	}

	public static User checkAuthentication() throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		// Check if the user is logged in with a
		User currentUser = userService.getCurrentUser();
		log.fine("checkAuthentication...");
		log.info("Checking authentication for user: " + currentUser);
		if (currentUser == null) {
			throw new Exception("Please Log in");
		}
		// do some validations...
		if (!whiteListEmails.contains(currentUser.getEmail().toLowerCase())) {
			throw new Exception(
					"You are not allowed to play with this app. Send a email to info@nideasystems.com if you want to try this app.");

		}
		return currentUser;
	}
}
