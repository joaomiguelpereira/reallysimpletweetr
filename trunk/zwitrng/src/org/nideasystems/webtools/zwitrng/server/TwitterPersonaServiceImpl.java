package org.nideasystems.webtools.zwitrng.server;


import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.client.services.TwitterPersonaService;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TwitterPersonaServiceImpl extends RemoteServiceServlet implements
		TwitterPersonaService {
	

	

	public PersonaDAO getPersonaDao() {
		return personaDao;
	}

	public void setPersonaDao(PersonaDAO personaDao) {
		this.personaDao = personaDao;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3805847312414045223L;
	
	private PersonaDAO personaDao;
	private static final Logger log = Logger
			.getLogger(TwitterPersonaServiceImpl.class.getName());

	public TwitterPersonaServiceImpl() {
		log.fine("Instantiating TwitterPersonaServiceImpl..");
		
		this.personaDao = new PersonaDAO();

	}

	

	/**
	 * Create a new Persona and return the representation
	 */
	@Override
	public PersonaDTO createPersona(final PersonaDTO persona) throws Exception {

		log.fine("createPersona");
		log.fine("persona Name:" + persona.getName());

		try {
			User currentUser = AuthorizationManager.checkAuthentication();

			if (persona.getTwitterAccount() == null) {
				throw new Exception("Please provide your twitter information");
			}

			if (persona.getName().isEmpty()) {
				throw new Exception(
						"Not all data provided. All field are required");
			}

			log.fine("preAutorizedTwitterAccount...");
			// Create a preAuthorized TwitterAccount
			TwitterAccountDTO preAutorizedTwitterAccount = TwitterServiceAdapter
					.get().getPreAuthorizedTwitterAccount();
			log.fine("preAutorizedTwitterAccount Url "
					+ preAutorizedTwitterAccount.getOAuthLoginUrl());
			log.fine("preAutorizedTwitterAccount Token "
					+ preAutorizedTwitterAccount.getOAuthToken());
			log.fine("preAutorizedTwitterAccount Token Secret "
					+ preAutorizedTwitterAccount.getOAuthTokenSecret());

			log.fine("Set the preAuthorizedAcctony to input PersonaDto...");
			persona.setTwitterAccount(preAutorizedTwitterAccount);

			PersonaDO personaDo = null;
			try {
				log.fine("Creating persona in Datastore");
				personaDo = personaDao.createPersona(persona, currentUser
						.getEmail());
			} catch (Exception e) {
				log.severe(e.getMessage());
				throw e;

			}
			return DataUtils.personaDtoFromDo(personaDo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.severe(e.getMessage());
			throw new Exception(e);
		}
	}

	@Override
	public String deletePersona(String persona) throws Exception {

		User user = AuthorizationManager.checkAuthentication();
		
		personaDao.deletePersona(persona, user.getEmail());
		return persona;
	}

	@Override
	public List<PersonaDTO> getPersonas() throws Exception {
		User user = AuthorizationManager.checkAuthentication();
		
		log.fine("Getting personas..");
		List<PersonaDTO> returnPersonas = null;
		if (user != null) {

			try {
				returnPersonas = personaDao.findAllPersonas(user.getEmail());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.severe(e.getMessage());
				throw new Exception(e);
			}
			// Get all Personas for email: email
		}

		return returnPersonas;
	}

	@Override
	public List<FilterCriteriaDTO> getPersonaFilters(String personaName)
			throws Exception {

		// get the DAO
		// Key personaKey = ;
		User user = AuthorizationManager.checkAuthentication();
		
		List<FilterCriteriaDTO> returnFilters = null;
		if (user != null) {

			returnFilters = personaDao.findAllPersonaFilters(personaName, user
					.getEmail());
			// Get all Personas for email: email
		}

		return returnFilters;

	}

}
