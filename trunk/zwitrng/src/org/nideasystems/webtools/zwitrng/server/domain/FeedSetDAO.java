package org.nideasystems.webtools.zwitrng.server.domain;

import java.util.Date;

import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTO;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class FeedSetDAO extends BaseDAO{

	public FeedSetDO find(PersonaDO persona, long id) {
		Key feedSetKey = KeyFactory.createKey(persona.getKey(), FeedSetDO.class.getSimpleName(), id);
		return  pm.getObjectById(FeedSetDO.class, feedSetKey);
		
		
		
	}

	public FeedSetDTO save(FeedSetDO feedSetDom,
			FeedSetDTO object) {
		
		feedSetDom.setModified(new Date());
		feedSetDom.setFeedUrls(object.getFeedUrls());
		feedSetDom.setFeedUrls(object.getFilter());
		
		
		return DataUtils.feedSetDtoFromDo(feedSetDom);
	}

	public FeedSetDTO create(PersonaDO persona, FeedSetDTO object) {
		FeedSetDO dom = new FeedSetDO();
		
		dom.setCreated(new Date());
		dom.setModified(new Date());
		
		dom.setName(object.getName());
		dom.setFeedUrls(object.getFeedUrls());
		dom.setFilter(object.getFilter());
		dom.setPersona(persona);
		
		return DataUtils.feedSetDtoFromDo(dom);
	
	}

	public void remove(PersonaDO persona, FeedSetDO feedSetDom) throws Exception{
		
		persona.getFeedSets().remove(feedSetDom);
	}

}
