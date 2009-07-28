package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.domain.AutoFollowRuleDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.servlets.AbstractHttpServlet;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class RunCreateAutoFollowQueueServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3383475522265905879L;

	
	private static boolean TESTING = false;
	private static int INTERVAL = 20;
	StringBuffer outBuffer = null;
	private static final Logger log = Logger
			.getLogger(RunCreateFollowBackQueueServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("=============Running Job: Create Auto  Follow Queue=============");
		// Check headers
		outBuffer = new StringBuffer();

		if (!TESTING) {
			if (req.getHeader("X-AppEngine-Cron") == null
					|| !req.getHeader("X-AppEngine-Cron").equals("true")) {
				log.severe("Job called outside of a cron context");
				throw new ServletException(
						"Job called outside of a cron context");

			}
		}
		
		try {
			startTransaction(true);
		} catch (Exception e1) {
			log.severe("Error Starting transaction");
			throw new ServletException(e1);
			
		}
		
		//get the personas in active state
		List<PersonaDO> personas = getBusinessHelper().getPersonaDao().findAllActivePersonas();
		
		log.fine("Goig to check "+personas.size()+" personas");
		for (PersonaDO persona: personas) {
			try {
				//Test
				log.fine("Creating Follow Queue for Persona: "+persona.getName());
				//Synch queu to sync
				TwitterAccountDO twitterAccount = persona.getTwitterAccount();
				//run only if the last time it ran was 60 minutes ago
				
				if ( twitterAccount.getLastCreateAutoFollowQueueTime()!= null  && !TESTING) {
					
					Date now = new Date();
					
					if ( now.getTime() - twitterAccount.getLastCreateAutoFollowQueueTime().longValue() < 1000*60*INTERVAL ) { //20 minutes
						continue;
					}
					
				}

				TwitterAccountDTO authorizedTwitterAccount = TwitterAccountDAO.createAuthorizedAccountDto(twitterAccount);
				
				if ( twitterAccount.getAutoFollowScreenNamesQueue()!=null) {
					log.fine("Actual size of follow Queue:"+twitterAccount.getAutoFollowScreenNamesQueue().size());
				}
				
				//need the rule here
				
				AutoFollowRuleDO autofollowrule = getBusinessHelper().getPersonaDao().getAutoFollowRule(persona, AutoFollowTriggerType.SEARCH);
				if ( autofollowrule != null &&  autofollowrule.isEnabled()) {
					getBusinessHelper().getTwitterPojo().updateFollowUsersScreenNamesQueue(twitterAccount,autofollowrule, authorizedTwitterAccount);
				}
				
			
				twitterAccount.setLastCreateAutoFollowQueueTime(new Date().getTime());
				if ( twitterAccount.getAutoFollowScreenNamesQueue()!=null) {
					log.fine("Persona: "+persona.getName());
					log.fine("Current size of follow Queue:"+twitterAccount.getAutoFollowScreenNamesQueue().size());
				}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.severe("Error running autofollowback rule for persona: "+persona.getName()+" User Email: "+persona.getUserEmail());
				e.printStackTrace();
				outBuffer.append("<div>Could not get the autofollow rule for persona: "+persona.getName()+" User Email: "+persona.getUserEmail()+"</div>");
				//Continue
			}
			if ( !TESTING ) {
				break;
			}
			
		}
		
		endTransaction();
		

		resp.setContentType("text/html");
		if (TESTING) {
			resp.getWriter().println(outBuffer.toString());
		} else {
			resp.getWriter().println("200 OK");
		}

	}
	

}
