package org.nideasystems.webtools.zwitrng.server.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.jobs.Job;



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
			
			
			//getBusinessHelper().getJobsQueuePojo().clearJobs();
			
			outBuffer.append(createDiv("Clearing job defs..."));
			List<PersonaDO> personas = getBusinessHelper().getPersonaDao().findAllActivePersonas();
			
			for ( PersonaDO persona: personas ) {
				outBuffer.append(createDiv("Clear Jobs Defs for Persona: "+persona.getName()));
				if (persona.getJobDefs()!= null) {
					persona.getJobDefs().clear();
				}
			}
			//queue.setJobs(jobs);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e);
		} finally {
			endTransaction();
		}

		//Add it to cache
		Cache cache = null;
		try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
        } catch (CacheException e) {
            throw new ServletException(e);
        }
        //Try to get the list from cahc
        outBuffer.append(createDiv("Reseting jobs Queuue"));
        List<Job> jobList = (List<Job>)cache.put("jobs", new ArrayList<Job>());
        
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
