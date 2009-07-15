package org.nideasystems.webtools.zwitrng.server.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.shared.model.AutoFollowRuleDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class DtoAssembler {

	public static PersonaDTO assemble(PersonaDO personaDo) {

		PersonaDTO returnPersona = new PersonaDTO();

		returnPersona.setCreated(personaDo.getCreated());
		returnPersona.setModified(personaDo.getModified());
		returnPersona.setName(personaDo.getName());
		returnPersona.setUserEmail(personaDo.getUserEmail());
		returnPersona.setTwitterAccount(DtoAssembler.assemble(personaDo
				.getTwitterAccount()));

		return returnPersona;
	}

	public static TwitterAccountDTO assemble(TwitterAccountDO twitterAccount) {
		// TODO Auto-generated method stub
		TwitterAccountDTO dto = new TwitterAccountDTO();
		dto.setOAuthLoginUrl(twitterAccount.getOAuthLoginUrl());
		dto.setOAuthToken(twitterAccount.getOAuthToken());
		dto.setOAuthTokenSecret(twitterAccount.getOAuthTokenSecret());
		dto.setId(twitterAccount.getId());

		return dto;

	}

	public static AutoFollowRuleDTO assemble(AutoFollowRuleDO inDom) {
		AutoFollowRuleDTO outRule = new AutoFollowRuleDTO();

		outRule.setEnabled(inDom.isEnabled());
		List<String> excludedWords = new ArrayList<String>();

		if (inDom.getExcludedWordsInNames() != null) {
			for (String str : inDom.getExcludedWordsInNames()) {
				excludedWords.add(str);
			}
		}
		outRule.setExcludedWordsInNames(excludedWords);

		outRule.setMaxRatio(inDom.getMaxRatio());
		outRule.setMinUpdates(inDom.getMinUpdates());
		outRule.setSendDirectMessage(inDom.isSendDirectMessage());
		outRule.setTriggerType(inDom.getTriggerType());
		outRule.setTemplateName(inDom.getTemplateName());
		return outRule;

	}

}
