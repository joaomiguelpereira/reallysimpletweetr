package org.nideasystems.webtools.zwitrng.server.domain.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.Query;

import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.FeedSetDO;
import org.nideasystems.webtools.zwitrng.server.domain.FilterDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaStatus;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateFragmentDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;

import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;

public class PersonaDAO extends BaseDAO {

	public PersonaDAO() {

	}

	private static final Logger log = Logger.getLogger(PersonaDAO.class
			.getName());

	/**
	 * delete a Persona
	 * 
	 * @param personaName
	 * @param email
	 */
	public void deletePersona(String personaName, String email) {

		// PersistenceManager pm = PMF.get().getPersistenceManager();
		Query queryPersona = pm.newQuery(PersonaDO.class);
		queryPersona
				.setFilter("name==paramPersonaName && userEmail==paramUserEmail");
		queryPersona
				.declareParameters("String paramPersonaName, String paramUserEmail");
		queryPersona.setUnique(true);

		PersonaDO persona = (PersonaDO) queryPersona
				.execute(personaName, email);

		if (persona != null) {
			pm.deletePersistent(persona);

		}

	}

	
	public List<PersonaDO> findAllPersonas(String email) throws Exception {

		Query queryPersona = pm.newQuery(PersonaDO.class);
		queryPersona.setFilter("userEmail==paramUserEmail");
		queryPersona.declareParameters("String paramUserEmail");

		List<PersonaDO> personas = (List<PersonaDO>) queryPersona
				.execute(email);

		return personas;

	}

	/**
	 * Find a persona by its name and email
	 * 
	 * @param personaName
	 * @param userEmail
	 * @return
	 */
	public PersonaDO findPersonaByNameAndEmail(String personaName,
			String userEmail) {
		// PersistenceManager pm = PMF.get().getPersistenceManager();
		Query queryPersona = pm.newQuery(PersonaDO.class);// PMF.get().getPersistenceManager().newQuery(
		// PersonaDO.class);
		queryPersona
				.setFilter("name==paramPersonaName && userEmail==paramUserEmail");
		queryPersona
				.declareParameters("String paramPersonaName, String paramUserEmail");
		queryPersona.setUnique(true);

		PersonaDO persona = (PersonaDO) queryPersona.execute(personaName,
				userEmail);

		return persona;

	}

	/**
	 * Create a new Persona in DB
	 * 
	 * @param personaDto
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public PersonaDO createPersona(PersonaDTO personaDto, String email)
			throws Exception {

		// It cannot have personas, that is, twitter accounts with the same name

		if (findPersonaByNameAndEmail(personaDto.getName(), email) != null) {
			throw new Exception("The persona with name " + personaDto.getName()
					+ " already exists");
		}

		// Create a PersonaDO from a DTO
		PersonaDO personaDom = new PersonaDO();
		
		personaDom.setCreated(new Date());
		personaDom.setModified(new Date());
		
		personaDom.setName(personaDto.getName());
		personaDom.setUserEmail(email);
		//No campaigns
		personaDom.setCampaigns(new ArrayList<CampaignDO>());
		personaDom.setFeedSets(new ArrayList<FeedSetDO>());
		personaDom.setFilters(new ArrayList<FilterDO>());
		personaDom.setTemplateFragments(new ArrayList<TemplateFragmentDO>());
		personaDom.setTemplates(new ArrayList<TemplateDO>());
		personaDom.setTwitterAccount(new TwitterAccountDO());
		personaDom.setStatus(PersonaStatus.ACTIVE);
		
		//PersonaDO personaToSave = DataUtils.personaDofromDto(persona, email);
		// Make the Object Persistent
		try {
			pm.makePersistent(personaDom);
		} catch (Exception e) {
			// Nothing special here... Just log and let the client handle it
			log.severe("Error: " + e.getLocalizedMessage());
			throw e;

		}
		return personaDom;

	}


	/**
	 * @deprecated
	 * @param personaName
	 * @param userEmail
	 * @return
	 */
	public List<FilterCriteriaDTO> findAllPersonaFilters(String personaName,
			String userEmail) {
		// PersistenceManager pm = PMF.get().getPersistenceManager();

		List<FilterDO> filters = null;
		List<FilterCriteriaDTO> returnList = new ArrayList<FilterCriteriaDTO>();
		;
		PersonaDO persona = this.findPersonaByNameAndEmail(personaName,
				userEmail);

		if (persona != null) {
			// I've found it
			// Now copy the filter list
			filters = persona.getFilters();

			if (filters != null && filters.size() > 0) {
				// Convert to DTO FILTER
				for (FilterDO filterDo : filters) {
					returnList.add(DataUtils.createFilterCriteriaDto(filterDo));
				}

			}
		}

		return returnList;
	}

	public void updatePersonaTwitterAccount(PersonaDTO personaDto,
			TwitterAccountDO twitterAccountDo) throws Exception {
		// Find the persona
		// PersistenceManager pm = PMF.get().getPersistenceManager();
		Query queryPersona = pm.newQuery(PersonaDO.class);// PMF.get().getPersistenceManager().newQuery(
		// PersonaDO.class);
		queryPersona
				.setFilter("name==paramPersonaName && userEmail==paramUserEmail");
		queryPersona
				.declareParameters("String paramPersonaName, String paramUserEmail");
		queryPersona.setUnique(true);

		PersonaDO persona = (PersonaDO) queryPersona.execute(personaDto
				.getName(), personaDto.getUserEmail());

		if (persona == null) {
			throw new Exception("Persona not found");
		}
		if (persona.getTwitterAccount() != null) {
			persona.getTwitterAccount().setOAuthLoginUrl("");
			persona.getTwitterAccount().setOAuthToken(
					twitterAccountDo.getOAuthToken());
			persona.getTwitterAccount().setOAuthTokenSecret(
					twitterAccountDo.getOAuthTokenSecret());

		} else {
			persona.setTwitterAccount(twitterAccountDo);
		}

	}

	public AutoFollowRuleDO getAutoFollowRule(PersonaDO parentPersona, AutoFollowTriggerType on_follow_me) throws Exception {
		
		Query autoFollowRule = pm.newQuery(AutoFollowRuleDO.class);
		autoFollowRule.setFilter("triggerType==theTriggerType && persona==parentPersona");
		
		autoFollowRule.declareParameters("org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType theTriggerType, PersonaDO parentPersona");
		autoFollowRule.setUnique(true);
		
		AutoFollowRuleDO rule = (AutoFollowRuleDO)autoFollowRule.execute(on_follow_me,parentPersona);
		
		return rule;
	}

	public List<PersonaDO> findAllActivePersonas() {
		
		Query queryPersona = pm.newQuery(PersonaDO.class);
		
		queryPersona.setFilter("status==theStatus");
		queryPersona
		.declareParameters("org.nideasystems.webtools.zwitrng.server.domain.PersonaStatus theStatus");
		PersonaStatus status = PersonaStatus.ACTIVE;
		
		return (List<PersonaDO>)queryPersona.execute(status);
	}

}
