package org.nideasystems.webtools.zwitrng.server.domain.dao;

import javax.jdo.PersistenceManager;



public class BaseDAO {
	
	protected PersistenceManager pm;

	public void setPm(PersistenceManager pm) {
		this.pm = pm;
	}



	public PersistenceManager getPm() {
		return pm;
	}

	
}
