package org.nideasystems.webtools.zwitrng.server.jobs;



import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.rules.AutoFollowOnSearchRuleRunner;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;

public class AutoFollowUserJob extends AbstractJob {

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
		int followSize = persona.getTwitterAccount()
				.getAutoFollowScreenNamesQueue() != null ? persona
				.getTwitterAccount().getAutoFollowScreenNamesQueue().size() : 0;

		int autoFollowedSize = persona.getTwitterAccount()
				.getAutoFollowedScreenNames() != null ? persona
				.getTwitterAccount().getAutoFollowedScreenNames().size() : 0;

		log.fine("FollowBack list Size:" + backSize);
		log.fine("Ignore list Size:" + ignoreSize);
		log.fine("Friends: " + followingSize);
		log.fine("Followers: " + followersSize);
		log.fine("Unfollow Back Followers: " + unfollowSize);
		log.fine("Follow Queue: " + followSize);

		log.fine("Followed Size: " + autoFollowedSize);

		AutoFollowRuleDO rule = getBusinessHelper().getPersonaDao()
				.getAutoFollowRule(persona, AutoFollowTriggerType.SEARCH);

		AutoFollowOnSearchRuleRunner ruleEx = new AutoFollowOnSearchRuleRunner(
				persona, rule);

		ruleEx.setBusinessHelper(getBusinessHelper());
		if (rule != null
				&& rule.isEnabled()
				&& persona.getTwitterAccount().getAutoFollowScreenNamesQueue() != null) {
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
		followSize = persona.getTwitterAccount()
				.getAutoFollowScreenNamesQueue() != null ? persona
				.getTwitterAccount().getAutoFollowScreenNamesQueue().size() : 0;

		autoFollowedSize = persona.getTwitterAccount()
				.getAutoFollowedScreenNames() != null ? persona
				.getTwitterAccount().getAutoFollowedScreenNames().size() : 0;

		log.fine("FollowBack list Size:" + backSize);
		log.fine("Ignore list Size:" + ignoreSize);
		log.fine("Friends: " + followingSize);
		log.fine("Followers: " + followersSize);
		log.fine("Unfollow Back Followers: " + unfollowSize);
		log.fine("Follow Queue: " + followSize);
	}

}
