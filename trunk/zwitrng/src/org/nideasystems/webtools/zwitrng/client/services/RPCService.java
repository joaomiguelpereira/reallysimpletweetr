package org.nideasystems.webtools.zwitrng.client.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.DataLoadedCallBack;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonasCompositeController;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.StatusDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RPCService implements IService {

	
	private TwitterPersonaServiceAsync personaService = null;
	private TwitterServiceAsync twitterService = null;
	private Map<String, PersonaDTO> personas = null;

	public RPCService() {
		personas = new HashMap<String, PersonaDTO>();
		personaService = GWT.create(TwitterPersonaService.class);
		twitterService = GWT.create(TwitterService.class);
	}


	public void search(FilterCriteriaDTO query, TwitterAccountDTO twitterAccount,AsyncCallback<List<StatusDTO>> callback) {
		this.twitterService.search(twitterAccount, query, callback);	
	}
	
	public void createPersona(PersonaDTO string,AsyncCallback<PersonaDTO> callBack) throws Exception {
		this.personaService.createPersona(string,callBack);
		/*
				new AsyncCallback<PersonaDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						handleRPCServiceError(caught);

					}

					@Override
					public void onSuccess(PersonaDTO result) {

						handleCreatePersonaResult(result);

					}

				});
*/
	}

	private void handleRPCServiceError(Throwable caught) {
		//PersonasCompositeController.getInstance().addError(caught.getMessage());

	}

	private void handleCreatePersonaResult(PersonaDTO persona) {
//
//		// Add the persona to the personas list
//		this.personas.put(persona.getName(), persona);
//		// Update window
//		PersonasCompositeController.getInstance().addPersonaTab(persona);
//		// WindowManager.getInstance().addPersonaTab(persona);

	}

	public void loadPersonas(final AsyncCallback<List<PersonaDTO>> callback) {
		this.personaService.getPersonas(callback);
	}

	public void deletePersona(final String name,AsyncCallback<String> callBack) {

	this.personaService.deletePersona(name,callBack);/* new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());

			}

			@Override
			public void onSuccess(String result) {
				Window.alert(result);

				if (result.startsWith("ok")) {
					PersonaDTO personaObj = personas.get(name);
					personas.remove(personaObj);
					PersonasCompositeController.getInstance().removePersonaTab(name);

					// WindowManager.getInstance().removePersonaTab(name);
					personas.remove(name);

				} else {
					Window.alert(result);
				}

			}

		});*/
	}

	/**
	 * Craete a copy of the Actual personas
	 * 
	 * @return
	 */
	public Map<String, PersonaDTO> getPersonas() {
		return new HashMap<String, PersonaDTO>(this.personas);

	}

	public void getPersonaFilters(String key, final AsyncCallback callBack) {
		
		this.personaService.getPersonaFilters(key,callBack);
		
		
	}

}
