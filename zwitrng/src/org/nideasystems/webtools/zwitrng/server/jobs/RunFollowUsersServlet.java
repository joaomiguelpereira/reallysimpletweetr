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
import org.nideasystems.webtools.zwitrng.server.rules.AutoFollowOnSearchRuleRunner;
import org.nideasystems.webtools.zwitrng.server.servlets.AbstractHttpServlet;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;

public class RunFollowUsersServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3385749160814731036L;
	private static boolean TESTING = false;
	private static final int INTERVAL = 15;
	StringBuffer outBuffer = null;
	private static final Logger log = Logger
			.getLogger(RunCreateFollowBackQueueServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("=============Running Job: Auto Follow=============");
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

		// get the personas in active state
		List<PersonaDO> personas = getBusinessHelper().getPersonaDao()
				.findAllActivePersonas();

		log.fine("Goig to check " + personas.size() + " persona");

		// For each persona
		for (PersonaDO persona : personas) {
			// Get the autoFollowRule

			// run only if the last time it ran was 60 minutes ago
			if (persona.getTwitterAccount().getLastAutoFollowTime() != null && !TESTING) {

				Date now = new Date();

				if (now.getTime()
						- persona.getTwitterAccount().getLastAutoFollowTime()
								.longValue() < 1000 * 60 * INTERVAL) { // 11 minutes
					continue;
				}

			}

			int followingSize = persona.getTwitterAccount().getFollowingIds() != null ? persona
					.getTwitterAccount().getFollowingIds().size()
					: 0;
			int followersSize = persona.getTwitterAccount().getFollowersIds() != null ? persona
					.getTwitterAccount().getFollowersIds().size()
					: 0;
			int backSize = persona.getTwitterAccount()
					.getAutoFollowBackIdsQueue() != null ? persona
					.getTwitterAccount().getAutoFollowBackIdsQueue().size() : 0;
			int ignoreSize = persona.getTwitterAccount().getIgnoreUsersIds() != null ? persona
					.getTwitterAccount().getIgnoreUsersIds().size()
					: 0;
			int unfollowSize = persona.getTwitterAccount()
					.getAutoUnFollowBackIdsQueue() != null ? persona
					.getTwitterAccount().getAutoUnFollowBackIdsQueue().size()
					: 0;
			int followSize = persona.getTwitterAccount()
					.getAutoFollowScreenNamesQueue() != null ? persona
					.getTwitterAccount().getAutoFollowScreenNamesQueue().size()
					: 0;

			int autoFollowedSize = persona.getTwitterAccount()
					.getAutoFollowedScreenNames() != null ? persona
					.getTwitterAccount().getAutoFollowedScreenNames().size()
					: 0;

			log.fine("FollowBack list Size:" + backSize);
			log.fine("Ignore list Size:" + ignoreSize);
			log.fine("Friends: " + followingSize);
			log.fine("Followers: " + followersSize);
			log.fine("Unfollow Back Followers: " + unfollowSize);
			log.fine("Follow Queue: " + followSize);

			log.fine("Followed Size: " + autoFollowedSize);
			try {

				

				AutoFollowRuleDO rule = getBusinessHelper().getPersonaDao()
						.getAutoFollowRule(persona,
								AutoFollowTriggerType.SEARCH);

				AutoFollowOnSearchRuleRunner ruleEx = new AutoFollowOnSearchRuleRunner(
						persona, rule);

				ruleEx.setBusinessHelper(getBusinessHelper());
				if (rule != null
						&& rule.isEnabled()
						&& persona.getTwitterAccount()
								.getAutoFollowScreenNamesQueue() != null) {
					ruleEx.execute();
				}
				persona.getTwitterAccount().setLastAutoFollowTime(
						new Date().getTime());

				// getBusinessHelper().getTwitterPojo().updateFollowBackUsersIdQueue(twitterAccount,authorizedTwitterAccount);
				// doAutoFollowBack(persona);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.severe("Error running autounfollowback rule for persona: "
						+ persona.getName() + " Useremail: "
						+ persona.getUserEmail());
				log.severe("Exception" + e.getMessage());
				e.printStackTrace();
				outBuffer
						.append("<div>Could not get the autounfollow rule for persona: "
								+ persona.getName()
								+ " User Email: "
								+ persona.getUserEmail() + "</div>");
				// Continue
			}

			followingSize = persona.getTwitterAccount().getFollowingIds() != null ? persona
					.getTwitterAccount().getFollowingIds().size()
					: 0;
			followersSize = persona.getTwitterAccount().getFollowersIds() != null ? persona
					.getTwitterAccount().getFollowersIds().size()
					: 0;
			backSize = persona.getTwitterAccount().getAutoFollowBackIdsQueue() != null ? persona
					.getTwitterAccount().getAutoFollowBackIdsQueue().size()
					: 0;
			ignoreSize = persona.getTwitterAccount().getIgnoreUsersIds() != null ? persona
					.getTwitterAccount().getIgnoreUsersIds().size()
					: 0;
			unfollowSize = persona.getTwitterAccount()
					.getAutoUnFollowBackIdsQueue() != null ? persona
					.getTwitterAccount().getAutoUnFollowBackIdsQueue().size()
					: 0;
			followSize = persona.getTwitterAccount()
					.getAutoFollowScreenNamesQueue() != null ? persona
					.getTwitterAccount().getAutoFollowScreenNamesQueue().size()
					: 0;

			autoFollowedSize = persona.getTwitterAccount()
					.getAutoFollowedScreenNames() != null ? persona
					.getTwitterAccount().getAutoFollowedScreenNames().size()
					: 0;

			log.fine("FollowBack list Size:" + backSize);
			log.fine("Ignore list Size:" + ignoreSize);
			log.fine("Friends: " + followingSize);
			log.fine("Followers: " + followersSize);
			log.fine("Unfollow Back Followers: " + unfollowSize);
			log.fine("Follow Queue: " + followSize);

			log.fine("Followed Size: " + autoFollowedSize);
			if (!TESTING) {
				break;
			}
			
			
		}

		endTransaction();
*/
		resp.setContentType("text/html");
		if (TESTING) {
			resp.getWriter().println(outBuffer.toString());
		} else {
			resp.getWriter().println("200 OK");
		}
	}

}
