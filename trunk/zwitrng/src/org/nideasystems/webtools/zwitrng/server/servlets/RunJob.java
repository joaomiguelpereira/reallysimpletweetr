package org.nideasystems.webtools.zwitrng.server.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.AuthorizationManager;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.jobs.AutoFollowUserJob;
import org.nideasystems.webtools.zwitrng.server.jobs.CreateAutoFollowQueueJob;
import org.nideasystems.webtools.zwitrng.server.jobs.CreateAutoUnfollowBackQueueJob;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class RunJob extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7643382560633110712L;
	private StringBuffer outBuffer = null;
	private User loggedUser = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		outBuffer = new StringBuffer();
		try {
			startTransaction(true);
		} catch (Exception e1) {
			throw new ServletException(e1);

		}

		outBuffer.append(createDiv("Run Job:"));
		outBuffer.append(createDiv("Check Authentication"));

		UserService userService = UserServiceFactory.getUserService();
		// Check if the user is logged in with a

		try {
			this.loggedUser = AuthorizationManager.checkAuthentication();
		} catch (Exception e) {
			String loginLink = "<a href=\""+ userService.createLoginURL(req.getRequestURI())+ "\">login</a>";
			throw new ServletException("Please login "+loginLink);
		}
		if (req.getParameter("name") == null) {
			throw new ServletException("Bad parameters");
		}
		if (req.getParameter("name").equals("followUsers")) {
			outBuffer.append(createDiv("Running Job Follow Users"));
			// Find all persona for the user
			try {
				List<PersonaDO> personas = getBusinessHelper().getPersonaDao()
						.findAllPersonas(loggedUser.getEmail());

				for (PersonaDO persona : personas) {
					outBuffer.append(createDiv("Running job for persona: "
							+ persona.getName()));
					AutoFollowUserJob job = new AutoFollowUserJob();
					job.setBusinessHelper(getBusinessHelper());
					job.setPersona(persona);

					job.execute();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ServletException(e);
			}

		} else if (req.getParameter("name").equals("createFollowQueue")) {
			outBuffer.append(createDiv("Running Job Follow Users"));
			// Find all persona for the user
			try {

				List<PersonaDO> personas = getBusinessHelper().getPersonaDao()
						.findAllPersonas(loggedUser.getEmail());

				for (PersonaDO persona : personas) {
					outBuffer.append(createDiv("Running job for persona: "
							+ persona.getName()));
					CreateAutoFollowQueueJob job = new CreateAutoFollowQueueJob();
					job.setBusinessHelper(getBusinessHelper());
					job.setPersona(persona);

					job.execute();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ServletException(e);
			}

		} else if (req.getParameter("name").equals("createunFollowQueue")) {
			try {

				List<PersonaDO> personas = getBusinessHelper().getPersonaDao()
						.findAllPersonas(loggedUser.getEmail());
				
				for (PersonaDO persona : personas) {
					outBuffer.append(createDiv("Running Create Unfollow Queue for persona: "
							+ persona.getName()));
					outBuffer.append(createDiv("Unfollow Queue size is: "+(persona.getTwitterAccount().getAutoUnFollowBackIdsQueue()!=null?persona.getTwitterAccount().getAutoUnFollowBackIdsQueue():"NULL")));
					CreateAutoUnfollowBackQueueJob job = new CreateAutoUnfollowBackQueueJob();
					job.setBusinessHelper(getBusinessHelper());
					job.setPersona(persona);
					job.execute();
					outBuffer.append(createDiv("Unfollow Queue size is now: "+(persona.getTwitterAccount().getAutoUnFollowBackIdsQueue()!=null?persona.getTwitterAccount().getAutoUnFollowBackIdsQueue():"NULL")));

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ServletException(e);
			}

		} else {
			throw new ServletException("Invalid parameters");
		}

		try {
			outBuffer
					.append(createDiv("Logged User: " + loggedUser.getEmail()));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e);
		} finally {
			endTransaction();
		}

		outBuffer.append(createDiv("Job done!"));
		resp.setContentType("text/html");

		resp.getWriter().println(outBuffer.toString());
	}

	private String createDiv(String text) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div>");
		sb.append(text);
		sb.append("</div>");
		return sb.toString();
	}
}
