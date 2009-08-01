package org.nideasystems.webtools.zwitrng.server.rules;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.Configuration;
import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.pojos.BusinessHelper;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import twitter4j.User;

public class AutoUnFollowBackOnFollowMeRuleRunner extends AbstractRuleRunner {

	private static final Logger log = Logger
			.getLogger(AutoUnFollowBackOnFollowMeRuleRunner.class.getName());

	public AutoUnFollowBackOnFollowMeRuleRunner(PersonaDO persona,
			AutoFollowRuleDO autofollowrule) {
		super(persona, autofollowrule);

	}

	public void execute() throws Exception {

		log.fine("> > Persona: " + persona.getName()
				+ " executing AutoUnfollowBack Users");
		List<Integer> queue = persona.getTwitterAccount()
				.getAutoUnFollowBackIdsQueue();

		Set<Integer> unfollowedIds = persona.getTwitterAccount()
				.getAutoUnfollowedIds();

		Set<Integer> followersIds = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount()
				.getFollowersIdsBlob());

		Set<Integer> followingIds = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount()
				.getFollowingIdsBlob());

		if (unfollowedIds == null) {
			unfollowedIds = new HashSet<Integer>();
		}

		/*
		 * Set<Integer> followingIds = persona.getTwitterAccount()
		 * .getFollowingIds();
		 */

		int maxAutoUnFollow = Configuration.MAX_FOLLOW_JOB; 

		if (queue != null) {
			maxAutoUnFollow = queue.size() > maxAutoUnFollow ? maxAutoUnFollow
					: queue.size();
			List<Integer> removeList = new ArrayList<Integer>();
			
			for (int i = 0; i < maxAutoUnFollow; i++) {
				int userId = queue.get(i);
				log.fine("> > Persona:" + persona.getName()
						+ " will try to unfollow user id: " + userId);
				removeList.add(userId);
				// followingIds.remove(userId);
				unfollowedIds.add(userId);
				// check if are friends

				if (followingIds.contains(
						userId)
						&& !followersIds
								.contains(userId)) {
					if (processUser(userId)) {
						log.fine("> > Persona:" + persona.getName()
								+ " unfollowed user " + userId);
						// followingIds.remove(userId);
					} else {
						log.fine("> > Persona:" + persona.getName()
								+ " could NOT unfollow user " + userId);
					}

				} else {
					log
							.fine("> > Persona:"
									+ persona.getName()
									+ " do not have the user in friends list or they are mutal friends");
				}
				long now = new Date().getTime();
				if (now - getBusinessHelper().getStartTime() > Configuration.MAX_JOB_RUN_TIME) {
					log.fine("-----> Ending Job because time elapsed. Users Processed:"+i);
					break;
				}


			}
			for (Integer remId : removeList) {
				queue.remove(remId);
			}

		}

		persona.getTwitterAccount().setAutoUnFollowBackIdsQueue(queue);
		persona.getTwitterAccount().setAutoUnfollowedIds(unfollowedIds);
		// persona.getTwitterAccount().setFollowingIds(followingIds);

	}

	private boolean processUser(int userId) {
		boolean UnFollowed = true;

		try {

			User user = getTwitterUser(userId);

			TwitterAccountDTO authAccount = TwitterAccountDAO
					.createAuthorizedAccountDto(persona.getTwitterAccount());
			PersonaDTO personaDto = DataUtils.createPersonaDto(persona,
					authAccount);

			log.info("Unfollowing user: " + user.getScreenName());
			// check if the user is really not following me
			// Try to send a message
			unfollow(personaDto, user);
			if (rule.isSendDirectMessage()) {
				sendReplyToUser(personaDto, rule.getTemplateName(), user);
			}

		} catch (Exception e) {
			log.warning("Error unfollowing following user: " + e.getMessage());
			e.printStackTrace();
			UnFollowed = false;
		}

		return UnFollowed;

	}

	private void unfollow(PersonaDTO persona, User user) throws Exception {
		// TODO Auto-generated method stub
		TwitterServiceAdapter twitterService = TwitterServiceAdapter
				.get(persona.getTwitterAccount());
		try {

			twitterService.followUser(false, user.getId());
		} catch (Exception e) {
			log.severe("Could not Un follow the User" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		updateRateLimist(twitterService);

	}

	private void sendReplyToUser(PersonaDTO personaDto, String templateName,
			User user) throws Exception {

		TemplateDO templateDO = getBusinessHelper().getTemplatePojo()
				.getTemplateFragments(persona, templateName);
		// Tranform the template text
		if (templateDO == null) {
			throw new Exception("Template not found");
		}
		log.info("Template text is:" + templateDO);
		List<String> userNames = new ArrayList<String>(1);
		userNames.add("@" + user.getScreenName());
		String message = getBusinessHelper().getTemplatePojo()
				.buildTweetFromTemplate(templateDO.getText().getValue(),
						persona, userNames);
		log.fine("Sending message: " + message);
		// Send the direct message
		TwitterUpdateDTO update = new TwitterUpdateDTO();
		update.setInReplyToUserId(user.getId());
		update.setText(message);

		TwitterServiceAdapter twitterService = TwitterServiceAdapter
				.get(personaDto.getTwitterAccount());
		twitterService.postUpdate(update);
		// getBusinessHelper().getTwitterPojo().postUpdate(personaDto, update);
		updateRateLimist(twitterService);
	}

	public void setPersona(PersonaDO persona) {
		this.persona = persona;
	}

	public PersonaDO getPersona() {
		return persona;
	}

	public void setRule(AutoFollowRuleDO rule) {
		this.rule = rule;
	}

	public AutoFollowRuleDO getRule() {
		return rule;
	}

	public void setBusinessHelper(BusinessHelper businessHelper) {
		this.businessHelper = businessHelper;
	}

	public BusinessHelper getBusinessHelper() {
		return businessHelper;
	}

}
