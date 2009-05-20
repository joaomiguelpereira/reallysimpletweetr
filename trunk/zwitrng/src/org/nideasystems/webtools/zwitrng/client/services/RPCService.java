package org.nideasystems.webtools.zwitrng.client.services;


import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.WindowManager;
import org.nideasystems.webtools.zwitrng.client.controller.DataLoadedCallBack;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.utils.JSONUtils;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaObj;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountObj;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RPCService {
	
	private static RPCService instance = null;
	private TwitterPersonaServiceAsync personaService = null;
	private Map<String, PersonaObj> personas = null;
	
	
	private RPCService() {
		personas = new HashMap<String, PersonaObj>();
		personaService  = GWT.create(TwitterPersonaService.class);
	}
	
	public static RPCService getInstance() {
		if ( instance == null ) {
			instance = new RPCService();
		}
		return instance;
	}

	public void createPersona(String string) {
		
		this.personaService.createPersona(string, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				handleCreatePersonaResult(result);
				
			}
			
		});
		
	}
	
	private void handleCreatePersonaResult(String result) {
		// Parse to JSON
		
		String isError = JSONUtils.isError(result);
		
		if (isError != null) {

			Window.alert(isError);
		} else {
			JSONValue personaValue = JSONParser.parse(result);
			PersonaObj persona = PersonaObj.fromJson(personaValue);
			
			this.personas.put(persona.getName(), persona);
			MainController.getInstance().addPersonaTab(persona);
			//WindowManager.getInstance().addPersonaTab(persona);

		}
		
		
		
	}
	
	public void loadPersonas(final DataLoadedCallBack callback) {
		this.personaService.getPesonas(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				//Window.alert(caught.getMessage());
				callback.onError(caught.getMessage());
				
			}
			@Override
			public void onSuccess(String result) {
				
				//Create tabbed panel
				JSONValue jsonValue= JSONParser.parse(result);
				JSONArray jsonArray = jsonValue.isArray();
				
				for (int i=0; i< jsonArray.size();i++) {
					JSONObject personaJson = jsonArray.get(i).isObject();
					PersonaObj personaObj = new PersonaObj();
					personaObj.setName(personaJson.get("personaName").isString().stringValue());
					
					if ( personaJson.get("twitterAccount") != null ) {
						
						TwitterAccountObj twitterObj = new TwitterAccountObj();
						JSONObject twitterJsonObj = personaJson.get("twitterAccount").isObject();
						twitterObj.setTwitterDescription(twitterJsonObj.get("twitterDescription").isString().stringValue());
						twitterObj.setTwitterImageUrl(twitterJsonObj.get("twitterImageUrl").isString().stringValue());
						twitterObj.setTwitterScreenName(twitterJsonObj.get("twitterScreenName").isString().stringValue());
						twitterObj.setTwitterUserName(twitterJsonObj.get("twitterUserName").isString().stringValue());
						personaObj.setTwitterAccount(twitterObj);
					}
					personas.put(personaObj.getName(), personaObj);
				}
				//Update Contoller and fire envent 
				
				//Update
				callback.onSuccess();
				//WindowManager.getInstance().createPersonasTab(personas);
			}
			
		});
	}

	/**
	 * @deprecated
	 */
	public void loadPersonas() {
		
		this.personaService.getPesonas(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				
			}
			@Override
			public void onSuccess(String result) {
				
				//Create tabbed panel
				JSONValue jsonValue= JSONParser.parse(result);
				JSONArray jsonArray = jsonValue.isArray();
				
				for (int i=0; i< jsonArray.size();i++) {
					JSONObject personaJson = jsonArray.get(i).isObject();
					PersonaObj personaObj = new PersonaObj();
					personaObj.setName(personaJson.get("personaName").isString().stringValue());
					
					if ( personaJson.get("twitterAccount") != null ) {
						
						TwitterAccountObj twitterObj = new TwitterAccountObj();
						JSONObject twitterJsonObj = personaJson.get("twitterAccount").isObject();
						twitterObj.setTwitterDescription(twitterJsonObj.get("twitterDescription").isString().stringValue());
						twitterObj.setTwitterImageUrl(twitterJsonObj.get("twitterImageUrl").isString().stringValue());
						twitterObj.setTwitterScreenName(twitterJsonObj.get("twitterScreenName").isString().stringValue());
						twitterObj.setTwitterUserName(twitterJsonObj.get("twitterUserName").isString().stringValue());
						personaObj.setTwitterAccount(twitterObj);
					}
					personas.put(personaObj.getName(), personaObj);
				}
				//Update Contoller and fire envent 
				
				//Update
				WindowManager.getInstance().createPersonasTab(personas);
			}
			
		});
		
	}

	public void deletePersona(final String name) {
		
		this.personaService.deletePersona(name, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				
			}

			@Override
			public void onSuccess(String result) {
				Window.alert(result);
				
				if (result.startsWith("ok")) {
					PersonaObj personaObj = personas.get(name);
					personas.remove(personaObj);
					MainController.getInstance().removePersonaTab(name);
					
					//WindowManager.getInstance().removePersonaTab(name);
					personas.remove(name);
					
				} else {
					Window.alert(result);
				}
				
			}
			
		});
	}

	/**
	 * Craete a copy of the Actual personas
	 * @return
	 */
	public Map<String, PersonaObj> getPersonas() {
		return new HashMap<String, PersonaObj>(this.personas);
		
	}

}
