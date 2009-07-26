package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.servlets.AbstractHttpServlet;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class RunCreateUnFollowBackQueueServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1286943776778534172L;
	
	private static boolean TESTING = false;
	StringBuffer outBuffer = null;
	private static final Logger log = Logger
			.getLogger(RunCreateFollowBackQueueServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("=============Running Job: Create Auto Un Follow Back Queue=============");
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
				
				//Synch queu to sync
				TwitterAccountDO twitterAccount = persona.getTwitterAccount();
				TwitterAccountDTO authorizedTwitterAccount = TwitterAccountDAO.createAuthorizedAccountDto(twitterAccount);
				if ( twitterAccount.getAutoUnFollowBackIdsQueue()!=null) {
					log.fine("Actual size of Unbfollow Queue:"+twitterAccount.getAutoUnFollowBackIdsQueue().size());
				}
				
				
				
				getBusinessHelper().getTwitterPojo().updateUnFollowBackUsersIdQueue(twitterAccount, authorizedTwitterAccount);
			
				if ( twitterAccount.getAutoUnFollowBackIdsQueue()!=null) {
					log.fine("Persona: "+persona.getName());
					log.fine("Current size of unfollow Queue:"+twitterAccount.getAutoUnFollowBackIdsQueue().size());
				}
			
				/*
				AutoFollowRuleDO autofollowrule = getBusinessHelper().getPersonaDao().getAutoFollowRule(persona, AutoFollowTriggerType.ON_FOLLOW_ME);
				
				AutoFollowBackOnFollowMeRuleRunner ruleEx = new AutoFollowBackOnFollowMeRuleRunner(persona,autofollowrule);
				ruleEx.setBusinessHelper(getBusinessHelper());
				
				ruleEx.execute();*/
				
				//getBusinessHelper().getTwitterPojo().updateFollowBackUsersIdQueue(twitterAccount,authorizedTwitterAccount);
				//doAutoFollowBack(persona);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.severe("Error running autounfollowback rule for persona: "+persona.getName()+" User Email: "+persona.getUserEmail());
				e.printStackTrace();
				outBuffer.append("<div>Could not get the autounfollow rule for persona: "+persona.getName()+" User Email: "+persona.getUserEmail()+"</div>");
				//Continue
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
