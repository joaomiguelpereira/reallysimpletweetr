package org.nideasystems.webtools.zwitrng.client.services;
/*
import org.restlet.gwt.Callback;
import org.restlet.gwt.Client;
import org.restlet.gwt.data.Protocol;
import org.restlet.gwt.data.Reference;
import org.restlet.gwt.data.Request;
import org.restlet.gwt.data.Response;*/

import com.google.gwt.user.client.Window;

public class BasicAutehnticationService implements IService {

	public String getAuthenticationUrl() {
		/*Client restClient = new Client(Protocol.HTTP);
		Reference ref = new Reference();
		restClient.get(resourceRef, callback)
		resource.setReferrerRef("http://www.mysite.org");*/ 
		
		return null;
	}

	public void doAuthentication(String loginUrl) {
	/*	//must open a new window
		
		Client restClient = new Client(Protocol.HTTP);
		Reference ref = new Reference(loginUrl);
		
		restClient.get(ref, new Callback() {

			@Override
			public void onEvent(Request request, Response response) {
				Window.alert(response.getEntity().getText());
				
			}
			
		});
		 */
		
	}
}
