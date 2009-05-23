package org.nideasystems.webtools.zwitrng.client.services;

import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RPCService implements IService {

	private TwitterPersonaServiceAsync personaService = null;
	private TwitterServiceAsync twitterService = null;

	public RPCService() {
		personaService = GWT.create(TwitterPersonaService.class);
		twitterService = GWT.create(TwitterService.class);
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

}
