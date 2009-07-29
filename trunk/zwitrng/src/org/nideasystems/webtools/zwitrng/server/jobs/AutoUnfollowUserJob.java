package org.nideasystems.webtools.zwitrng.server.jobs;


import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.rules.AutoUnFollowBackOnFollowMeRuleRunner;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;

public class AutoUnfollowUserJob extends AbstractJob {

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
		int unfollowSize = persona.getTwitterAccount()
				.getAutoUnFollowBackIdsQueue() != null ? persona
				.getTwitterAccount().getAutoUnFollowBackIdsQueue().size() : 0;

		log.fine("FollowBack list Size:" + backSize);
		log.fine("Ignore list Size:" + ignoreSize);
		log.fine("Friends: " + followingSize);
		log.fine("Followers: " + followersSize);
		log.fine("Unfollow Back Followers: " + unfollowSize);

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

		AutoFollowRuleDO rule = getBusinessHelper().getPersonaDao()
				.getAutoFollowRule(persona, AutoFollowTriggerType.UNFOLLOW);

		AutoUnFollowBackOnFollowMeRuleRunner ruleEx = new AutoUnFollowBackOnFollowMeRuleRunner(
				persona, rule);

		ruleEx.setBusinessHelper(getBusinessHelper());
		if (rule != null
				&& rule.isEnabled()
				&& persona.getTwitterAccount().getAutoUnFollowBackIdsQueue() != null) {
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
		unfollowSize = persona.getTwitterAccount()
				.getAutoUnFollowBackIdsQueue() != null ? persona
				.getTwitterAccount().getAutoUnFollowBackIdsQueue().size() : 0;
		log.fine("FollowBack list Size:" + backSize);
		log.fine("Ignore list Size:" + ignoreSize);
		log.fine("Friends: " + followingSize);
		log.fine("Followers: " + followersSize);
		log.fine("Unfollow Back Followers: " + unfollowSize);

	}

}
