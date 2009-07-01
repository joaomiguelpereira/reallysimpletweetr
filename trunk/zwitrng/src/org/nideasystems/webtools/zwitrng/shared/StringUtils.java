package org.nideasystems.webtools.zwitrng.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.MainController;

import com.google.gwt.user.client.Window;

public class StringUtils {

	public static String[] splitText(String tags) {
		List<String> list = new ArrayList<String>();
		// Clear any trailing spaces
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
			if (word.trim().length() > 0) {
				list.add(word.trim());
			}

			/*
			 * if (word.contains("@") && processScreenName(word) != null &&
			 * !twitterUserNameArray.contains(itHelper)) {
			 * twitterUserNameArray.add(itHelper); }
			 * 
			 * if (word.startsWith("http://") && !linksArray.contains(word)) {
			 * linksArray.add(word); } if (word.contains("#")) {
			 * processHashTag(word); }
			 */

		} while (process);

		return list.toArray(new String[list.size()]);
	}

	public static String[] getUserNames(String text) {

		String splittedText[] = text.split("\\B@[\\w\\d_]{1,15}");

		String newText = text;
		if (splittedText.length > 0) {
			for (int i = 0; i < splittedText.length; i++) {
				if (splittedText[i].length() > 0) {

					newText = newText.replace(splittedText[i], ",");
				}

			}
		}
		String[] ret = newText.split(",");
		List<String> list = new ArrayList<String>();
		for (String str : ret) {
			if (str.trim().length() > 0) {
				list.add(str);
			}
		}
		return list.toArray(new String[list.size()]);

	}

	/*
	 * private static String replace(String sourceText, String what, String
	 * replacement) { String newStr = sourceText .replaceAll("\\Q" + what +
	 * "\\E", replacement); return newStr; }
	 */

	/*
	 * private static String[] getHashTags(String text) { String splittedText[]
	 * = text.split("[#]+[A-Za-z0-9-_]+");
	 * 
	 * String newText = text; if (splittedText.length > 0) { for (int i = 0; i <
	 * splittedText.length; i++) { if (splittedText[i].length() > 0) {
	 * 
	 * newText = newText.replaceAll("\\Q" + splittedText[i] + "\\E", ","); }
	 * 
	 * } } String[] ret = newText.split(","); List<String> list = new
	 * ArrayList<String>(); for (String str : ret) { if (str.trim().length() >
	 * 0) { list.add(str); } } return list.toArray(new String[list.size()]);
	 * 
	 * }
	 * 
	 * public static String[] getLinks(String text) {
	 * 
	 * // TODO: Change the alg
	 * 
	 * String splittedText[] = text.split(" ");
	 * 
	 * List<String> list = new ArrayList<String>(); if (splittedText.length > 0)
	 * { for (int i = 0; i < splittedText.length; i++) { if
	 * (splittedText[i].trim().startsWith("http://")) {
	 * list.add(splittedText[i].trim()); } } }
	 * 
	 * return list.toArray(new String[list.size()]);
	 * 
	 * 
	 * String splittedText[] = text.split(
	 * "(@)?(href=\")?(http://)?[A-Za-z]+(\\.\\w+)+(/[&\\n=?\\+\\%/\\.\\w]+)?"
	 * );
	 * 
	 * 
	 * String newText = text; if (splittedText.length > 0) { for (int i = 0; i <
	 * splittedText.length; i++) { if (splittedText[i].length() > 0) {
	 * 
	 * newText = newText.replaceAll("\\Q" + splittedText[i] + "\\E", ","); }
	 * 
	 * } } String[] ret = newText.split(","); List<String> list = new
	 * ArrayList<String>(); for (String str : ret) { if (str.trim().length() >
	 * 0) { list.add(str); } } return list.toArray(new String[list.size()]);
	 * 
	 * 
	 * }
	 */

	public static String jsParseText(String templateText) {

		return MainController.jsParseText(templateText);
	}

	public static String randomizeString(String templateText) {
		// String templateText =
		// "this is a [smart|very smart|trully smart] tweet :)";
		String randomizedString = templateText;
		// find all occurrences like [xxx|xxx|xxx|]
		int startIndex = templateText.indexOf("[");
		System.out.println("Chechink: " + templateText);

		if (startIndex >= 0) {
			int endIndex = templateText.indexOf("]", startIndex);

			if (endIndex > 0) {
				String tmpWord = templateText.substring(startIndex + 1,
						endIndex);
				String otherWord = templateText.substring(startIndex,
						endIndex + 1);
				System.out.println("Foudn " + tmpWord);
				String replacement = getOneOf(tmpWord);
				System.out.println("Other word " + otherWord);
				System.out.println("Random: " + replacement);

				randomizedString = randomizedString.replace(otherWord,
						replacement);

				randomizedString = randomizeString(randomizedString);

			}
		}
		System.out.println("returning : " + randomizedString);
		return randomizedString;

	}

	private static String getOneOf(String tmpWord) {
		// one|two|three

		String[] splitedText = tmpWord.split("[|]");
		
		double rand = Math.random();
		int index = (int) Math.round(rand * (splitedText.length - 1));

		return splitedText[index];
	}

	public static List<String> getFragmentLists(String templateText) {

		// Find all occurences of {{xxx}} and return xxx
		int startIndex = templateText.indexOf("{{");
		List<String> returnList = new ArrayList<String>();
		if (startIndex >= 0) {
			int endIndex = templateText.indexOf("}}");
			if (endIndex > 2) {
				String listName = templateText.substring(startIndex + 2,
						endIndex);
				returnList.add(listName);
				returnList.addAll(getFragmentLists(templateText.substring(endIndex+1,templateText.length())));
			}
		}
		return returnList;
	}

	public static String replaceFragmentsLists(String templateText,
			Map<String, String> result) {
		for (String key: result.keySet()) {
			String charSeq = "{{"+key+"}}";
			
			templateText = templateText.replace(charSeq, "["+result.get(key)+"]");
		}
		return templateText;
	}
}
