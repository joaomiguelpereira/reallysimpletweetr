package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.List;

public class CampaignDTODTOList implements IModel {

	private List<CampaignDTO> campaigns = new ArrayList<CampaignDTO>();
	
	public void addCampaign(CampaignDTO c1) {
		
		this.campaigns.add(c1);
		
	}

	public List<CampaignDTO> getCampaigns() {
		return campaigns;
	}
	
	

}
