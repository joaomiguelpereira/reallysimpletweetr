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

public class RunJobsServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6248867066738776402L;
	private static final boolean TESTING = false;
	private static final Logger log = Logger.getLogger(RunJobsServlet.class
			.getName());

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		log
				.info("= = = = = = = = = = = = =   R U N N I N G    J O B S   = = = = = = = = = = = = =");
		// Check headers

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
		JobsQueueDO jobsQueueDO = getBusinessHelper().getJobsQueuePojo()
				.getQueue();
		Integer lastUsedPersona = jobsQueueDO.getLastPersonaUsedIndex();
		if (lastUsedPersona == null) {
			lastUsedPersona = 0;
		}

		PersonaDO personaDO = personas.get(lastUsedPersona);
		// Get job defs
		List<PersonaJobDefDO> jobDefs = personaDO.getJobDefs();
		if (jobDefs == null) {
			initializePersonaJobs(personaDO);
		}
		jobDefs = personaDO.getJobDefs();
		boolean executed = false;
		log.fine("-- Going to run jobs for persona: "+personaDO.getName());
		for (PersonaJobDefDO job : jobDefs) {

			long now = new Date().getTime();
			log.fine("**Going to check job: " + job.getName());
			log.fine("**Last Run: " + job.getLastRun());
			log.fine("**Class: " + job.getJobClass());
			log.fine("**Wait Time: " + job.getWaitTime());
			
			long elapsedTime = now - job.getLastRun();
			log.fine("**Elapsed Time: " + elapsedTime);
			if (elapsedTime >= job.getWaitTime()) {
				try {

					execute(job, personaDO);
					executed = true;
					job.setLastRun(now);

				} catch (Exception e) {
					log.warning("Could not execute the job:" + job.getName());
					e.printStackTrace();
				}
			}
			
			if (executed) {
				break;
			}
		}

		//Go to next persona
		lastUsedPersona++;
		if (lastUsedPersona>=personas.size()) {
			lastUsedPersona=0;
		}
		jobsQueueDO.setLastPersonaUsedIndex(lastUsedPersona);
		endTransaction();
		resp.setContentType("text/html");
		resp.getWriter().println("200 OK");

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
		createAutoFollowBackUserJob
				.setName("Create Auto Follow Back User Queue");
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
		createAutoUnFollowBackQueueJob.setWaitTime(1000 * 60 * 60 * 24);// 1 day
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

	private void execute(PersonaJobDefDO jobDef, PersonaDO persona)
			throws Exception {

		log.fine("** Start executing job for Persona: " + persona.getName());
		Class<?> jobClass = null;
		try {
			jobClass = Class.forName(jobDef.getJobClass());
		} catch (ClassNotFoundException e) {
			log.severe("Could not instantiate the class:"
					+ jobDef.getJobClass());
			e.printStackTrace();
			throw new Exception(e);
		}
		IJob jobInstance = (IJob) jobClass.newInstance();
		jobInstance.setBusinessHelper(getBusinessHelper());
		jobInstance.setPersona(persona);
		jobInstance.execute();
		log.fine("** Ending executing job for Persona: " + persona.getName());

	}

	private void execute(JobDO job) throws Exception {
		PersonaDO persona = getBusinessHelper().getPersonaDao().findPersona(
				job.getPersonaKey());

		if (persona == null) {
			throw new Exception("Persona not found");
		}
		log.fine("** Start executing job for Persona: " + persona.getName());
		Class<?> jobClass = null;
		try {
			jobClass = Class.forName(job.getJobClassName());
		} catch (ClassNotFoundException e) {
			log.severe("Could not instantiate the class:"
					+ job.getJobClassName());
			e.printStackTrace();
			throw new Exception(e);
		}
		IJob jobInstance = (IJob) jobClass.newInstance();

		jobInstance.setBusinessHelper(getBusinessHelper());
		jobInstance.setPersona(persona);

		jobInstance.execute();
		log.fine("** Ending executing job for Persona: " + persona.getName());

	}

}
