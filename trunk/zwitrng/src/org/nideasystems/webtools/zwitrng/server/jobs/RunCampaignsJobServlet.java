package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RunCampaignsJobServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7705658558425439973L;

	private static final Logger log = Logger.getLogger(RunCampaignsJobServlet.class.getName());
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		log.info("=============Running Job: Run campaigns=============");
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");

	}

}
