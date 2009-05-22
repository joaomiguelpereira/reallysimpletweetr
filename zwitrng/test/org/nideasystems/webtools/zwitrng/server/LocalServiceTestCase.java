package org.nideasystems.webtools.zwitrng.server;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;



import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

public class LocalServiceTestCase extends TestCase {

	protected String validTwitterName = "joaomrpereira";
	protected String validTwitterPassword = "PasterNark132435";
	protected User user = new User("joao","gmail.com");
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		//Get SystemEnv vars
		
		
		ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
		ApiProxy.setDelegate(new ApiProxyLocalImpl(new File(".")) {} );
		//Configure Logger
		
		Logger.getLogger("org.nideasystems.webtools.zwitrng.server").setLevel(Level.FINEST);
		
	}

	@Override
	protected void tearDown() throws Exception {
		ApiProxy.setDelegate(null);
		ApiProxy.setEnvironmentForCurrentThread(null);
		super.tearDown();
	}
	
	

}
