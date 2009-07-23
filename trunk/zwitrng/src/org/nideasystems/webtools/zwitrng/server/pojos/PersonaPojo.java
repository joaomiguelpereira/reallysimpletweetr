package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.List;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.RateLimitsDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.RateLimitsDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import twitter4j.User;



public class PersonaPojo extends AbstractPojo {

	public PersonaDO createPersona(PersonaDTO persona, String email) throws Exception {
		//Create the Persona Domain Object in Database
		PersonaDO personaDo = businessHelper.getPersonaDao().createPersona(persona, email);
		//Create the TwitterAccount
		 businessHelper.getTwitterAccountDao().populateTwitterAccount(personaDo, persona.getTwitterAccount());
		return personaDo;
		
		//return businessHelper.getPersonaDao().createPersona(persona, email);
	}

	public void deletePersona(String persona, String email) throws Exception{
		businessHelper.getPersonaDao().deletePersona(persona, email);
		
	}

	public PersonaDTOList getAllPersonas(String email) throws Exception {
		//before return build a proper TwitterAccount
		List<PersonaDO> personas = businessHelper.getPersonaDao().findAllPersonas(email);
		PersonaDTOList returnList = new PersonaDTOList();
		//For each persona build the DTO
		for (PersonaDO persona:personas) {
				
				// now Try to get authorize the twitter account
				User twitterUser = null;
				TwitterAccountDTO authorizedTwitterAccount = null;
				
				if (persona.getTwitterAccount() != null) {
					//Check if is authenticated
					//Create an TwitterAccountDTO 
					
					authorizedTwitterAccount = TwitterAccountDAO.createAuthorizedAccountDto(persona.getTwitterAccount());
					
					//authorizedTwitterAccount = DataUtils.twitterAccountDtoFromDo(persona.getTwitterAccount());
					//try to authenticate the User
					try {
						twitterUser = businessHelper.getTwitterPojo().getAuthenticatedUser(authorizedTwitterAccount);
						//twitterUser = TwitterServiceAdapter.get().getUserInfo(authorizedTwitterAccount);
					} catch (Exception e) {
						//No prob, just mean that the user has to authenticate
						e.printStackTrace();
					}

				} 
				
				if (twitterUser != null ) {
					//authorizedTwitterAccount = DataUtils.createAuthenticatedTwitterAccount();
					authorizedTwitterAccount = DataUtils.createAutenticatedTwitterAccountDto(twitterUser, authorizedTwitterAccount);
					authorizedTwitterAccount.setIsOAuthenticated(true);
					
					//add the lists
					
					
					
					
					
					//RateLimitsDTO rateLimits = TwitterServiceAdapter.get().getRateLimits(authorizedTwitterAccount); 
					
					RateLimitsDO rateLimisDo = persona.getTwitterAccount().getRateLimits();
					RateLimitsDTO rateLimitsDto = new RateLimitsDTO();
					if ( rateLimisDo!= null && rateLimisDo.getRateLimitLimit()!=null&&rateLimisDo.getRateLimitRemaining()!=null&&rateLimisDo.getRateLimitReset()!=null) {	
						rateLimitsDto.setRateLimitLimit(rateLimisDo.getRateLimitLimit());
						rateLimitsDto.setRateLimitRemaining(rateLimisDo.getRateLimitRemaining());
						rateLimitsDto.setRateLimitReset(rateLimisDo.getRateLimitReset());
					}
					authorizedTwitterAccount.setRateLimits(rateLimitsDto);
					
					authorizedTwitterAccount.setNewFollowers(getNewFollowersCount(persona,authorizedTwitterAccount));
					authorizedTwitterAccount.setNewFriends(getNewFriendsCount(persona,authorizedTwitterAccount));
					authorizedTwitterAccount.setNewBlocking(getNewBlockingCount(persona,authorizedTwitterAccount));
					
					authorizedTwitterAccount.setAutoFollowedUsersSize(persona.getTwitterAccount().getAutoFollowedCount()!=null?persona.getTwitterAccount().getAutoFollowedCount():0);
					
					authorizedTwitterAccount.setFollowBackQueueSize(persona.getTwitterAccount().getAutoFollowBackIdsQueue()!=null?persona.getTwitterAccount().getAutoFollowBackIdsQueue().size():0);
					authorizedTwitterAccount.setIgnoreUsersListSize(persona.getTwitterAccount().getIgnoreUsersIds()!=null?persona.getTwitterAccount().getIgnoreUsersIds().size():0);
					
					
					
					
					
				} else {
					authorizedTwitterAccount =  TwitterServiceAdapter.createPreAuthorizedTwitterAccount();
				}
				
				returnList.addPersona(DataUtils.createPersonaDto(persona, authorizedTwitterAccount));

			
		}
		return returnList;
		
		//return  businessHelper.getPersonaDao().findAllPersonas(email);

	}

