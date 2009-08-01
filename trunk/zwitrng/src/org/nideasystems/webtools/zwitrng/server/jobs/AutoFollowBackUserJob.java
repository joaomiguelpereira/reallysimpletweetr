package org.nideasystems.webtools.zwitrng.server.jobs;


import java.util.Set;

import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.rules.AutoFollowBackOnFollowMeRuleRunner;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;

public class AutoFollowBackUserJob extends AbstractJob {

	@Override
	public void execute() throws Exception {
		log.fine("Executing Job: " + this.getClass().getName());

				
		//Get the followers from blob
		Set<Integer> followersIds = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount().getFollowersIdsBlob());
		
		
		int followersSize = followersIds != null ? followersIds.size()
				: 0;
		int backSize = persona.getTwitterAccount().getAutoFollowBackIdsQueue() != null ? persona
				.getTwitterAccount().getAutoFollowBackIdsQueue().size()
				: 0;
		int ignoreSize = persona.getTwitterAccount().getIgnoreUsersIds() != null ? persona
				.getTwitterAccount().getIgnoreUsersIds().size()
				: 0;
		log.fine("FollowBack list Size:" + backSize);
		log.fine("Ignore list Size:" + ignoreSize);
		log.fine("Followers: " + followersSize);

		// Test

		// Synch queu to sync
		/*
		 * TwitterAccountDO twitterAccount = persona.getTwitterAccount();
		 * TwitterAccountDTO authorizedTwitterAccount =
		 * TwitterAccountDAO.createAuthorizedAccountDto(twitterAccount);
		 * getBusinessHelper
		 * ().getTwitterPojo().updateFollowBackUsersIdQueue(twitterAccount,
		 * authorizedTwitterAccount);
		 */

		AutoFollowRuleDO autofollowrule = getBusinessHelper().getPersonaDao()
				.getAutoFollowRule(persona, AutoFollowTriggerType.ON_FOLLOW_ME);

		AutoFollowBackOnFollowMeRuleRunner ruleEx = new AutoFollowBackOnFollowMeRuleRunner(
				persona, autofollowrule);

		ruleEx.setBusinessHelper(getBusinessHelper());
		if (autofollowrule != null
				&& autofollowrule.isEnabled()
				&& persona.getTwitterAccount().getAutoFollowBackIdsQueue() != null) {
			ruleEx.execute();
		}

		backSize = persona.getTwitterAccount().getAutoFollowBackIdsQueue() != null ? persona
				.getTwitterAccount().getAutoFollowBackIdsQueue().size()
				: 0;
		ignoreSize = persona.getTwitterAccount().getIgnoreUsersIds() != null ? persona
				.getTwitterAccount().getIgnoreUsersIds().size()
				: 0;
		log.fine("FollowBack list Size:" + backSize);
		log.fine("Ignore list Size:" + ignoreSize);
		log.fine("Followers: " + followersSize);

	}

}
