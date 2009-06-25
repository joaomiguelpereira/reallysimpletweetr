package org.nideasystems.webtools.zwitrng.client.view.configuration;



import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.configuration.ConfigurationController;

public abstract class ConfigurationWidget extends VerticalPanel implements IConfigurationWidget, Selectable{
	
	protected ConfigurationController controller = null;

	private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	
	@Override
	public void init() {
		waitingImg.setVisible(false);
		this.add(waitingImg);
		
	}

	public void setController(ConfigurationController controller) {
		this.controller = controller;
	}

	public ConfigurationController getController() {
		return controller;
	}
	
	public void isProcessing(boolean b) {
		waitingImg.setVisible(b);
		
	}

	
	
}
