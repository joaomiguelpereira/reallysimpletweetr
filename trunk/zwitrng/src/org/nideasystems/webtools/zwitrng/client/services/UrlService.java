package org.nideasystems.webtools.zwitrng.client.services;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("urlService")
public interface UrlService extends RemoteService {

	Map<String,String> shortLinks(List<String> links) throws Exception;
}
