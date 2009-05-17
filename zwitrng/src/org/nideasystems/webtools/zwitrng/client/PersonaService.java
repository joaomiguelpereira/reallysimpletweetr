package org.nideasystems.webtools.zwitrng.client;

import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.objects.PersonaObj;
import org.nideasystems.webtools.zwitrng.client.objects.TwitterAccountObj;
import org.nideasystems.webtools.zwitrng.client.utils.JSONUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PersonaService {
	
	private static PersonaService instance = null;
	private TwitterPersonaServiceAsync personaService = null;
	private Map<String, PersonaObj> personas = null;
	
	
	private PersonaService() {
		personas = new HashMap<String, PersonaObj>();
		personaService  = GWT.create(TwitterPersonaService.class);
	}
	
	public static PersonaService getInstance() {
		if ( instance == null ) {
			instance = new PersonaService();
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
			WindowManager.getInstance().addPersonaTab(persona);

		}
		
		
		
	}

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
					WindowManager.getInstance().removePersonaTab(name);
					personas.remove(name);
					
				} else {
					Window.alert(result);
				}
				
			}
			
		});
	}

}
