package org.nideasystems.webtools.zwitrng.client.services;

import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTODTOList;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTOList;





import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("personaService")
public interface TwitterPersonaService extends RemoteService {
	PersonaDTO createPersona(PersonaDTO persona) throws Exception;

	PersonaDTOList getPersonas() throws Exception;

	String deletePersona(String persona) throws Exception;

	List<FilterCriteriaDTO> getPersonaFilters(String personaKey)
			throws Exception;

	TemplateDTOList getTemplates(String name) throws Exception;

	TemplateDTO createTemplate(PersonaDTO model, TemplateDTO template)
			throws Exception;

	TemplateDTO deleteTemplate(PersonaDTO model, TemplateDTO template)
			throws Exception;

	TemplateDTO saveTemplate(PersonaDTO model, TemplateDTO template)
			throws Exception;

	TemplateFragmentDTOList getTemplateFragments(PersonaDTO personaDto)
			throws Exception;

	TemplateFragmentDTO createTemplateFragment(PersonaDTO model,
			TemplateFragmentDTO object) throws Exception;

	TemplateFragmentDTO saveTemplateFragment(PersonaDTO model,
			TemplateFragmentDTO object) throws Exception;

	TemplateFragmentDTO deleteTemplateFragment(PersonaDTO model,
			TemplateFragmentDTO dataObject) throws Exception;

	Map<String, String> getTemplateFragmentsLists(PersonaDTO model,
			List<String> lists) throws Exception;
	
	CampaignDTODTOList getCampaigns(PersonaDTO model) throws Exception;

	CampaignDTO createCampaign(PersonaDTO model, CampaignDTO object) throws Exception;
	
	CampaignDTO saveCampaign(PersonaDTO model, CampaignDTO object) throws Exception;
	CampaignDTO deleteCampaign(PersonaDTO model, CampaignDTO dataObject) throws Exception;
	FeedSetDTOList getFeedSets(PersonaDTO model) throws Exception;
	FeedSetDTO createFeedSet(PersonaDTO model, FeedSetDTO object) throws Exception;
	FeedSetDTO saveFeedSet(PersonaDTO model, FeedSetDTO object) throws Exception;
	FeedSetDTO deleteFeedSet(PersonaDTO model, FeedSetDTO dataObject) throws Exception;
	

	


	
}

