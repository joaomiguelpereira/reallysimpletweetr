package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class RunJobsServlet extends AbstractJobRunnerServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6248867066738776402L;
	
	public RunJobsServlet() {
		super("jobs",false);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doRun(req, resp);
	}
	
}
