package org.nideasystems.webtools.zwitrng.shared;


import com.google.gwt.user.client.rpc.IsSerializable;

public class OAuthInfoDTO implements IsSerializable{
	
	private String loginUrl ="";
	private String token = "";
	private String tokenSecret ="";
	private byte[] requestToken = null;
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
	public String getTokenSecret() {
		return tokenSecret;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}
	public void setRequestToken(byte[] requestToken) {
		this.requestToken = requestToken;
	}
	public byte[] getRequestToken() {
		return requestToken;
	}
	

}
