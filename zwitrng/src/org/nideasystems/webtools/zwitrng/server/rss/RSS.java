package org.nideasystems.webtools.zwitrng.server.rss;

import java.io.IOException;
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

import org.nideasystems.webtools.zwitrng.server.bitly.BitLyServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.XMLUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

	/*private String getCharacterDataFromElement(Element e) {
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
	}*/

	public List<RSSItem> read(String randUrl) throws Exception{

		List<RSSItem> retList = new ArrayList<RSSItem>();
		try {
			URL url = new URL(randUrl);
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			Document doc = builder.parse(url.openStream());
			
			NodeList nList = doc.getElementsByTagName("item");
			
			for (int i = 0; i < nList.getLength(); i++) {
				Element element = (Element) nList.item(i);
				RSSItem rssItem = new RSSItem();
				
				rssItem.setTitle(XMLUtils.getElementValue(element, "title"));
				rssItem.setLink(XMLUtils.getElementValue(element, "link"));
				rssItem.setPubDate(XMLUtils.getElementValue(element, "pubDate"));
				retList.add(rssItem);
				
			}
		} catch (MalformedURLException e) {
			log.severe("MalformedURLException: "+e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (ParserConfigurationException e) {
			log.severe("ParserConfigurationException: "+e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (SAXException e) {
			log.severe("SAXException: "+e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			log.severe("IOException: "+e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		
		return retList;
	}

	public RSSItem getNextRssItem(String randomUrl, boolean random) throws Exception{
		log.fine("Getting next RSS Item...");
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
			log.fine("Getting Items from cache");
			items =(List<RSSItem>)cache.get(randomUrl);
		}

		//miss
		if ( items== null) {
			log.fine("Could not get any items from cache, getting new ones from source");
			items = read(randomUrl);
			
			if ( cache!=null) {
				log.fine("Adding RSS URL to cache");
				//cache.put(randomUrl, items);
				
				//Add the URL to the list of updatable URLS
				List<String> rssUrls = (List<String>)cache.get("RSS_URL");
				if ( rssUrls == null ) {
					rssUrls = new ArrayList<String>();
				}
				rssUrls.add(randomUrl);
				cache.put("RSS_URL", rssUrls);
			}
		}
				
		if (items==null) {
			throw new Exception("Could not get a ItemsList for the URL");
		}
		log.fine("Items are in memory now");
		//Check if this title is in the cache, and has been sent alread
		log.fine("Items Size is :"+items.size());
		//Ordered by the newsest
		if ( items.size()>0) {
			log.fine("Items Size is greater than 0");
			int index = 0;
			if ( random ) {
				double rand = Math.random();
				index = (int) Math.round(rand * (items.size() - 1));

	
			}
			
			item = items.get(index);
			log.fine("RSS Item:"+item.getTitle());
			
			items.remove(item);
			log.fine("Items Size now is :"+items.size());
			if (cache!=null) {
				cache.put(randomUrl, items);
			}	
		}
		//log.fine("Returning RSS Item:"+item.getTitle());
		return item;
	}

	public String createStatusFromRssItem(RSSItem item, boolean addLink) {
		String str = item.getTitle();
		
		if ( addLink ) {
			if (str.length()>115) {
				str = str.substring(0,115)+"...";
			}
			
			String link = BitLyServiceAdapter.get().shortLink(item.getLink());
			str = str+" "+link;
			
		}
		return str;
	}

}
