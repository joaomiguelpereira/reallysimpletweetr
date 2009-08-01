package org.nideasystems.webtools.zwitrng.server.domain.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.RateLimitsDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.appengine.api.datastore.Blob;

public class TwitterAccountDAO extends BaseDAO {

	

	public void populateTwitterAccount(PersonaDO parentPersona,
			TwitterAccountDTO twitterAccount) throws Exception {
		TwitterAccountDO twitterAccountDo = parentPersona.getTwitterAccount();
		twitterAccountDo.setBlockingIds(new HashSet<Integer>());
		
		
		Set<Integer> followersIds = new HashSet<Integer>();
		Blob blob = new Blob(TwitterAccountDAO.toByteArray(followersIds));
		twitterAccountDo.setFollowersIdsBlob(blob);

		

		Set<Integer> followingIds = new HashSet<Integer>();
		Blob blobFollowing = new Blob(TwitterAccountDAO.toByteArray(followingIds));

		twitterAccountDo.setFollowingIdsBlob(blobFollowing);
		twitterAccountDo.setIgnoreUsersIds(new HashSet<Integer>());
		twitterAccountDo.setAutoFollowBackIdsQueue(new ArrayList<Integer>());

		twitterAccountDo.setId(twitterAccount.getId());
		twitterAccountDo.setOAuthLoginUrl(twitterAccount.getOAuthLoginUrl());
		twitterAccountDo.setOAuthToken(twitterAccount.getOAuthToken());
		twitterAccountDo.setOAuthTokenSecret(twitterAccount
				.getOAuthTokenSecret());

		twitterAccountDo.setTwitterName(twitterAccount.getTwitterScreenName());
		RateLimitsDO rateLimits = new RateLimitsDO();
		rateLimits.setRateLimitLimit(-1);
		rateLimits.setRateLimitRemaining(-1);
		rateLimits.setRateLimitReset(new Long(0));

		twitterAccountDo.setRateLimits(rateLimits);

		// twitterAccountDo.setPersona(parentPersona);
		// parentPersona.setTwitterAccount(twitterAccountDo);

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

	public static byte[] toByteArray(Set<Integer> set) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(set);
		out.close();
		return bos.toByteArray();
	}
	
	public static Set<Integer> toIntegerSet(Blob blob) {
		
		Set<Integer> out = new HashSet<Integer>();
		if (blob != null) {
			
			try {
				ObjectInput in = new ObjectInputStream(
						new ByteArrayInputStream(blob.getBytes()));
				out = (Set<Integer>) in.readObject();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}

		}

		return out;

	}

}
