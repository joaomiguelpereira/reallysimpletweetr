package org.nideasystems.webtools.zwitrng.server.jobs;

import java.util.HashSet;
import java.util.Set;

import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class SynchUsersInfoJob extends AbstractJob {

	@Override
	public void execute() throws Exception {
		log.fine("Executing Job: " + this.getClass().getName());
		log.fine("S y n c h r o n i z i n g   U s e r   I n f o");
		TwitterAccountDO twitterAccount = persona.getTwitterAccount();
		TwitterAccountDTO authorizedAccount = TwitterAccountDAO
				.createAuthorizedAccountDto(twitterAccount);
		// Update follwers list
		log.fine("$$ Following size: "+persona.getTwitterAccount().getFollowingIds().size());
		log.fine("$$ Followers size: "+persona.getTwitterAccount().getFollowersIds().size());
		
		Set<Integer> followersIds = getFollowersIds(authorizedAccount);
		persona.getTwitterAccount().setFollowersIds(followersIds);

		//Update following list
		Set<Integer> followingIds = getFollowingIds(authorizedAccount);
		persona.getTwitterAccount().setFollowingIds(followingIds);
		log.fine("$$ Following size now: "+persona.getTwitterAccount().getFollowingIds().size());
		log.fine("$$ Followers size now: "+persona.getTwitterAccount().getFollowersIds().size());
		
	}

	private Set<Integer> getFollowingIds(TwitterAccountDTO authorizedAccount)
			throws Exception {
		
		Set<Integer> followingIds = new HashSet<Integer>();

		TwitterServiceAdapter adapter = TwitterServiceAdapter
				.get(authorizedAccount);
		int[] following = adapter.getFollowingIds();
		for (int id : following) {
			followingIds.add(id);
		}
		return followingIds;
	}

	private Set<Integer> getFollowersIds(TwitterAccountDTO authorizedAccount)
			throws Exception {
		Set<Integer> followersIds = new HashSet<Integer>();

		TwitterServiceAdapter adapter = TwitterServiceAdapter
				.get(authorizedAccount);
		int[] followers = adapter.getFollowersIds();
		for (int id : followers) {
			followersIds.add(id);
		}
		return followersIds;
	}

}
