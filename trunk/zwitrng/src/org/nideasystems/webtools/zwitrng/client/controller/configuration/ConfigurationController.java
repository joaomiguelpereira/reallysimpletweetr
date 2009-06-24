package org.nideasystems.webtools.zwitrng.client.controller.configuration;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.view.configuration.ConfigurationView;
import org.nideasystems.webtools.zwitrng.client.view.configuration.TemplatesConfigurationWidget;
import org.nideasystems.webtools.zwitrng.shared.model.IModel;

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
		// TODO Auto-generated method stub
		
	}
	


	public void getTemplates(
			TemplatesConfigurationWidget templatesConfigurationWidget) {
		
			PersonaController controller = (PersonaController)getParentController();
			controller.loadTemplates(templatesConfigurationWidget);
		
		
	}

}
