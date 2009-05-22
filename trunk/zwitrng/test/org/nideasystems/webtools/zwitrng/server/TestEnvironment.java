package org.nideasystems.webtools.zwitrng.server;
import com.google.apphosting.api.ApiProxy;;

public class TestEnvironment implements ApiProxy.Environment {
	
	@Override
	public String getAppId() {
		
		return "Unit tests";
	}

	@Override
	public String getAuthDomain() {
		
		return "gmail.com";
	}

	@Override
	public String getDefaultNamespace() {
		
		return "";
	}

	@Override
	public String getEmail() {
		return "";
	}

	@Override
	public String getRequestNamespace() {
		
		return "gmail.com";
	}

	@Override
	public String getVersionId() {
		
		return "1.0";
	}

	@Override
	public boolean isAdmin() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDefaultNamespace(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
