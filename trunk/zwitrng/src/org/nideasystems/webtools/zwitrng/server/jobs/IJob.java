package org.nideasystems.webtools.zwitrng.server.jobs;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.pojos.BusinessHelper;

public interface IJob {
	
	public void execute() throws Exception;
	public void setPersona(PersonaDO persona);

	public void setBusinessHelper(BusinessHelper bh);
}
