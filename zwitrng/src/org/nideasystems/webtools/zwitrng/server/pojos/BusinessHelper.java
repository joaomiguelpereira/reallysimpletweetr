package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;

public class BusinessHelper {

	private static final Logger log = Logger.getLogger(BusinessHelper.class.getName());
	private PersistenceManager pm = null;
	//Persona Pojos
	private ThreadLocal<PersonaPojo> personaPojo = new ThreadLocal<PersonaPojo>() {

		@Override
		protected PersonaPojo initialValue() {
			return new PersonaPojo();
		}

	};

	//Templates Pojos
	private ThreadLocal<TemplatePojo> templatePojo = new ThreadLocal<TemplatePojo>() {

		@Override
		protected TemplatePojo initialValue() {
			return new TemplatePojo();
		}

	};
	
	private ThreadLocal<PersonaDAO> personaDao = new ThreadLocal<PersonaDAO>() {
		@Override
		protected PersonaDAO initialValue() {
			return new PersonaDAO();
		}
	};

	public void setPm(PersistenceManager pm) {
		this.pm = pm;
	}

	public PersistenceManager getPm() {
		return pm;
	}


	/**
	 * Get the Template Pojo
	 * @return
	 */
	public TemplatePojo getTemplatePojo() {
		TemplatePojo pojo = templatePojo.get();
		pojo.setBusinessHelper(this);
		return pojo;
	}
	
	/**
	 * Get the Persona Pojo
	 * @return
	 */
	public PersonaPojo getPersonaPojo() {
		PersonaPojo pojo = personaPojo.get();
		pojo.setBusinessHelper(this);
		return pojo;
		
	}
	
	
	
	protected PersonaDAO getPersonaDao() {

		PersonaDAO dao = personaDao.get();
		dao.setPm(pm);
		log.fine("Returning DAO " + dao.hashCode());
		return dao;
	}
	
}