	private int getNewFriendsCount(PersonaDO persona, TwitterAccountDTO twAccount) {

		int newCount = 0;
		//Call twitter
		try {
			
			newCount = businessHelper.getTwitterPojo().getFollowingIds(twAccount).length;
				
				//TwitterServiceAdapter.get().getFollowingIds(twAccount).length;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		if ( persona.getTwitterAccount().getFollowingIds() == null ) {
			return -1;
		}

		return newCount - persona.getTwitterAccount().getFollowingIds().size();
	}

	private int getNewFollowersCount(PersonaDO persona,TwitterAccountDTO twAccount) {
		int newCount = 0;
		//Call twitter
		try {
			newCount = businessHelper.getTwitterPojo().getFollowersIds(twAccount).length;
			//newCount = TwitterServiceAdapter.get().getFollowersIds(twAccount).length;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if ( persona.getTwitterAccount().getFollowersIds() == null ) {
			return -1;
		}
		
		return newCount - persona.getTwitterAccount().getFollowersIds().size();	
		
	}

	private int getNewBlockingCount(PersonaDO persona,TwitterAccountDTO twAccount) {
		int newCount = 0;
		//Call twitter
		try {
			newCount = businessHelper.getTwitterPojo().getBlockingIds(twAccount).length;
			
			//newCount = TwitterServiceAdapter.get().getBlockingIds(twAccount).length;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if ( persona.getTwitterAccount().getBlockingIds() == null ) {
			return -1;
		}
		
		return newCount - persona.getTwitterAccount().getBlockingIds().size();	
		
	}	
	


	public void updatePersonaTwitterAccount(PersonaDTO personaDto,
			TwitterAccountDO twitterAccountDo) throws Exception{
		businessHelper.getPersonaDao().updatePersonaTwitterAccount(personaDto,
				twitterAccountDo);
		
	}

	public PersonaDTO getPersona(String personaName, String email) throws Exception{

		
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(personaName, email);
		
		if ( persona == null ) {
			throw new Exception("Persona Not found");
		}
		
		
		User twitterUser = null;
		TwitterAccountDTO authorizedTwitterAccount = null;
		
		if (persona.getTwitterAccount() != null) {
			//Check if is authenticated
			//Create an TwitterAccountDTO 
			
			authorizedTwitterAccount = TwitterAccountDAO.createAuthorizedAccountDto(persona.getTwitterAccount());
			
			//authorizedTwitterAccount = DataUtils.twitterAccountDtoFromDo(persona.getTwitterAccount());
			//try to authenticate the User
			try {
				twitterUser = businessHelper.getTwitterPojo().getAuthenticatedUser(authorizedTwitterAccount);
				//twitterUser = TwitterServiceAdapter.get().getUserInfo(authorizedTwitterAccount);
			} catch (Exception e) {
				//Nothing to do here
			}

		} 
		
		if (twitterUser != null ) {
			//authorizedTwitterAccount = DataUtils.createAuthenticatedTwitterAccount();
			authorizedTwitterAccount = DataUtils.createAutenticatedTwitterAccountDto(twitterUser, authorizedTwitterAccount);
			authorizedTwitterAccount.setIsOAuthenticated(true);
			
			
			RateLimitsDO rateLimisDo = persona.getTwitterAccount().getRateLimits();
			RateLimitsDTO rateLimitsDto = new RateLimitsDTO();
			if ( rateLimisDo!= null) {	
				rateLimitsDto.setRateLimitLimit(rateLimisDo.getRateLimitLimit());
				rateLimitsDto.setRateLimitRemaining(rateLimisDo.getRateLimitRemaining());
				rateLimitsDto.setRateLimitReset(rateLimisDo.getRateLimitReset());
			}
			authorizedTwitterAccount.setRateLimits(rateLimitsDto);
			
			authorizedTwitterAccount.setNewFollowers(getNewFollowersCount(persona,authorizedTwitterAccount));
			authorizedTwitterAccount.setNewFriends(getNewFriendsCount(persona,authorizedTwitterAccount));
			authorizedTwitterAccount.setNewBlocking(getNewBlockingCount(persona,authorizedTwitterAccount));
			
			authorizedTwitterAccount.setAutoFollowedUsersSize(persona.getTwitterAccount().getAutoFollowedCount()!=null?persona.getTwitterAccount().getAutoFollowedCount():0);
			
			authorizedTwitterAccount.setFollowBackQueueSize(persona.getTwitterAccount().getAutoFollowBackIdsQueue()!=null?persona.getTwitterAccount().getAutoFollowBackIdsQueue().size():0);
			authorizedTwitterAccount.setIgnoreUsersListSize(persona.getTwitterAccount().getIgnoreUsersIds()!=null?persona.getTwitterAccount().getIgnoreUsersIds().size():0);
			
			
		} else {
			authorizedTwitterAccount =  TwitterServiceAdapter.createPreAuthorizedTwitterAccount();
		}
		
		return DataUtils.createPersonaDto(persona, authorizedTwitterAccount);
	}

	

	

}
