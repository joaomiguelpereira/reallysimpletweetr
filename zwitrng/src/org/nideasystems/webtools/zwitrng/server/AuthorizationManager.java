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
		//whiteListEmails.add("marco.pais@gmail.com");

	}
	
	public static User checkAuthentication() throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		// Check if the user is logged in with a
		User currentUser = userService.getCurrentUser();
		log.fine("checkAuthentication...");
		// do some validations...
		if (currentUser == null
				|| !whiteListEmails.contains(currentUser.getEmail())) {
			throw new Exception("You must be logged in");

		}
		return currentUser;
	}
}
