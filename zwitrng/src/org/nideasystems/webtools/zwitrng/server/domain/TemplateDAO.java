package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.Query;

import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTO;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class TemplateDAO extends BaseDAO {

	private static final Logger log = Logger.getLogger(TemplateDAO.class
			.getName());

	public void deleteTemplate(PersonaDO persona, TemplateDTO template)
			throws Exception {
		log.fine("Deleting template: " + template.getTemplateText());
		Key key = KeyFactory.createKey(persona.getKey(), TemplateDO.class
				.getSimpleName(), template.getId());
		log.fine("Key constructed is: " + key.toString());

		TemplateDO templateDo = pm.getObjectById(TemplateDO.class, key);

		if (templateDo == null) {
			log.severe("Error: The Template does not exists");
		} else {
			log.fine("Template found");
			// deleting
			persona.getTemplates().remove(templateDo);
		}

		// find the template

	}

	public TemplateDTO saveTemplate(PersonaDO persona, TemplateDTO template)
			throws Exception {
		log.fine("Saving template: " + template.getTemplateText());
		Key key = KeyFactory.createKey(persona.getKey(), TemplateDO.class
				.getSimpleName(), template.getId());
		log.fine("Key constructed is: " + key.toString());

		TemplateDO templateDo = pm.getObjectById(TemplateDO.class, key);
		TemplateDTO returnTemplateDto = null;
		if (templateDo == null) {
			log.severe("Error: The Template does not exists");
		} else {
			log.fine("Template found. Going to modify");

			// persona.getTemplates().remove(templateDo);
			templateDo.setModified(new Date());
			templateDo.setText(template.getTemplateText());

			if (templateDo.getUsedTimes()==null) {
				templateDo.setUsedTimes(new Long(0));
			}
			// Tags
			if (templateDo.getTags() != null) {
				templateDo.getTags().clear();
			}

			for (String tag : template.getTags()) {
				templateDo.addTag(tag.trim());
			}

			returnTemplateDto = DataUtils.templateDtoFromDom(templateDo);
		}
		return returnTemplateDto;

	}

	/**
	 * Find a template fragment by its name
	 * 
	 * @param persona
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public TemplateFragmentDO findTemplateFragmentByName(PersonaDO persona,
			String name) throws Exception {
		log.fine("trying to find a templateFragment by Name");
		Query queryTemplateFrag = pm.newQuery(TemplateFragmentDO.class);

		queryTemplateFrag
				.setFilter("name==tempFragName && persona==personaObj");
		queryTemplateFrag
				.declareParameters("String tempFragName, PersonaDO personaObj");
		queryTemplateFrag.setUnique(true);

		TemplateFragmentDO templateFragment = (TemplateFragmentDO) queryTemplateFrag
				.execute(name, persona);

		return templateFragment;
	}

	/**
	 * Create a new Template Fragment
	 * 
	 * @param persona
	 * @param object
	 * @return
	 */
	public TemplateFragmentDTO createTemplateFragment(PersonaDO persona,
			TemplateFragmentDTO object) {

		TemplateFragmentDO fragDo = new TemplateFragmentDO();
		fragDo.setName(object.getName());
		fragDo.setText(object.getList());
		for (String tag : object.getTags()) {
			fragDo.addTag(tag);
		}
		fragDo.setCreated(new Date());
		fragDo.setModified(new Date());
		fragDo.setPersona(persona);
		fragDo.setMaintainOrder(object.getMaintainOrder());
		fragDo.setRepeatInCampaignAndTemplate(object.getRepeatInCampaignAndTemplate());
		persona.addtemplateFragment(fragDo);
		
		return DataUtils.templateFragmentDtoFromDom(fragDo);
	}

	public TemplateFragmentDTO saveTemplateFragment(PersonaDO persona,
			TemplateFragmentDTO object) throws Exception{

		TemplateFragmentDO domFrag = findTemplateFragmentByName(persona, object.getName());
		
		if (domFrag == null) {
			throw new Exception("Object not found");
		}
		
		//domFrag.setName(object.getName());
		domFrag.setText(object.getList());
		if (domFrag.getTags()!=null) {
			domFrag.getTags().clear();
		}

		for (String tag : object.getTags()) {
			domFrag.addTag(tag);
		}
		domFrag.setMaintainOrder(object.getMaintainOrder());
		domFrag.setRepeatInCampaignAndTemplate(object.getRepeatInCampaignAndTemplate());
		domFrag.setModified(new Date());
		return DataUtils.templateFragmentDtoFromDom(domFrag);
	}

	public void deleteTemplateFragment(PersonaDO persona,
			TemplateFragmentDTO dataObject) {
		log.fine("Deleting template: " + dataObject.getList());
		
		Key key = KeyFactory.createKey(persona.getKey(), TemplateFragmentDO.class
				.getSimpleName(), dataObject.getId());
		
		log.fine("Key constructed is: " + key.toString());

		TemplateFragmentDO templateDo = pm.getObjectById(TemplateFragmentDO.class, key);

		if (templateDo == null) {
			log.severe("Error: The Template does not exists");
		} else {
			log.fine("Template found");
			// deleting
			persona.getTemplateFragments().remove(templateDo);
		}

		
	}

	public List<TemplateDO> findTemplate(CampaignDO campaign) throws Exception{
		log.fine("__________________FIND TEMPLATES FOR CAMPAIGN: "+campaign.getName());
		
		//Get the tags
		String[] tags = StringUtils.splitText(campaign.getFilterByTemplateTags());
		log.fine("Tags to find: ");
		StringBuffer sb = new StringBuffer();
		for (String tag: tags ) {
			log.fine("Using Tag: "+tag.trim());
			sb.append(" && tags.contains(\""+tag.trim()+"\")");
			
		}
		Query queryTemplate = pm.newQuery(TemplateDO.class);
		
		//queryTemplate.setFilter("persona==thePersona"+sb.toString());
		queryTemplate.setFilter("persona==thePersona");
		
		queryTemplate.setOrdering("usedTimes asc");
		queryTemplate.declareParameters("PersonaDO thePersona");
		log.fine("QUERY: "+queryTemplate.toString());
		
		
		//List<TemplateDO> templates = (List<TemplateDO>)queryTemplate.execute(campaign.getPersona());
		List<TemplateDO> templates = (List<TemplateDO>)queryTemplate.execute(campaign.getPersona());
		
		if (templates==null) {
			log.severe("No Templates found for Campaign"+campaign.getName()+" for user "+campaign.getPersona().getUserEmail());
			//templates = new ArrayList<TemplateDO>();
			throw new Exception("No Tamplates found for Campaign"+campaign.getName()+" for user "+campaign.getPersona().getUserEmail());
		} 

		//Optimization
		List<TemplateDO> retTemplates = new ArrayList<TemplateDO>();
		for (TemplateDO template: templates ) {
			//Find a template with tag 
			//if ( template.getTags().contains(tag))
			boolean add = false;
			for (String tag:tags) {
				log.fine("Template: "+template.getText());
				add = false;
				if ( template.getTags().contains(tag)) {
					add = true;
				}
				if ( add ) {
					retTemplates.add(template);
				}
				
			}
		}
		
		//return templates;
		return retTemplates;
		//Find the templates for the filter
		
		
	}

}
