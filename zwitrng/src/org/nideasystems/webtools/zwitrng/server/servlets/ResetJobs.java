package org.nideasystems.webtools.zwitrng.server.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ResetJobs extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2278267775532084337L;
	private StringBuffer outBuffer = null;
	private User loggedUser = null;
	private HttpServletRequest req;
	private static final List<String> ADMINS = new ArrayList<String>();
	static {
		ADMINS.add("joao");
		ADMINS.add("joaomiguel.pereira@gmail.com");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.req = req;
		outBuffer = new StringBuffer();
		try {
			startTransaction(true);
		} catch (Exception e1) {
			throw new ServletException(e1);

		}

		outBuffer.append(createDiv("Reseting jobs:"));
		outBuffer.append(createDiv("Check Authentication"));

		try {
			checkAuthentication();
			outBuffer
					.append(createDiv("Logged User: " + loggedUser.getEmail()));
			
			
			getBusinessHelper().getJobsQueuePojo().clearJobs();
			
			//queue.setJobs(jobs);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e);
		} finally {
			endTransaction();
		}

		outBuffer.append(createDiv("Job done! Jobs are now reseted!"));
		resp.setContentType("text/html");

		resp.getWriter().println(outBuffer.toString());
	}

	private void checkAuthentication() throws Exception {

		UserService userService = UserServiceFactory.getUserService();
		String loginUrl = userService.createLoginURL(req.getRequestURI());
		// Check if the user is logged in with a
		this.loggedUser = userService.getCurrentUser();
		boolean ok = false;
		if (this.loggedUser != null) {
			ok = ADMINS.contains(this.loggedUser.getEmail());

		}
		if (!ok) {
			throw new Exception(
					"You're not allowed to use this service. Please login <a href=\""
							+ loginUrl + "\">login</a>");
		}

	}

	private String createDiv(String text) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div>");
		sb.append(text);
		sb.append("</div>");
		return sb.toString();
	}
}
