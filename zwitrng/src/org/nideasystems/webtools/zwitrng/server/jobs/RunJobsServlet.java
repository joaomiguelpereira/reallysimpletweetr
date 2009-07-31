package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import java.util.List;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.Configuration;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;

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
		getBusinessHelper().setStartTime(new Date().getTime());
		//Get the cache
		Cache cache = null;
		try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
        } catch (CacheException e) {
            throw new ServletException(e);
        }
        //Try to get the list from cahce
        List<Job> jobList = (List<Job>)cache.get("jobs");
        
        
        //go thorugh all jobs and executin until theres time
        
        
        
        if ( jobList != null) {
        	
        	log.fine("---- Jobs cache size is "+jobList.size());
        	
        	List<Job> executedJobs = new ArrayList<Job>();
        	for (int i=0;i<jobList.size();i++) {
            
        		Job job = jobList.get(i);
            	//jobList.remove(0);
            	//cache.put("jobs", jobList);
            	
            	if (job!=null) {
            		executedJobs.add(job);
            		try {
    					execute(job);
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
            	}
            	long now = new Date().getTime();
            	log.fine("###Now:"+now);
            	log.fine("###Started:"+getBusinessHelper().getStartTime());
            	
            	if ( now - getBusinessHelper().getStartTime() >= Configuration.MAX_JOB_RUN_TIME ) {
            		log.fine("#############Time Elapsed. Will not try to run any more job. Jobs Executed:"+i+1);
            		break;
            	}
            
            }
            
        	for (Job job:executedJobs) {
        		jobList.remove(job);
        	}
        	
        	cache.put("jobs", jobList);
        	log.fine("---- Jobs cache size is now "+jobList.size());
        }
        
        endTransaction();
                
        

		resp.setContentType("text/html");
		resp.getWriter().println("200 OK");

	}

	/*private void initializePersonaJobs(PersonaDO persona) {
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
*/
	private void execute(Job job) throws Exception {
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
