package org.nideasystems.webtools.zwitrng.client.controller;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;


import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class GlobalToolsWidget extends AbstractVerticalPanelView {
	private final Image waitingImg = new Image(Constants.WAITING_IMAGE);
	HorizontalPanel toolPanel = new HorizontalPanel();
	
	@Override
	public void init() {
		//refreshImg.setVisible(false);
		toolPanel.add(waitingImg);
		super.add(toolPanel);
		
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		waitingImg.setVisible(isUpdating);
		
	}

}
