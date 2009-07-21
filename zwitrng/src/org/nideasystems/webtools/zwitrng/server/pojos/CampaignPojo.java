package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.CampaignInstanceDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.RSSItemDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;

public class CampaignPojo extends AbstractPojo {

	private static final Logger log = Logger.getLogger(CampaignPojo.class
			.getName());

	/**
	 * 
	 * @param name
	 * @param email
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public CampaignDTO createCampaign(String name, String email,
			CampaignDTO object) throws Exception {
		// find the persona
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);
		if (persona == null) {
			throw new Exception();
		}

		// Check if the campaign name alreadey exists
		CampaignDO campaignDom = businessHelper.getCampaignDao()
				.findByPersonaNameAndCampaignName(persona, object.getName());
		if (campaignDom != null) {
			throw new Exception("A Campaign with the same name already exixts");
		}

		campaignDom = businessHelper.getCampaignDao().create(persona, object);
		buildTweetTemplates(campaignDom);
		return DataUtils.campaignDtoFromDo(campaignDom);

	}

	/**
	 * 
	 * @param name
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public CampaignDTOList findCampaigns(String name, String email)
			throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		log.fine("findCampaigns");
		if (persona == null) {
			throw new Exception();
		}
		CampaignDTOList retList = new CampaignDTOList();

		if (persona.getCampaigns() != null) {
			for (CampaignDO dom : persona.getCampaigns()) {
				retList.addCampaign(DataUtils.campaignDtoFromDo(dom));
				log.fine("adding campaign");
			}
		}

		return retList;

	}

	/**
	 * 
	 * @param name
	 * @param email
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public CampaignDTO saveCampaign(String name, String email,
			CampaignDTO object) throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);
		if (persona == null) {
			throw new Exception();
		}

		// Check if the campaign name alreadey exists
		CampaignDO campaignDom = businessHelper.getCampaignDao()
				.findByPersonaNameAndCampaignName(persona, object.getName());
		if (campaignDom == null) {
			throw new Exception("The campaign was not found");
		}

		// clearTemplatesInCache(campaignDom);
		return businessHelper.getCampaignDao().save(persona, object,
				campaignDom);

	}

	public CampaignDTO setCampainStatus(PersonaDTO model, String campaignName,
			CampaignStatus status) throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(model.getName(),
						model.getUserEmail());
		if (persona == null) {
			throw new Exception();
		}
		CampaignDO campaign = null;
		try {
			campaign = businessHelper.getCampaignDao()
					.findByPersonaNameAndCampaignName(persona, campaignName);
		} catch (Exception e) {
			log.severe("Error finding the template");
			e.printStackTrace();
			throw e;
		}

		if (campaign == null) {
			throw new Exception("Campaign not found");
		}

		campaign.setStatus(status);
		// Trigger actions
		if (status.equals(CampaignStatus.NOT_STARTED)) {
			// Reset all data associated to campaign
			log
					.fine("-------------------------------------------NOT-STARTED------RESET-----------");
			CampaignInstanceDO cInstance = campaign.getRunningInstance();

			if (cInstance == null) {
				cInstance = new CampaignInstanceDO();
				campaign.setRunningInstance(cInstance);
			}
			cInstance.setLastRun(null);
			cInstance.setNextRun(null);
			cInstance.setTweetTemplates(null);
			cInstance.setNextTemplateNameIndex(0);
			cInstance.setTweetsSent(0);
			cInstance.setLastTimeRSSFetched(new Long(0));
			cInstance.setUsedFeedTitles(new ArrayList<String>());
			cInstance.setRssItems(new ArrayList<RSSItemDO>());
			cInstance.setTweetsSent(0);
			cInstance.setInfo("");

			buildTweetTemplates(campaign);

		}
		return DataUtils.campaignDtoFromDo(campaign);

	}

	/**
	 * 
	 * @param name
	 * @param email
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public CampaignDTO deleteCampaign(String name, String email,
			CampaignDTO object) throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);
		if (persona == null) {
			throw new Exception();
		}

		// Check if the campaign name alreadey exists
		CampaignDO campaignDom = businessHelper.getCampaignDao()
				.findByPersonaNameAndCampaignName(persona, object.getName());
		if (campaignDom == null) {
			throw new Exception("The campaign was not found");
		}
		clearTemplatesInCache(campaignDom);
		businessHelper.getCampaignDao().deleteCampaign(persona, campaignDom);

		return object;
	}

	/**
	 * 
	 * @param status
	 * @return
	 */
	public List<CampaignDO> getCampaigns(CampaignStatus status) {

		return businessHelper.getCampaignDao().findCandidateCampaignsToRun(
				status);
	}

	/**
	 * 
	 * @param campaign
	 */
	private void clearTemplatesInCache(CampaignDO campaign) {
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
			// Possible bug in production
			// cache.put(campaign.getKey(),null);
			cache.remove(campaign.getKey());
		}
	}

	public void buildTweetTemplates(CampaignDO campaign) throws Exception {

		log.info("Starting build templates: ");
		long start = new Date().getTime();
		List<String> tweetTemplates = new ArrayList<String>();
		for (String templateName : campaign.getTemplateNames()) {
			TemplateDO template = businessHelper.getTemplateDao().findTemplate(
					campaign.getPersona(), templateName);
			if (template != null) {
				String[] strList = template.getText().getValue().split("\\n");
				for (String tempText : strList) {
					if (tempText.trim().length() > 0) {
						tweetTemplates.add(tempText);
					}

				}
			}
		}
		campaign.getRunningInstance().setTweetTemplates(tweetTemplates);
		long end = new Date().getTime();
		log.info("<p>Templates Built in: " + (end - start) + " ms</p>");
	}

	public CampaignDTO getCampainStatus(PersonaDTO model, String name)
			throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(model.getName(),
						model.getUserEmail());
		if (persona == null) {
			throw new Exception();
		}

		// Check if the campaign name alreadey exists
		CampaignDO campaignDom = businessHelper.getCampaignDao()
				.findByPersonaNameAndCampaignName(persona, name);
		if (campaignDom == null) {
			throw new Exception("The campaign was not found");
		}
		return DataUtils.campaignDtoFromDo(campaignDom);
	}

	
	public String buildTweetFromTemplate(PersonaDTO persona,
			TemplateDTO template, List<String> userNames) throws Exception{
		PersonaDO personaDom = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(persona.getName(),
						persona.getUserEmail());
		if (persona == null) {
			throw new Exception();
		}
		
		//Find the template
		//Get a random line from the template
		String[] lines = template.getTemplateText().split("\\n");
		int index = 0;
		if ( lines.length>0) {
			double rand = Math.random();
			index = (int) Math.round(rand * (lines.length - 1));
		}
		
		String tweet =lines[index];
		String finalStatus = businessHelper.getTemplatePojo().replaceTemplateLists(personaDom,tweet);
		finalStatus = StringUtils.randomizeTemplate(finalStatus);
		
		if (userNames.size() > 0) {
			for (int i = 0; i < userNames.size(); i++) {
				finalStatus = finalStatus.replace("{username_"
						+ i + "}", userNames.get(i));
			}

		}

		//Now deal with the fucking feeds
		finalStatus = businessHelper.getFeedSetPojo().replaceFeeds(personaDom, finalStatus);
		
		finalStatus = finalStatus.trim().replace("\\n", "");
		
		return finalStatus;
	}

}
