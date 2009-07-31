package org.nideasystems.webtools.zwitrng.server.pojos;


import org.nideasystems.webtools.zwitrng.server.domain.JobsQueueDO;

public class JobsQueueuPojo extends AbstractPojo {

	public JobsQueueDO getQueue() {
		JobsQueueDO queue = businessHelper.getJobsQueueDao().getQueue();
		if (queue == null) {
			queue = businessHelper.getJobsQueueDao().creatJobsQueue();
		}

		return queue;
	}

	public void clearJobs() {
		JobsQueueDO queue = getQueue();
		queue.getJobs().clear();
		//List<JobDO> jobs = queue.getJobs();		
		queue.setLastUsedJobIndex(0);
		
		
	}

}
