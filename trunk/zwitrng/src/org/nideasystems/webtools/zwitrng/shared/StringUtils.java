package org.nideasystems.webtools.zwitrng.shared;

import java.util.ArrayList;
import java.util.List;

import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;

import com.google.gwt.user.client.Window;

public class StringUtils {

	public static String[] splitText(String tags) {
		List<String> list = new ArrayList<String>();
		//Clear any trailing spaces
		String tmpString = tags.trim();
		boolean process = true;
		int position = 0;
		String word = null;
		do {

			if (tmpString.contains(" ")) {
				word = tmpString.substring(0, tmpString.indexOf(" "));
				position += word.length() + 1;
				tmpString = tags.substring(position).trim();
			} else {
				word = tmpString;
				word = word.trim();
				process = false;
			}
			if ( word.trim().length()>0) {
				list.add(word.trim());
			}
			
			
/*
			if (word.contains("@") && processScreenName(word) != null
					&& !twitterUserNameArray.contains(itHelper)) {
				twitterUserNameArray.add(itHelper);
			}

			if (word.startsWith("http://") && !linksArray.contains(word)) {
				linksArray.add(word);
			}
			if (word.contains("#")) {
				processHashTag(word);
			}*/

		} while (process);

		return list.toArray(new String[list.size()]);
	}

	public static String[] getUserNames(String text) {
		
		String splittedText[] = text.split("\\B@[\\w\\d_]{1,15}");
		
		String newText = text;
		if (splittedText.length>0) {
			for (int i=0; i< splittedText.length; i++) {
				if (splittedText[i].length()>0) {
										
					newText = newText.replaceAll("\\Q"+splittedText[i]+"\\E", ",");
				}
				
			}	
		}
		String[] ret = newText.split(",");
		List<String> list = new ArrayList<String>();
		for (String str:ret) {
			if (str.trim().length()>0) {
				list.add(str);
			}
		}
		return list.toArray(new String[list.size()]);
					
		
	}

	public static String replace(String sourceText, String what,
			String replacement) {
		String newStr = sourceText.replaceAll(what, replacement);
		return newStr;
	}

	public static String[] getHashTags(String text) {
		String splittedText[] = text.split("[#]+[A-Za-z0-9-_]+");
		
		String newText = text;
		if (splittedText.length>0) {
			for (int i=0; i< splittedText.length; i++) {
				if (splittedText[i].length()>0) {
										
					newText = newText.replaceAll("\\Q"+splittedText[i]+"\\E", ",");
				}
				
			}	
		}
		String[] ret = newText.split(",");
		List<String> list = new ArrayList<String>();
		for (String str:ret) {
			if (str.trim().length()>0) {
				list.add(str);
			}
		}
		return list.toArray(new String[list.size()]);
					
	}

	public static String[] getLinks(String text) {
	String splittedText[] = text.split("(@)?(href=\")?(http://)?[A-Za-z]+(\\.\\w+)+(/[&\\n=?\\+\\%/\\.\\w]+)?");
		
		String newText = text;
		if (splittedText.length>0) {
			for (int i=0; i< splittedText.length; i++) {
				if (splittedText[i].length()>0) {
										
					newText = newText.replaceAll("\\Q"+splittedText[i]+"\\E", ",");
				}
				
			}	
		}
		String[] ret = newText.split(",");
		List<String> list = new ArrayList<String>();
		for (String str:ret) {
			if (str.trim().length()>0) {
				list.add(str);
			}
		}
		return list.toArray(new String[list.size()]);
		}

}
