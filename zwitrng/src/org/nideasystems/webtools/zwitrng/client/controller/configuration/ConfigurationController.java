package org.nideasystems.webtools.zwitrng.client.controller.configuration;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.view.configuration.ConfigurationEditListener;
import org.nideasystems.webtools.zwitrng.client.view.configuration.CampaignsConfigurationView;
import org.nideasystems.webtools.zwitrng.client.view.configuration.TemplateListsConfigurationWidget;
import org.nideasystems.webtools.zwitrng.shared.model.IDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;

public class ConfigurationController extends AbstractController<IDTO, CampaignsConfigurationView>{

	@Override
	public void handleAction(String action, Object... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		//super.init();
		CampaignsConfigurationView view = new CampaignsConfigurationView();
		view.setController(this);
		view.init();
		setView(view);
	}

	@Override
	public void reload() {
		
		
	}
	

/*
	public void getTemplates(
			AbstractListConfigurationWidget<TemplateDTO, TemplateDTOList> templatesConfigurationWidget) {
			PersonaController controller = (PersonaController)getParentController();
			controller.loadTemplates(templatesConfigurationWidget);
		
		
	}
*/

	public void removeTemplate(TemplateDTO template,
			ConfigurationEditListener<TemplateDTO> callBack) {
		
		((PersonaController)getParentController()).removeTemplate(template,callBack);		
	}

	public void getTemplateFragments(
			TemplateListsConfigurationWidget templateFragmentsConfigurationWidget) {
		
		((PersonaController)getParentController()).getTemplateFragments(templateFragmentsConfigurationWidget);		
		
	}

}
