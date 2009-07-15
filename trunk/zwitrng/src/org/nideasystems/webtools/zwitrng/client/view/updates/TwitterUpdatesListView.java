package org.nideasystems.webtools.zwitrng.client.view.updates;


import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesListController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractTabbedPanel;



public class TwitterUpdatesListView extends AbstractTabbedPanel<TwitterUpdatesListController> {
	@Override
	public void init() {
		super.setWidth(Constants.MAIN_TABBED_PANEL_WIDTH);
		super.setAnimationEnabled(true);
		
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		// TODO Auto-generated method stub
		
	}

	

}
