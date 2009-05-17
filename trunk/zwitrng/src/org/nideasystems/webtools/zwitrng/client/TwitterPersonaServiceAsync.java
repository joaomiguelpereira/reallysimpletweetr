package org.nideasystems.webtools.zwitrng.client;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterPersonaServiceAsync {
	void createPersona(String input, AsyncCallback<String> callback);
	void deletePersona(String input, AsyncCallback<String> callback);
	void getPesonas(AsyncCallback<String> callback);
	
}
