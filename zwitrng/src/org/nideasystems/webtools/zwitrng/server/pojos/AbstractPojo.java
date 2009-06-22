package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;

public abstract class AbstractPojo {


	private final static Logger log = Logger.getLogger(AbstractPojo.class.toString());
	private PersistenceManager pm;
	
	private ThreadLocal<PersonaDAO> personaDao = new ThreadLocal<PersonaDAO>() {

		@Override
		protected PersonaDAO initialValue() {
			return new PersonaDAO();
		}

	};

	protected PersonaDAO getPersonaDao() {

		PersonaDAO dao = personaDao.get();
		dao.setPm(pm);
		log.fine("Returning DAO " + dao.hashCode());
		return dao;
	}
	
	public PersistenceManager getPm() {
		return pm;
	}

	public void setPm(PersistenceManager pm) {
		this.pm = pm;
	}
	
}
