package org.nideasystems.webtools.zwitrng.server.jobs.campaigns;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.jobs.AbstractJobRunnerServlet;

public class RunCampaignsJobsServlet extends AbstractJobRunnerServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5728292814086142802L;

	public RunCampaignsJobsServlet() {
		super("tweetjobs",false);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		
		
		doRun(req, resp);
	}
	
}
