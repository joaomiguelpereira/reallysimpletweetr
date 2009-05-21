package org.nideasystems.webtools.zwitrng.server;

import java.util.List;
import java.util.logging.Logger;


import org.nideasystems.webtools.zwitrng.client.services.TwitterPersonaService;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterService;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;

import twitter4j.ExtendedUser;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TwitterPersonaServiceImpl extends RemoteServiceServlet implements
		TwitterPersonaService {

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

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
	private UserService userService;
	private PersonaDAO personaDao;
	private static final Logger log = Logger
			.getLogger(TwitterPersonaServiceImpl.class.getName());

	public TwitterPersonaServiceImpl() {
		log.fine("Instantiating TwitterPersonaServiceImpl..");
		this.userService = UserServiceFactory.getUserService();
		this.personaDao = new PersonaDAO();

	}

	/**
	 * Create a new Persona and return the representation
	 */
	@Override
	public PersonaDTO createPersona(final PersonaDTO persona) throws Exception {

		// Check if is logged in
		User currentUser = userService.getCurrentUser();

		// do some validations...
		if (currentUser == null) {
			throw new Exception("You must be logged in");

		}
		if (persona.getTwitterAccount() == null) {
			throw new Exception("Please provide your twitter information");
		}

		if (persona.getName().isEmpty()
				|| persona.getTwitterAccount().getTwitterScreenName().isEmpty()
				|| persona.getTwitterAccount().getTwitterPassword().isEmpty()) {
			throw new Exception("Not all data provided. All field are required");
		}

		ExtendedUser twitterUser = null;

		// Check if account exists/credentials ok
		try {
			twitterUser = TwitterService.get().getExtendedUser(
					persona.getTwitterAccount().getTwitterScreenName(),
					persona.getTwitterAccount().getTwitterPassword(), true);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new Exception(e1);
		}

		PersonaDO personaDo = null;
		try {
			personaDo = personaDao.createPersona(persona, currentUser
					.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		}

		return DataUtils.createPersonaDto(personaDo, twitterUser);
	}

	/**
	 * Return a String representing a JSONArray with all personas for the
	 * current user
	 */
	/*
	 * @Override public String getPesonas() {
	 * 
	 * User user = UserServiceFactory.getUserService().getCurrentUser();
	 * 
	 * JSONArray jsonArray = new JSONArray(); if (user != null) {
	 * 
	 * jsonArray = personaDao.findAllPersonas(user.getEmail()); // Get all
	 * Personas for email: email }
	 * 
	 * String returnval = jsonArray.toString(); return returnval; }
	 */

	@Override
	public String deletePersona(String persona) {
		// TODO Auto-generated method stub
		User user = UserServiceFactory.getUserService().getCurrentUser();

		personaDao.deletePersona(persona, user.getEmail());
		return persona;
	}

	@Override
	public List<PersonaDTO> getPersonas() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		List<PersonaDTO> returnPersonas = null;
		if (user != null) {

			returnPersonas = personaDao.findAllPersonas(user.getEmail());
			// Get all Personas for email: email
		}

		return returnPersonas;
	}

	@Override
	public List<FilterCriteriaDTO> getPersonaFilters(String personaName) {
		
		//get the DAO
		//Key personaKey = ;
		User user = UserServiceFactory.getUserService().getCurrentUser();
		List<FilterCriteriaDTO> returnFilters = null;
		if (user != null) {

			returnFilters = personaDao.findAllPersonaFilters(personaName,user.getEmail());
			// Get all Personas for email: email
		}

		return returnFilters;
		
		
		
	}

}
