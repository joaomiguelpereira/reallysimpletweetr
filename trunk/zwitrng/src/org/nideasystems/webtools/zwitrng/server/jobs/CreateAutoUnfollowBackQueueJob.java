package org.nideasystems.webtools.zwitrng.server.jobs;


import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class CreateAutoUnfollowBackQueueJob extends AbstractJob {

	@Override
	public void execute() throws Exception {
		log.fine("Executing Job: " + this.getClass().getName());

		// Synch queu to sync
		TwitterAccountDO twitterAccount = persona.getTwitterAccount();
		TwitterAccountDTO authorizedTwitterAccount = TwitterAccountDAO
				.createAuthorizedAccountDto(twitterAccount);
		if (twitterAccount.getAutoUnFollowBackIdsQueue() != null) {
			log.fine("Actual size of Unbfollow Queue:"
					+ twitterAccount.getAutoUnFollowBackIdsQueue().size());
		}

		getBusinessHelper().getTwitterPojo().updateUnFollowBackUsersIdQueue(
				twitterAccount, authorizedTwitterAccount);

		if (twitterAccount.getAutoUnFollowBackIdsQueue() != null) {
			log.fine("Persona: " + persona.getName());
			log.fine("Current size of unfollow Queue:"
					+ twitterAccount.getAutoUnFollowBackIdsQueue().size());
		}

	}

}
