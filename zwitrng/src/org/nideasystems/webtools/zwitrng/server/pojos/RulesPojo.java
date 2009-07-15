package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.ArrayList;
import java.util.Date;

import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.utils.DtoAssembler;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;
import org.nideasystems.webtools.zwitrng.shared.model.AutoFollowRuleDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;


public class RulesPojo extends AbstractPojo {

	public AutoFollowRuleDTO saveAutoFollowRule(PersonaDTO model,
			AutoFollowRuleDTO rule) throws Exception
			{
		
		//Find the persona
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(model.getName(), model.getUserEmail());
		
		if (persona==null) {
			throw new Exception("Persona not found");
		}
		AutoFollowRuleDO dom = null;
		//Get the rule
		if ( rule.getTriggerType().equals(AutoFollowTriggerType.ON_FOLLOW_ME)) {
			//Get the 
			
			dom = businessHelper.getPersonaDao().getAutoFollowRule(persona,AutoFollowTriggerType.ON_FOLLOW_ME);
			
			if (dom==null) {
				//To allow upgrade
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
			}
		}
		
		return DtoAssembler.assemble(dom);
	}

	public AutoFollowRuleDTO getAutoFollowRule(PersonaDTO model,
			AutoFollowTriggerType on_follow_me) throws Exception{
		//Find the persona
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(model.getName(), model.getUserEmail());
		
		if (persona==null) {
			throw new Exception("Persona not found");
		}
		AutoFollowRuleDO dom = null;
		
		dom = businessHelper.getPersonaDao().getAutoFollowRule(persona,AutoFollowTriggerType.ON_FOLLOW_ME);
		
		if (dom==null) {
			//To allow upgrade
			dom = new AutoFollowRuleDO();
			dom.setCreated(new Date());
			dom.setEnabled(false);
			dom.setExcludedWordsInNames(new ArrayList<String>());
			dom.setMaxRatio(140);
			dom.setMinUpdates(150);
			dom.setModified(new Date());
			dom.setSendDirectMessage(false);
			dom.setTriggerType(AutoFollowTriggerType.ON_FOLLOW_ME);
			dom.setTemplateName("");
			
			dom.setPersona(persona);
			persona.addAutoFollowRule(dom);
		}
		return DtoAssembler.assemble(dom);
	}

}
