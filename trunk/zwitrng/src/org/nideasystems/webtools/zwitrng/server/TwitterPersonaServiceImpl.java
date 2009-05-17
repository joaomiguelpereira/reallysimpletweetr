package org.nideasystems.webtools.zwitrng.server;

import java.util.Date;
import java.util.List;

import javax.jdo.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nideasystems.webtools.zwitrng.client.TwitterPersonaService;
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
				twitterUser = TwitterService.get().getExtendedUser(twitterUseName, twitterPassword);
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
					personaDo = PersonaDAO.createPersona(personaName, twitterUseName,
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

	/*private String createPersonaJson(PersonaDO personaDo,
			ExtendedUser twitterUser) {

		// Create JSOn Object for personaInfo
		JSONObject persona = new JSONObject();
		try {
			persona = new JSONObject();
			persona.accumulate("personaName", personaDo.getName());
			persona.accumulate("personaCreationDate", personaDo
					.getCreationDate());

			JSONObject twitterAccount = new JSONObject();
			twitterAccount.accumulate("twitterUserName", twitterUser.getName());
			twitterAccount.accumulate("twitterDescription", twitterUser
					.getDescription());
			twitterAccount.accumulate("twitterScreenName", twitterUser
					.getScreenName());
			twitterAccount.accumulate("twitterImageUrl", twitterUser
					.getProfileImageURL());

			persona.accumulate("twitterAccount", twitterAccount);
		} catch (JSONException e) {
			return JSONUtils.createError(e.getMessage()).toString();
		}

		return persona.toString();
	}
*/
	/*@SuppressWarnings("unchecked")
	public PersonaDO createPersona(String personaName, String twitterName,
			String password, String userEmail) throws Exception {

		Query queryPersona = PMF.get().getPersistenceManager().newQuery(
				PersonaDO.class);
		queryPersona
				.setFilter("name==paramPersonaName && userEmail==paramUserEmail");
		queryPersona
				.declareParameters("String paramPersonaName, String paramUserEmail");
		queryPersona.setUnique(true);

		PersonaDO persona = (PersonaDO) queryPersona.execute(personaName,
				userEmail);

		// If could not found, cool, we'll create it
		if (persona == null) {
			persona = new PersonaDO();
			persona.setCreationDate(new Date());
			persona.setName(personaName);
			persona.setUserEmail(userEmail);
			

			// Create the twitterAccount
			TwitterAccountDO twitterAccount = new TwitterAccountDO();
			twitterAccount.setTwitterName(twitterName);
			twitterAccount.setTwitterPass(password);
			//PMF.get().getPersistenceManager().makePersistent(twitterAccount);
			persona.setTwitterAccount(twitterAccount);
			PMF.get().getPersistenceManager().makePersistent(persona);
			System.out.println("Twitter Accounted created: "
					+ twitterAccount.getKey().toString());

			
		} else {
			throw new Exception("Persona " + personaName + " already exixts");
		}

		System.out.println("Persona created: " + persona.getId());
		return persona;

	}

	private ExtendedUser accountExists(String twitterUseName,
			String twitterPassword) {
		this.twitter = new Twitter(twitterUseName, twitterPassword);

		// Try an operation to login
		try {
			List<DirectMessage> msgs = this.twitter.getDirectMessages();
		} catch (TwitterException e1) {
			e1.printStackTrace();
			return null;
		}

		ExtendedUser userDetails = null;
		if (twitter != null) {
			String userId = twitter.getUserId();
			if (userId != null && !userId.isEmpty()) {
				try {
					userDetails = twitter.getUserDetail(userId);
					System.out.println(userDetails.getCreatedAt());
					System.out.println(userDetails.getDescription());
					System.out.println(userDetails.getFavouritesCount());
					System.out.println(userDetails.getFollowersCount());
					System.out.println(userDetails.getFriendsCount());
					System.out.println(userDetails.getLocation());
					System.out.println(userDetails.getScreenName());
					System.out.println(userDetails.getStatusesCount());
					System.out.println(userDetails.getProfileImageURL());

				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return userDetails;

	}

	private String createError(String string) {
		String retValue = null;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("errorMsg", string);
			jsonObject.accumulate("serverTime", new Date());
			retValue = jsonObject.toString();
		} catch (JSONException e) {

		}
		return retValue;
	}*/

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
		
		PersonaDAO.deletePersona(persona,user.getEmail());
		return "ok";
	}

}
