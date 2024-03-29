package org.nideasystems.webtools.zwitrng.client.view.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.core.client.GWT;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.TextArea;

public class HTMLHelper {

	private static long nextId = 0;
	// public static final String USER_NAME_REGEX = "/[@]+[A-Za-z0-9-_]+/";
	public static final String USER_NAME_REGEX = "\\B@[\\w\\d_]{1,15}";
	public static final String DELIMITATORS_REGEX = "/\\W/ ";

	public static final String URL_REGEX = "(@)?(href=\")?(http://)?[A-Za-z]+(\\.\\w+)+(/[&\\n=?\\+\\%/\\.\\w]+)?";

	// public static final String URL_REGEX =
	// "^[A-Za-z]+://[A-Za-z0-9-_]+\\.[A-Za-z0-9-_%&\\?\\/.=]+$";
	// public static final String HASHTAG_REGEX = "/[#]+[A-Za-z0-9-_]+/";
	public static final String HASHTAG_REGEX = "\\B#[\\w\\d_]{1,15}";

	private List<String> twitterUserNameArray = new ArrayList<String>();
	private List<String> linksArray = new ArrayList<String>();
	private List<String> hashTags = new ArrayList<String>();

	private String itHelper = null;

	public static native String getNativeJsRegex() /*-{
		pattern = '^(?#Protocol)(?:(?:ht|f)tp(?:s?)\:\/\/|~/|/)?(?#Username:Password)(?:\w+:\w+@)?(?#Subdomains)(?:(?:[-\w]+\.)+(?#TopLevel Domains)(?:com|org|net|gov|mil|biz|info|mobi|name|aero|jobs|museum|travel|[a-z]{2}))(?#Port)(?::[\d]{1,5})?(?#Directories)(?:(?:(?:/(?:[-\w~!$+|.,=]|%[a-f\d]{2})+)+|/)+|\?|#)?(?#Query)(?:(?:\?(?:[-\w~!$+|.,*:]|%[a-f\d{2}])+=(?:[-\w~!$+|.,*:=]|%[a-f\d]{2})*)(?:&(?:[-\w~!$+|.,*:]|%[a-f\d{2}])+=(?:[-\w~!$+|.,*:=]|%[a-f\d]{2})*)*)*(?#Anchor)(?:#(?:[-\w~!$+|.,*:=]|%[a-f\d]{2})*)?$' 

		$wnd.alert(pattern)
		return pattern;
		//return "((https?|ftp|gopher|telnet|file|notes|ms-help):((//)|(\\\\))+[\w\d:#@%/;$()~_?\+-=\\\.&]*)"
	}-*/;

	private HTMLHelper() {

	}

	public static HTMLHelper get() {
		return new HTMLHelper();
	}

	/**
	 * Get a parsed text
	 * @deprecated user StringUtil.jsParse instead 
	 * @param text
	 * @return
	 */
	
	/*public String getParsedUpdateHtml(String text) {
		// parse(text);

		// get the names
	
		String newText = text;
		String[] uNames = StringUtils.getUserNames(text);		
		String[] links = StringUtils.getLinks(text);
		String[] hashtags = StringUtils.getHashTags(text);
		
		for (String url : links) {
			GWT.log("Replacing URL: "+url+" - Old Text: "+newText, null);
			newText = newText.replaceAll("\\Q" + url + "\\E", "<a href=\""
					+ url + "\" target=\"_blank\">" + url + "</a>");
			GWT.log("Replaced: "+newText, null);
		}
		
		for (String twitterScreenName : uNames) {
			// twitterScreenName = twitterScreenName.substring(1);
			String twitterId = String.valueOf(++nextId) + twitterScreenName;

			newText = newText.replaceAll("\\Q" + twitterScreenName + "\\E",
					"<a id=\"" + twitterId
							+ "\" href=\"javascript:showUserPanel('"
							+ twitterScreenName + "','" + twitterId + "')\">"
							+ twitterScreenName + "</a>");

		}
		
		for (String hashTag : hashtags) {
			newText = newText.replaceAll("\\Q" + hashTag + "\\E",
					"<a href=\"javascript:processHashTag('" + hashTag + "')\">"
							+ hashTag + "</a>");
		}

		return newText;
	}
*/
	/**
	 * Helper method to remove any trailing trash from the screenname.
	 * 
	 * @param tmpString
	 * @return
	 */
	private static String clearWord(String tmpString, String regex) {
		GWT.log("Clearing " + tmpString, null);
		String[] tmp = tmpString.split(regex);

		String newString = tmpString.trim();

		if (tmp != null) {
			for (String str : tmp) {
				newString = newString.replace(str, "");
				GWT.log(str, null);
			}
		}

		return newString;
	}

