package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nideasystems.webtools.zwitrng.server.PMF;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterService;
import org.nideasystems.webtools.zwitrng.server.utils.JSONUtils;

import twitter4j.ExtendedUser;

public class PersonaDAO {


	public static void deletePersona(String personaName, String email) {
		PersistenceManager pM = PMF.get().getPersistenceManager();
		Query queryPersona = pM.newQuery(
				PersonaDO.class);
		queryPersona
				.setFilter("name==paramPersonaName && userEmail==paramUserEmail");
		queryPersona
				.declareParameters("String paramPersonaName, String paramUserEmail");
		queryPersona.setUnique(true);

		PersonaDO persona = (PersonaDO) queryPersona.execute(personaName,
				email);
		
		if (persona!= null) {
			pM.deletePersistent(persona);
			pM.close();
		}
		

	}

	public static PersonaDO createPersona(String personaName,
			String twitterName, String password, String userEmail)
			throws Exception {

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
			// PMF.get().getPersistenceManager().makePersistent(twitterAccount);
			persona.setTwitterAccount(twitterAccount);
			PMF.get().getPersistenceManager().makePersistent(persona);
			System.out.println("Twitter Accounted created: "
					+ twitterAccount.getKey().toString());
			PMF.get().getPersistenceManager().close();

		} else {
			throw new Exception("Persona " + personaName + " already exixts");
		}

		System.out.println("Persona created: " + persona.getId());
		return persona;
	}

	public JSONArray findAllPersonas(String email) {

		JSONArray jsonArray = new JSONArray();
		Query queryPersona = PMF.get().getPersistenceManager().newQuery(
				PersonaDO.class);
		queryPersona.setFilter("userEmail==paramUserEmail");
		queryPersona.declareParameters("String paramUserEmail");

		List<PersonaDO> personas = (List<PersonaDO>) queryPersona
				.execute(email);

		if (personas.iterator().hasNext()) {

			for (PersonaDO persona : personas) {

				// now Try to login in TwitterAccount
				ExtendedUser twitterUser = null;

				if (persona.getTwitterAccount() != null) {

					try {
						twitterUser = TwitterService.get().getExtendedUser(
								persona.getTwitterAccount().getTwitterName(),
								persona.getTwitterAccount().getTwitterPass());
					} catch (Exception e) {

						// Could not get the twitter account
					}

				}

				jsonArray.put(PersonaDAO
						.createPersonaJson(persona, twitterUser));

			}
		}
		PMF.get().getPersistenceManager().close();
		return jsonArray;
	}

	public static JSONObject createPersonaJson(PersonaDO personaDo,
			ExtendedUser twitterUser) {

		// Create JSOn Object for personaInfo
		JSONObject persona = new JSONObject();
		try {
			persona = new JSONObject();
			persona.accumulate("personaName", personaDo.getName());
			persona.accumulate("personaCreationDate", personaDo
					.getCreationDate());

			if (twitterUser != null) {
				JSONObject twitterAccount = new JSONObject();
				twitterAccount.accumulate("twitterUserName", twitterUser
						.getName());
				twitterAccount.accumulate("twitterDescription", twitterUser
						.getDescription());
				twitterAccount.accumulate("twitterScreenName", twitterUser
						.getScreenName());
				twitterAccount.accumulate("twitterImageUrl", twitterUser
						.getProfileImageURL());

				persona.accumulate("twitterAccount", twitterAccount);

			} else {
				persona.accumulate("twitterAccount", null);
			}
		} catch (JSONException e) {
			return JSONUtils.createError(e.getMessage());
		}

		return persona;
	}

}
