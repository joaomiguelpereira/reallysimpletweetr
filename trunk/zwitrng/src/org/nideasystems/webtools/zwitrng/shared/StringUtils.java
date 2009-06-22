package org.nideasystems.webtools.zwitrng.shared;

import java.util.ArrayList;
import java.util.List;

import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;

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

}
