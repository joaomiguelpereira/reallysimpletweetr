package org.nideasystems.webtools.zwitrng.server.bitly;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import twitter4j.TwitterException;
import twitter4j.http.HttpClient;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

public class BitLyServiceAdapter {
	private static final String bitUser = "jpereira";
	private static final String bitPassword = "";
	private static final String bitKey = "R_a2a4d7eac9641deaad17010bbd27a815";
	private static final Logger log = Logger
			.getLogger(BitLyServiceAdapter.class.getName());

	private BitLyServiceAdapter() {

	}

	public static BitLyServiceAdapter get() {
		return new BitLyServiceAdapter();
	}

	public Map<String, String> shortLinks(List<String> links) throws Exception {

		Map<String, String> retMap = new HashMap<String, String>();
		HttpClient httpClient = new HttpClient(bitUser, bitPassword);

		for (String link : links) {
			// ignore already shortned links
			if (!link.startsWith("http://bit.ly")) {
				String encodedURL = URLEncoder.encode(link, "UTF-8");
				String url = "http://api.bit.ly/shorten?version=2.0.1&history=1&longUrl="
						+ encodedURL
						+ "&login="
						+ bitUser
						+ "&apiKey="
						+ bitKey;
				String shortedUrl = null;
				log.fine("Shortening URL: " + link);
				try {

					JSONObject jObj = httpClient.get(url).asJSONObject();
					log.fine("returned: " + jObj.toString());

					// String decodedLink = URLDecoder.decode(link, "UTF-8");

					// log.fine("Decoded Link: "+decodedLink);
					shortedUrl = jObj.getJSONObject("results").getJSONObject(
							link).getString("shortUrl");

				} catch (JSONException e) {
					log.severe("Error while getting the short URL");
					e.printStackTrace();
					throw new Exception(e);
				} catch (TwitterException e) {
					log.severe("Error while getting the short URL");
					e.printStackTrace();
					throw new Exception(e);
				}

				if (shortedUrl != null) {
					log.fine("returned URL: " + shortedUrl);
					retMap.put(link, shortedUrl);
				}

			}

		}

		/*
		 * String url = new HttpClient(bitUser,
		 * bitPass).get("http://api.bit.ly/expand?version=2.0.1&apiKey=
		 * " + apiKey &hash=" + hash, true).asJSONObject() .getJSONObject
		 * ("results").getJSONObject(hash).getString("longUrl");
		 * System.out.println(url);
		 */
		return retMap;
	}

	public String shortLink(String link) {
		HttpClient httpClient = new HttpClient(bitUser, bitPassword);
		String shortedUrl = link;

		if (!link.startsWith("http://bit.ly")) {
			String encodedURL = null;

			try {
				encodedURL = URLEncoder.encode(link, "UTF-8");
			} catch (UnsupportedEncodingException e1) {

				e1.printStackTrace();
			}

			if (encodedURL != null) {
				String url = "http://api.bit.ly/shorten?version=2.0.1&history=1&longUrl="
						+ encodedURL
						+ "&login="
						+ bitUser
						+ "&apiKey="
						+ bitKey;

				try {

					JSONObject jObj = httpClient.get(url).asJSONObject();
					shortedUrl = jObj.getJSONObject("results").getJSONObject(
							link).getString("shortUrl");

				} catch (JSONException e) {
					log.severe("Error while getting the short URL");
					e.printStackTrace();
					// throw new Exception(e);
				} catch (TwitterException e) {
					log.severe("Error while getting the short URL");
					e.printStackTrace();
					// throw new Exception(e);
				}

			}
		}
		return shortedUrl;

	}

}
