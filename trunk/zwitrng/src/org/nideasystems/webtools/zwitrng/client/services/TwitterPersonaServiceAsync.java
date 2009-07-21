package org.nideasystems.webtools.zwitrng.client.services;


import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;
import org.nideasystems.webtools.zwitrng.shared.model.AutoFollowRuleDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateListDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateListDTOList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterPersonaServiceAsync {
	void createPersona(PersonaDTO persona, AsyncCallback<PersonaDTO> callback) throws Exception;
	void deletePersona(String input, AsyncCallback<String> callback) throws Exception;
	void getPersonas(AsyncCallback<PersonaDTOList> callback) throws Exception;
	void getPersonaFilters(String personaKey,
			AsyncCallback<List<FilterCriteriaDTO>> asyncCallback) throws Exception;
	void getTemplates(String name, AsyncCallback<TemplateDTOList> callback) throws Exception;
	void createTemplate(PersonaDTO model, TemplateDTO template,
			AsyncCallback<TemplateDTO> asyncCallback) throws Exception;
	void deleteTemplate(PersonaDTO model, TemplateDTO template,
			AsyncCallback<TemplateDTO> asyncCallback) throws Exception;
	void saveTemplate(PersonaDTO model, TemplateDTO template,
			AsyncCallback<TemplateDTO> asyncCallback);
	void getTemplateFragments(PersonaDTO personaDto,
			AsyncCallback<TemplateListDTOList> asyncCallback) throws Exception;
	void createTemplateFragment(PersonaDTO model, TemplateListDTO object,
			AsyncCallback<TemplateListDTO> asyncCallback) throws Exception;
	void saveTemplateFragment(PersonaDTO model, TemplateListDTO object,
			AsyncCallback<TemplateListDTO> asyncCallback);
	void deleteTemplateFragment(PersonaDTO model,
			TemplateListDTO dataObject,
			AsyncCallback<TemplateListDTO> asyncCallback) throws Exception;
	void getTemplateFragmentsLists(PersonaDTO model, List<String> lists,
			AsyncCallback<Map<String, String>> asyncCallback) throws Exception;
	void getCampaigns(PersonaDTO model,
			AsyncCallback<CampaignDTOList> asyncCallback) throws Exception;
	void createCampaign(PersonaDTO model, CampaignDTO object,
			AsyncCallback<CampaignDTO> asyncCallback) throws Exception;
	void saveCampaign(PersonaDTO model, CampaignDTO object,
			AsyncCallback<CampaignDTO> asyncCallback) throws Exception;
	void deleteCampaign(PersonaDTO model, CampaignDTO dataObject,
			AsyncCallback<CampaignDTO> asyncCallback) throws Exception;
	void getFeedSets(PersonaDTO model,
			AsyncCallback<FeedSetDTOList> asyncCallback) throws Exception;
	void createFeedSet(PersonaDTO model, FeedSetDTO object,
			AsyncCallback<FeedSetDTO> asyncCallback) throws Exception;
	void saveFeedSet(PersonaDTO model, FeedSetDTO object,
			AsyncCallback<FeedSetDTO> asyncCallback) throws Exception;

	void deleteFeedSet(PersonaDTO model, FeedSetDTO dataObject,
			AsyncCallback<FeedSetDTO> asyncCallback) throws Exception;
	void getPersona(String personaName,
			AsyncCallback<PersonaDTO> asyncCallback) throws Exception;
	void saveAutoFollowRule(PersonaDTO model, AutoFollowRuleDTO rule,
			AsyncCallback<AutoFollowRuleDTO> asyncCallback) throws Exception;
	void loadAutoFollowRule(PersonaDTO model,
			AutoFollowTriggerType on_follow_me,
			AsyncCallback<AutoFollowRuleDTO> asyncCallback) throws Exception;
	void getTemplateNames(PersonaDTO model,
			AsyncCallback<List<String>> asyncCallback) throws Exception;
	void setCampaignStatus(PersonaDTO model, String campaignNameId, CampaignStatus status,
			AsyncCallback<CampaignDTO> asyncCallback) throws Exception;
	void getCampaign(PersonaDTO model, String name,
			AsyncCallback<CampaignDTO> asyncCallback) throws Exception;
	void buildTweetFromTemplate(PersonaDTO persona, TemplateDTO template,
			List<String> userNames, AsyncCallback<String> asyncCallback) throws Exception;
	

	

}
