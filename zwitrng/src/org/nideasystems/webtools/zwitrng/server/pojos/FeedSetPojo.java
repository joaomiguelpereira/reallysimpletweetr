package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.FeedSetDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;

import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTOList;

public class FeedSetPojo extends AbstractPojo {

	private final static Logger log = Logger.getLogger(FeedSetPojo.class
			.getName());

	public FeedSetDTOList findFeedSets(String name, String email)
			throws Exception {

		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		log.fine("Finding FeedSets");
		if (persona == null) {
			throw new Exception("Persona not found");
		}
		FeedSetDTOList retList = new FeedSetDTOList();

		if (persona.getFeedSets() != null) {
			for (FeedSetDO dom : persona.getFeedSets()) {
				retList.addFeedSet(DataUtils.feedSetDtoFromDo(dom));
				log.fine("adding Feed Set:");
			}
		}

		return retList;

	}

	public FeedSetDTO createFeedSet(String name, String email, FeedSetDTO object)
			throws Exception {

		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);
		if (persona == null) {
			throw new Exception();
		}

		// Check if the campaign name alreadey exists
		FeedSetDO feedSetDom = businessHelper.getFeedSetDao().findByName(persona,
				object.getName());

		if (feedSetDom != null) {
			throw new Exception("A FeedSet with the same name already exixts");
		}

		return businessHelper.getFeedSetDao().create(persona, object);

	}

	public FeedSetDTO saveFeedSet(String name, String email, FeedSetDTO object)
			throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);
		if (persona == null) {
			throw new Exception();
		}

		// Check if the campaign name alreadey exists
		FeedSetDO feedSetDom = businessHelper.getFeedSetDao().findByName(persona,
				object.getName());

		if (feedSetDom == null) {
			throw new Exception("The feed does not exists.");
		}

		return businessHelper.getFeedSetDao().save(feedSetDom, object);
	}

	public void deleteFeedSet(String name, String email, FeedSetDTO object) throws Exception{
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);
		if (persona == null) {
			throw new Exception();
		}

		// Check if the campaign name alreadey exists
		FeedSetDO feedSetDom = businessHelper.getFeedSetDao().findByName(persona,
				object.getName());

		if (feedSetDom == null) {
			throw new Exception("The feed does not exists.");
		}

		businessHelper.getFeedSetDao().remove(persona,feedSetDom);

	}

}
