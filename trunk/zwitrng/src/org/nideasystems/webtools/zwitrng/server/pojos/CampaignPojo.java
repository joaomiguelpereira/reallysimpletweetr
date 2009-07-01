package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTODTOList;

public class CampaignPojo extends AbstractPojo{

	private static final Logger log = Logger.getLogger(CampaignPojo.class.getName());
	public CampaignDTO createCampaign(String name, String email,
			CampaignDTO object) throws Exception{
		//find the persona
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(name, email);
		if ( persona==null) {
			throw new Exception();
		}
		
		//Check if the campaign name alreadey exists
		CampaignDO campaignDom = businessHelper.getCampaignDao().findByPersonaNameAndcampaignName(persona,object.getName());
		if (campaignDom!=null) {
			throw new Exception("A Campaign with the same name already exixts");
		}
		
		return businessHelper.getCampaignDao().create(persona,object);
		
	}

	public CampaignDTODTOList findCampaigns(String name, String email) throws Exception{
		PersonaDO persona = businessHelper.getPersonaDao().findPersonaByNameAndEmail(name, email);
		
		log.fine("findCampaigns");
		if ( persona==null) {
			throw new Exception();
		}
		CampaignDTODTOList retList = new CampaignDTODTOList();
		
		if ( persona.getCampaigns()!= null) {
			for (CampaignDO dom: persona.getCampaigns()) {
				retList.addCampaign(DataUtils.campaignDtoFromDo(dom));
				log.fine("adding campaign");
			}
		}
		
		
		return retList;
		
		
		
	}
}
