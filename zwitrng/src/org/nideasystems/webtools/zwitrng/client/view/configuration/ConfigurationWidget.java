package org.nideasystems.webtools.zwitrng.client.view.configuration;



import com.google.gwt.user.client.ui.VerticalPanel;
import org.nideasystems.webtools.zwitrng.client.controller.configuration.ConfigurationController;

public abstract class ConfigurationWidget extends VerticalPanel implements IConfigurationWidget, Selectable{
	
	protected ConfigurationController controller = null;

	public void setController(ConfigurationController controller) {
		this.controller = controller;
	}

	public ConfigurationController getController() {
		return controller;
	}
	
	
}
