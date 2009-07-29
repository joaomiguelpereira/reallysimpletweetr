package org.nideasystems.webtools.zwitrng.server.jobs;

import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.pojos.BusinessHelper;

public abstract class AbstractJob implements IJob {
	

	protected final static Logger log = Logger.getLogger(AbstractJob.class.getName());
	protected PersonaDO persona;
	protected BusinessHelper businessHelper;
	

	@Override
	public void setPersona(PersonaDO persona) {
		this.persona = persona;

	}


	public BusinessHelper getBusinessHelper() {
		return businessHelper;
	}


	public void setBusinessHelper(BusinessHelper businessHelper) {
		this.businessHelper = businessHelper;
	}
	
	

}
