package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterUserDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.server.utils.DtoAssembler;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;
import org.nideasystems.webtools.zwitrng.shared.model.AutoFollowRuleDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;

import sun.security.action.GetBooleanAction;
import sun.text.normalizer.CharTrie.FriendAgent;

public class RulesPojo extends AbstractPojo {

	private final static Logger log = Logger.getLogger(RulesPojo.class
			.getName());

	private static final int FOLLOW_BACK_PER_HOUR = 20;

	public AutoFollowRuleDTO saveAutoFollowRule(PersonaDTO model,
			AutoFollowRuleDTO rule) throws Exception

	{

		// Find the persona
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(model.getName(),
						model.getUserEmail());

		if (persona == null) {
			throw new Exception("Persona not found");
		}
		AutoFollowRuleDO dom = null;
		// Get the rule
		//if (rule.getTriggerType().equals(AutoFollowTriggerType.ON_FOLLOW_ME)) {
			// Get the

			dom = businessHelper.getPersonaDao().getAutoFollowRule(persona,
					rule.getTriggerType());

			if (dom == null) {
				// To allow upgrade
				dom = new AutoFollowRuleDO();

				dom.setCreated(new Date());
				dom.setEnabled(rule.isEnabled());
				dom.setExcludedWordsInNames(rule.getExcludedWordsInNames());
				dom.setMaxRatio(rule.getMaxRatio());
				dom.setMinUpdates(rule.getMinUpdates());
				dom.setModified(new Date());
				dom.setSendDirectMessage(rule.isSendDirectMessage());
				dom.setTriggerType(rule.getTriggerType());
				dom.setTemplateName(rule.getTemplateName());
				
				dom.setSendDirectMessageOnIgnore(rule.isSendDirectMessageOnIgnore());
				dom.setIgnoreTemplate(rule.getIgnoreTemplate());
				dom.setPersona(persona);
				persona.addAutoFollowRule(dom);
			} else {
				dom.setEnabled(rule.isEnabled());
				dom.setExcludedWordsInNames(rule.getExcludedWordsInNames());
				dom.setMaxRatio(rule.getMaxRatio());
				dom.setMinUpdates(rule.getMinUpdates());
				dom.setModified(new Date());
				dom.setSendDirectMessage(rule.isSendDirectMessage());
				dom.setTriggerType(rule.getTriggerType());
				dom.setTemplateName(rule.getTemplateName());
				dom.setSendDirectMessageOnIgnore(rule.isSendDirectMessageOnIgnore());
				dom.setIgnoreTemplate(rule.getIgnoreTemplate());
			}
		//}

		return DtoAssembler.assemble(dom);
	}

	public AutoFollowRuleDTO getAutoFollowRule(PersonaDTO model,
			AutoFollowTriggerType on_follow_me) throws Exception {
		// Find the persona
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(model.getName(),
						model.getUserEmail());

		if (persona == null) {
			throw new Exception("Persona not found");
		}
		AutoFollowRuleDO dom = null;

		dom = businessHelper.getPersonaDao().getAutoFollowRule(persona,
				on_follow_me);

		if (dom == null) {
			// To allow upgrade
			dom = new AutoFollowRuleDO();
			dom.setCreated(new Date());
			dom.setEnabled(false);
			dom.setExcludedWordsInNames(new ArrayList<String>());
			dom.setMaxRatio(140);
			dom.setMinUpdates(150);
			dom.setModified(new Date());
			dom.setSendDirectMessage(false);
			dom.setTriggerType(on_follow_me);
			dom.setTemplateName("");
			dom.setSendDirectMessageOnIgnore(false);
			dom.setIgnoreTemplate("");
			dom.setPersona(persona);
			persona.addAutoFollowRule(dom);
		}
		return DtoAssembler.assemble(dom);
	}

