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
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RPCService implements IService {

	private TwitterPersonaServiceAsync personaService = null;
	private TwitterServiceAsync twitterService = null;
	private UrlServiceAsync urlService = null;

	public RPCService() {
		personaService = GWT.create(TwitterPersonaService.class);
		twitterService = GWT.create(TwitterService.class);
		urlService = GWT.create(UrlService.class);
	}

	
	
	/*public void getOAuthInfo(TwitterAccountDTO twitterAccount, AsyncCallback<OAuthInfoDTO> callback) throws Exception {
		this.twitterService.getOAuthInfo(twitterAccount, callback);
	}*/
	public void postUpdate(PersonaDTO persona, TwitterUpdateDTO update, AsyncCallback<TwitterUpdateDTO> callback) throws Exception {
		this.twitterService.postUpdate(persona,update,callback);
		
	}
		
	public void getTwitterUpdates(
			PersonaDTO persona, FilterCriteriaDTO filter,
			AsyncCallback<TwitterUpdateDTOList> callback) throws Exception {
		this.twitterService.getTwitterUpdates(persona, filter, callback);
		
	}


	public void createPersona(PersonaDTO string,
			AsyncCallback<PersonaDTO> callBack) throws Exception {
		this.personaService.createPersona(string, callBack);

	}

	public void loadPersonas(final AsyncCallback<PersonaDTOList> callback) throws Exception{
		this.personaService.getPersonas(callback);
	}

	public void deletePersona(final String name, AsyncCallback<String> callBack) throws Exception{

		this.personaService.deletePersona(name, callBack) ;
	}

	public void authenticateUser(PersonaDTO personaDto, String pinCode,
			 AsyncCallback<TwitterAccountDTO> asyncCallback) throws Exception{
		this.twitterService.authenticateUser(personaDto,pinCode, asyncCallback);
		
	}

	public void getUserInfo(PersonaDTO persona, String userIdOrScreenName,
			AsyncCallback<TwitterUserDTO> callback) throws Exception {
		this.twitterService.getUserInfo(persona,userIdOrScreenName, callback);
		
	}
/*
	public void followUser(PersonaDTO persona, boolean follow, Integer id,
			AsyncCallback<Void> asyncCallback) {
		this.twitterService.followUser(persona, follow, id, asyncCallback);
		
	}

	public void blockUser(PersonaDTO persona, boolean block, Integer id,
			AsyncCallback<Void> callback) {
		this.twitterService.blockUser(persona, block, id, callback);
		
	}*/

	public void getUsers(PersonaDTO persona, TwitterUserFilterDTO currentFilter,
			AsyncCallback<TwitterUserDTOList> callback) throws Exception{
		
		this.twitterService.getUsers(persona,currentFilter,callback);
		
	}

	public void shortLinks(List<String> links,AsyncCallback<Map<String,String>> callback) throws Exception{
		this.urlService.shortLinks(links, callback);
	}

	public void getTemplatesList(String name, AsyncCallback<TemplateDTOList> callback) throws Exception{
		this.personaService.getTemplates(name,callback );
		
	}

	public void createTemplate(PersonaDTO model, TemplateDTO template,
			AsyncCallback<TemplateDTO> asyncCallback) throws Exception {
		this.personaService.createTemplate(model, template, asyncCallback);
		
	}

	public void deleteTemplate(PersonaDTO model, TemplateDTO template,
			AsyncCallback<TemplateDTO> asyncCallback) throws Exception{
		this.personaService.deleteTemplate(model, template, asyncCallback);
		
	}

	public void saveTemplate(PersonaDTO model, TemplateDTO template,
			AsyncCallback<TemplateDTO> asyncCallback) throws Exception {
		this.personaService.saveTemplate(model, template, asyncCallback);
		
	}


	public void getTemplateFragmentList(PersonaDTO model,
			AsyncCallback<TemplateListDTOList> asyncCallback) throws Exception {
		this.personaService.getTemplateFragments(model,asyncCallback);
		
	}

	public void createTemplateFragment(PersonaDTO model,
			TemplateListDTO object,
			AsyncCallback<TemplateListDTO> asyncCallback) throws Exception{
		this.personaService.createTemplateFragment(model, object, asyncCallback);
		
	}

	public void saveTemplateFragment(PersonaDTO model,
			TemplateListDTO object,
			AsyncCallback<TemplateListDTO> asyncCallback) throws Exception{
		this.personaService.saveTemplateFragment(model, object, asyncCallback);
		
	}

	public void deleteTemplateFragment(PersonaDTO model,
			TemplateListDTO dataObject,
			AsyncCallback<TemplateListDTO> asyncCallback) throws Exception{
		this.personaService.deleteTemplateFragment(model, dataObject, asyncCallback);
		
	}

	public void getTemplateFragmentsLists(PersonaDTO model, List<String> lists,
			AsyncCallback<Map<String, String>> asyncCallback) throws Exception{
		this.personaService.getTemplateFragmentsLists(model, lists,
				asyncCallback);
		
	}

	public void getCampaigns(PersonaDTO model,
			AsyncCallback<CampaignDTOList> asyncCallback) throws Exception {
		this.personaService.getCampaigns(model,asyncCallback);
		
	}

	public void createCampaign(PersonaDTO model, CampaignDTO object,
			AsyncCallback<CampaignDTO> asyncCallback) throws Exception{
		this.personaService.createCampaign(model, object, asyncCallback);
		
	}

	public void saveCampaign(PersonaDTO model, CampaignDTO object,
			AsyncCallback<CampaignDTO> asyncCallback) throws Exception{
		this.personaService.saveCampaign(model, object, asyncCallback);
		
	}

	public void deleteCampaign(PersonaDTO model, CampaignDTO dataObject,
			AsyncCallback<CampaignDTO> asyncCallback) throws Exception{
		this.personaService.deleteCampaign(model, dataObject, asyncCallback);
		
	}

	public void getFeedSets(PersonaDTO model,
			AsyncCallback<FeedSetDTOList> asyncCallback) throws Exception{
		this.personaService.getFeedSets(model, asyncCallback);
		
	}

	public void createFeedSet(PersonaDTO model, FeedSetDTO object,
			AsyncCallback<FeedSetDTO> asyncCallback) throws Exception{
		this.personaService.createFeedSet(model,object, asyncCallback);
		
	}

	public void saveFeedSet(PersonaDTO model, FeedSetDTO object,
			AsyncCallback<FeedSetDTO> asyncCallback) throws Exception {
		this.personaService.saveFeedSet(model,object, asyncCallback);
		
	}

	public void deleteFeedSet(PersonaDTO model, FeedSetDTO dataObject,
			AsyncCallback<FeedSetDTO> asyncCallback) throws Exception {
		this.personaService.deleteFeedSet(model, dataObject, asyncCallback);
		
	}

	public void synchronizeTwitterAccount(PersonaDTO model,
			AsyncCallback<Void> asyncCallback) throws Exception{
		this.twitterService.synchronizeTwitterAccount(model, asyncCallback);
		
	}

	public void followUser(PersonaDTO currentPersona, TwitterUserDTO user,boolean synch,
			AsyncCallback<Void> asyncCallback) throws Exception{
		this.twitterService.followUser(currentPersona,user, synch,asyncCallback);
		
	}

	public void unfollowUser(PersonaDTO currentPersona, TwitterUserDTO user,
			AsyncCallback<Void> asyncCallback) throws Exception{
		this.twitterService.unfollowUser(currentPersona,user, asyncCallback);
		
	}

	public void blockUser(PersonaDTO currentPersona, TwitterUserDTO user,
			AsyncCallback<Void> asyncCallback) throws Exception{
		this.twitterService.blockUser(currentPersona,user, asyncCallback);
		
	}

	public void unblockUser(PersonaDTO currentPersona, TwitterUserDTO user,
			AsyncCallback<Void> asyncCallback) throws Exception{
		this.twitterService.unblockUser(currentPersona,user, asyncCallback);
		
	}



	public void sendDM(PersonaDTO persona, TwitterUpdateDTO twitterUpdate,
			AsyncCallback<TwitterUpdateDTO> asyncCallback) throws Exception{
		this.twitterService.sendDirectMessage(persona, twitterUpdate, asyncCallback);
		
	}



	public void getPersona(String personaName, 
			AsyncCallback<PersonaDTO> asyncCallback) throws Exception{
		this.personaService.getPersona(personaName, asyncCallback);
		
	}



	public void saveAutoFollowRule(PersonaDTO model, AutoFollowRuleDTO rule,
			AsyncCallback<AutoFollowRuleDTO> asyncCallback) throws Exception {
		this.personaService.saveAutoFollowRule(model, rule, asyncCallback);
		
	}



	public void loadAutoFollowRule(PersonaDTO model,
			AutoFollowTriggerType on_follow_me,
			AsyncCallback<AutoFollowRuleDTO> asyncCallback) throws Exception {
		
		this.personaService.loadAutoFollowRule(model, on_follow_me, asyncCallback);
		
	}



	public void loadTemplateNames(PersonaDTO model,
			AsyncCallback<List<String>> asyncCallback) throws Exception{

		this.personaService.getTemplateNames(model, asyncCallback);
		
	}



	public void setCampaignStatus(PersonaDTO model, String campaignName, CampaignStatus status,
			AsyncCallback<CampaignDTO> asyncCallback) throws Exception{
		this.personaService.setCampaignStatus(model, campaignName,status, asyncCallback);
		
	}



	public void getCampaign(PersonaDTO model, String name,
			AsyncCallback<CampaignDTO> asyncCallback) throws Exception{
		this.personaService.getCampaign(model, name, asyncCallback);

		
	}



	public void buildTweetFromTemplate(PersonaDTO persona, TemplateDTO template,
			List<String> userNames, AsyncCallback<String> asyncCallback) throws Exception{
		this.personaService.buildTweetFromTemplate(persona,template, userNames, asyncCallback);
		
	}



	

}
