package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;

public abstract class AbstractPojo {


	private final static Logger log = Logger.getLogger(AbstractPojo.class.toString());
	
	protected BusinessHelper businessHelper = null;

	
	
	public void setBusinessHelper(BusinessHelper businessHelper) {
		this.businessHelper = businessHelper;
		
	}
	
	
}
