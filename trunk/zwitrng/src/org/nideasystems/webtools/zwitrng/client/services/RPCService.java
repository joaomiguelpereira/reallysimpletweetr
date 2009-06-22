package org.nideasystems.webtools.zwitrng.client.services;

import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountListDTO;
import org.nideasystems.webtools.zwitrng.shared.OAuthInfoDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
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

	public void getOAuthInfo(TwitterAccountDTO twitterAccount, AsyncCallback<OAuthInfoDTO> callback) throws Exception {
		this.twitterService.getOAuthInfo(twitterAccount, callback);
	}
	public void postUpdate(TwitterUpdateDTO update, AsyncCallback<TwitterUpdateDTO> callback) throws Exception {
		this.twitterService.postUpdate(update,callback);
		
	}
		
	public void getTwitterUpdates(
			TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter,
			AsyncCallback<TwitterUpdateDTOList> callback) throws Exception {
		this.twitterService.getTwitterUpdates(twitterAccount, filter, callback);
		
	}

	public void search(FilterCriteriaDTO query,
			TwitterAccountDTO twitterAccount,
			AsyncCallback<List<TwitterUpdateDTO>> callback) throws Exception {
		this.twitterService.search(twitterAccount, query, callback);
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

	public void getExtendedUser(TwitterAccountDTO autehnticatedUser, String userIdOrScreenName,
			AsyncCallback<TwitterAccountDTO> callback) throws Exception {
		this.twitterService.getExtendedUserAccount(autehnticatedUser,userIdOrScreenName, callback);
		
	}

	public void followUser(TwitterAccountDTO account, boolean follow, Integer id,
			AsyncCallback<Void> asyncCallback) {
		this.twitterService.followUser(account, follow, id, asyncCallback);
		
	}

	public void blockUser(TwitterAccountDTO account, boolean block, Integer id,
			AsyncCallback<Void> callback) {
		this.twitterService.blockUser(account, block, id, callback);
		
	}

	public void getUsers(TwitterAccountDTO account, TwitterUserFilterDTO currentFilter,
			AsyncCallback<TwitterAccountListDTO> callback) throws Exception{
		this.twitterService.getUsers(account,currentFilter,callback);
		
	}

	public void shortLinks(List<String> links,AsyncCallback<Map<String,String>> callback) throws Exception{
		this.urlService.shortLinks(links, callback);
	}

	public void getTemplatesList(String name, AsyncCallback<TemplateDTOList> callback) throws Exception{
		this.personaService.getTemplates(name,callback );
		
	}

	

}
