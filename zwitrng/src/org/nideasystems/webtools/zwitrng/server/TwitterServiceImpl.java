package org.nideasystems.webtools.zwitrng.server;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountListDTO;
import org.nideasystems.webtools.zwitrng.client.services.TwitterService;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;

import org.nideasystems.webtools.zwitrng.shared.OAuthInfoDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;

import twitter4j.Status;
import twitter4j.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TwitterServiceImpl extends RemoteServiceServlet implements
		TwitterService {

	private static final long serialVersionUID = -481643127871478064L;
	private static final Logger log = Logger.getLogger(TwitterServiceImpl.class
			.getName());
	private PersistenceManager pm;
	private long transactionStartTime;

	ThreadLocal<PersonaDAO> personaDao = new ThreadLocal<PersonaDAO>() {

		@Override
		protected PersonaDAO initialValue() {
			return new PersonaDAO();
		}

	};

	private PersonaDAO getPersonaDao() {

		PersonaDAO dao = personaDao.get();
		dao.setPm(pm);
		log.fine("Returning DAO " + dao.hashCode());
		return dao;
	}

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
		AuthorizationManager.checkAuthentication();
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
		AuthorizationManager.checkAuthentication();

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
		getPersonaDao().updatePersonaTwitterAccount(personaDto,
				twitterAccountDo);
		endTransaction();
		return fullAuthorizeddAccount;
	}

	private void endTransaction() {
		long endTransactionTime = new Date().getTime();
		log.fine("End trasaction in "
				+ (endTransactionTime - transactionStartTime) + " ms");
		if (this.pm != null && !this.pm.isClosed()) {
			try {
				pm.close();
			} catch (Exception e) {
				log.severe("exception: " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	private void startTransaction(boolean persistenceNeeded) {
		log.fine("Starting transaction. PersistenceNeeded? "
				+ persistenceNeeded);
		if (persistenceNeeded) {
			this.pm = PMF.get().getPersistenceManager();
		}
		this.transactionStartTime = new Date().getTime();

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
	public TwitterAccountListDTO getUsers(TwitterAccountDTO account,
			TwitterUserFilterDTO currentFilter) throws Exception {
		startTransaction(false);
		TwitterAccountListDTO list = null;
		try {
			list = TwitterServiceAdapter.get().getUsers(account, currentFilter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			endTransaction();
		}
		return list;
		
	}

}
