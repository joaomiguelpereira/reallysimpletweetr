package org.nideasystems.webtools.zwitrng.server.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import twitter4j.User;

public class AutoFollowOnSearchRuleRunner extends AbstractRuleRunner {
	private static final Logger log = Logger
			.getLogger(AutoFollowOnSearchRuleRunner.class.getName());

	public AutoFollowOnSearchRuleRunner(PersonaDO persona2,
			AutoFollowRuleDO autofollowrule) {
		super(persona2, autofollowrule);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() throws Exception {
		List<String> queue = persona.getTwitterAccount()
				.getAutoFollowScreenNamesQueue();
		Set<String> autoFollowedUsersScreenNames = persona.getTwitterAccount()
				.getAutoFollowedScreenNames();
		if (autoFollowedUsersScreenNames == null) {
			autoFollowedUsersScreenNames = new HashSet<String>();
		}

		//Keep my ratio low, please
		
		if (queue != null) {
			int maxFollowing = 5;
			List<String> removeList = new ArrayList<String>();
			maxFollowing = queue.size() > maxFollowing ? maxFollowing : queue
					.size();
			for (int i = 0; i < maxFollowing; i++) {
				// Check if the user stuff
				removeList.add(queue.get(i));
				// Get the User

				try {
					if ( process(queue.get(i)) ) {
						autoFollowedUsersScreenNames.add(queue.get(i));
					} else {
						log.fine("Could not follow user :"+queue.get(i));
					}
					
				} catch (Exception e) {
					log.fine("Could not follow user " + e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				log.fine("User Followed Successfully");
			}

			// Remove the ones already followed
			for (String str : removeList) {
				queue.remove(str);
			}
			persona.getTwitterAccount().setAutoFollowScreenNamesQueue(queue);
			persona.getTwitterAccount().setAutoFollowedScreenNames(
					autoFollowedUsersScreenNames);

		}

	}

	private boolean process(String screenName) throws Exception {

		boolean followed = true;
		User user = getTwitterUser(screenName);
		if (canFollow(user)) {
			log.fine("Following User");
			TwitterAccountDTO authAccount = TwitterAccountDAO
					.createAuthorizedAccountDto(persona.getTwitterAccount());
			PersonaDTO personaDto = DataUtils.createPersonaDto(this.persona,
					authAccount);
			// Get the use

			TwitterServiceAdapter twitterService = TwitterServiceAdapter
					.get(personaDto.getTwitterAccount());

			twitterService.followUser(true, user.getId());
		} else {
			log.fine("Will not follow the user");
			followed = false;
		}
		// Now check preconditions
		return followed;
	}

}
