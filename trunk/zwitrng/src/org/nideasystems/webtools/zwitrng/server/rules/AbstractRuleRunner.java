package org.nideasystems.webtools.zwitrng.server.rules;

import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.RateLimitsDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.pojos.BusinessHelper;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import twitter4j.User;

public abstract class AbstractRuleRunner {

	private static final Logger log = Logger
	.getLogger(AbstractRuleRunner.class.getName());
	protected PersonaDO persona;
	protected AutoFollowRuleDO rule;
	protected BusinessHelper businessHelper;

	
	public AbstractRuleRunner(PersonaDO persona2,
			AutoFollowRuleDO autofollowrule) {
		this.persona = persona2;
		this.rule = autofollowrule;
	}


	public abstract void execute() throws Exception;
	
	protected User getTwitterUser(Integer userId) throws Exception {
		log.fine("Getting User:" + userId);

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

	protected User getTwitterUser(String screenName) throws Exception {
		log.fine("Getting User:" + screenName);

		TwitterAccountDTO authAccount = TwitterAccountDAO
				.createAuthorizedAccountDto(persona.getTwitterAccount());
		PersonaDTO personaDto = DataUtils.createPersonaDto(this.persona,
				authAccount);
		// Get the use

		TwitterServiceAdapter twitterService = TwitterServiceAdapter
				.get(personaDto.getTwitterAccount());

		User user = twitterService.getUserInfo(screenName);

		return user;

	}

	
	protected void updateRateLimist(TwitterServiceAdapter twService) {

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
	
	protected boolean canFollow(User user) {
		log.fine("Persona: "+persona.getName()+" Cheching user: " + user.getScreenName());
		log.fine("Persona: "+persona.getName()+" Check number of updates..." + user.getStatusesCount());
		log.fine("Persona: "+persona.getName()+" Check number of friends..." + user.getFriendsCount());
		log.fine("Persona: "+persona.getName()+" Check number of followers..." + user.getFollowersCount());

		boolean ok = true;

		if (user.getFollowersCount() != 0) {
			// Get the rule ratio
			double currentRatio = Double.valueOf(user.getFriendsCount())
					/ Double.valueOf(user.getFollowersCount());

			double ratio = currentRatio * 100;

			if (ratio > rule.getMaxRatio()) {
				log.fine("Persona: "+persona.getName()+" Ratio is nOk for user "+ user.getScreenName());
				ok = false;
			}

		} else {
			ok = false;
			log.fine("Persona: "+persona.getName()+" No followers for user "+ user.getScreenName());
		}

		if (user.getStatusesCount() < rule.getMinUpdates()) {
			log.fine("Persona: "+persona.getName()+" Updates is nOK for user "+ user.getScreenName());
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
		log.fine("Persona: "+persona.getName()+" User can be followed back?  for user "+ user.getScreenName()+": " + ok);
		return ok;

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
