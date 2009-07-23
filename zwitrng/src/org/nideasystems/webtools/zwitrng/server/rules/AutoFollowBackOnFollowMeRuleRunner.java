package org.nideasystems.webtools.zwitrng.server.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.RateLimitsDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.pojos.BusinessHelper;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import twitter4j.User;

public class AutoFollowBackOnFollowMeRuleRunner {

	private static final Logger log = Logger
			.getLogger(AutoFollowBackOnFollowMeRuleRunner.class.getName());
	private PersonaDO persona;
	private AutoFollowRuleDO rule;
	private BusinessHelper businessHelper;

	public AutoFollowBackOnFollowMeRuleRunner(PersonaDO persona,
			AutoFollowRuleDO autofollowrule) {
		this.persona = persona;
		this.rule = autofollowrule;
	}

	public BusinessHelper getBusinessHelper() {
		return businessHelper;
	}

	public void setBusinessHelper(BusinessHelper businessHelper) {
		this.businessHelper = businessHelper;
	}

	public void execute() throws Exception {

		// get the list of new personas to follow
		List<Integer> queue = persona.getTwitterAccount()
				.getAutoFollowBackIdsQueue();
		Set<Integer> followingIds = persona.getTwitterAccount()
				.getFollowingIds();
		Set<Integer> followersIds = persona.getTwitterAccount()
				.getFollowersIds();
		int autoFollowCount = persona.getTwitterAccount()
				.getAutoFollowedCount() != null ? persona.getTwitterAccount()
				.getAutoFollowedCount() : 0;

		if (followersIds == null) {
			followersIds = new HashSet<Integer>();
		}
		if (followingIds == null) {
			followingIds = new HashSet<Integer>();
		}

		log.fine("Follow Back Queue Size: " + queue.size());
		// per hour
		int maxAutoFollow = 5; // =5*10 per hour
		Set<Integer> ignoreList = persona.getTwitterAccount()
				.getIgnoreUsersIds();
		if (ignoreList == null) {
			ignoreList = new HashSet<Integer>();

		}

		if (queue != null) {
			maxAutoFollow = queue.size() > maxAutoFollow ? maxAutoFollow : queue.size();
			for (int i = 0; i < maxAutoFollow; i++) {
				int userId = queue.get(i);
				//queue.remove(i);

				followersIds.add(userId);

				if (!processUser(userId)) {
					log.fine("Ignoring User: " + userId);
					ignoreList.add(userId);
				} else {
					autoFollowCount++;
					followingIds.add(userId);

				}
			}
		}
		for (int i=0; i< maxAutoFollow;i++) {
			queue.remove(i);
		}
		persona.getTwitterAccount().setAutoFollowedCount(autoFollowCount);
		persona.getTwitterAccount().setFollowersIds(followersIds);
		persona.getTwitterAccount().setFollowingIds(followingIds);
		persona.getTwitterAccount().setAutoFollowBackIdsQueue(queue);
		persona.getTwitterAccount().setIgnoreUsersIds(ignoreList);

	}

	private boolean processUser(Integer userId) {
		boolean followed = true;

		try {

			User user = getUserToFollowBack(userId);

			TwitterAccountDTO authAccount = TwitterAccountDAO
					.createAuthorizedAccountDto(persona.getTwitterAccount());
			PersonaDTO personaDto = DataUtils.createPersonaDto(persona,
					authAccount);

			if (canFollow(user)) {
				log.fine("Trying to follow user:" + user.getScreenName());

				follow(personaDto, user);
				// Check if a message is to be sent?
				if (rule.isSendDirectMessage()) {
					sendDirectMessage(personaDto, rule.getTemplateName(), user);
				}
			} else {
				followed = false;
				// Send ignore message
				if (rule.getSendDirectMessageOnIgnore()) {
					sendDirectMessage(personaDto, rule.getIgnoreTemplate(),
							user);
				}
			}

		} catch (Exception e) {
			log.warning("Error following user: " + e.getMessage());
			e.printStackTrace();
			followed = false;
		}

		return followed;

	}

