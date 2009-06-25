package org.nideasystems.webtools.zwitrng.server;

import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.client.services.TwitterPersonaService;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;


public class TwitterPersonaServiceImpl extends AbstractRemoteServiceServlet
		implements TwitterPersonaService {
	private static final long serialVersionUID = 3805847312414045223L;
	
	private static final Logger log = Logger
			.getLogger(TwitterPersonaServiceImpl.class.getName());

	public TwitterPersonaServiceImpl() {
		log.fine("Instantiating TwitterPersonaServiceImpl..");

	}

	/**
	 * Create a new Persona and return its representation
	 */
	@Override
	public PersonaDTO createPersona(final PersonaDTO persona) throws Exception {
		log.fine("createPersona...");
		startTransaction(true);

		try {

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
				personaDo = getBusinessHelper().getPersonaPojo().createPersona(persona,
						user.getEmail());
				// personaDo = getPersonaDao().createPersona(persona,
				// currentUser.getEmail());

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

		startTransaction(true);
		getBusinessHelper().getPersonaPojo().deletePersona(persona, user.getEmail());
		endTransaction();
		return persona;
	}

	@Override
	public PersonaDTOList getPersonas() throws Exception {
		log.fine("Start getting personas..");
		startTransaction(true);
				//User user = AuthorizationManager.checkAuthentication();

		PersonaDTOList returnPersonas = null;
		if (user != null) {
			try {

				//
				returnPersonas = getBusinessHelper().getPersonaPojo().getAllPersonas(
						user.getEmail());
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
		startTransaction(true);
		// get the DAO
		// Key personaKey = ;
		//User user = AuthorizationManager.checkAuthentication();

		List<FilterCriteriaDTO> returnFilters = null;
		if (user != null) {

			returnFilters = getBusinessHelper().getPersonaPojo().getAllFilters(personaName,
					user.getEmail());

			// Get all Personas for email: email
		}
		endTransaction();
		return returnFilters;

	}

	@Override
	public TemplateDTOList getTemplates(String name) throws Exception {
		startTransaction(true);
		TemplateDTOList list = null;
		list = getBusinessHelper().getTemplatePojo().getTemplates(name,user.getEmail());
		endTransaction();
		return list;
	}

	@Override
	public TemplateDTO createTemplate(PersonaDTO model, TemplateDTO template)
			throws Exception {
		startTransaction(true);
		TemplateDTO outTemplate = getBusinessHelper().getTemplatePojo().createTemplate(model.getName(), user.getEmail(),template); 
		endTransaction();
		return outTemplate;
		

	}

	@Override
	public TemplateDTO deleteTemplate(PersonaDTO model, TemplateDTO template)
			throws Exception {
		startTransaction(true);
		TemplateDTO outTemplate = getBusinessHelper().getTemplatePojo().deleteTemplate(model.getName(), user.getEmail(),template);
		endTransaction();
		return outTemplate;
	}

	

}
