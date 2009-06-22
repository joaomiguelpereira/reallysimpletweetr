package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.nideasystems.webtools.zwitrng.server.PMF;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;

public class TemplateDAO extends BaseDAO {
	
	private static final Logger log = Logger.getLogger(TemplateDAO.class.getName());

	public TemplateDTO createTemplate(long personaId, TemplateDTO template) {
		
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		
		/*//Find the persona
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query queryPersona = pm.newQuery(PersonaDO.class);// PMF.get().getPersistenceManager().newQuery(
		// PersonaDO.class);
		queryPersona
				.setFilter("name==paramPersonaName && userEmail==paramUserEmail");
		queryPersona
				.declareParameters("String paramPersonaName, String paramUserEmail");
		queryPersona.setUnique(true);

		PersonaDO persona = (PersonaDO) queryPersona.execute(personaName,
				userEmail);
*/

		
		
		
		TemplateDO templateDo = new TemplateDO();
		
		return null;
	}

	
}
