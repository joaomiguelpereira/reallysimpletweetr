package org.nideasystems.webtools.zwitrng.client.controller.configuration;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.view.configuration.ConfigurationEditListener;
import org.nideasystems.webtools.zwitrng.client.view.configuration.ConfigurationView;
import org.nideasystems.webtools.zwitrng.client.view.configuration.AbstractListConfigurationWidget;
import org.nideasystems.webtools.zwitrng.client.view.configuration.TemplateFragmentsConfigurationWidget;
import org.nideasystems.webtools.zwitrng.client.view.configuration.TemplatesConfigurationWidget;
import org.nideasystems.webtools.zwitrng.client.view.configuration.TemplatesConfigurationWidget.SelectableTemplate;
import org.nideasystems.webtools.zwitrng.shared.model.IModel;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;

public class ConfigurationController extends AbstractController<IModel, ConfigurationView>{

	@Override
	public void handleAction(String action, Object... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		//super.init();
		ConfigurationView view = new ConfigurationView();
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
			TemplateFragmentsConfigurationWidget templateFragmentsConfigurationWidget) {
		
		((PersonaController)getParentController()).getTemplateFragments(templateFragmentsConfigurationWidget);		
		
	}

}
