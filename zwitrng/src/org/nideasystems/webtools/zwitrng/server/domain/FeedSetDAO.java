package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.Query;

import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTO;



public class FeedSetDAO extends BaseDAO{

	private final static Logger log = Logger.getLogger(FeedSetDAO.class.getName());
	
	public FeedSetDO findByName(PersonaDO persona, String name) {
		
		log.fine("trying to find a FeedSetDO  by Name");
		Query queryTemplateFrag = pm.newQuery(FeedSetDO .class);

		queryTemplateFrag
				.setFilter("name==feedSetName && persona==personaObj");
		queryTemplateFrag
				.declareParameters("String feedSetName, PersonaDO personaObj");
		queryTemplateFrag.setUnique(true);

		FeedSetDO feedSet = (FeedSetDO) queryTemplateFrag
				.execute(name, persona);

		return feedSet;

		
		
		
		
		
		
	}

	public FeedSetDTO save(FeedSetDO feedSetDom,
			FeedSetDTO object) {
		
		feedSetDom.setModified(new Date());
		feedSetDom.setFeedUrls(object.getFeedUrls());
		
		
		
		return DataUtils.feedSetDtoFromDo(feedSetDom);
	}

	public FeedSetDTO create(PersonaDO persona, FeedSetDTO object) {
		FeedSetDO dom = new FeedSetDO();
		
		dom.setCreated(new Date());
		dom.setModified(new Date());
		
		dom.setName(object.getName());
		dom.setFeedUrls(object.getFeedUrls());
		
		dom.setPersona(persona);
		persona.addFeedSet(dom);
		
		return DataUtils.feedSetDtoFromDo(dom);
	
	}

	public void remove(PersonaDO persona, FeedSetDO feedSetDom) throws Exception{
		
		persona.getFeedSets().remove(feedSetDom);
	}

}
