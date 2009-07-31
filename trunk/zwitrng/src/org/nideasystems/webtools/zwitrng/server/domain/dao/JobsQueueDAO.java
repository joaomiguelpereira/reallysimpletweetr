package org.nideasystems.webtools.zwitrng.server.domain.dao;

import java.util.ArrayList;

import javax.jdo.Query;

import org.nideasystems.webtools.zwitrng.server.domain.JobsQueueDO;
import org.nideasystems.webtools.zwitrng.server.jobs.Job;

public class JobsQueueDAO extends BaseDAO {

	public JobsQueueDO getQueue() {
		Query queryJobs = pm.newQuery(JobsQueueDO.class);
		
		
		queryJobs.setUnique(true);
		return (JobsQueueDO)queryJobs.execute();
	}

	public JobsQueueDO creatJobsQueue() {
		JobsQueueDO queue = new JobsQueueDO();
		queue.setJobs(new ArrayList<Job>());
		pm.makePersistent(queue);
		return queue;
	}

}
