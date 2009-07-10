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

}
