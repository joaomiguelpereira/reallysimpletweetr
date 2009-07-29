package org.nideasystems.webtools.zwitrng.server.jobs;


import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.rules.AutoFollowBackOnFollowMeRuleRunner;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;

public class AutoFollowBackUserJob extends AbstractJob {

	@Override
	public void execute() throws Exception {
		log.fine("Executing Job: " + this.getClass().getName());

		int followingSize = persona.getTwitterAccount().getFollowingIds() != null ? persona
				.getTwitterAccount().getFollowingIds().size()
				: 0;
		int followersSize = persona.getTwitterAccount().getFollowersIds() != null ? persona
				.getTwitterAccount().getFollowersIds().size()
				: 0;
		int backSize = persona.getTwitterAccount().getAutoFollowBackIdsQueue() != null ? persona
				.getTwitterAccount().getAutoFollowBackIdsQueue().size()
				: 0;
		int ignoreSize = persona.getTwitterAccount().getIgnoreUsersIds() != null ? persona
				.getTwitterAccount().getIgnoreUsersIds().size()
				: 0;
		log.fine("FollowBack list Size:" + backSize);
		log.fine("Ignore list Size:" + ignoreSize);
		log.fine("Friends: " + followingSize);
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

		followingSize = persona.getTwitterAccount().getFollowingIds() != null ? persona
				.getTwitterAccount().getFollowingIds().size()
				: 0;
		followersSize = persona.getTwitterAccount().getFollowersIds() != null ? persona
				.getTwitterAccount().getFollowersIds().size()
				: 0;
		backSize = persona.getTwitterAccount().getAutoFollowBackIdsQueue() != null ? persona
				.getTwitterAccount().getAutoFollowBackIdsQueue().size()
				: 0;
		ignoreSize = persona.getTwitterAccount().getIgnoreUsersIds() != null ? persona
				.getTwitterAccount().getIgnoreUsersIds().size()
				: 0;
		log.fine("FollowBack list Size:" + backSize);
		log.fine("Ignore list Size:" + ignoreSize);
		log.fine("Friends: " + followingSize);
		log.fine("Followers: " + followersSize);

	}

}
