package org.nideasystems.webtools.zwitrng.server.jobs;



import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class CreateAutoFollowBackQueueJob  extends  AbstractJob {

	@Override
	public void execute() throws Exception {
		log.fine("> > Persona:"+persona.getName()+" executing Job : "+this.getClass().getSimpleName());
		TwitterAccountDO twitterAccount = persona.getTwitterAccount();
		TwitterAccountDTO authorizedTwitterAccount = TwitterAccountDAO.createAuthorizedAccountDto(twitterAccount);
		if ( twitterAccount.getAutoFollowBackIdsQueue()!=null) {
			log.fine("> >Persona:"+persona.getName()+" has a Queue with :"+twitterAccount.getAutoFollowBackIdsQueue().size()+" elements");
		}
		
		getBusinessHelper().getTwitterPojo().updateFollowBackUsersIdQueue(twitterAccount, authorizedTwitterAccount);
	
		if ( twitterAccount.getAutoFollowBackIdsQueue()!=null) {
			log.fine("> >Persona:"+persona.getName()+" has a Queue with :"+twitterAccount.getAutoFollowBackIdsQueue().size()+" elements");
		}
		log.fine("> >END Persona:"+persona.getName()+" executing Job : "+this.getClass().getSimpleName());
	}

}
