package org.nideasystems.webtools.zwitrng.server;

import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import junit.framework.TestCase;

public class TestStringUtils extends TestCase {

	
	public void testgetUserNames() {
		String initialvalue = "@joao";
		String[] userNames = null;		
		userNames = StringUtils.getUserNames(initialvalue);
		assertNotNull(userNames);
		assertEquals(1, userNames.length);
		
		
	}
	
	
	public void testFormatPercentage() {
		float number = 90F/99F;
		
		String str = StringUtils.formatPercentage(number,2);
		System.out.println("Number "+str);
	}
	public void testgetNoUserNames() {
		String initialvalue = "nothing joao";
		String[] userNames = null;		
		userNames = StringUtils.getUserNames(initialvalue);
		assertNotNull(userNames);
		assertEquals(0, userNames.length);
		
		
	}
	
	public void testgetTwoUserNames() {
		String initialvalue = "@joao is cool. Yes so does @miguel";
		String[] userNames = null;		
		userNames = StringUtils.getUserNames(initialvalue);
		assertNotNull(userNames);
		assertEquals(2, userNames.length);
	}
	
	
	public void testReplaceOndeUserName() {
		String initialvalue = "@joao is cool.";
		String templateText = "Hello {username_0}, how are you?";
		String[] userNames = null;		
		
		userNames = StringUtils.getUserNames(initialvalue);
		/*for (int i=0; i<userNames.length;i++) {
			templateText = StringUtils.replace(templateText, "{username_"+i+"}", userNames[i]);	
		}*/
		assertEquals("Hello @joao, how are you?", templateText);
		
		
	}
	
	public void testReplaceTwiUserName() {
		String initialvalue = "@joao is cool. @otherusername";
		String templateText = "Hello {username_0}, how are you? And you? {username_1}";
		String[] userNames = null;		
		
		userNames = StringUtils.getUserNames(initialvalue);
		/*for (int i=0; i<userNames.length;i++) {
			templateText = StringUtils.replace(templateText, "{username_"+i+"}", userNames[i]);	
		}*/
		assertEquals("Hello @joao, how are you? And you? @otherusername", templateText);
		
		
	}
	
	public void testRandomizeList() {
		String templateText = "this is a [smart|very smart|trully smart] tweet :)";
	
		String radoimizedString = StringUtils.randomizeString(templateText);
		assertEquals("this is a trully smart tweet :)", radoimizedString);
		
		
		
	}

	public void testRandomizeInitList() {
		String templateText = "[smart|very smart|trully smart] tweet :)";
	
		String radoimizedString = StringUtils.randomizeString(templateText);
		assertEquals("this is a trully smart tweet :)", radoimizedString);
		
		
		
	}
	
	public void testRandomizeMalList() {
		String templateText = "[smart|very smart|trully smart tweet :)";
	
		String radoimizedString = StringUtils.randomizeString(templateText);
		assertEquals("this is a trully smart tweet :)", radoimizedString);
		
		
		
	}
	public void testRandomizeEndList() {
		String templateText = "very [smart|very smart|trully smart]";
	
		String radoimizedString = StringUtils.randomizeString(templateText);
		assertEquals("this is a trully smart tweet :)", radoimizedString);
		
		
		
	}
	
	public void testRandomizeTwiist() {
		String templateText = "very [smart|very smart|trully smart] and [cool|fine|nice]";
	
		String radoimizedString = StringUtils.randomizeString(templateText);
		System.out.println("last: "+radoimizedString);
		assertEquals("this is a trully smart tweet :)", radoimizedString);
		
		
		
	}
	
	public void testRandomizeFalseTwiist() {
		String templateText = "very [smart|very smart|trully smart] and [cool|fine|nice";
	
		String radoimizedString = StringUtils.randomizeString(templateText);
		System.out.println("last: "+radoimizedString);
		assertEquals("this is a trully smart tweet :)", radoimizedString);
		
		
		
	}

}
