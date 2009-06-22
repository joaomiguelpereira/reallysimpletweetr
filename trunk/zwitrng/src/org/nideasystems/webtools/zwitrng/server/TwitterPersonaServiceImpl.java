package org.nideasystems.webtools.zwitrng.server;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.nideasystems.webtools.zwitrng.client.services.TwitterPersonaService;
import org.nideasystems.webtools.zwitrng.server.domain.AbstractDAO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDAO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TwitterPersonaServiceImpl extends RemoteServiceServlet implements
		TwitterPersonaService {

	private long transactionsStartTime = 0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3805847312414045223L;

	ThreadLocal<PersonaDAO> personaDao = new ThreadLocal<PersonaDAO>() {

		@Override
		protected PersonaDAO initialValue() {
			return new PersonaDAO();
		}

	};

	private PersistenceManager pm = null;

	private static final Logger log = Logger
			.getLogger(TwitterPersonaServiceImpl.class.getName());

	public TwitterPersonaServiceImpl() {
		log.fine("Instantiating TwitterPersonaServiceImpl..");

	}

	private PersonaDAO getPersonaDao() {
		
		PersonaDAO dao = personaDao.get();
		dao.setPm(pm);
		log.fine("Returning DAO " + dao.hashCode());
		return dao;
	}

	private void startTransaction() {
		pm = PMF.get().getPersistenceManager();
		log.fine("Starting transaction...");
		transactionsStartTime = new Date().getTime();
	}

	private void endTransaction() throws Exception{
		long endTime = new Date().getTime();
		log.fine("Ending transation. Elaplsed millisenconds: "
				+ (endTime - transactionsStartTime) + " ms");
		
		try {
			pm.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
			
		}
	}

	/**
	 * Create a new Persona and return the representation
	 */
	@Override
	public PersonaDTO createPersona(final PersonaDTO persona) throws Exception {
		log.fine("createPersona...");
		startTransaction();

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

				personaDo = getPersonaDao().createPersona(persona, currentUser.getEmail());

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
		} finally {
			endTransaction();
		}
	}

	@Override
	public String deletePersona(String persona) throws Exception {

		startTransaction();

		User user = AuthorizationManager.checkAuthentication();
		getPersonaDao().deletePersona(persona, user.getEmail());
		endTransaction();
		return persona;
	}

	@Override
	public PersonaDTOList getPersonas() throws Exception {
		log.fine("Start getting personas..");
		startTransaction();
		User user = AuthorizationManager.checkAuthentication();
		
		PersonaDTOList returnPersonas = null;
		if (user != null) {
			try {
				
				returnPersonas = getPersonaDao().findAllPersonas(user.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
				log.severe(e.getMessage());
				throw new Exception(e);
			} finally {
				endTransaction();
			}
		}
		
		log.fine("End getting personas..");
		return returnPersonas;
	}

	@Override
	public List<FilterCriteriaDTO> getPersonaFilters(String personaName)
			throws Exception {
		startTransaction();
		// get the DAO
		// Key personaKey = ;
		User user = AuthorizationManager.checkAuthentication();

		List<FilterCriteriaDTO> returnFilters = null;
		if (user != null) {

			returnFilters = getPersonaDao().findAllPersonaFilters(personaName, user
					.getEmail());
			// Get all Personas for email: email
		}
		endTransaction();
		return returnFilters;

	}

	@Override
	public TemplateDTOList getTemplates(String name) throws Exception {
		TemplateDTOList list = new TemplateDTOList();
		/*
		 * for (long i=0; i<2; i++) { TemplateDTO t = new TemplateDTO();
		 * TemplateDTO t2 = new TemplateDTO();
		 * 
		 * t.setId(i);t.setTemplateText(
		 * "This is a template that is somewhat big. Lets try something new here. What-s upda to you"
		 * +i); t.addTags("Tag 1"); t.addTags("Tag 3"); t2.setId(i*100);
		 * t2.setTemplateText
		 * ("This is a template that is somewhat small"+i*100);
		 * t2.addTags("Tag 1"); t2.addTags("Tag 3"); list.addTemplate(t);
		 * list.addTemplate(t2);
		 * 
		 * } log.fine("returning "+list.getTemplates().size());
		 */
		return list;
	}

	@Override
	public TemplateDTO createTemplate(PersonaDTO model, TemplateDTO template)
			throws Exception {
		TemplateDTO t = new TemplateDTO();

		t.setId(1000 + template.hashCode());
		t.setTemplateText(template.getTemplateText());
		t.addTags("Tag 1");
		t.addTags("Tag 3");
		// PersonaDAO personaDao = new PersonaDAO();

		// TemplateDAO dao = new TemplateDAO();
		// return dao.createTemplate(model.getName(),template);
		return t;

	}

}
