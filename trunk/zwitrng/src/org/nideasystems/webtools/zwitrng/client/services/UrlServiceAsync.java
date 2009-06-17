package org.nideasystems.webtools.zwitrng.client.services;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UrlServiceAsync {

	void shortLinks(List<String> links, AsyncCallback<Map<String,String>> callback) throws Exception;
}
