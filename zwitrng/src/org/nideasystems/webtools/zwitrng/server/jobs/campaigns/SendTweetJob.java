package org.nideasystems.webtools.zwitrng.server.jobs.campaigns;

import org.nideasystems.webtools.zwitrng.server.jobs.AbstractJob;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

public class SendTweetJob extends AbstractJob {

	@Override
	public void execute() throws Exception {
		//Get the update
		TwitterUpdateDTO upate = (TwitterUpdateDTO)parameters.get("status");
		
		if ( upate == null ) {
			throw new Exception("Update not set");
		}
		getBusinessHelper().getTwitterPojo().postUpdate(upate);

 	}

}
