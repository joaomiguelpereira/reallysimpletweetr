package org.nideasystems.webtools.zwitrng.client.services;

import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.OAuthInfoDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterServiceAsync {
	void search(TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter, AsyncCallback<List<TwitterUpdateDTO>> callback) throws Exception;
	void getTwitterUpdates(TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter, AsyncCallback<List<TwitterUpdateDTO>> callback) throws Exception;
	void postUpdate(TwitterAccountDTO twitterAccount, String tweetText,AsyncCallback<TwitterUpdateDTO> callback) throws Exception;
	
	void  getOAuthInfo(TwitterAccountDTO twitterAccount, AsyncCallback<OAuthInfoDTO> callback) throws Exception;
	
	void authenticateUser(PersonaDTO personaDto,
			 AsyncCallback<TwitterAccountDTO> asyncCallback) throws Exception;
	
}
