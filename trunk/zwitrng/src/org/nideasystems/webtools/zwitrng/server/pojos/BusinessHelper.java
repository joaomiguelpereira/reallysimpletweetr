package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.nideasystems.webtools.zwitrng.server.domain.CampaignDAO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDAO;

public class BusinessHelper {

	private static final Logger log = Logger.getLogger(BusinessHelper.class
			.getName());
	private PersistenceManager pm = null;
	// Persona Pojos
	private ThreadLocal<PersonaPojo> personaPojo = new ThreadLocal<PersonaPojo>() {

		@Override
		protected PersonaPojo initialValue() {
			return new PersonaPojo();
		}

	};

	// campaign Pojos
	private ThreadLocal<CampaignPojo> campaignPojo = new ThreadLocal<CampaignPojo>() {

		@Override
		protected CampaignPojo initialValue() {
			return new CampaignPojo();
		}

	};

	// Templates Pojos
	private ThreadLocal<TemplatePojo> templatePojo = new ThreadLocal<TemplatePojo>() {

		@Override
		protected TemplatePojo initialValue() {
			return new TemplatePojo();
		}

	};

	// Persona DAO

	private ThreadLocal<PersonaDAO> personaDao = new ThreadLocal<PersonaDAO>() {
		@Override
		protected PersonaDAO initialValue() {
			return new PersonaDAO();
		}
	};

	// Campaign DAO

	private ThreadLocal<CampaignDAO> campaignDao = new ThreadLocal<CampaignDAO>() {
		@Override
		protected CampaignDAO initialValue() {
			return new CampaignDAO();
		}
	};
	// template DAO
	private ThreadLocal<TemplateDAO> templateDao = new ThreadLocal<TemplateDAO>() {
		@Override
		protected TemplateDAO initialValue() {
			return new TemplateDAO();
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
	 * 
	 * @return
	 */
	public TemplatePojo getTemplatePojo() {
		TemplatePojo pojo = templatePojo.get();
		pojo.setBusinessHelper(this);
		return pojo;
	}

	/**
	 * Get the Persona Pojo
	 * 
	 * @return
	 */
	public PersonaPojo getPersonaPojo() {
		PersonaPojo pojo = personaPojo.get();
		pojo.setBusinessHelper(this);
		return pojo;

	}

	/**
	 * Get the template DAO
	 * 
	 * @return
	 */
	protected TemplateDAO getTemplateDao() {
		TemplateDAO dao = templateDao.get();
		dao.setPm(pm);
		log.fine("Returning DAO " + dao.hashCode());
		return dao;
	}

	/**
	 * Get the Persona DAO
	 * 
	 * @return
	 */
	protected CampaignDAO getCampaignDao() {
		CampaignDAO dao = campaignDao.get();
		dao.setPm(pm);
		log.fine("Returning DAO " + dao.hashCode());
		return dao;
	}

	/**
	 * Get the Persona DAO
	 * 
	 * @return
	 */
	protected PersonaDAO getPersonaDao() {
		PersonaDAO dao = personaDao.get();
		dao.setPm(pm);
		log.fine("Returning DAO " + dao.hashCode());
		return dao;
	}

	public CampaignPojo getCampaignPojo() {
		CampaignPojo pojo = campaignPojo.get();
		pojo.setBusinessHelper(this);
		return pojo;
	}

}