	/*public void executeRule(PersonaDO persona, AutoFollowRuleDO autofollowrule)
			throws Exception {
		// businessHelper.getLogPojo().doLog("Starting running rule: "+autofollowrule.getTriggerType());

		// get the type of rule
		if (autofollowrule.getTriggerType().equals(
				AutoFollowTriggerType.ON_FOLLOW_ME)) {
			// Create a TwitterAccount
			TwitterAccountDTO account = TwitterAccountDAO
					.createAuthorizedAccountDto(persona.getTwitterAccount());
			synchAutoFollowBackQueue(persona, account, autofollowrule);
			followBackUsers(persona, account, autofollowrule,
					FOLLOW_BACK_PER_HOUR);
		}

	}*/

/*	private void followBackUsers(final PersonaDO persona,
			TwitterAccountDTO account, AutoFollowRuleDO rule, int max)
			throws Exception {
		log.severe("Start to follow back users...");
		List<Integer> autoFollowQueue = persona.getTwitterAccount()
				.getAutoFollowBackIdsQueue();
		if (autoFollowQueue != null) {

			for (int i = 0; i < autoFollowQueue.size(); i++) {

				if (i > max - 1) {
					break;
				}

				if (canFollowBack(autoFollowQueue.get(i), persona, rule)) {
					log.fine("---Following user: " + autoFollowQueue.get(i));
				}

			}
		}

	}*/
/*
	private boolean canFollowBack(Integer userId, final PersonaDO persona,
			AutoFollowRuleDO rule) throws Exception {
		log.fine("canFollowBack:" + userId);
		// Create a persona DTO
		TwitterAccountDTO autAccount = TwitterAccountDAO
				.createAuthorizedAccountDto(persona.getTwitterAccount());

		PersonaDTO personaDto = DataUtils.createPersonaDto(persona, autAccount);
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
		float currentRation = twitterUser.getTwitterFriends()
				/ twitterUser.getTwitterFollowers();

		boolean ok = false;
		if (currentRation < (rule.getMaxRatio() / 100)) {
			log.fine("Ratio is Ok");
			ok = true;
		}

		if (twitterUser.getTwitterUpdates() > rule.getMinUpdates()) {
			log.fine("Updates is OK");
			ok = true;
		}

		// Chech Username
		if (rule.getExcludedWordsInNames() != null) {
			ok = true;
			for (String excludeWord : rule.getExcludedWordsInNames()) {
				if (twitterUser.getTwitterScreenName().contains(excludeWord)) {
					ok = false;
					break;
				}
			}

		}
		log.fine("User can be followed back? " + ok);
		return ok;

	}
*/
	/*private void synchAutoFollowBackQueue(PersonaDO persona,
			TwitterAccountDTO twAccount, AutoFollowRuleDO autofollowrule)
			throws Exception {
		// Get the updated list of followers
		//
		int followers[] = businessHelper.getTwitterPojo().getFollowersIds(
				twAccount);
		// clear auto follow
		// persona.getTwitterAccount().setAutoFollowBackIdsQueue(new
		// ArrayList<Integer>());
		// now get the actual following list
		List<Integer> actualFollowersLis = persona.getTwitterAccount()
				.getFollowersIds();

		// Now check if there are any new user following me
		int newFollowersCount = followers.length - actualFollowersLis.size();

		if (newFollowersCount > 0) {
			// get the list of new users
			// is from index 0 to newFollowerCount
			for (int i = 0; i < newFollowersCount; i++) {
				log.fine("------Adding user to FollowingBackQueue :"
						+ followers[followers.length - (1 + i)]);

				if (persona.getTwitterAccount().getAutoFollowBackIdsQueue() == null) {
					ArrayList<Integer> list = new ArrayList<Integer>();
					list
							.add(new Integer(followers[followers.length
									- (1 + i)]));
					persona.getTwitterAccount().setAutoFollowBackIdsQueue(list);
				} else if (!persona.getTwitterAccount()
						.getAutoFollowBackIdsQueue().contains(
								new Integer(followers[followers.length
										- (1 + i)]))) {
					persona.getTwitterAccount().getAutoFollowBackIdsQueue()
							.add(
									new Integer(followers[followers.length
											- (1 + i)]));
				}

			}
		}

	}*/

}
