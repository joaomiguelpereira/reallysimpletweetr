package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.logging.Logger;

import javax.jdo.Query;

import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;



public class TemplateDAO extends BaseDAO {
	
	private static final Logger log = Logger.getLogger(TemplateDAO.class.getName());

	public void deleteTemplate(PersonaDO persona, TemplateDTO template) throws Exception {
		log.fine("Deleting template: "+template.getTemplateText());
		Key key = KeyFactory.createKey(persona.getKey(), TemplateDO.class.getSimpleName(), template.getId());
		log.fine("Key constructed is: "+key.toString());
		
		TemplateDO templateDo = pm.getObjectById(TemplateDO.class, key);
		
		if ( templateDo == null ) {
			log.severe("Error: The Template does not exists");
		} else {
			log.fine("Template found");
			//deleting
			persona.getTemplates().remove(templateDo);
		}

		//find the template
		
		
	}
	
}
