package org.nideasystems.webtools.zwitrng.server.rules;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.Configuration;
import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import twitter4j.User;

public class AutoFollowOnSearchRuleRunner extends AbstractRuleRunner {
	private static final Logger log = Logger
			.getLogger(AutoFollowOnSearchRuleRunner.class.getName());

	public AutoFollowOnSearchRuleRunner(PersonaDO persona2,
			AutoFollowRuleDO autofollowrule) {
		super(persona2, autofollowrule);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() throws Exception {
		
		List<String> queue = persona.getTwitterAccount()
				.getAutoFollowScreenNamesQueue();
		
		Set<String> autoFollowedUsersScreenNames = persona.getTwitterAccount()
				.getAutoFollowedScreenNames();
		
		Set<Integer> ignoreSet = persona.getTwitterAccount().getIgnoreUsersIds();
		
		Set<Integer> followersIds = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount()
				.getFollowersIdsBlob());
		
		
		Set<Integer> followingIds = TwitterAccountDAO.toIntegerSet(persona.getTwitterAccount()
				.getFollowingIdsBlob());
		Integer autoFollowedCount = persona.getTwitterAccount().getAutoFollowedCount();

		if (autoFollowedCount==null) {
			autoFollowedCount = new Integer(0);
		}
		
		if ( ignoreSet == null) {
			ignoreSet = new HashSet<Integer>();
		} else if (ignoreSet.size()>200) {
			ignoreSet.clear();
		}
		if (autoFollowedUsersScreenNames == null) {
			autoFollowedUsersScreenNames = new HashSet<String>();
		} 

		
		log.fine(" $ $ Executing Follow Back On Search");
		//Keep my ratio low, please
		//My Ration
		
		float currentRatio = 0;
		if ( followersIds != null ) {
			currentRatio = new Float(followingIds.size())/new Float(followersIds.size());
		}
		
		
		log.fine("$$ My Ration i: "+currentRatio);
		
		float allowedRation = new Float(rule.getKeepRatioBellow()/100F);
		log.fine("$$ Allowed Ration: "+allowedRation);
		
		 
		
		if (queue != null && currentRatio<allowedRation) {
			int maxFollowing = Configuration.MAX_FOLLOW_JOB;
			List<String> removeList = new ArrayList<String>();
			
			maxFollowing = queue.size() > maxFollowing ? maxFollowing : queue
					.size();
			
			for (int i = 0; i < maxFollowing; i++) {
				
				//Mark to remove
				removeList.add(queue.get(i));

				//check if they are not friends yet
				int userId = 0;
				try {
					if ( (userId = process(queue.get(i))) > 0) {
						log.fine("$$ Persona: "+persona.getName()+" is now following user:"+queue.get(i)+" with id:"+userId);
						autoFollowedCount++;
						autoFollowedUsersScreenNames.add(queue.get(i));
						ignoreSet.add(userId);
					} else {
						log.fine("$$Persona "+persona.getName()+" Could not follow user :"+queue.get(i)+" Ignoring.");
					}
					
				} catch (Exception e) {
					log.fine("$$Persona "+persona.getName()+"Could not follow user:"+queue.get(i) +" Exception: "+ e.getMessage());
					// TODO Auto-generated catch block
					if ( e.getMessage().contains(" You are unable to follow more people at this time")) {
						log.fine("%%%%%%%%%%%%%%%Wil not try again%%%%%%%%%%%%%");
						break;
					}
					e.printStackTrace();
				}
				long now = new Date().getTime();
				if (now - getBusinessHelper().getStartTime() > Configuration.MAX_JOB_RUN_TIME) {
					log.fine("-----> Ending Job because time elapsed. Users Processed:"+i);
					break;
				}

				
			}

			// Remove the ones already followed
			for (String str : removeList) {
				queue.remove(str);
			}
			
			persona.getTwitterAccount().setAutoFollowedCount(autoFollowedCount);
			persona.getTwitterAccount().setIgnoreUsersIds(ignoreSet);
			persona.getTwitterAccount().setAutoFollowScreenNamesQueue(queue);
			persona.getTwitterAccount().setAutoFollowedScreenNames(
					autoFollowedUsersScreenNames);

		}

	}

	private int process(String screenName) throws Exception {

		boolean followed = true;
		int userId = 0;
		User user = getTwitterUser(screenName);
		userId = user.getId();
		if (canFollow(user)) {
			
			log.fine("Persona: "+persona.getName()+ ", following User: "+screenName);
			TwitterAccountDTO authAccount = TwitterAccountDAO
					.createAuthorizedAccountDto(persona.getTwitterAccount());
			PersonaDTO personaDto = DataUtils.createPersonaDto(this.persona,
					authAccount);
			// Get the use

			TwitterServiceAdapter twitterService = TwitterServiceAdapter
					.get(personaDto.getTwitterAccount());

			twitterService.followUser(true, user.getId());
		} else {
			log.fine("Persona: "+persona.getName()+ " will not follow User: "+screenName);
			followed = false;
		}
		// Now check preconditions
		return userId;
	}

}
