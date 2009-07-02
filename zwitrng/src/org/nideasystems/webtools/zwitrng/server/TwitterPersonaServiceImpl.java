package org.nideasystems.webtools.zwitrng.server;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.client.services.TwitterPersonaService;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTODTOList;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FilterOperator;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TimeUnits;
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
				personaDo = getBusinessHelper().getPersonaPojo().createPersona(
						persona, user.getEmail());
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
		getBusinessHelper().getPersonaPojo().deletePersona(persona,
				user.getEmail());
		endTransaction();
		return persona;
	}

	@Override
	public PersonaDTOList getPersonas() throws Exception {
		log.fine("Start getting personas..");
		startTransaction(true);
		// User user = AuthorizationManager.checkAuthentication();

		PersonaDTOList returnPersonas = null;
		if (user != null) {
			try {

				//
				returnPersonas = getBusinessHelper().getPersonaPojo()
						.getAllPersonas(user.getEmail());
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
		// User user = AuthorizationManager.checkAuthentication();

		List<FilterCriteriaDTO> returnFilters = null;
		if (user != null) {

			returnFilters = getBusinessHelper().getPersonaPojo().getAllFilters(
					personaName, user.getEmail());

			// Get all Personas for email: email
		}
		endTransaction();
		return returnFilters;

	}

	@Override
	public TemplateDTOList getTemplates(String name) throws Exception {
		startTransaction(true);
		TemplateDTOList list = null;
		list = getBusinessHelper().getTemplatePojo().getTemplates(name,
				user.getEmail());
		endTransaction();
		return list;
	}

	@Override
	public TemplateDTO createTemplate(PersonaDTO model, TemplateDTO template)
			throws Exception {
		startTransaction(true);
		TemplateDTO outTemplate = getBusinessHelper().getTemplatePojo()
				.createTemplate(model.getName(), user.getEmail(), template);
		endTransaction();
		return outTemplate;

	}

	@Override
	public TemplateDTO deleteTemplate(PersonaDTO model, TemplateDTO template)
			throws Exception {
		startTransaction(true);
		TemplateDTO outTemplate = getBusinessHelper().getTemplatePojo()
				.deleteTemplate(model.getName(), user.getEmail(), template);
		endTransaction();
		return outTemplate;
	}

	@Override
	public TemplateDTO saveTemplate(PersonaDTO model, TemplateDTO template)
			throws Exception {
		startTransaction(true);
		TemplateDTO outTemplate = getBusinessHelper().getTemplatePojo()
				.saveTemplate(model.getName(), user.getEmail(), template);
		endTransaction();
		return outTemplate;
	}

	@Override
	public TemplateFragmentDTOList getTemplateFragments(PersonaDTO personaDto)
			throws Exception {
		startTransaction(true);
		TemplateFragmentDTOList outTemplateFragList = getBusinessHelper()
				.getTemplatePojo().getTemplateFragments(personaDto.getName(),
						user.getEmail());
		endTransaction();
		return outTemplateFragList;
	}

	@Override
	public TemplateFragmentDTO createTemplateFragment(PersonaDTO model,
			TemplateFragmentDTO object) throws Exception {

		startTransaction(true);
		
		TemplateFragmentDTO returnFrag = getBusinessHelper().getTemplatePojo()
				.createTemplateFragment(object, model.getName(),
						user.getEmail());
		endTransaction();
		return returnFrag;
	}

	@Override
	public TemplateFragmentDTO saveTemplateFragment(PersonaDTO model,
			TemplateFragmentDTO object) throws Exception {
		startTransaction(true);
		TemplateFragmentDTO returnFrag = getBusinessHelper().getTemplatePojo()
		.saveTemplateFragment(object, model.getName(),
				user.getEmail());

		endTransaction();
		return returnFrag;
	}

	@Override
	public TemplateFragmentDTO deleteTemplateFragment(PersonaDTO model,
			TemplateFragmentDTO dataObject) throws Exception {
		startTransaction(true);
		TemplateFragmentDTO outTemplateFrag = getBusinessHelper().getTemplatePojo()
				.deleteTemplateFragment(model.getName(), user.getEmail(), dataObject);
		endTransaction();
		return outTemplateFrag;
	}

	@Override
	public Map<String, String> getTemplateFragmentsLists(PersonaDTO model,
			List<String> lists) throws Exception {
		startTransaction(true);
		Map<String, String> ret = getBusinessHelper().getTemplatePojo().getFragmentsLists(model.getName(), user.getEmail(), lists);
		endTransaction();
		return ret;
	}

	@Override
	public CampaignDTODTOList getCampaigns(PersonaDTO model) throws Exception {
		CampaignDTODTOList retList = null;
		startTransaction(true);
		retList = getBusinessHelper().getCampaignPojo().findCampaigns(model.getName(),user.getEmail());
		endTransaction();
		return retList;
	}

	@Override
	public CampaignDTO createCampaign(PersonaDTO model, CampaignDTO object)
			throws Exception {
		
		CampaignDTO campaign = null;
		startTransaction(true);
		try {
			campaign = getBusinessHelper().getCampaignPojo().createCampaign(model.getName(),user.getEmail(),object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
			
		} finally {
			endTransaction();
		}
		
		return campaign;
	}

	@Override
	public CampaignDTO saveCampaign(PersonaDTO model, CampaignDTO object)
			throws Exception {
		CampaignDTO campaign = null;
		startTransaction(true);
		try {
			campaign = getBusinessHelper().getCampaignPojo().saveCampaign(model.getName(),user.getEmail(),object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
			
		} finally {
			endTransaction();
		}
		
		return campaign;	}

	@Override
	public CampaignDTO deleteCampaign(PersonaDTO model, CampaignDTO object)
			throws Exception {
		CampaignDTO campaign = null;
		startTransaction(true);
		try {
			campaign = getBusinessHelper().getCampaignPojo().deleteCampaign(model.getName(),user.getEmail(),object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
			
		} finally {
			endTransaction();
		}
		
		return campaign;		}

}
