package org.nideasystems.webtools.zwitrng.server.domain.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.ObjectState;
import javax.jdo.Query;

import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.CampaignInstanceDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.RSSItemDO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class CampaignDAO extends BaseDAO {

	private static final Logger log = Logger.getLogger(CampaignDAO.class
			.getName());

	public CampaignDO findByPersonaNameAndCampaignName(PersonaDO persona,
			String name) {

		log.fine("trying to find a Campaign by Name");
		Query queryTemplateFrag = pm.newQuery(CampaignDO.class);

		queryTemplateFrag
				.setFilter("name==tempCampName && persona==personaObj");
		queryTemplateFrag
				.declareParameters("String tempCampName, PersonaDO personaObj");
		queryTemplateFrag.setUnique(true);

		CampaignDO templateFragment = (CampaignDO) queryTemplateFrag.execute(
				name, persona);

		return templateFragment;

	}

	public CampaignDO create(PersonaDO persona, CampaignDTO object) {
		
		
		CampaignDO dom = new CampaignDO();
		dom.setCreated(new Date());
		dom.setModified(new Date());
		dom.setName(object.getName());
		dom.setTemplateNames(object.getTemplatesNames());
		dom.setUseTemplatesRandomly(object.isUseTemplatesRandomly());
		dom.setAllowRepeatTemplates(object.isAllowRepeatTemplates());
		dom.setTrackClicksOnLinks(object.isTrackClicksOnLinks());
		dom.setStartDate(object.getStartDate());
		dom.setEndDate(object.getEndDate());
		dom.setTimeBetweenTweets(object.getTimeBetweenTweets());
		dom.setTimeUnit(object.getTimeUnit());
		dom.setStartHourOfTheDay(object.getStartHourOfTheDay());
		dom.setEndHourOfTheDay(object.getEndHourOfTheDay());
		dom.setLimitNumberOfTweetsSent(object.isLimitNumberOfTweetsSent());
		dom.setMaxTweets(object.getMaxTweets());		
		dom.setPersona(persona);
		dom.setStatus(object.getStatus());
		
		CampaignInstanceDO cInstance = new CampaignInstanceDO();
		dom.setRunningInstance(cInstance);
		cInstance.setNextTemplateNameIndex(0);
		cInstance.setTweetsSent(0);
		cInstance.setRssItems(new ArrayList<RSSItemDO>());
		cInstance.setUsedFeedTitles(new ArrayList<String>());
		cInstance.setLastTimeRSSFetched(new Long(0));
		
		
		
		
		persona.addCampaign(dom);
		
		return dom;
	}

	public CampaignDTO save(PersonaDO persona, CampaignDTO object,
			CampaignDO dom) {

		
		dom.setModified(new Date());
		dom.setTemplateNames(object.getTemplatesNames());
		dom.setUseTemplatesRandomly(object.isUseTemplatesRandomly());
		dom.setAllowRepeatTemplates(object.isAllowRepeatTemplates());
		dom.setTrackClicksOnLinks(object.isTrackClicksOnLinks());
		dom.setStartDate(object.getStartDate());
		dom.setEndDate(object.getEndDate());
		dom.setTimeBetweenTweets(object.getTimeBetweenTweets());
		dom.setTimeUnit(object.getTimeUnit());
		dom.setStartHourOfTheDay(object.getStartHourOfTheDay());
		dom.setEndHourOfTheDay(object.getEndHourOfTheDay());
		dom.setLimitNumberOfTweetsSent(object.isLimitNumberOfTweetsSent());
		dom.setMaxTweets(object.getMaxTweets());		
		//dom.setPersona(persona);
		//persona.addCampaign(dom);
		
		//dom.setStatus(object.getStatus());
		
		return DataUtils.campaignDtoFromDo(dom);

	}

	public void deleteCampaign(PersonaDO persona, CampaignDO campaignDom) {

		persona.getCampaigns().remove(campaignDom);

	}

	public List<CampaignDO> findCandidateCampaignsToRun(CampaignStatus status) {

		Query queryCampaigns = pm.newQuery(CampaignDO.class);

		queryCampaigns
				.setFilter("status==theStatus");
		queryCampaigns
				.declareParameters("org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus theStatus");

		return (List<CampaignDO>) queryCampaigns.execute(status);

	}

	public List<CampaignDO> findCampaignsByPersonaAndStatus(PersonaDO persona,
			CampaignStatus status) {
		Query queryTemplateFrag = pm.newQuery(CampaignDO.class);

		queryTemplateFrag
				.setFilter("status==theStatus&& persona==personaObj");
		queryTemplateFrag
				.declareParameters("org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus theStatus, PersonaDO personaObj");
		

		List<CampaignDO> list = (List<CampaignDO>) queryTemplateFrag.execute(
				status, persona);	

		return list ;

	}

	public List<RSSItemDO> findRssItemsToUse(CampaignDO campaign, String url) {
			log.fine("trying to find a RssItemByFeedURL: "+url);
	
			//TODO: Refactor model or this
			List<RSSItemDO> result = new ArrayList<RSSItemDO>();
			if ( campaign.getRunningInstance().getRssItems()!=null) {
				for (RSSItemDO itemDo:campaign.getRunningInstance().getRssItems() ) {
					if ( itemDo.getFeedUrl().equals(url)) {
						result.add(itemDo);
					}
					
					
				}

			}
			/*
			Query rssItemQuery = pm.newQuery(RSSItemDO .class);
			//campaign.getRunningInstance().get

			rssItemQuery
					.setFilter("feedUrl==theUrl && campaignInstance==theCampaignInstance");
			rssItemQuery
					.declareParameters("String theUrl, CampaignInstanceDO theCampaignInstance");
			
			
			List<RSSItemDO> rssItemList = (List<RSSItemDO>) rssItemQuery
					.execute(url, campaign.getRunningInstance());
*/
			return result;

	}

	

}
