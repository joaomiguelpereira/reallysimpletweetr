package org.nideasystems.webtools.zwitrng.client.services;

import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The twitter service
 * 
 * @author jpereira
 * 
 */

@RemoteServiceRelativePath("twitterService")
public interface TwitterService extends RemoteService {

	
	/**
	 * Post an update to twitter
	 * @param persona The persona posting the update
	 * @param update the Update
	 * @return the Update
	 * @throws Exception
	 */
	TwitterUpdateDTO postUpdate(PersonaDTO persona, TwitterUpdateDTO update) throws Exception;

	
	/**
	 * Get the Updates from twitter according to the filter
	 * @param persona The persona
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	TwitterUpdateDTOList getTwitterUpdates(PersonaDTO persona,
			FilterCriteriaDTO filter) throws Exception;

	/**
	 * 
	 * @param personaDto
	 * @param pinCode
	 * @return
	 * @throws Exception
	 */
	TwitterAccountDTO authenticateUser(PersonaDTO personaDto, String pinCode) throws Exception;

	/**
	 * 
	 * @param persona
	 * @param userIdorScreenName
	 * @return
	 * @throws Exception
	 */
	TwitterUserDTO getUserInfo(PersonaDTO persona,
			String userIdorScreenName) throws Exception;
	
	
	/**
	 * 
	 * @param persona
	 * @param currentFilter
	 * @return
	 * @throws Exception
	 */
	TwitterUserDTOList getUsers(PersonaDTO persona,
			TwitterUserFilterDTO currentFilter) throws Exception;
	
	/**
	 * 
	 * @param model
	 * @throws Exception
	 */
	void synchronizeTwitterAccount(PersonaDTO model) throws Exception;	
	
	/**
	 * 
	 * @param currentPersona
	 * @param user
	 * @throws Exception
	 */
	void followUser(PersonaDTO currentPersona, TwitterUserDTO user, boolean synch) throws Exception;
	

	/**
	 * 
	 * @param currentPersona
	 * @param user
	 * @throws Exception
	 */
	void unfollowUser(PersonaDTO currentPersona, TwitterUserDTO user) throws Exception;
	

	/**
	 * 
	 * @param currentPersona
	 * @param user
	 * @throws Exception
	 */
	void blockUser(PersonaDTO currentPersona, TwitterUserDTO user) throws Exception;

	/**
	 * 
	 * @param currentPersona
	 * @param user
	 * @throws Exception
	 */
	void unblockUser(PersonaDTO currentPersona, TwitterUserDTO user) throws Exception;

	
	/**
	 * 
	 * @param currentPersona
	 * @param user
	 * @throws Exception
	 */
	TwitterUpdateDTO sendDirectMessage(PersonaDTO currentPersona, TwitterUpdateDTO dm) throws Exception;


}
