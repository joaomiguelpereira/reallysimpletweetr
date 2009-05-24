package org.nideasystems.webtools.zwitrng.client.services;

import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.OAuthInfoDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RPCService implements IService {

	private TwitterPersonaServiceAsync personaService = null;
	private TwitterServiceAsync twitterService = null;

	public RPCService() {
		personaService = GWT.create(TwitterPersonaService.class);
		twitterService = GWT.create(TwitterService.class);
	}

	public void getOAuthInfo(TwitterAccountDTO twitterAccount, AsyncCallback<OAuthInfoDTO> callback) throws Exception {
		this.twitterService.getOAuthInfo(twitterAccount, callback);
	}
	public void postUpdate(TwitterAccountDTO twitterAccount, String tweetText,AsyncCallback<TwitterUpdateDTO> callback) throws Exception {
		this.twitterService.postUpdate(twitterAccount, tweetText,callback);
		
	}
		
	public void getTwitterUpdates(
			TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter,
			AsyncCallback<List<TwitterUpdateDTO>> callback) throws Exception {
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

	public void loadPersonas(final AsyncCallback<List<PersonaDTO>> callback) throws Exception{
		this.personaService.getPersonas(callback);
	}

	public void deletePersona(final String name, AsyncCallback<String> callBack) throws Exception{

		this.personaService.deletePersona(name, callBack) ;
	}

	public void authenticateUser(PersonaDTO personaDto,
			 AsyncCallback<TwitterAccountDTO> asyncCallback) throws Exception{
		this.twitterService.authenticateUser(personaDto,asyncCallback);
		
	}

	

}
