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

public abstract class AbstractJobRunnerServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5215965661223511869L;
	private String queueName;
	private boolean single = false;
	private static final boolean TESTING = true;
	protected static final Logger log = Logger.getLogger(RunJobsServlet.class
			.getName());

	protected AbstractJobRunnerServlet(String queueName, boolean single ) {
		this.queueName = queueName;
		this.single = single;
	}
	
	protected void doRun(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log
				.info("= = = = = = = = = = = = =   R U N N I N G    J O B S  for QUEUE: "+queueName+  " = = = = = = = = = = = = =");
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
        List<Job> jobList = (List<Job>)cache.get(this.queueName);
        
        
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
            	if (single) {
            		log.fine("#############One job per call");
            		break;
            	}
            
            }
            
        	for (Job job:executedJobs) {
        		jobList.remove(job);
        	}
        	
        	cache.put(this.queueName, jobList);
        	log.fine("---- Jobs cache size is now "+jobList.size());
        }
        
        endTransaction();
                
        

		resp.setContentType("text/html");
		resp.getWriter().println("200 OK");

	}
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
		jobInstance.setParameters(job.getParameters());
		

		jobInstance.execute();
		log.fine("** Ending executing job for Persona: " + persona.getName());

	}

}
