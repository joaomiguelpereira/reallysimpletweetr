package org.nideasystems.webtools.zwitrng.server.domain.dao;

import java.util.ArrayList;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.RateLimitsDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class TwitterAccountDAO extends BaseDAO {

	public void populateTwitterAccount(PersonaDO parentPersona,
			TwitterAccountDTO twitterAccount) {
		TwitterAccountDO twitterAccountDo = parentPersona.getTwitterAccount();
		twitterAccountDo.setBlockingIds(new ArrayList<Integer>());
		twitterAccountDo.setFollowersIds(new ArrayList<Integer>());
		twitterAccountDo.setFollowingIds(new ArrayList<Integer>());
		twitterAccountDo.setId(twitterAccount.getId());
		twitterAccountDo.setOAuthLoginUrl(twitterAccount.getOAuthLoginUrl());
		twitterAccountDo.setOAuthToken(twitterAccount.getOAuthToken());
		twitterAccountDo.setOAuthTokenSecret(twitterAccount.getOAuthTokenSecret());
		
		twitterAccountDo.setTwitterName(twitterAccount.getTwitterScreenName());
		twitterAccountDo.setRateLimits(new RateLimitsDO());
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
