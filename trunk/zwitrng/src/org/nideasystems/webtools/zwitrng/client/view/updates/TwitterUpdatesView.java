package org.nideasystems.webtools.zwitrng.client.view.updates;

import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class TwitterUpdatesView extends AbstractVerticalPanelView {
	private final TwitterUpdatesToolWidget toolBar = new TwitterUpdatesToolWidget();
	private final HorizontalPanel toolContainer = new HorizontalPanel();
	private Image waitingImage = new Image("/images/ajax-loader.gif");
	@Override
	public void init() {
		
		
		toolBar.setController(getController());
		toolBar.init();
		toolContainer.add(toolBar);
		waitingImage.setVisible(false);
		toolContainer.add(waitingImage);
		this.add(toolContainer);
		
		//I Have some tools That I want to control
		
		
		
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		waitingImage.setVisible(isUpdating);
		
	}

}
