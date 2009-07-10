package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.ExtendedTwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;

import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;


import twitter4j.User;

public class TwitterPojo extends AbstractPojo{

	private final static Logger log = Logger.getLogger(TwitterPojo.class.getName());
	
	
	
	
	public TwitterUserDTOList getUsers(PersonaDTO thePersonaDto,
			TwitterUserFilterDTO currentFilter) throws Exception{
		
		//Get the users from the accoun
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(thePersonaDto.getName(),thePersonaDto.getUserEmail());
		
		if (persona == null) {
			throw new Exception("Could not find the persona");
		}
		
		
		if (persona.getFollowersIds()==null || persona.getBlockingIds()==null||persona.getFollowingIds()==null) {
		
			synchronize(persona, thePersonaDto.getTwitterAccount());
		}
		
		
		//Get all Users from Twitter, according to current filter
		
		//return //DataUtils.twitterUserDtoFromDo(persona.getFollowers());
		
		//Get the users from twitter according to the filter
		List<User> twitterUserList = TwitterServiceAdapter.get().getUsers(thePersonaDto.getTwitterAccount(), currentFilter);
		
		TwitterUserDTOList ret = new TwitterUserDTOList();
		ret.setFilter(currentFilter);
		
		for (User twitterUser: twitterUserList) {
			TwitterAccountDTO twitterAccount = DataUtils.createTwitterAccountDto(twitterUser);
			//Create extension
			
			ExtendedTwitterAccountDTO extUser = new ExtendedTwitterAccountDTO();
			extUser.setImBlocking(persona.getBlockingIds().contains(new Integer(twitterAccount.getId())));
			
			extUser.setImFollowing(persona.getFollowingIds().contains(new Integer(twitterAccount.getId())));
			extUser.setMutualFriendShip(extUser.isImFollowing() && persona.getFollowersIds().contains(new Integer(twitterAccount.getId())));
			twitterAccount.setExtendedUserAccount(extUser);
			ret.add(twitterAccount);
			
		}
		return ret;
		
		
	}

	private void synchronize(PersonaDO persona, TwitterAccountDTO account) throws Exception {
		
		//get from twitter the followers
		int[] newFollowersIds;
		try {
			newFollowersIds = TwitterServiceAdapter.get().getFollowersIds(account);
		} catch (Exception e) {
			log.severe("Error:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		if (persona.getFollowersIds()!=null) {
			//Clear current array
			persona.getFollowersIds().clear();
			
		}
		
		List<Integer> followersIds = new ArrayList<Integer>();
		for (int id:newFollowersIds) {
			followersIds.add(id);
		}
		persona.setFollowersIds(followersIds);
		
		
		//Get the following
		int[] newFollowingIds;
		try {
			newFollowingIds = TwitterServiceAdapter.get().getFollowingIds(account);
		} catch (Exception e) {
			log.severe("Error:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		if ( persona.getFollowingIds()!=null) {
			//Clear current array
			persona.getFollowingIds().clear();
			
		}
		
		List<Integer> followingIds = new ArrayList<Integer>();
		for (int id:newFollowingIds) {
			followingIds.add(id);
		}
		persona.setFollowingIds(followingIds);


		//Get the blocinkg ids
		
		int[] newBlockingIds;
		try {
			newBlockingIds = TwitterServiceAdapter.get().getBlockingIds(account);
		} catch (Exception e) {
			log.severe("Error:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		if ( persona.getBlockingIds()!=null) {
			//Clear current array
			persona.getBlockingIds().clear();			
		}
		
		List<Integer> blockingIds = new ArrayList<Integer>();
		for (int id:newBlockingIds) {
			blockingIds.add(id);
		}
		persona.setBlockingIds(blockingIds);
		
		
	}

	public void synchronize(PersonaDTO thePersonaDto) throws Exception{
		//Get the users from the accoun
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(thePersonaDto.getName(),thePersonaDto.getUserEmail());
		
		if (persona == null) {
			throw new Exception("Could not find the persona");
		}
		synchronize(persona, thePersonaDto.getTwitterAccount());
		
		
	}

	public void followUser(PersonaDTO currentPersona, TwitterAccountDTO user) throws Exception{
		log.fine("Start following user: "+user.getTwitterScreenName());
		log.fine("Start following user: "+user.getId());
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(currentPersona.getName(),currentPersona.getUserEmail());

		if (persona==null) {
			throw new Exception("Could not find the persona");
		}
		//Try to call Twitter service
		try {
			TwitterServiceAdapter.get().followUser(currentPersona.getTwitterAccount(), true, user.getId());
		} catch (Exception e) {
			log.severe("Could not follow the User"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		persona.getFollowingIds().add(user.getId());
		
		
	}

	public void unfollowUser(PersonaDTO currentPersona, TwitterAccountDTO user) throws Exception{
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(currentPersona.getName(),currentPersona.getUserEmail());
		log.fine("Unfollow user: "+user.getTwitterScreenName() );
		log.fine("Unfollow user: "+user.getId() );
		
		if (persona==null) {
			throw new Exception("Could not find the persona");
		}
		//Try to call Twitter service
		try {
			TwitterServiceAdapter.get().followUser(currentPersona.getTwitterAccount(), false, user.getId());
		} catch (Exception e) {
			log.severe("Could not follow the User"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		persona.getFollowingIds().remove(user.getId());
	}

	public void blockUser(PersonaDTO currentPersona, TwitterAccountDTO user) throws Exception{
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(currentPersona.getName(),currentPersona.getUserEmail());
		log.fine("Block  user: "+user.getTwitterScreenName() );
		log.fine("Block user: "+user.getId() );
		
		if (persona==null) {
			throw new Exception("Could not find the persona");
		}
		//Try to call Twitter service
		try {
			TwitterServiceAdapter.get().blockUser(currentPersona.getTwitterAccount(), true, user.getId());
		} catch (Exception e) {
			log.severe("Could not block the User"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		persona.getBlockingIds().remove(user.getId());
		
	}

	public void unblockUser(PersonaDTO currentPersona, TwitterAccountDTO user) throws Exception{
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(currentPersona.getName(),currentPersona.getUserEmail());
		log.fine("unBlock  user: "+user.getTwitterScreenName() );
		log.fine("unBlock user: "+user.getId() );
		
		if (persona==null) {
			throw new Exception("Could not find the persona");
		}
		//Try to call Twitter service
		try {
			TwitterServiceAdapter.get().blockUser(currentPersona.getTwitterAccount(), false, user.getId());
		} catch (Exception e) {
			log.severe("Could not unblock the User"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		persona.getBlockingIds().remove(user.getId());
		
	}

}
