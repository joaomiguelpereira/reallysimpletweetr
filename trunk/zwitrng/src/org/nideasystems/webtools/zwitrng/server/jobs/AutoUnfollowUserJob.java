package org.nideasystems.webtools.zwitrng.server.jobs;


import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.rules.AutoUnFollowBackOnFollowMeRuleRunner;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;

public class AutoUnfollowUserJob extends AbstractJob {

	@Override
	public void execute() throws Exception {
		log.fine("> > Persona: "+persona.getName()+" Executing Job: " + this.getClass().getSimpleName());
		
		int backSize = persona.getTwitterAccount().getAutoFollowBackIdsQueue() != null ? persona
				.getTwitterAccount().getAutoFollowBackIdsQueue().size()
				: 0;
		int ignoreSize = persona.getTwitterAccount().getIgnoreUsersIds() != null ? persona
				.getTwitterAccount().getIgnoreUsersIds().size()
				: 0;
		int unfollowSize = persona.getTwitterAccount()
				.getAutoUnFollowBackIdsQueue() != null ? persona
				.getTwitterAccount().getAutoUnFollowBackIdsQueue().size() : 0;

		log.fine("> > Persona: "+persona.getName()+" FollowBack list Size:" + backSize);
		log.fine("> > Persona: "+persona.getName()+" Ignore list Size:" + ignoreSize);
		
		log.fine("> > Persona: "+persona.getName()+" Unfollow Back Followers: " + unfollowSize);


		AutoFollowRuleDO rule = getBusinessHelper().getPersonaDao()
				.getAutoFollowRule(persona, AutoFollowTriggerType.UNFOLLOW);

		AutoUnFollowBackOnFollowMeRuleRunner ruleEx = new AutoUnFollowBackOnFollowMeRuleRunner(
				persona, rule);
		log.fine("> > Is Rule null?: "+rule!=null?"NO":"YES");
		log.fine("> > Is Enabled: "+(rule!=null?rule.isEnabled():"null rule"));
		log.fine("> > Unfollow Ids Size: "+unfollowSize);
		
		ruleEx.setBusinessHelper(getBusinessHelper());
		if (rule != null
				&& rule.isEnabled()
				&& persona.getTwitterAccount().getAutoUnFollowBackIdsQueue() != null) {
			
			ruleEx.execute();
		}
		backSize = persona.getTwitterAccount().getAutoFollowBackIdsQueue() != null ? persona
				.getTwitterAccount().getAutoFollowBackIdsQueue().size()
				: 0;
		ignoreSize = persona.getTwitterAccount().getIgnoreUsersIds() != null ? persona
				.getTwitterAccount().getIgnoreUsersIds().size()
				: 0;
		unfollowSize = persona.getTwitterAccount()
				.getAutoUnFollowBackIdsQueue() != null ? persona
				.getTwitterAccount().getAutoUnFollowBackIdsQueue().size() : 0;
		log.fine("> > Persona: "+persona.getName()+" FollowBack list Size:" + backSize);
		log.fine("> > Persona: "+persona.getName()+" Ignore list Size:" + ignoreSize);
		
		log.fine("> > Persona: "+persona.getName()+" Unfollow Back Followers: " + unfollowSize);

	}

}
