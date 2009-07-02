package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.Query;

import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;

public class CampaignDAO extends BaseDAO {

	private static final Logger log = Logger.getLogger(CampaignDAO.class
			.getName());

	public CampaignDO findByPersonaNameAndcampaignName(PersonaDO persona,
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

	public CampaignDTO create(PersonaDO persona, CampaignDTO object) {
		CampaignDO dom = new CampaignDO();
		dom.setCreated(new Date());
		dom.setEndDate(object.getEndDate());
		dom.setFilterByTemplateTags(object.getFilterByTemplateTags());
		dom.setFilterByTemplateText(object.getFilterByTemplateText());
		dom.setFilterOperator(object.getFilterOperator());
		dom.setMaxTweetsPerTemplate(object.getMaxTweetsPerTemplate());
		dom.setModified(new Date());

		dom.setStartHourOfTheDay(object.getStartHourOfTheDay());
		dom.setEndHourOfTheDay(object.getEndHourOfTheDay());

		dom.setName(object.getName());
		dom.setPersona(persona);
		dom.setStartDate(object.getStartDate());
		dom.setTimeBetweenTweets(object.getTimeBetweenTweets());
		dom.setTimeUnit(object.getTimeUnit());
		if (new Date().after(object.getStartDate())) {
			dom.setStatus(CampaignStatus.RUNNING);
		} else {
			dom.setStatus(CampaignStatus.NOT_STARTED);
		}

		persona.addCampaign(dom);
		return DataUtils.campaignDtoFromDo(dom);
	}

	public CampaignDTO save(PersonaDO persona, CampaignDTO object,
			CampaignDO dom) {

		dom.setEndDate(object.getEndDate());
		dom.setFilterByTemplateTags(object.getFilterByTemplateTags());
		dom.setFilterByTemplateText(object.getFilterByTemplateText());
		dom.setFilterOperator(object.getFilterOperator());
		dom.setMaxTweetsPerTemplate(object.getMaxTweetsPerTemplate());
		dom.setModified(new Date());
		// dom.setName(object.getName());
		// dom.setPersona(persona);
		dom.setStartDate(object.getStartDate());
		dom.setTimeBetweenTweets(object.getTimeBetweenTweets());
		dom.setTimeUnit(object.getTimeUnit());

		dom.setStartHourOfTheDay(object.getStartHourOfTheDay());
		dom.setEndHourOfTheDay(object.getEndHourOfTheDay());

		if (new Date().after(object.getStartDate())) {
			dom.setStatus(CampaignStatus.RUNNING);
		} else {
			if (dom.getStatus().equals(CampaignStatus.RUNNING)) {
				dom.setStatus(CampaignStatus.RESCHEDULED);
			} else if (!dom.getStatus().equals(CampaignStatus.RESCHEDULED)) {
				dom.setStatus(CampaignStatus.NOT_STARTED);
			}

			// persona.addCampaign(dom);
		}
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

}
