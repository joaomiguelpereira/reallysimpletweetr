package org.nideasystems.webtools.zwitrng.server.rss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RSS {

	private static final Logger log = Logger.getLogger(RSS.class.getName());

	private static ThreadLocal<RSS> instance = new ThreadLocal<RSS>() {

		@Override
		protected RSS initialValue() {

			return new RSS();
		}

	};

	private RSS() {

	}

	public static RSS get() {
		return instance.get();
	}

	private String getCharacterDataFromElement(Element e) {
		try {
			Node child = e.getFirstChild();
			if (child instanceof CharacterData) {
				CharacterData cd = (CharacterData) child;
				return cd.getData();
			}
		} catch (Exception ex) {

		}
		return "";
	} // private String getCharacterDataFromElement

	protected float getFloat(String value) {
		if (value != null && !value.equals("")) {
			return Float.parseFloat(value);
		}
		return 0;
	}

	protected String getElementValue(Element parent, String label) {
		return getCharacterDataFromElement((Element) parent
				.getElementsByTagName(label).item(0));
	}

	public List<RSSItem> read(String randUrl) {

		List<RSSItem> retList = new ArrayList<RSSItem>();
		try {
			URL url = new URL(randUrl);
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(url.openStream());
			NodeList nList = doc.getElementsByTagName("item");
			
			for (int i = 0; i < nList.getLength(); i++) {
				Element element = (Element) nList.item(i);
				RSSItem rssItem = new RSSItem();
				
				rssItem.setTitle(getElementValue(element, "title"));
				rssItem.setLink(getElementValue(element, "link"));
				rssItem.setPubDate(getElementValue(element, "pubDate"));
				retList.add(rssItem);
				
				

			}
		} catch (MalformedURLException e) {
			log.severe("MalformedURLException: "+e.getMessage());
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			log.severe("ParserConfigurationException: "+e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			log.severe("SAXException: "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("IOException: "+e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return retList;
	}

	public RSSItem getRandomRssItem(String randomUrl) {
		
		RSSItem item = null;
		List<RSSItem> items = null;
		//Try the cache
		Cache cache = null;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(
					Collections.emptyMap());
		} catch (CacheException e1) {
			// TODO Auto-generated catch block
			log.warning("Could not get a Cache instance " + cache);
			e1.printStackTrace();
		}
		if (cache != null) {
			//Possible bug in production
			//cache.put(campaign.getKey(),null);
			items =(List<RSSItem>)cache.get(randomUrl);
		}

		if ( items== null) {
			items = read(randomUrl);
			if ( cache!=null) {
				cache.put(randomUrl, items);
			}
		}
				
		if ( items.size()>0) {
			double rand = Math.random();
			int index = (int) Math.round(rand * (items.size() - 1));
			item = items.get(index);
				
		}
		return item;
	}

}
