package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateFragmentDO;
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
		templateDom.setUsedTimes(new Long(0));

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
			TemplateDTO template) throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		TemplateDTO retTemplate = null;
		if (persona == null) {
			throw new Exception("Persona not found");
		}

		try {
			retTemplate = businessHelper.getTemplateDao().saveTemplate(persona,
					template);
		} catch (Exception e) {
			log.severe("Error trying to saving the Template");
			e.printStackTrace();
			throw e;

		}

		// Just return was was given
		return retTemplate;
	}

	public TemplateFragmentDTOList getTemplateFragments(String name,
			String email) throws Exception {
		// Find the persona
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);
		if (persona == null) {
			throw new Exception("Persona not found");
		}

		TemplateFragmentDTOList retList = new TemplateFragmentDTOList();
		if (persona.getTemplateFragments() != null) {
			for (TemplateFragmentDO frag : persona.getTemplateFragments()) {
				retList.addTemplateFragmentList(DataUtils
						.templateFragmentDtoFromDom(frag));
				log.fine("Adding template frag");
			}
		}

		return retList;
	}

	public TemplateFragmentDTO createTemplateFragment(
			TemplateFragmentDTO object, String name, String email)
			throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		if (persona == null) {
			throw new Exception("Persona not found");
		}
		// Check if exists any fragment with the same name
		TemplateFragmentDO domFrag = businessHelper.getTemplateDao()
				.findTemplateFragmentByName(persona, object.getName());
		TemplateFragmentDTO returnDto = null;
		if (domFrag != null) {
			throw new Exception("A Template Fragment exists with the Same name");
		} else {
			log.fine("Creating a new Template Fragment");
			returnDto = businessHelper.getTemplateDao().createTemplateFragment(
					persona, object);

		}

		return returnDto;
	}

	public TemplateFragmentDTO saveTemplateFragment(TemplateFragmentDTO object,
			String name, String email) throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		if (persona == null) {
			throw new Exception("Persona not found");
		}

		return businessHelper.getTemplateDao().saveTemplateFragment(persona,
				object);
	}

	public TemplateFragmentDTO deleteTemplateFragment(String name,
			String email, TemplateFragmentDTO dataObject) throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		if (persona == null) {
			throw new Exception("Persona not found");
		}

		try {
			businessHelper.getTemplateDao().deleteTemplateFragment(persona,
					dataObject);
		} catch (Exception e) {
			log.severe("Error trying to delete the Template");
			e.printStackTrace();
			throw e;

		}

		// Just return was was given
		return dataObject;
	}

	public Map<String, String> getFragmentsLists(String name, String email,
			List<String> lists) throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);
		Map<String, String> ret = new HashMap<String, String>();

		if (persona == null) {
			throw new Exception("Persona not found");
		}

		// Get the templateFrag
		for (String listName : lists) {
			TemplateFragmentDO tFrag = businessHelper.getTemplateDao()
					.findTemplateFragmentByName(persona, listName);
			if (tFrag != null) {
				ret.put(listName, tFrag.getText());
			} else {
				ret.put(listName, "NOT_FOUND");
			}
		}

		return ret;

	}

	public TemplateDO getRandomTemplateForCampaign(CampaignDO campaign) {
		TemplateDO template = null;
		List<TemplateDO> templates = null;
		// Try the cache
		Cache cache = null;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(
					Collections.emptyMap());
		} catch (CacheException e1) {
			// TODO Auto-generated catch block
			log.warning("Could not get a Cache instance " + cache);
			e1.printStackTrace();
		}
		if (cache != null) {
			templates = (List<TemplateDO>) cache.get(campaign.getKey());
			log.fine("using Cache");
		}

		if (templates == null) {

			try {

				templates = businessHelper.getTemplateDao().findTemplate(
						campaign);
			} catch (Exception e) {
				log.warning("Could not find any template for campaign: "
						+ campaign.getName());
				e.printStackTrace();
			}

			// add to cache
			if (cache != null && templates != null) {
				log.fine("Adding templates for campaign "+campaign.getName()+" to cache");
				cache.put(campaign.getKey(), templates);
			}

		}
		if (templates != null) {
			// /Select a random one

			if (templates.size() > 0) {
				double rand = Math.random();
				int index = (int) Math.round(rand * (templates.size() - 1));
				template = templates.get(index);

			}

		}

		// Only need one, and since I'm ordering for the least used, return the
		// first in the list and discart others
		return template;
	}
	
	

}
