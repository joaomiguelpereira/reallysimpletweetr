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
import org.nideasystems.webtools.zwitrng.server.rules.AutoFollowBackOnFollowMeRuleRunner;
import org.nideasystems.webtools.zwitrng.server.servlets.AbstractHttpServlet;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;


public class RunFollowBackUsersServlet extends AbstractHttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5021407133990715706L;
	/**
	 * 
	 */
	
	private static boolean TESTING = false;
	private static final int INTERVAL = 25;
	StringBuffer outBuffer = null;
	private static final Logger log = Logger
			.getLogger(RunCreateFollowBackQueueServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("=============Running Job: Auto follow=============");
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
		
		/*try {
			startTransaction(true);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			throw new ServletException(e1);
			
		}
		
		//get the personas in active state
		List<PersonaDO> personas = getBusinessHelper().getPersonaDao().findAllActivePersonas();
		
		log.fine("Goig to check "+personas.size()+" persona");
		
		//For each persona
		for (PersonaDO persona: personas) {
			
			//run only if the last time it ran was 60 minutes ago
			if ( persona.getTwitterAccount().getLastAutoFollowBackTime()!= null  ) {
				
				Date now = new Date();
				
				if ( now.getTime() - persona.getTwitterAccount().getLastAutoFollowBackTime().longValue() < 1000*60*INTERVAL ) { //13 minutes
					continue;
				}
				
			}

			//Get the autoFollowRule
			
			int followingSize = persona.getTwitterAccount().getFollowingIds()!=null?persona.getTwitterAccount().getFollowingIds().size():0;
			int followersSize = persona.getTwitterAccount().getFollowersIds()!=null?persona.getTwitterAccount().getFollowersIds().size():0;
			int backSize = persona.getTwitterAccount().getAutoFollowBackIdsQueue()!=null?persona.getTwitterAccount().getAutoFollowBackIdsQueue().size():0;
			int ignoreSize = persona.getTwitterAccount().getIgnoreUsersIds()!=null?persona.getTwitterAccount().getIgnoreUsersIds().size():0;
			log.fine("FollowBack list Size:"+backSize);
			log.fine("Ignore list Size:"+ignoreSize);
			log.fine("Friends: "+followingSize);
			log.fine("Followers: "+followersSize);
			
			
			try {
				
				//Test
				
				//Synch queu to sync
				TwitterAccountDO twitterAccount = persona.getTwitterAccount();
				TwitterAccountDTO authorizedTwitterAccount = TwitterAccountDAO.createAuthorizedAccountDto(twitterAccount);
				getBusinessHelper().getTwitterPojo().updateFollowBackUsersIdQueue(twitterAccount, authorizedTwitterAccount);
				
				AutoFollowRuleDO autofollowrule = getBusinessHelper().getPersonaDao().getAutoFollowRule(persona, AutoFollowTriggerType.ON_FOLLOW_ME);
				
				AutoFollowBackOnFollowMeRuleRunner ruleEx = new AutoFollowBackOnFollowMeRuleRunner(persona,autofollowrule);
				
				ruleEx.setBusinessHelper(getBusinessHelper());
				if ( autofollowrule != null && autofollowrule.isEnabled() && persona.getTwitterAccount().getAutoFollowBackIdsQueue()!=null) {
					ruleEx.execute();
				}
				persona.getTwitterAccount().setLastAutoFollowBackTime(new Date().getTime());
				
				
				
				//getBusinessHelper().getTwitterPojo().updateFollowBackUsersIdQueue(twitterAccount,authorizedTwitterAccount);
				//doAutoFollowBack(persona);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.severe("Error running autofollowback rule for persona: "+persona.getName()+" Useremail: "+persona.getUserEmail());
				log.severe("Exception"+e.getMessage());
				e.printStackTrace();
				outBuffer.append("<div>Could not get the autofollow rule for persona: "+persona.getName()+" Useremail: "+persona.getUserEmail()+"</div>");
				//Continue
			}

			followingSize = persona.getTwitterAccount().getFollowingIds()!=null?persona.getTwitterAccount().getFollowingIds().size():0;
			followersSize = persona.getTwitterAccount().getFollowersIds()!=null?persona.getTwitterAccount().getFollowersIds().size():0;
			backSize = persona.getTwitterAccount().getAutoFollowBackIdsQueue()!=null?persona.getTwitterAccount().getAutoFollowBackIdsQueue().size():0;
			ignoreSize = persona.getTwitterAccount().getIgnoreUsersIds()!=null?persona.getTwitterAccount().getIgnoreUsersIds().size():0;
			log.fine("FollowBack list Size:"+backSize);
			log.fine("Ignore list Size:"+ignoreSize);
			log.fine("Friends: "+followingSize);
			log.fine("Followers: "+followersSize);
			break;
		
			
		}
		
		endTransaction();*/
		

		resp.setContentType("text/html");
		if (TESTING) {
			resp.getWriter().println(outBuffer.toString());
		} else {
			resp.getWriter().println("200 OK");
		}
	}

	
}