	/**
	 * Find twitter names inside the text and decorate them with a <a
	 * href=""></a> tag
	 * 
	 * @param text
	 * @return
	 */
	private void parse(String text) {

		GWT.log("Testing text: " + text, null);
		String[] list = StringUtils.splitText(text);

		for (String word : list) {

			if (word.contains("@") && processScreenName(word) != null
					&& !twitterUserNameArray.contains(itHelper)) {
				twitterUserNameArray.add(itHelper);
			}

			if (word.startsWith("http://") && !linksArray.contains(word)) {
				linksArray.add(word);
			}
			if (word.contains("#")) {
				processHashTag(word);
			}

		}
		/*
		 * int position = 0;
		 * 
		 * 
		 * String tmpString = text.trim(); boolean process = true; String word =
		 * null; do {
		 * 
		 * if (tmpString.contains(" ")) { word = tmpString.substring(0,
		 * tmpString.indexOf(" ")); position += word.length() + 1; tmpString =
		 * text.substring(position).trim(); } else { word = tmpString; word =
		 * word.trim(); process = false; }
		 * 
		 * if (word.contains("@") && processScreenName(word) != null &&
		 * !twitterUserNameArray.contains(itHelper)) {
		 * twitterUserNameArray.add(itHelper); }
		 * 
		 * if (word.startsWith("http://") && !linksArray.contains(word)) {
		 * linksArray.add(word); } if (word.contains("#")) {
		 * processHashTag(word); }
		 * 
		 * } while (process);
		 */
	}

	public static String[] getLines(String excludeUserNames) {
		String[] exStrings = excludeUserNames.split("\\n");
		for (int i=0; i<exStrings.length; i++ ) {
			GWT.log("String: "+exStrings[i], null);
			exStrings[i] = exStrings[i].trim();
			GWT.log("String: "+exStrings[i], null);
		}
		// return HTMLWidgetUtils.getLines(excludeUserNames);
		return exStrings;
	}

	public static void adjustLines(TextArea excludeUserNames, int lines, int min,
			int max) {
		if (lines < min) {
			lines = min;
		} else if (lines > max) {
			lines = max;
		} else {
			lines++;
		}
		
		
		excludeUserNames.setVisibleLines(lines);		
	}

	/**
	 * From the text, return the links
	 * 
	 * @param text
	 * @return
	 */
	public List<String> getLinks(String text) {
		ArrayList<String> retList = new ArrayList<String>();
		text = text.trim();
		//line by line, find any occurence of http
		String lines[] = getLines(text);
		
		//for each word find an occurence of http://
		for ( String line:lines) {
			
			
			
				retList.addAll(getUrls(line));
			
			
			
		}

		return retList;

	}
	private Collection<? extends String> getUrls(String line) {
		line = line.trim();
		ArrayList<String> retList = new ArrayList<String>();
		int startIndex = line.indexOf("http://");
		if (startIndex>=0) {
			int endIndex = line.indexOf(" ",startIndex);
			
			if (endIndex<0 ) {
				endIndex = line.length();
			}
			
			if ( endIndex>0) {
				
				String url = line.substring(startIndex,endIndex);
				
				retList.add(url);	
				
				if ( endIndex<line.length() )  {
					int nextStart = line.substring(endIndex,line.length()).indexOf("http://");
					if ( nextStart>0) {
						retList.addAll(getUrls(line.substring(endIndex+nextStart,line.length())));
					}
				}
				
			}			
		}
		return retList;
	
	}

	

	private void processHashTag(String word) {
		GWT.log("Processing hashTag " + word, null);
		word = HTMLHelper.clearWord(word, HTMLHelper.HASHTAG_REGEX);
		if (word.matches(HTMLHelper.HASHTAG_REGEX)) {
			GWT.log("Adding Hashtag " + word, null);
			hashTags.add(word);
		} else {
			GWT.log("Is not an HashTag " + word, null);
		}
	}

