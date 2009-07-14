package org.nideasystems.webtools.zwitrng.server;

import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.client.services.TwitterService;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;

/*
 public class TwitterServiceImpl extends RemoteServiceServlet implements
 TwitterService {
 */
public class TwitterServiceImpl extends AbstractRemoteServiceServlet implements
		TwitterService {

	private static final long serialVersionUID = -481643127871478064L;
	private static final Logger log = Logger.getLogger(TwitterServiceImpl.class
			.getName());

	

	
	/**
	 * Get the updates according to the filter
	 */
	@Override
	public TwitterUpdateDTOList getTwitterUpdates(
			PersonaDTO persona, FilterCriteriaDTO filter)
			throws Exception {
		startTransaction(true);
		// Check if is logged in
		TwitterUpdateDTOList list = null;
		try {
			list = getBusinessHelper().getTwitterPojo().getUpdates(persona,
					filter);
			
			//list = TwitterServiceAdapter.get().getUpdates(twitterAccount,
			//		filter);
		} catch (Exception e) {
			log.severe("Error: "+e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			endTransaction();
		}

		return list;

	}

	@Override
	public TwitterUpdateDTO postUpdate(PersonaDTO persona,TwitterUpdateDTO update)
			throws Exception {
		// Send tweet
		// Check if is logged in
		startTransaction(true);

		
		TwitterUpdateDTO lastUpdate = null;
		
		try {
			lastUpdate =  getBusinessHelper().getTwitterPojo().postUpdate(persona, update);
			
			//lastUpdate = TwitterServiceAdapter.get().postUpdate(update);
		} catch (Exception e) {
			throw e;
		} finally {
			endTransaction();
		}
		
		
		/*if (status != null) {
			lastUpdate = DataUtils.createTwitterUpdateDto(status, true);
		}*/
		
		return lastUpdate;

		// throw new Exception("Not implemented");

	}

/*	@Override
	public OAuthInfoDTO getOAuthInfo(TwitterAccountDTO twitterAccount)
			throws Exception {

		// throw new Exception("Not implemented");
		// TODO Auto-generated method stub
		// return TwitterServiceAdapter.get().getOAuthInfo(twitterAccount);
		return null;
	}*/

	@Override
	public TwitterAccountDTO authenticateUser(PersonaDTO personaDto,
			String pinCode) throws Exception {
		
		//Start the transaction
		startTransaction(true);
		
		//Authorize the account
		TwitterAccountDTO authorizedAccount;
		try {
			authorizedAccount = TwitterServiceAdapter.authorizeAccount(personaDto.getTwitterAccount(), pinCode);
			
			//update database		
			getBusinessHelper().getTwitterPojo().updateAuthorizedTwitterAccount(personaDto,authorizedAccount);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
			
		} finally {
			endTransaction();

		}
		
		return authorizedAccount;

		
		
/*		//Now get the user info 
		
		
		User exUser = TwitterServiceAdapter.get().getUserInfo(
				authorizedTwitterAccount);

		TwitterAccountDTO fullAuthorizedAccount = DataUtils
				.createAutenticatedTwitterAccountDto(exUser, authorizedTwitterAccount);
		// Update DOmain Object in DB with new oauth token

		TwitterAccountDO twitterAccountDo = DataUtils
				.twitterAccountDoFromDto(fullAuthorizedAccount);

		getBusinessHelper().getPersonaPojo().updatePersonaTwitterAccount(
				personaDto, twitterAccountDo);
*/
	}

	@Override
	public TwitterUserDTO getUserInfo(
			PersonaDTO persona, String userIdOrScreenName)
			throws Exception {
		startTransaction(true);
		
		TwitterUserDTO returnDto = null;

		try {
			
			returnDto = getBusinessHelper().getTwitterPojo().getUserInfo(persona, userIdOrScreenName);
			
		} catch (Exception e) {
			log.severe("Error " + e.getMessage());
			throw new Exception(e);
		} finally {
			endTransaction();
		}
		
		return returnDto;

	}

	/*@Override
	public void followUser(PersonaDTO persona, boolean follow,
			Integer userId) throws Exception {
		startTransaction(false);

		try {
			getBusinessHelper().getTwitterPojo().followUser(persona, userId);
			
			//TwitterServiceAdapter.get().followUser(account, follow, userId);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			endTransaction();
		}

	}

	@Override
	public void blockUser(PersonaDTO persona, boolean block,
			Integer userId) throws Exception {
		startTransaction(false);

		try {
			TwitterServiceAdapter.get().blockUser(account, block, userId);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			endTransaction();
		}

	}*/

	@Override
	public TwitterUserDTOList getUsers(PersonaDTO persona,
			TwitterUserFilterDTO currentFilter) throws Exception {
		// new Transaction
		startTransaction(true);
		TwitterUserDTOList list;

		try {
			list = getBusinessHelper().getTwitterPojo().getUsers(persona,
					currentFilter);
		} catch (Exception e1) {
			log.severe("Error: " + e1.getMessage());
			e1.printStackTrace();
			throw e1;
		} finally {
			endTransaction();
		}

		return list;
		/*
		 * startTransaction(false); //TwitterAccountDTOList list = null; try {
		 * list =
		 * TwitterServiceAdapter.get().getUsers(persona.getTwitterAccount(),
		 * currentFilter); } catch (Exception e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } finally { endTransaction(); } return
		 * list;
		 */

	}

	@Override
	public void synchronizeTwitterAccount(PersonaDTO model) throws Exception {
		startTransaction(true);
		
		try {
			getBusinessHelper().getTwitterPojo().synchronize(model);
		} catch (Exception e) {
			
			log.severe("Error Synchronizing: "+e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			endTransaction();
		}
		
	}

	@Override
	public void followUser(PersonaDTO currentPersona, TwitterUserDTO user)
			throws Exception {
		startTransaction(true);
		
		try {
			getBusinessHelper().getTwitterPojo().followUser(currentPersona,user);
		} catch (Exception e) {
			
			log.severe("Error Following User: "+e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			endTransaction();
		}
		
	}

	@Override
	public void unfollowUser(PersonaDTO currentPersona, TwitterUserDTO user)
			throws Exception {
		startTransaction(true);
		try {
			getBusinessHelper().getTwitterPojo().unfollowUser(currentPersona,user);
		} catch (Exception e) {
			
			log.severe("Error Unfollowing User: "+e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			endTransaction();
		}
		
	}

	@Override
	public void blockUser(PersonaDTO currentPersona, TwitterUserDTO user)
			throws Exception {
		startTransaction(true);
		try {
			getBusinessHelper().getTwitterPojo().blockUser(currentPersona,user);
		} catch (Exception e) {
			
			log.severe("Error blocking User: "+e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			endTransaction();
		}
		
	}

	@Override
	public void unblockUser(PersonaDTO currentPersona, TwitterUserDTO user)
			throws Exception {
		startTransaction(true);
		try {
			getBusinessHelper().getTwitterPojo().unblockUser(currentPersona,user);
		} catch (Exception e) {
			
			log.severe("Error blocking User: "+e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			endTransaction();
		}
		
	}

	@Override
	public TwitterUpdateDTO sendDirectMessage(PersonaDTO currentPersona,
			TwitterUpdateDTO dm) throws Exception {
		// Send tweet
		// Check if is logged in
		startTransaction(true);

		
		TwitterUpdateDTO directM = null;
		
		try {
			directM =  getBusinessHelper().getTwitterPojo().sendDM(currentPersona, dm);
			
			//lastUpdate = TwitterServiceAdapter.get().postUpdate(update);
		} catch (Exception e) {
			throw e;
		} finally {
			endTransaction();
		}
		return directM;
	}

}
