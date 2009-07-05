package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTODTOList;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;


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
				.findByPersonaNameAndcampaignName(persona, object.getName());
		if (campaignDom != null) {
			throw new Exception("A Campaign with the same name already exixts");
		}

		return businessHelper.getCampaignDao().create(persona, object);

	}

	/**
	 * 
	 * @param name
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public CampaignDTODTOList findCampaigns(String name, String email)
			throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		log.fine("findCampaigns");
		if (persona == null) {
			throw new Exception();
		}
		CampaignDTODTOList retList = new CampaignDTODTOList();

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
				.findByPersonaNameAndcampaignName(persona, object.getName());
		if (campaignDom == null) {
			throw new Exception("The campaign was not found");
		}

		clearTemplatesInCache(campaignDom);
		return businessHelper.getCampaignDao().save(persona, object,
				campaignDom);

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
				.findByPersonaNameAndcampaignName(persona, object.getName());
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
		
		return businessHelper.getCampaignDao().findCandidateCampaignsToRun(status);
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
			//Possible bug in production
			//cache.put(campaign.getKey(),null);
			cache.remove(campaign.getKey());
		}
	}
	

}
