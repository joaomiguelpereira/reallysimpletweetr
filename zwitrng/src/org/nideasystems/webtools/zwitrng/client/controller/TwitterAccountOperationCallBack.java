package org.nideasystems.webtools.zwitrng.client.controller;


import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;

public interface TwitterAccountOperationCallBack {
	
	public void onTwitterAccountLoadSuccess(TwitterUserDTO twitterAccount);
	public void onTwitterAccountLoadError(String error);
	public void onFollowUserSuccess(Object result);
	public void onFollowUserError(String error);
	public void onBlockUserSuccess(Object result);
	public void onBlockUserError(String error);
	public void onPrivateMessageSuccess(Object result);
	public void onPrivateMessageError(String error);
	

}
