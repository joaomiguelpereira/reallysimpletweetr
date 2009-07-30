package org.nideasystems.webtools.zwitrng.server.jobs;


import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class CreateAutoFollowQueueJob  extends  AbstractJob {

	@Override
	public void execute() throws Exception {
		log.fine("> > Persona:"+persona.getName()+" executing Job : "+this.getClass().getSimpleName());
		TwitterAccountDO twitterAccount = persona.getTwitterAccount();
		TwitterAccountDTO authorizedTwitterAccount = TwitterAccountDAO.createAuthorizedAccountDto(twitterAccount);
		
		if ( twitterAccount.getAutoFollowScreenNamesQueue()!=null) {
			log.fine("Actual size of follow Queue:"+twitterAccount.getAutoFollowScreenNamesQueue().size());
		}
		
		
		AutoFollowRuleDO autofollowrule = getBusinessHelper().getPersonaDao().getAutoFollowRule(persona, AutoFollowTriggerType.SEARCH);
		
		if ( autofollowrule != null &&  autofollowrule.isEnabled()) {
			getBusinessHelper().getTwitterPojo().updateFollowUsersScreenNamesQueue(twitterAccount,autofollowrule, authorizedTwitterAccount);
		}
		
		if ( twitterAccount.getAutoFollowScreenNamesQueue()!=null) {
			log.fine("Persona: "+persona.getName());
			log.fine("Current size of follow Queue:"+twitterAccount.getAutoFollowScreenNamesQueue().size());
		}
		log.fine("> > END Persona:"+persona.getName()+" executing Job : "+this.getClass().getSimpleName());
	}

}
