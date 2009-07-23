package org.nideasystems.webtools.zwitrng.server.domain.dao;

import java.util.ArrayList;
import java.util.HashSet;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.RateLimitsDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class TwitterAccountDAO extends BaseDAO {

	public void populateTwitterAccount(PersonaDO parentPersona,
			TwitterAccountDTO twitterAccount) {
		TwitterAccountDO twitterAccountDo = parentPersona.getTwitterAccount();
		twitterAccountDo.setBlockingIds(new HashSet<Integer>());
		twitterAccountDo.setFollowersIds(new HashSet<Integer>());
		twitterAccountDo.setFollowingIds(new HashSet<Integer>());
		twitterAccountDo.setIgnoreUsersIds(new HashSet<Integer>());
		twitterAccountDo.setAutoFollowBackIdsQueue(new ArrayList<Integer>());
		
		twitterAccountDo.setId(twitterAccount.getId());
		twitterAccountDo.setOAuthLoginUrl(twitterAccount.getOAuthLoginUrl());
		twitterAccountDo.setOAuthToken(twitterAccount.getOAuthToken());
		twitterAccountDo.setOAuthTokenSecret(twitterAccount.getOAuthTokenSecret());
		
		twitterAccountDo.setTwitterName(twitterAccount.getTwitterScreenName());
		RateLimitsDO rateLimits = new RateLimitsDO();
		rateLimits.setRateLimitLimit(-1);
		rateLimits.setRateLimitRemaining(-1);
		rateLimits.setRateLimitReset(new Long(0));
		
		twitterAccountDo.setRateLimits(rateLimits);
		
		//twitterAccountDo.setPersona(parentPersona);
		//parentPersona.setTwitterAccount(twitterAccountDo);
		
		
	}

	public static TwitterAccountDTO createAuthorizedAccountDto(
			TwitterAccountDO twitterAccount) {
		TwitterAccountDTO dto = new TwitterAccountDTO();
		dto.setId(twitterAccount.getId());
		dto.setOAuthLoginUrl(twitterAccount.getOAuthLoginUrl());
		dto.setOAuthToken(twitterAccount.getOAuthToken());
		dto.setOAuthTokenSecret(twitterAccount.getOAuthTokenSecret());
		return dto;
	}

}
