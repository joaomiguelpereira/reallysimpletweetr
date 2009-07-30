package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.domain.JobDO;
import org.nideasystems.webtools.zwitrng.server.domain.JobsQueueDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaJobDefDO;
import org.nideasystems.webtools.zwitrng.server.servlets.AbstractHttpServlet;

public class CreateJobsServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5946090469465219700L;
	private static final boolean TESTING = false;
	private StringBuffer outBuffer;
	private static final Logger log = Logger.getLogger(CreateJobsServlet.class
			.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log
				.info("= = = = = = = = = = = = =   C R E A T I N G    J O B S   = = = = = = = = = = = = =");
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
			// TODO Auto-generated catch block
			throw new ServletException(e1);

		}

		List<PersonaDO> personas = getBusinessHelper().getPersonaDao()
				.findAllActivePersonas();
		log.fine("Going to run jobs for " + personas.size() + " personas");

		for (PersonaDO persona : personas) {
			long now = new Date().getTime();
			// Check if the last execution of this persona was less than 2
			// minutes ago

			log.fine("==C r e a t i n g   J o b s   f o r   P e r s o n a "
					+ persona.getName() + "===");
			List<PersonaJobDefDO> jobs = persona.getJobDefs();

			if (jobs == null || jobs.size() == 0) {
				initializePersonaJobs(persona);
				jobs = persona.getJobDefs();
			}

			// Get the jobs

			for (PersonaJobDefDO job : jobs) {

				log.fine("**Going to check job: " + job.getName());
				log.fine("**Last Run: " + job.getLastRun());
				log.fine("**Class: " + job.getJobClass());
				log.fine("**Wait Time: " + job.getWaitTime());
				log.fine("**Elapsed Time: " + job.getWaitTime());
				long elapsedTime = now - job.getLastRun();
				if (elapsedTime >= job.getWaitTime()) {
					try {
						
						enqueuJob(job, persona);

					} catch (Exception e) {
						log.warning("Could not execute the job:"
								+ job.getName());
						e.printStackTrace();
					}
				}
			}

			log.fine("==End C r e a t i n g   J o b s   f o r   P e r s o n a "
					+ persona.getName() + "===");
		}

		endTransaction();
		resp.setContentType("text/html");
		if (TESTING) {
			resp.getWriter().println(outBuffer.toString());
		} else {
			resp.getWriter().println("200 OK");
		}
	}

	private void enqueuJob(PersonaJobDefDO job, PersonaDO persona)
			throws Exception {

		log.fine(" = = = = = Enqueueing job ---"
				+ job.getName() + "--- for persona: " + persona.getName());
		// Create the class instance
		// Get the JobsQueueDo
		JobsQueueDO jobsQueue = getBusinessHelper().getJobsQueuePojo()
				.getQueue();

		if (jobsQueue.getJobs() == null) {
			jobsQueue.setJobs(new ArrayList<JobDO>());
		}
		JobDO jobDo = new JobDO();
		jobDo.setJobClassName(job.getJobClass());
		jobDo.setPersonaKey(persona.getKey());
		jobsQueue.getJobs().add(jobDo);
		job.setLastRun(new Date().getTime());

	}

	private void initializePersonaJobs(PersonaDO persona) {
		List<PersonaJobDefDO> jobDefs = new ArrayList<PersonaJobDefDO>();

		PersonaJobDefDO createAutoFollowJob = new PersonaJobDefDO();
		createAutoFollowJob.setLastRun(0);
		createAutoFollowJob.setName("Create Auto Follow Queue");
		createAutoFollowJob.setWaitTime(1000 * 60 * 30);// 30 min
		createAutoFollowJob.setJobClass(CreateAutoFollowQueueJob.class
				.getName());
		jobDefs.add(createAutoFollowJob);

		PersonaJobDefDO autoFollowJob = new PersonaJobDefDO();
		autoFollowJob.setLastRun(0);
		autoFollowJob.setName("Auto Follow User");
		autoFollowJob.setWaitTime(1000 * 60 * 5);// 5 minutes
		autoFollowJob.setJobClass(AutoFollowUserJob.class.getName());
		jobDefs.add(autoFollowJob);

		PersonaJobDefDO createAutoFollowBackUserJob = new PersonaJobDefDO();
		createAutoFollowBackUserJob.setName("Create Auto Follow Back User Queue");
		createAutoFollowBackUserJob.setLastRun(0);
		createAutoFollowBackUserJob.setWaitTime(1000 * 60 * 30);// 30 min
		createAutoFollowBackUserJob
				.setJobClass(CreateAutoFollowBackQueueJob.class.getName());
		jobDefs.add(createAutoFollowBackUserJob);

		PersonaJobDefDO autoFollowBackUser = new PersonaJobDefDO();
		autoFollowBackUser.setName("Auto Follow Back User");
		autoFollowBackUser.setWaitTime(1000 * 60 * 5);// 5 minutes
		autoFollowBackUser.setLastRun(0);
		autoFollowBackUser.setJobClass(AutoFollowBackUserJob.class.getName());
		jobDefs.add(autoFollowBackUser);

		PersonaJobDefDO createAutoUnFollowBackQueueJob = new PersonaJobDefDO();
		createAutoUnFollowBackQueueJob
				.setName("Create Auto unfollow User queue");
		createAutoUnFollowBackQueueJob.setWaitTime(1000 * 60 * 60*24);// 1 day 
		createAutoUnFollowBackQueueJob.setLastRun(0);
		createAutoUnFollowBackQueueJob
				.setJobClass(CreateAutoUnfollowBackQueueJob.class.getName());
		jobDefs.add(createAutoUnFollowBackQueueJob);

		PersonaJobDefDO autoUnfollowUserJob = new PersonaJobDefDO();
		autoUnfollowUserJob.setName("Auto unfollow User");
		autoUnfollowUserJob.setLastRun(0);
		autoUnfollowUserJob.setWaitTime(1000 * 60 * 5);// 5 minutes
		autoUnfollowUserJob.setJobClass(AutoUnfollowUserJob.class.getName());
		jobDefs.add(autoUnfollowUserJob);

		PersonaJobDefDO synchUsersInfoJob = new PersonaJobDefDO();
		synchUsersInfoJob.setName("Synchronize Users Information");
		synchUsersInfoJob.setLastRun(0);
		synchUsersInfoJob.setWaitTime(1000 * 60 * 10);// 10 minutes
		synchUsersInfoJob.setJobClass(SynchUsersInfoJob.class.getName());
		jobDefs.add(synchUsersInfoJob);
		persona.setJobDefs(jobDefs);

	}
}
