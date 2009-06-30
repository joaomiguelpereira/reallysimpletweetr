package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.Date;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTOList;

public class TemplatePojo extends AbstractPojo {

	private static final Logger log = Logger.getLogger(TemplatePojo.class
			.getName());

	/**
	 * Create a new Template
	 * 
	 * @param name
	 * @param email
	 * @param template
	 * @return
	 * @throws Exception
	 */
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

		// Create the Tags
		if (template.getTags() != null) {
			for (String tag : template.getTags()) {
				log.fine("creating Tag: " + tag);
				templateDom.addTag(tag);

			}
		}
		templateDom.setCreated(new Date());
		templateDom.setModified(new Date());

		// Save it
		persona.addtemplate(templateDom);
		// convert to DOM

		return DataUtils.templateDtoFromDom(templateDom);

	}

	/**
	 * Get all templates
	 * 
	 * @param name
	 *            Name of the person
	 * @param email
	 *            Email of the User
	 * @return A list with all templates
	 * @throws Exception
	 *             If something do wrong
	 */
	public TemplateDTOList getTemplates(String name, String email)
			throws Exception {

		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		if (persona == null) {
			throw new Exception("Persona not found");
		}

		TemplateDTOList list = new TemplateDTOList();
		if (persona.getTemplates() != null) {
			for (TemplateDO templateDom : persona.getTemplates()) {
				TemplateDTO templateDto = DataUtils
						.templateDtoFromDom(templateDom);
				log.fine("Adding Template DTO:" + templateDto);
				log.fine("Adding Template KEy:" + templateDom.getKey());

				list.addTemplate(templateDto);
			}
		}
		return list;
	}

	/**
	 * Delete a template for a persona
	 * 
	 * @param name
	 *            Persona nam
	 * @param email
	 *            user email
	 * @param template
	 *            template to delete
	 * @return the deleted template
	 */
	public TemplateDTO deleteTemplate(String name, String email,
			TemplateDTO template) throws Exception {

		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		if (persona == null) {
			throw new Exception("Persona not found");
		}

		try {
			businessHelper.getTemplateDao().deleteTemplate(persona, template);
		} catch (Exception e) {
			log.severe("Error trying to delete the Template");
			e.printStackTrace();
			throw e;

		}

		// Just return was was given
		return template;
	}

	public TemplateDTO saveTemplate(String name, String email,
			TemplateDTO template) throws Exception{
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		TemplateDTO retTemplate = null;
		if (persona == null) {
			throw new Exception("Persona not found");
		}

		try {
			retTemplate = businessHelper.getTemplateDao().saveTemplate(persona, template);
		} catch (Exception e) {
			log.severe("Error trying to saving the Template");
			e.printStackTrace();
			throw e;

		}

		// Just return was was given
		return retTemplate;
	}

	public TemplateFragmentDTOList getTemplateFragments(String name,
			String email) throws Exception{
		//Find the persona
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(name, email);
		if (persona == null) {
			throw new Exception("Persona not found");
		}
		TemplateFragmentDTO templateFrag = new TemplateFragmentDTO();
		templateFrag.setName("Name");
		templateFrag.setId(new Long(10));
		templateFrag.setList("123|123|123");
		templateFrag.addTag("Tag1");
		templateFrag.addTag("Tag2");
		templateFrag.setCreated(new Date());
		templateFrag.setModified(new Date());
		
		TemplateFragmentDTO templateFrag1 = new TemplateFragmentDTO();
		templateFrag1.setName("Name 1");
		templateFrag1.setId(new Long(10));
		templateFrag1.setList("123|123|123");
		templateFrag1.addTag("Tag3");
		templateFrag1.addTag("Tag4");
		templateFrag1.setCreated(new Date());
		templateFrag1.setModified(new Date());
		
		TemplateFragmentDTOList ret =new TemplateFragmentDTOList();
		ret.addTemplateFragmentList(templateFrag);
		ret.addTemplateFragmentList(templateFrag1);
		
		
		return ret;
	}

}
