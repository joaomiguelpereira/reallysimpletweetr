package org.nideasystems.webtools.zwitrng.server;

import java.util.Date;
import java.util.List;

import javax.jdo.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nideasystems.webtools.zwitrng.client.services.TwitterPersonaService;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterService;
import org.nideasystems.webtools.zwitrng.server.utils.JSONUtils;

import twitter4j.DirectMessage;
import twitter4j.ExtendedUser;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TwitterPersonaServiceImpl extends RemoteServiceServlet implements
		TwitterPersonaService {

	private Twitter twitter = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3805847312414045223L;

	/**
	 * Create a new Persona and return the representation
	 */
	@Override
	public String createPersona(String personaAsString) {

		PersonaDAO personaDao = new PersonaDAO();
		// Check if is logged in
		User currentUser = UserServiceFactory.getUserService().getCurrentUser();

		if (currentUser == null) {
			return JSONUtils.createError("You must be logged in").toString();
		}

		// Try to Authenticate the
		String returnValue = null;
		JSONObject jsonObject = null;
		ExtendedUser twitterUser = null;

		try {
			jsonObject = new JSONObject(personaAsString);

			JSONObject twitterAccount = jsonObject
					.getJSONObject("twitterAccount");

			String personaName = jsonObject.getString("personaName");
			
			String twitterUseName = twitterAccount
					.getString("twitterScreenName");

			String twitterPassword = twitterAccount
					.getString("twitterPassword");

			if (personaName.isEmpty() || twitterUseName.isEmpty()
					|| twitterPassword.isEmpty()) {
				return JSONUtils.createError("Not all data provided. All field are required").toString();
			}

			// Check if account exists/credentials ok
			try {
				twitterUser = TwitterService.get().getExtendedUser(twitterUseName, twitterPassword, true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Login ok?
			if (twitterUser != null) {
				// cool, it exists
				// Try to create, throws exception if exists
				PersonaDO personaDo = null;
				try {
					personaDo = personaDao.createPersona(personaName, twitterUseName,
							twitterPassword, currentUser.getEmail());
					returnValue = PersonaDAO.createPersonaJson(personaDo, twitterUser).toString();
				} catch (Exception e) {
					returnValue = JSONUtils.createError(e.getMessage()).toString();

				}

			} else {
				returnValue = JSONUtils.createError("Twitter Account not found or Authentication failed").toString();
			}
		} catch (JSONException e) {
			returnValue = JSONUtils.createError("ERROR " + e.getMessage()).toString();
		}

		return returnValue;
	}

	

	/**
	 * Return a String representing a JSONArray with all personas for the current user
	 */
	@Override
	public String getPesonas() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		
		JSONArray jsonArray = new JSONArray();
		if (user != null) {
			PersonaDAO dao = new PersonaDAO();
			jsonArray =  dao.findAllPersonas(user.getEmail());
			// Get all Personas for email: email
		}

		String returnval = jsonArray.toString();
		return returnval;
	}

	@Override
	public String deletePersona(String persona) {
		// TODO Auto-generated method stub
		User user = UserServiceFactory.getUserService().getCurrentUser();
		PersonaDAO personDao = new PersonaDAO();
		personDao.deletePersona(persona,user.getEmail());
		return "ok";
	}

}
