package org.nideasystems.webtools.zwitrng.server;


import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.services.UrlService;
import org.nideasystems.webtools.zwitrng.server.bitly.BitLyServiceAdapter;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UrlServiceImpl extends RemoteServiceServlet implements UrlService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8862991875338024516L;

	@Override
	public Map<String, String> shortLinks(List<String> links) throws Exception {

		//Map<String, String> linksMap = new HashMap<String, String>();
		
		return BitLyServiceAdapter.get().shortLinks(links);
		
		//return linksMap;
	}

}
