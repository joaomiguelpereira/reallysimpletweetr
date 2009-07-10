package org.nideasystems.webtools.zwitrng.server;

import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.client.services.TwitterService;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.OAuthInfoDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;

import twitter4j.Status;
import twitter4j.User;

/*
 public class TwitterServiceImpl extends RemoteServiceServlet implements
 TwitterService {
 */
public class TwitterServiceImpl extends AbstractRemoteServiceServlet implements
		TwitterService {

	private static final long serialVersionUID = -481643127871478064L;
	private static final Logger log = Logger.getLogger(TwitterServiceImpl.class
			.getName());

	@Override
	public List<TwitterUpdateDTO> search(TwitterAccountDTO twitterAccount,
			FilterCriteriaDTO filter) throws Exception {

		// Check if is logged in
		AuthorizationManager.checkAuthentication();
		return null;

		// return TwitterServiceAdapter.get().searchStatus(twitterAccount,
		// filter);

	}

	@Override
	public TwitterUpdateDTOList getTwitterUpdates(
			TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter)
			throws Exception {
		startTransaction(false);
		// Check if is logged in
		TwitterUpdateDTOList list = null;
		try {
			list = TwitterServiceAdapter.get().getUpdates(twitterAccount,
					filter);
		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			endTransaction();
		}

		return list;

	}

	@Override
	public TwitterUpdateDTO postUpdate(TwitterUpdateDTO update)
			throws Exception {
		// Send tweet
		// Check if is logged in
		startTransaction(false);

		TwitterUpdateDTO lastUpdate = null;
		Status status = TwitterServiceAdapter.get().postUpdate(update);
		if (status != null) {
			lastUpdate = DataUtils.createTwitterUpdateDto(status, true);
		}
		endTransaction();
		return lastUpdate;

		// throw new Exception("Not implemented");

	}

	@Override
	public OAuthInfoDTO getOAuthInfo(TwitterAccountDTO twitterAccount)
			throws Exception {

		// throw new Exception("Not implemented");
		// TODO Auto-generated method stub
		// return TwitterServiceAdapter.get().getOAuthInfo(twitterAccount);
		return null;
	}

	@Override
	public TwitterAccountDTO authenticateUser(PersonaDTO personaDto,
			String pinCode) throws Exception {
		startTransaction(true);
		TwitterAccountDTO authorizedTwitterAccount = TwitterServiceAdapter
				.get()
				.authorizeAccount(personaDto.getTwitterAccount(), pinCode);

		User exUser = TwitterServiceAdapter.get().getExtendedUser(
				authorizedTwitterAccount);

		TwitterAccountDTO fullAuthorizeddAccount = DataUtils
				.mergeTwitterAccount(exUser, authorizedTwitterAccount);
		// Update DOmain Object in DB with new oauth token

		TwitterAccountDO twitterAccountDo = DataUtils
				.twitterAccountDoFromDto(fullAuthorizeddAccount);

		getBusinessHelper().getPersonaPojo().updatePersonaTwitterAccount(
				personaDto, twitterAccountDo);

		endTransaction();
		return fullAuthorizeddAccount;
	}

	@Override
	public TwitterAccountDTO getExtendedUserAccount(
			TwitterAccountDTO twitterAccount, String userIdOrScreenName)
			throws Exception {
		startTransaction(false);
		TwitterAccountDTO returnDto = null;

		try {
			returnDto = TwitterServiceAdapter.get().getExtendedUser(
					twitterAccount, userIdOrScreenName);
		} catch (Exception e) {
			log.severe("Error " + e.getMessage());
			throw new Exception(e);
		}
		endTransaction();
		return returnDto;

	}

	@Override
	public void followUser(TwitterAccountDTO account, boolean follow,
			Integer userId) throws Exception {
		startTransaction(false);

		try {
			TwitterServiceAdapter.get().followUser(account, follow, userId);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			endTransaction();
		}

	}

	@Override
	public void blockUser(TwitterAccountDTO account, boolean block,
			Integer userId) throws Exception {
		startTransaction(false);

		try {
			TwitterServiceAdapter.get().blockUser(account, block, userId);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			endTransaction();
		}

	}

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
	public void followUser(PersonaDTO currentPersona, TwitterAccountDTO user)
			throws Exception {
		startTransaction(true);
		log.fine("Follow User: "+user.getTwitterScreenName());
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
	public void unfollowUser(PersonaDTO currentPersona, TwitterAccountDTO user)
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
	public void blockUser(PersonaDTO currentPersona, TwitterAccountDTO user)
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
	public void unblockUser(PersonaDTO currentPersona, TwitterAccountDTO user)
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

}
