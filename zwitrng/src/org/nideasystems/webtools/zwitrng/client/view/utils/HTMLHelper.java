package org.nideasystems.webtools.zwitrng.client.view.utils;


import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;


public class HTMLHelper {

	private static long nextId = 0;
	//public static final String USER_NAME_REGEX = "/[@]+[A-Za-z0-9-_]+/";
	public static final String USER_NAME_REGEX = "\\B@[\\w\\d_]{1,15}";
	public static final String DELIMITATORS_REGEX = "/\\W/ ";
	private String itHelper = null;
	private HTMLHelper() {
		
	}
	
	public static HTMLHelper get() {
		return new HTMLHelper();
	}
	
	/**
	 * Get a parsed text
	 * @param text
	 * @return
	 */
	public String getParsedUpdateHtml(String text) {
		String[] twitterScreenNames = parseTwitterScreenNames(text);
		String newString = text;
		
		for (String twitterScreenName : twitterScreenNames) {
			twitterScreenName = twitterScreenName.substring(1);
			
			String twitterId = String.valueOf(++nextId)+twitterScreenName;
			newString = newString.replaceAll(twitterScreenName,
					"<a id=\""+twitterId+"\" href=\"javascript:showUserPanel('"+twitterScreenName+"','"+twitterId+"')\">"+twitterScreenName+"</a>");			
		//onmouseover=\"showUserPanel('"+twitterScreenName+"','"+twitterId+"')\" onmouseout=\"hideUserPanel('"+twitterId+"')\"
		}
		return newString;
	}
	/**
	 * Helper method to remove any trailing trash from the screenname.  
	 * @param tmpString
	 * @return
	 */
	private static String clearUserName(String tmpString) {
		
		String[] tmp = tmpString.split(USER_NAME_REGEX);
		
		String newString = tmpString.trim();
		
		if (tmp!= null) {
			for (String str : tmp ) {
				newString = newString.replace(str, "");
				GWT.log(str, null);
			}
		}
		
		return newString;
	}
	
	/**
	 * Find twitter names inside the text and decorate them with a <a href=""></a> tag
	 * @param text
	 * @return
	 */
	private String[] parseTwitterScreenNames(String text) {

		List<String> array = new ArrayList<String>();
		GWT.log("Testing text: " + text, null);
		int position = 0;

		String tmpString = text.trim();
		boolean process = true;
		String word = null;
		do {

			if (tmpString.contains(" ")) {
				word = tmpString.substring(0, tmpString.indexOf(" "));
				position += word.length() + 1;
				tmpString = text.substring(position).trim();
			} else {
				word = tmpString;
				process = false;
			}

			if (word.contains("@") && processScreenName(word) != null
					&& !array.contains(itHelper)) {
				array.add(itHelper);
			}

		} while (process);

		String[] screenNames = array.toArray(new String[array.size()]);
		return screenNames;
	}
	
	/**
	 * Process a word, with no spaces, and check if it is a username
	 * @param word
	 * @return
	 */
	private String processScreenName(String word) {
		GWT.log("Processing String: " + word, null);
		//ithelper, works like an iterator
		itHelper = null;
		word = HTMLHelper.clearUserName(word);

		if (word.matches(HTMLHelper.USER_NAME_REGEX)) {
			itHelper = word;
			GWT.log("Found: " + word, null);
		} else {
			GWT.log("Nothing found", null);
		}

		
		return itHelper;
	}
}
