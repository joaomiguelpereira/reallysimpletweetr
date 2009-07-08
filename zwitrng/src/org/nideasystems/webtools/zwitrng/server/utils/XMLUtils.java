package org.nideasystems.webtools.zwitrng.server.utils;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLUtils {
	public static String getCharacterDataFromElement(Element e) {
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

	public static float getFloat(String value) {
		if (value != null && !value.equals("")) {
			return Float.parseFloat(value);
		}
		return 0;
	}

	public static String getElementValue(Element parent, String label) {
		return getCharacterDataFromElement((Element) parent
				.getElementsByTagName(label).item(0));
	}
}
