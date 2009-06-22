package org.nideasystems.webtools.zwitrng.server.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.nideasystems.webtools.zwitrng.server.PMF;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import twitter4j.User;

public class PersonaDAO extends BaseDAO {

	
	private PersistenceManager pm;
	
	public PersonaDAO() {
		
	}
	private static final Logger log = Logger.getLogger(PersonaDAO.class.getName());
	/**
	 * delete a Persona
	 * 
	 * @param personaName
	 * @param email
	 */
	public void deletePersona(String personaName, String email) {

		//PersistenceManager pm = PMF.get().getPersistenceManager();
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

	

	/**
	 * Get all personas
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 *  
	 */
	
	public PersonaDTOList findAllPersonas(String email) throws Exception {

		PersonaDTOList returnList = new PersonaDTOList();
		Query queryPersona = pm.newQuery(PersonaDO.class);
		queryPersona.setFilter("userEmail==paramUserEmail");
		queryPersona.declareParameters("String paramUserEmail");

		
		List<PersonaDO> personas = (List<PersonaDO>) queryPersona
				.execute(email);

		
		if (personas.iterator().hasNext()) {

			for (PersonaDO persona : personas) {
				
				
				// now Try to get twitt user info
				User twitterUser = null;
				TwitterAccountDTO authorizedTwitterAccount = null;
				
				
				if (persona.getTwitterAccount() != null) {
					//Check if is authenticated
					//Create an TwitterAccountDTO 
					authorizedTwitterAccount = DataUtils.twitterAccountDtoFromDo(persona.getTwitterAccount());
					//try to authenticate the User
					try {
						twitterUser = TwitterServiceAdapter.get().getExtendedUser(authorizedTwitterAccount);
					} catch (Exception e) {
						//No prob, just mean that the user has to authenticate
						e.printStackTrace();
					}

				} 
				
				
				
				if (twitterUser != null ) {
					authorizedTwitterAccount = DataUtils.mergeTwitterAccount(twitterUser, authorizedTwitterAccount);
					authorizedTwitterAccount.setIsOAuthenticated(true);
				} else {
					authorizedTwitterAccount =  TwitterServiceAdapter.get().getPreAuthorizedTwitterAccount();
					
				}
				
				returnList.addPersona(DataUtils.createPersonaDto(persona, authorizedTwitterAccount));

			}
		}
		
		
		return returnList;
		
	}

	private PersonaDO findPersonaByNameAndEmail(String personaName,
			String userEmail) {
		//PersistenceManager pm = PMF.get().getPersistenceManager();
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
	 * @param persona
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public PersonaDO createPersona(PersonaDTO persona, String email)
			throws Exception {
		//PersistenceManager pm = PMF.get().getPersistenceManager();

		// If it exists, throw an exception
		if (findPersonaByNameAndEmail(persona.getName(), email) != null) {
			throw new Exception("The persona with name " + persona.getName()
					+ " already exists");
		}

		// Create a PersonaDO from a DTO
		PersonaDO personaToSave = DataUtils.personaDofromDto(persona, email);

		StringBuffer sb =new StringBuffer();
		sb.append("\nIn DAO created a persona do Save:\n");
		sb.append("\nPersona Name: "+personaToSave.getName());
		sb.append("\nPersona Email: "+personaToSave.getUserEmail());
		sb.append("\nPersona Twitter Account URL:"+personaToSave.getTwitterAccount().getOAuthLoginUrl());
		sb.append("\nPersona Twitter Account Token:"+personaToSave.getTwitterAccount().getOAuthToken());
		sb.append("\nPersona Twitter Account TokenSecret:"+personaToSave.getTwitterAccount().getOAuthTokenSecret());
		
		log.fine(sb.toString());
		// Make the Object Persistent
		try {
			pm.makePersistent(personaToSave);
		} catch (Exception e) {
			// Nothing special here... Just log and let the client handle it
			log.severe("Error: "+e.getLocalizedMessage());
			throw e;
			
		} 
		
		sb = new StringBuffer();
		
		sb.append("\nIn DAO after created a persona do Save:\n");
		sb.append("\nPersona Name: "+personaToSave.getName());
		sb.append("\nPersona Email: "+personaToSave.getUserEmail());
		sb.append("\nPersona Twitter Account URL:"+personaToSave.getTwitterAccount().getOAuthLoginUrl());
		sb.append("\nPersona Twitter Account Token:"+personaToSave.getTwitterAccount().getOAuthToken());
		sb.append("\nPersona Twitter Account TokenSecret:"+personaToSave.getTwitterAccount().getOAuthTokenSecret());

		// return the persisted object
		return personaToSave;

	}
/**
 * @deprecated
 * @param personaName
 * @param userEmail
 * @return
 */
	public List<FilterCriteriaDTO> findAllPersonaFilters(String personaName, String userEmail) {
		//PersistenceManager pm = PMF.get().getPersistenceManager();
		
		
		List<FilterDO> filters = null;
		List<FilterCriteriaDTO> returnList = new ArrayList<FilterCriteriaDTO>();;
		PersonaDO persona = this.findPersonaByNameAndEmail(personaName, userEmail);
		
		if ( persona != null ) {
			//I've found it 
			//Now copy the filter list
			filters = persona.getFilters();
			
			if (filters!= null && filters.size()>0 ) {
				//Convert to DTO FILTER
				for (FilterDO filterDo : filters ) {
					returnList.add(DataUtils.createFilterCriteriaDto(filterDo));
				}
				
			}
		}
		
		return returnList;
	}

	public void updatePersonaTwitterAccount(PersonaDTO personaDto, TwitterAccountDO twitterAccountDo) throws Exception {
		//Find the persona
		//PersistenceManager pm = PMF.get().getPersistenceManager();
		Query queryPersona = pm.newQuery(PersonaDO.class);// PMF.get().getPersistenceManager().newQuery(
		// PersonaDO.class);
		queryPersona
				.setFilter("name==paramPersonaName && userEmail==paramUserEmail");
		queryPersona
				.declareParameters("String paramPersonaName, String paramUserEmail");
		queryPersona.setUnique(true);

		PersonaDO persona = (PersonaDO) queryPersona.execute(personaDto.getName(),
				personaDto.getUserEmail());

		
		if ( persona == null ) {
			throw new Exception("Persona not found");
		}
		if (persona.getTwitterAccount()!= null) {
			persona.getTwitterAccount().setOAuthLoginUrl("");
			persona.getTwitterAccount().setOAuthToken(twitterAccountDo.getOAuthToken());
			persona.getTwitterAccount().setOAuthTokenSecret(twitterAccountDo.getOAuthTokenSecret());
			
		} else {
			persona.setTwitterAccount(twitterAccountDo);
		}
		
		
		
		
		
		
	}



	public void setPm(PersistenceManager pm) {
		this.pm = pm;
	}



	public PersistenceManager getPm() {
		return pm;
	}

	
}
