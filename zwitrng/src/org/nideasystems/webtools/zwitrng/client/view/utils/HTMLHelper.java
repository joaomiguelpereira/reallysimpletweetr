package org.nideasystems.webtools.zwitrng.client.view.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.core.client.GWT;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;

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
	 * 
	 * @param text
	 * @return
	 */
	public String getParsedUpdateHtml(String text) {
		parse(text);

		String newString = text;

		for (String twitterScreenName : this.twitterUserNameArray) {
			twitterScreenName = twitterScreenName.substring(1);
			String twitterId = String.valueOf(++nextId) + twitterScreenName;

			newString = newString.replaceAll(twitterScreenName, "<a id=\""
					+ twitterId + "\" href=\"javascript:showUserPanel('"
					+ twitterScreenName + "','" + twitterId + "')\">"
					+ twitterScreenName + "</a>");

		}

		for (String hashTag : this.hashTags) {
			newString = newString.replaceAll(hashTag,
					"<a href=\"javascript:processHashTag('" + hashTag + "')\">"
							+ hashTag + "</a>");
		}
		for (String url : this.linksArray) {
			newString = newString.replace((CharSequence) url, "<a href=\""
					+ url + "\" target=\"_blank\">" + url + "</a>");
		}
		return newString;
	}

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
				word = word.trim();
				process = false;
			}

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

		} while (process);
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
		String[] words = text.split(" ");

		for (String str : words) {
			if (str.startsWith("http") && !retList.contains(str)) {
				// GWT.log("URL before econding:" + str, null);
				// GWT.log("URL after econding:" + URL.encodeComponent(str),
				// null);
				// URL.
				retList.add(/* URL.encodeComponent( */str/* ) */);
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

	public String getParsedMetaDataHtml(TwitterUpdateDTO twitterUpdate, boolean showConversationOptions) {
		GWT.log("Parsing date:" + twitterUpdate.getCreatedAt(), null);
		GWT.log("Parsing Source:" + twitterUpdate.getSource(), null);
		GWT.log("Parsing date:" + twitterUpdate.getCreatedAt(), null);
		GWT.log("Parsing InReplyToStatusId:"
				+ twitterUpdate.getInReplyToStatusId(), null);
		GWT
				.log("Parsing InReplyToUserId:"
						+ twitterUpdate.getInReplyToUserId(), null);

		String inReplyTo = "";

		if (twitterUpdate.getInReplyToStatusId() > 0 && showConversationOptions ) {
			String elId = "update_" + twitterUpdate.getId();
			
			inReplyTo = "<a id=\"" + elId + "\" href=\"javascript:showStatus('"
					+ twitterUpdate.getInReplyToStatusId() + "','" + elId
					+ "')\">in reply to "
					+ twitterUpdate.getInReplyToScreenName() + "</a>";
			
			//Window.alert(inReplyTo);
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
}
