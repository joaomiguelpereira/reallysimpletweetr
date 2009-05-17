package org.nideasystems.webtools.zwitrng.client.utils;

import java.util.Date;

import org.nideasystems.webtools.zwitrng.client.objects.PersonaObj;
import org.nideasystems.webtools.zwitrng.client.objects.TwitterAccountObj;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class JSONUtils {

	public static JSONObject createNewPersonaJSonRequest(String personaName,
			String twUserName, String twPassword) {
		
		PersonaObj personaObj = new PersonaObj();
		personaObj.setCreationDate(new Date());
		personaObj.setName(personaName);
		
		TwitterAccountObj twitterAccount = new TwitterAccountObj();
		twitterAccount.setTwitterPassword(twPassword);
		twitterAccount.setTwitterScreenName(twUserName);
		personaObj.setTwitterAccount(twitterAccount);
		
		return personaObj.toJson();
		//Create an JSonArray persona: [
		
	}

	public static String isError(String result) {
		JSONValue obj = JSONParser.parse(result);
		String returnString =null;
		if ( obj.isObject() != null  ) {
			JSONValue val = obj.isObject().get("errorMsg");
			if (val != null ) {
				returnString = val.isString().stringValue();
			}
		}
		return returnString;

	}
	
}
