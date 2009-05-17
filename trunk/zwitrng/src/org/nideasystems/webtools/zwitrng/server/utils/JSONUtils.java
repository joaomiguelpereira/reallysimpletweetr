package org.nideasystems.webtools.zwitrng.server.utils;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {


	public static JSONObject createError(String string) {
		JSONObject retValue = new JSONObject();;
		try {
			retValue.accumulate("errorMsg", string);
			retValue.accumulate("serverTime", new Date());
			
		} catch (JSONException e) {

		}
		return retValue;
	}
}
