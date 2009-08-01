package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;

import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.servlets.AbstractHttpServlet;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class RunCreateStatsServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8345892459706480289L;
	private static boolean TESTING = true;
	private static final int INTERVAL = 15; // 15 min
	StringBuffer outBuffer = null;
	private static final Logger log = Logger
			.getLogger(RunCreateFollowBackQueueServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log
				.info("=============Running Job: Create Following/Follwowers Stats =============");
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

		// get the personas in active state
		List<PersonaDO> personas = getBusinessHelper().getPersonaDao()
				.findAllActivePersonas();

		log.fine("Goig to check " + personas.size() + " personas");
		for (PersonaDO persona : personas) {
			try {
				// update following count for the period
				List<Integer> historicalFollowing = persona.getTwitterAccount()
						.getHistoricalFollowing();
				List<Integer> historicalFollowers = persona.getTwitterAccount()
						.getHistoricalFollowers();

				if (historicalFollowers == null) {
					historicalFollowers = new ArrayList<Integer>();
				}
				if (historicalFollowing == null) {
					historicalFollowing = new ArrayList<Integer>();
				}

				TwitterAccountDTO account = TwitterAccountDAO
						.createAuthorizedAccountDto(persona.getTwitterAccount());
				historicalFollowing.add(getFollowingCount(account));
				historicalFollowers.add(getFollowersCount(account));

				persona.getTwitterAccount().setHistoricalFollowers(historicalFollowers);
				persona.getTwitterAccount().setHistoricalFollowing(historicalFollowing);
				outBuffer.append("<h3>"+persona.getName()+"</h3>");
				
				outBuffer.append("<div>Following:"+historicalFollowing.size()+"</div>");
				outBuffer.append("<div>Followers:"+historicalFollowers.size()+"</div>");
				outBuffer.append("</hr>");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.severe("Error creating stats for persona: "
						+ persona.getName() + " User Email: "
						+ persona.getUserEmail());
				e.printStackTrace();
				outBuffer.append("<div>Could not create stats for persona: "
						+ persona.getName() + " User Email: "
						+ persona.getUserEmail() + "</div>");
				// Continue
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

	private Integer getFollowersCount(TwitterAccountDTO account) {
		TwitterServiceAdapter adapter = TwitterServiceAdapter.get(account);
		int size = 0;

		try {
			size = adapter.getFollowersIds().length;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.severe("Error getting followers ids:" + e.getMessage());
			e.printStackTrace();
		}

		return size;
	}

	private int getFollowingCount(TwitterAccountDTO account) {

		TwitterServiceAdapter adapter = TwitterServiceAdapter.get(account);
		int size = 0;

		try {
			size = adapter.getFollowingIds().length;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.severe("Error getting following ids:" + e.getMessage());
			e.printStackTrace();
		}

		return size;
	}
}
