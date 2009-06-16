package org.nideasystems.webtools.zwitrng.client.services;

import java.util.List;

import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountListDTO;
import org.nideasystems.webtools.zwitrng.shared.OAuthInfoDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterServiceAsync {
	void search(TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter,
			AsyncCallback<List<TwitterUpdateDTO>> callback) throws Exception;

	void getTwitterUpdates(TwitterAccountDTO twitterAccount,
			FilterCriteriaDTO filter,
			AsyncCallback<TwitterUpdateDTOList> callback) throws Exception;

	void postUpdate(TwitterUpdateDTO update,
			AsyncCallback<TwitterUpdateDTO> callback) throws Exception;

	void getOAuthInfo(TwitterAccountDTO twitterAccount,
			AsyncCallback<OAuthInfoDTO> callback) throws Exception;

	void authenticateUser(PersonaDTO personaDto, String pinCode,
			AsyncCallback<TwitterAccountDTO> asyncCallback) throws Exception;

	void getExtendedUserAccount(TwitterAccountDTO twitterAccount,
			String userIdOrScreenName, AsyncCallback<TwitterAccountDTO> callbak)
			throws Exception;

	void followUser(TwitterAccountDTO account, boolean follow, Integer id,
			AsyncCallback<java.lang.Void> asyncCallback);

	void blockUser(TwitterAccountDTO account, boolean block, Integer id,
			AsyncCallback<Void> callback);

	void getUsers(TwitterAccountDTO account,
			TwitterUserFilterDTO currentFilter,
			AsyncCallback<TwitterAccountListDTO> callback) throws Exception;

}
