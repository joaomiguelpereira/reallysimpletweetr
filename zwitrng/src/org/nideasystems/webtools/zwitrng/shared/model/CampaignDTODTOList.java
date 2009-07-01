package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.List;

public class CampaignDTODTOList implements IModel {

	private List<CampaignDTO> campaigns;
	
	public void addCampaign(CampaignDTO c1) {
		if (this.campaigns == null) {
			this.campaigns = new ArrayList<CampaignDTO>();
		}
		this.campaigns.add(c1);
		
	}

	public List<CampaignDTO> getCampaigns() {
		return campaigns;
	}
	
	

}
