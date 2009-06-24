package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;

public class TemplatePojo extends AbstractPojo {

	private static final Logger log = Logger.getLogger(TemplatePojo.class
			.getName());

	public TemplateDTO createTemplate(String name, String email,
			TemplateDTO template) throws Exception {
		log.fine("Creating template " + template);

		// Get the persona
		// PersonaDO persona =
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		if (persona == null) {
			throw new Exception("Persona not found");
		}

		// Create new Template
		TemplateDO templateDom = new TemplateDO();
		templateDom.setPersona(persona);
		templateDom.setText(template.getTemplateText());
		
		//Create the Tags
		if ( template.getTags()!= null ) {
			for (String tag: template.getTags() ) {
				log.fine("creating Tag: "+tag);
				templateDom.addTag(tag);
				
			}
		}
		// Save it
		persona.addtemplate(templateDom);
		// convert to DOM
		
		return DataUtils.templateDtoFromDom(templateDom);

	}

	public TemplateDTOList getTemplates(String name, String email) throws Exception{
		
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		if (persona == null) {
			throw new Exception("Persona not found");
		}

		TemplateDTOList list = new TemplateDTOList();
		if ( persona.getTemplates()!= null ) {
			for ( TemplateDO templateDom:persona.getTemplates()) {
				list.addTemplate(DataUtils.templateDtoFromDom(templateDom));
			}
		}
		return list;
	}

}
