package org.nideasystems.webtools.zwitrng.client.services;


import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterServiceAsync {
	


	/**
	 * 
	 * @param persona
	 * @param filter
	 * @param callback
	 * @throws Exception
	 */
	void getTwitterUpdates(PersonaDTO persona,
			FilterCriteriaDTO filter,
			AsyncCallback<TwitterUpdateDTOList> callback) throws Exception;

	/**
	 * 
	 * @param persona
	 * @param update
	 * @param callback
	 * @throws Exception
	 */
	void postUpdate(PersonaDTO persona,TwitterUpdateDTO update,
			AsyncCallback<TwitterUpdateDTO> callback) throws Exception;

	/**
	 * 
	 * @param personaDto
	 * @param pinCode
	 * @param asyncCallback
	 * @throws Exception
	 */
	void authenticateUser(PersonaDTO personaDto, String pinCode,
			AsyncCallback<TwitterAccountDTO> asyncCallback) throws Exception;

	/**
	 * 
	 * @param persona
	 * @param userIdOrScreenName
	 * @param callbak
	 * @throws Exception
	 */
	void getUserInfo(PersonaDTO persona,
			String userIdOrScreenName, AsyncCallback<TwitterUserDTO> callbak)
			throws Exception;

	/**
	 * 
	 * @param persona
	 * @param currentFilter
	 * @param callback
	 * @throws Exception
	 */
	void getUsers(PersonaDTO persona,
			TwitterUserFilterDTO currentFilter,
			AsyncCallback<TwitterUserDTOList> callback) throws Exception;

	/**
	 * 
	 * @param model
	 * @param asyncCallback
	 * @throws Exception
	 */
	void synchronizeTwitterAccount(PersonaDTO model,
			AsyncCallback<Void> asyncCallback) throws Exception;

	
	/**
	 * 
	 * @param currentPersona
	 * @param user
	 * @param asyncCallback
	 * @throws Exception
	 */
	void followUser(PersonaDTO currentPersona, TwitterUserDTO user,
			AsyncCallback<Void> asyncCallback) throws Exception;

	/**
	 * 
	 * @param currentPersona
	 * @param user
	 * @param asyncCallback
	 * @throws Exception
	 */
	void unfollowUser(PersonaDTO currentPersona, TwitterUserDTO user,
			AsyncCallback<Void> asyncCallback) throws Exception;

	/**
	 * 
	 * @param currentPersona
	 * @param user
	 * @param asyncCallback
	 * @throws Exception
	 */
	void blockUser(PersonaDTO currentPersona, TwitterUserDTO user,
			AsyncCallback<Void> asyncCallback) throws Exception;

	/**
	 * 
	 * @param currentPersona
	 * @param user
	 * @param asyncCallback
	 * @throws Exception
	 */
	void unblockUser(PersonaDTO currentPersona, TwitterUserDTO user,
			AsyncCallback<Void> asyncCallback) throws Exception;

	void sendDirectMessage(PersonaDTO currentPersona, TwitterUpdateDTO dm,AsyncCallback<TwitterUpdateDTO> asyncCallback) throws Exception;
}
