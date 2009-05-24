package org.nideasystems.webtools.zwitrng.server;

import java.util.ArrayList;
import java.util.List;

import org.nideasystems.webtools.zwitrng.client.services.TwitterService;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;

import org.nideasystems.webtools.zwitrng.shared.OAuthInfoDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import twitter4j.ExtendedUser;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TwitterServiceImpl extends RemoteServiceServlet implements
		TwitterService {

	private static final List<String> whiteListEmails = new ArrayList<String>();
	static {
		whiteListEmails.add("joao");
		whiteListEmails.add("joaomiguel.pereira@gmail.com");

	}
	private UserService userService = UserServiceFactory.getUserService();;
	/**
	 * 
	 */
	private static final long serialVersionUID = -481643127871478064L;

	@Override
	public List<TwitterUpdateDTO> search(TwitterAccountDTO twitterAccount,
			FilterCriteriaDTO filter) throws Exception {

		// Check if is logged in
		User currentUser = userService.getCurrentUser();

		// do some validations...
		if (currentUser == null
				|| !whiteListEmails.contains(currentUser.getEmail())) {
			throw new Exception("You must be logged in");

		}

		return TwitterServiceAdapter.get().searchStatus(twitterAccount, filter);

	}

	@Override
	public List<TwitterUpdateDTO> getTwitterUpdates(
			TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter)
			throws Exception {
		// Check if is logged in
		User currentUser = userService.getCurrentUser();

		// do some validations...
		if (currentUser == null
				|| !whiteListEmails.contains(currentUser.getEmail())) {
			throw new Exception("You must be logged in");

		}

		return TwitterServiceAdapter.get().getUpdates(twitterAccount, filter);

	}

	@Override
	public TwitterUpdateDTO postUpdate(TwitterAccountDTO twitterAccount, String tweetText)
			throws Exception {
		//Send tweet
		// Check if is logged in
		User currentUser = userService.getCurrentUser();

		// do some validations...
		if (currentUser == null
				|| !whiteListEmails.contains(currentUser.getEmail())) {
			throw new Exception("You must be logged in");

		}

		return TwitterServiceAdapter.get().postUpdate(twitterAccount, tweetText);
		
		//throw new Exception("Not implemented");
		
	}

	@Override
	public OAuthInfoDTO getOAuthInfo(TwitterAccountDTO twitterAccount)
			throws Exception {
		
		//throw new Exception("Not implemented");
		// TODO Auto-generated method stub
		//return TwitterServiceAdapter.get().getOAuthInfo(twitterAccount);
		return null;
	}

	@Override
	public TwitterAccountDTO authenticateUser(PersonaDTO personaDto)
			throws Exception {
		
		TwitterAccountDTO authorizedTwitterAccount = TwitterServiceAdapter.get().authorizeAccount(personaDto.getTwitterAccount());
		
		ExtendedUser exUser = TwitterServiceAdapter.get().getExtendedUser(authorizedTwitterAccount);
		
		TwitterAccountDTO fullAuthorizeddAccount =  DataUtils.mergeTwitterAccount(exUser,authorizedTwitterAccount);
		//Update DOmain Object in DB with new oauth token
		PersonaDAO personaDao = new PersonaDAO();
		TwitterAccountDO twitterAccountDo = DataUtils.twitterAccountDoFromDto(fullAuthorizeddAccount);
		personaDao.updatePersonaTwitterAccount(personaDto,twitterAccountDo);
		
		
		
		
		
		/*twitter4j.User authenticatedTwitterUser = null;
		
		authenticatedTwitterUser = TwitterServiceAdapter.get().getAuthenticatedUser(twitterAccount);
		//Merge 
		TwitterAccountDTO authenticatedAccount =  DataUtils.mergeTwitterAccount(authenticatedTwitterUser,twitterAccount);
		
		//Update with Extended User
		ExtendedUser exUser = TwitterServiceAdapter.get().getExtendedUser(authenticatedAccount);
		//return authenticatedAccount;
		DataUtils.mergeExtendedUserAccount(exUser,authenticatedAccount);*/
		
		return fullAuthorizeddAccount;
	}

}
