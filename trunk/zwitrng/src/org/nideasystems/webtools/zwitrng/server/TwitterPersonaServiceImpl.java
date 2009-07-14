package org.nideasystems.webtools.zwitrng.server;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.client.services.TwitterPersonaService;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.server.utils.DtoAssembler;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTOList;
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
	public PersonaDTO createPersona(final PersonaDTO personaDto)
			throws Exception {
		log.fine("createPersona...");
		startTransaction(true);
		PersonaDO personaDo = null;

		if (personaDto.getTwitterAccount() == null) {
			throw new Exception("Please provide your twitter information");
		}

		if (personaDto.getName().isEmpty()) {
			throw new Exception("Not all data provided. All field are required");
		}

		log.fine("Creating a Pre-AutorizedTwitterAccount...");
		// Create a preAuthorized TwitterAccount

		TwitterAccountDTO preAutorizedTwitterAccount = TwitterServiceAdapter
				.createPreAuthorizedTwitterAccount();

		log.fine("Set the preAuthorizedAcctony to input PersonaDto...");
		personaDto.setTwitterAccount(preAutorizedTwitterAccount);

		log.fine("Creating persona in Datastore");
		personaDo = getBusinessHelper().getPersonaPojo().createPersona(
				personaDto, user.getEmail());

		endTransaction();

		return DtoAssembler.assemble(personaDo);
		// return DataUtils.personaDtoFromDo(personaDo);
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
		/*
		 * startTransaction(true); // get the DAO // Key personaKey = ; // User
		 * user = AuthorizationManager.checkAuthentication();
		 * 
		 * List<FilterCriteriaDTO> returnFilters = null; if (user != null) {
		 * 
		 * returnFilters = getBusinessHelper().getPersonaPojo().getAllFilters(
		 * personaName, user.getEmail());
		 * 
		 * // Get all Personas for email: email } endTransaction();
		 */
		throw new Exception("Not implemented");
		// return null;

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
				.saveTemplateFragment(object, model.getName(), user.getEmail());

		endTransaction();
		return returnFrag;
	}

	@Override
	public TemplateFragmentDTO deleteTemplateFragment(PersonaDTO model,
			TemplateFragmentDTO dataObject) throws Exception {
		startTransaction(true);
		TemplateFragmentDTO outTemplateFrag = getBusinessHelper()
				.getTemplatePojo().deleteTemplateFragment(model.getName(),
						user.getEmail(), dataObject);
		endTransaction();
		return outTemplateFrag;
	}

	@Override
	public Map<String, String> getTemplateFragmentsLists(PersonaDTO model,
			List<String> lists) throws Exception {
		startTransaction(true);
		Map<String, String> ret = getBusinessHelper().getTemplatePojo()
				.getFragmentsLists(model.getName(), user.getEmail(), lists);
		endTransaction();
		return ret;
	}

	@Override
	public CampaignDTOList getCampaigns(PersonaDTO model) throws Exception {
		CampaignDTOList retList = null;
		startTransaction(true);
		retList = getBusinessHelper().getCampaignPojo().findCampaigns(
				model.getName(), user.getEmail());
		endTransaction();
		return retList;
	}

	@Override
	public CampaignDTO createCampaign(PersonaDTO model, CampaignDTO object)
			throws Exception {

		CampaignDTO campaign = null;
		startTransaction(true);
		try {
			campaign = getBusinessHelper().getCampaignPojo().createCampaign(
					model.getName(), user.getEmail(), object);
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
			campaign = getBusinessHelper().getCampaignPojo().saveCampaign(
					model.getName(), user.getEmail(), object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		} finally {
			endTransaction();
		}

		return campaign;
	}

	@Override
	public CampaignDTO deleteCampaign(PersonaDTO model, CampaignDTO object)
			throws Exception {
		CampaignDTO campaign = null;
		startTransaction(true);
		try {
			campaign = getBusinessHelper().getCampaignPojo().deleteCampaign(
					model.getName(), user.getEmail(), object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		} finally {
			endTransaction();
		}

		return campaign;
	}

	// FeedSets
	@Override
	public FeedSetDTOList getFeedSets(PersonaDTO model) throws Exception {
		FeedSetDTOList returnList = null;
		startTransaction(true);
		try {
			returnList = getBusinessHelper().getFeedSetPojo().findFeedSets(
					model.getName(), user.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		} finally {
			endTransaction();
		}
		return returnList;
	}

	@Override
	public FeedSetDTO createFeedSet(PersonaDTO model, FeedSetDTO object)
			throws Exception {
		FeedSetDTO feedSet = null;
		startTransaction(true);
		try {
			feedSet = getBusinessHelper().getFeedSetPojo().createFeedSet(
					model.getName(), user.getEmail(), object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		} finally {
			endTransaction();
		}
		return feedSet;

	}

	@Override
	public FeedSetDTO deleteFeedSet(PersonaDTO model, FeedSetDTO object)
			throws Exception {
		FeedSetDTO feedSet = object;
		startTransaction(true);
		try {
			getBusinessHelper().getFeedSetPojo().deleteFeedSet(model.getName(),
					user.getEmail(), object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		} finally {
			endTransaction();
		}
		return feedSet;
	}

	@Override
	public FeedSetDTO saveFeedSet(PersonaDTO model, FeedSetDTO object)
			throws Exception {
		FeedSetDTO feedSet = null;
		startTransaction(true);
		try {
			feedSet = getBusinessHelper().getFeedSetPojo().saveFeedSet(
					model.getName(), user.getEmail(), object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		} finally {
			endTransaction();
		}
		return feedSet;

	}
}