	/**
	 * Process a word, with no spaces, and check if it is a username
	 * 
	 * @param word
	 * @return
	 */
	private String processScreenName(String word) {
		GWT.log("Processing String: " + word, null);
		// ithelper, works like an iterator
		itHelper = null;
		word = HTMLHelper.clearWord(word, HTMLHelper.USER_NAME_REGEX);

		if (word.matches(HTMLHelper.USER_NAME_REGEX)) {
			itHelper = word;
			GWT.log("Found: " + word, null);
		} else {
			GWT.log("Nothing found", null);
		}

		return itHelper;
	}

	public String getParsedMetaDataHtml(TwitterUpdateDTO twitterUpdate,
			boolean showConversationOptions) {
		GWT.log("Parsing date:" + twitterUpdate.getCreatedAt(), null);
		GWT.log("Parsing Source:" + twitterUpdate.getSource(), null);
		GWT.log("Parsing date:" + twitterUpdate.getCreatedAt(), null);
		GWT.log("Parsing InReplyToStatusId:"
				+ twitterUpdate.getInReplyToStatusId(), null);
		GWT
				.log("Parsing InReplyToUserId:"
						+ twitterUpdate.getInReplyToUserId(), null);

		String inReplyTo = "";

		if (twitterUpdate.getInReplyToStatusId() > 0 && showConversationOptions) {
			String elId = "update_" + twitterUpdate.getId();

			inReplyTo = "<a id=\"" + elId + "\" href=\"javascript:showStatus('"
					+ twitterUpdate.getInReplyToStatusId() + "','" + elId
					+ "')\">in reply to "
					+ twitterUpdate.getInReplyToScreenName() + "</a>";

			// Window.alert(inReplyTo);
		}

		String returnString = "<span class=\"createdAt\">"
				+ getParsedUpdateCreated(twitterUpdate.getCreatedAt())
				+ "<span> from <span class=\"source\">"
				+ getParsedSource(twitterUpdate.getSource()) + "</span> "
				+ inReplyTo;

		return returnString;

	}

	private String getParsedSource(String source) {
		return source.replace("&lt;", "<").replace("&quot;", "\"").replace(
				"&gt;", ">");

	}

	private String getParsedUpdateCreated(Date createdAt) {
		String returnStr = "";

		Date now = new Date();

		long longTimeBetween = now.getTime() - createdAt.getTime();

		// if it's more that 24 hours,
		if (longTimeBetween > 24 * 60 * 60 * 1000) {
			DateTimeFormat df = DateTimeFormat
					.getFormat("k:mm, EEE, MMM d, yyyyy");
			returnStr = df.format(createdAt);
		} else {
			// if less that an second
			long seconds = longTimeBetween / 1000;

			long minutes = 0;

			long hours = 0;

			if (seconds > 0)
				minutes = (seconds / 60);

			if (minutes > 0)
				hours = (minutes / 60);

			if (seconds <= 60) {
				returnStr = "less than 1 minute ago";
			} else if (hours == 0 && minutes < 60) {
				if (minutes > 1) {
					returnStr = minutes + " minutes ago";
				} else {
					returnStr = minutes + " minute ago";
				}

			} else {
				returnStr = "about " + hours + " hours ago";
			}

			// if ( minutes)
			// returnStr =
			// longTimeBetweenSeconds+" seconds, "+minutes+" minutes and "+hours+" hours";
		}

		// DateFormat df = new SimpleDateFormat("k M,d y");

		return returnStr;
	}

	public String replaceText(String currentStr, Map<String, String> result) {
		// String currentStr = update.getValue();
		String newStr = currentStr;

		for (String longLink : result.keySet()) {
			// Strinr decoded =

			//Window.alert("Replacing link: " + result.get(longLink) + " with "
			//		+ longLink);
			GWT.log("Replacing link: " + result.get(longLink) + " with "
					+ longLink, null);

			// String decodedLongLink = URL.decodeComponent(longLink);
			// GWT.log("Decoded: replacing link: "+result.get(longLink)+" with "+decodedLongLink,
			// null);
			// newStr = currentStr.replaceAll(longLink.,result.get(longLink));
			newStr = newStr.replace(longLink, result.get(longLink));

		}
		return newStr;

	}

}
