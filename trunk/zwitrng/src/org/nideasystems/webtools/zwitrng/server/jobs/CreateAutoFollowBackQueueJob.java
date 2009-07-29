package org.nideasystems.webtools.zwitrng.server.jobs;

import java.util.Date;

import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class CreateAutoFollowBackQueueJob  extends  AbstractJob {

	@Override
	public void execute() throws Exception {
		log.fine("Executing Job: "+this.getClass().getName());
		TwitterAccountDO twitterAccount = persona.getTwitterAccount();
		TwitterAccountDTO authorizedTwitterAccount = TwitterAccountDAO.createAuthorizedAccountDto(twitterAccount);
		if ( twitterAccount.getAutoFollowBackIdsQueue()!=null) {
			log.fine("Actual size of Queue:"+twitterAccount.getAutoFollowBackIdsQueue().size());
		}
		
		
		
		getBusinessHelper().getTwitterPojo().updateFollowBackUsersIdQueue(twitterAccount, authorizedTwitterAccount);
	
		if ( twitterAccount.getAutoFollowBackIdsQueue()!=null) {
			log.fine("Persona: "+persona.getName());
			log.fine("Current size of Queue:"+twitterAccount.getAutoFollowBackIdsQueue().size());
		}
	}

}
