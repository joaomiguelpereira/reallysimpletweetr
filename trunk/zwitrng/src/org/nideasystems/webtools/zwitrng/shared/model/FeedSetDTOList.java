package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.List;

public class FeedSetDTOList implements IDTO {
	
	private List<FeedSetDTO> feedSets = new ArrayList<FeedSetDTO>();;

	public void setFeedSets(List<FeedSetDTO> feedSets) {
		this.feedSets = feedSets;
	}

	public List<FeedSetDTO> getFeedSets() {
		return feedSets;
	}
	
	public void addFeedSet(FeedSetDTO feedSet) {
		this.feedSets.add(feedSet);
	}
	
	

}
