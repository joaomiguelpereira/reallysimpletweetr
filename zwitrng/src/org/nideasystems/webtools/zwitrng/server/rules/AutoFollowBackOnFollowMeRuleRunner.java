package org.nideasystems.webtools.zwitrng.server.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.RateLimitsDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.pojos.BusinessHelper;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;

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
		// per hour
		int maxAutoFollowPer15Minutes = 10; // =3*10 per hour
		int counter = 0;
		// Check if it's null
		if (queue != null) {
			for (Integer userId : queue) {
				// Check if user is ok according to the rule
				TwitterUserDTO user = getUserNameToFollowBack(userId);
				if (user != null) {
					
					try {
						log.fine("Trying to follow user:"
								+ user.getTwitterScreenName());
						TwitterAccountDTO authAccount = TwitterAccountDAO
								.createAuthorizedAccountDto(persona
										.getTwitterAccount());
						PersonaDTO personaDto = DataUtils.createPersonaDto(persona,
								authAccount);

						follow(personaDto, user);
						// Check if a message is to be sent?
						if (rule.isSendDirectMessage()) {
							// Send the message
							// Create a personaDTO

							sendDirectMessage(personaDto, rule.getTemplateName(),
									user, userId);

						}
					} catch (Exception e) {
						log.warning("Could not follow the user: "+e.getMessage());
						//continue
						//log ignored user
						e.printStackTrace();
					}
					
					// queue.remove(userId);

				} else if (persona.getTwitterAccount().getFollowersIds() != null) {
					persona.getTwitterAccount().getFollowersIds().add(
							new Integer(userId));

				}

				// even if it's not to follow, synh with followers list

				persona.getTwitterAccount().getAutoFollowBackIdsQueue().remove(
						userId);
				counter++;
				if (counter >= maxAutoFollowPer15Minutes) {
					break;
				}
			}

		}

	}

	private void follow(PersonaDTO personaDto, TwitterUserDTO user)
			throws Exception {
		// getBusinessHelper().getTwitterPojo().followUser(persona, user);
		// Create an auth Account

		log.fine("Following User: " + user.getTwitterScreenName());
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
		if (persona.getTwitterAccount().getFollowingIds() != null) {
			log.fine(("Adding new FOLLOWER: " + user.getId()));
			log.fine(("SIZE BEFORE: " + persona.getTwitterAccount()
					.getFollowingIds().size()));

			persona.getTwitterAccount().addFollowingId(
					new Integer(user.getId()));
			log.fine(("SIZE AFTER: " + persona.getTwitterAccount()
					.getFollowingIds().size()));
		}

		if (persona.getTwitterAccount().getFollowersIds() != null) {
			persona.getTwitterAccount().getFollowersIds().add(
					new Integer(user.getId()));

		}

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
			TwitterUserDTO user, Integer userId) throws Exception {
		// Get the template
		TemplateDO templateDO = getBusinessHelper().getTemplatePojo()
				.getTemplateFragments(persona, templateName);
		// Tranform the template text
		if (templateDO == null) {
			throw new Exception("Template not found");
		}
		log.info("Template text is:" + templateDO);
		List<String> userNames = new ArrayList<String>(1);
		userNames.add(user.getTwitterScreenName());
		String message = getBusinessHelper().getTemplatePojo()
				.buildTweetFromTemplate(templateDO.getText().getValue(),
						persona, userNames);
		log.fine("Sending message: " + message);
		// Send the direct message
		TwitterUpdateDTO update = new TwitterUpdateDTO();
		update.setInReplyToUserId(userId);
		update.setText(message);

		getBusinessHelper().getTwitterPojo().sendDM(personaDto, update);

	}

	private TwitterUserDTO getUserNameToFollowBack(Integer userId)
			throws Exception {
		log.fine("canFollowBack:" + userId);

		TwitterAccountDTO authAccount = TwitterAccountDAO
				.createAuthorizedAccountDto(persona.getTwitterAccount());
		PersonaDTO personaDto = DataUtils.createPersonaDto(this.persona,
				authAccount);
		// Get the use
		TwitterUserDTO twitterUser = businessHelper.getTwitterPojo()
				.getUserInfo(personaDto, String.valueOf(userId));

		log.fine("Cheching user: " + twitterUser.getTwitterScreenName());
		log
				.fine("Check number of updates..."
						+ twitterUser.getTwitterUpdates());
		log
				.fine("Check number of friends..."
						+ twitterUser.getTwitterFriends());
		log.fine("Check number of followers..."
				+ twitterUser.getTwitterFollowers());

		// Get the rule ratio
		double currentRatio = Double.valueOf(twitterUser.getTwitterFriends())
				/ Double.valueOf(twitterUser.getTwitterFollowers());

		double ratio = currentRatio * 100;

		boolean ok = true;
		if (ratio > rule.getMaxRatio()) {
			log.fine("Ratio is nOk");
			ok = false;
		}

		if (twitterUser.getTwitterUpdates() < rule.getMinUpdates()) {
			log.fine("Updates is nOK");
			ok = false;
		}

		// Chech Username
		if (rule.getExcludedWordsInNames() != null) {

			for (String excludeWord : rule.getExcludedWordsInNames()) {
				if (twitterUser.getTwitterScreenName().contains(excludeWord)) {
					ok = false;
					break;
				}

			}
		}
		log.fine("User can be followed back? " + ok);
		if (ok) {
			return twitterUser;
		} else {

		}
		return null;

	}

}
