package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.nideasystems.webtools.zwitrng.server.servlets.AbstractHttpServlet;

public class RunRefreshRSSItems extends AbstractHttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5975194926127169075L;

	
	private static boolean TESTING = true;
	
	
	
	private static final Logger log = Logger
	.getLogger(RunRefreshRSSItems.class.getName());
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("=============Running Job: Regreshing RSS Items=============");
		// Check headers

		if (!TESTING) {
			if (req.getHeader("X-AppEngine-Cron") == null
					|| !req.getHeader("X-AppEngine-Cron").equals("true")) {
				log.severe("Job called outside of a cron context");
				throw new ServletException(
						"Job called outside of a cron context");

			}
		}
		
		
		
		Cache cache = null;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(
					Collections.emptyMap());
		} catch (CacheException e1) {
			// TODO Auto-generated catch block
			log.warning("Could not get a Cache instance " + cache);
			e1.printStackTrace();
			throw new ServletException(e1);
			
			
		}
		
		if (cache != null) {
			log.fine("Getting RSS URLS from cache");
			List<String> rssUrls =(List<String>)cache.get("RSS_URL");
			
			if ( rssUrls != null ) {
				for ( String url:rssUrls) {
					log.fine("Clearing URL: "+url);
					cache.remove(url);
				}
			}
		}

		resp.setContentType("text/html");
		resp.getWriter().println("200 OK");
		
	}
	

}