	private void follow(PersonaDTO personaDto, User user) throws Exception {
		// getBusinessHelper().getTwitterPojo().followUser(persona, user);
		// Create an auth Account

		log.fine("Following User: " + user.getScreenName());
		TwitterServiceAdapter twitterService = TwitterServiceAdapter
				.get(personaDto.getTwitterAccount());
		try {
			twitterService.followUser(true, user.getId());
		} catch (Exception e) {
			log.severe("Could not follow the User" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		// add to followers
		/*
		 * if (persona.getTwitterAccount().getFollowingIds() != null) {
		 * log.fine(("Adding new FOLLOWER: " + user.getId()));
		 * log.fine(("SIZE BEFORE: " + persona.getTwitterAccount()
		 * .getFollowingIds().size()));
		 * 
		 * persona.getTwitterAccount().addFollowingId( new
		 * Integer(user.getId())); log.fine(("SIZE AFTER: " +
		 * persona.getTwitterAccount() .getFollowingIds().size())); }
		 * 
		 * if (persona.getTwitterAccount().getFollowersIds() != null) {
		 * persona.getTwitterAccount().getFollowersIds().add( new
		 * Integer(user.getId()));
		 * 
		 * }
		 */

		updateRateLimist(twitterService);

	}

	private void updateRateLimist(TwitterServiceAdapter twService) {

		if (persona.getTwitterAccount().getRateLimits() != null) {
			persona.getTwitterAccount().getRateLimits().setRateLimitLimit(
					twService.getRateLimitLimit());
			persona.getTwitterAccount().getRateLimits().setRateLimitRemaining(
					twService.getRateLimitRemaining());
			persona.getTwitterAccount().getRateLimits().setRateLimitReset(
					twService.getRateLimitReset());

		} else {
			RateLimitsDO limitsDo = new RateLimitsDO();
			limitsDo.setRateLimitLimit(twService.getRateLimitLimit());
			limitsDo.setRateLimitRemaining(twService.getRateLimitRemaining());
			limitsDo.setRateLimitReset(twService.getRateLimitReset());
			limitsDo.setTwitterAccount(persona.getTwitterAccount());
			persona.getTwitterAccount().setRateLimits(limitsDo);

		}

	}

	private void sendDirectMessage(PersonaDTO personaDto, String templateName,
			User user) throws Exception {
		// Get the template
		TemplateDO templateDO = getBusinessHelper().getTemplatePojo()
				.getTemplateFragments(persona, templateName);
		// Tranform the template text
		if (templateDO == null) {
			throw new Exception("Template not found");
		}
		log.info("Template text is:" + templateDO);
		List<String> userNames = new ArrayList<String>(1);
		userNames.add(user.getScreenName());
		String message = getBusinessHelper().getTemplatePojo()
				.buildTweetFromTemplate(templateDO.getText().getValue(),
						persona, userNames);
		log.fine("Sending message: " + message);
		// Send the direct message
		TwitterUpdateDTO update = new TwitterUpdateDTO();
		update.setInReplyToUserId(user.getId());
		update.setText(message);

		getBusinessHelper().getTwitterPojo().sendDM(personaDto, update);

	}

	private boolean canFollow(User user) {
		log.fine("Cheching user: " + user.getScreenName());
		log.fine("Check number of updates..." + user.getStatusesCount());
		log.fine("Check number of friends..." + user.getFriendsCount());
		log.fine("Check number of followers..." + user.getFollowersCount());

		boolean ok = true;

		if (user.getFollowersCount() != 0) {
			// Get the rule ratio
			double currentRatio = Double.valueOf(user.getFriendsCount())
					/ Double.valueOf(user.getFollowersCount());

			double ratio = currentRatio * 100;

			if (ratio > rule.getMaxRatio()) {
				log.fine("Ratio is nOk");
				ok = false;
			}

		} else {
			ok = false;
			log.fine("No followers");
		}

		if (user.getStatusesCount() < rule.getMinUpdates()) {
			log.fine("Updates is nOK");
			ok = false;
		}

		// Chech Username
		if (rule.getExcludedWordsInNames() != null) {

			for (String excludeWord : rule.getExcludedWordsInNames()) {
				if (user.getScreenName().contains(excludeWord)) {
					ok = false;
					break;
				}

			}
		}
		log.fine("User can be followed back? " + ok);
		return ok;

	}

	private User getUserToFollowBack(Integer userId) throws Exception {
		log.fine("canFollowBack:" + userId);

		TwitterAccountDTO authAccount = TwitterAccountDAO
				.createAuthorizedAccountDto(persona.getTwitterAccount());
		PersonaDTO personaDto = DataUtils.createPersonaDto(this.persona,
				authAccount);
		// Get the use

		TwitterServiceAdapter twitterService = TwitterServiceAdapter
				.get(personaDto.getTwitterAccount());

		User user = twitterService.getUserInfo(userId.toString());

		return user;

	}

}
