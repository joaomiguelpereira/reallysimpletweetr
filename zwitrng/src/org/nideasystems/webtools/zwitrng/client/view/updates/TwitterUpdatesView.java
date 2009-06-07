package org.nideasystems.webtools.zwitrng.client.view.updates;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;


import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TwitterUpdatesView extends AbstractVerticalPanelView<TwitterUpdatesController> {
	private final TwitterUpdatesToolWidget toolBar = new TwitterUpdatesToolWidget();
	private final HorizontalPanel toolContainer = new HorizontalPanel();
	private Image waitingImage = new Image(Constants.WAITING_IMAGE);
	private TwitterUpdatesSearchToolWidget searchWidget = null;
	
	private FilterCriteriaDTO currentFilter = null;
	@Override
	public void init() {

		VerticalPanel topPanel = new TwitterUpdatesSearchToolWidget();
		
		if ( currentFilter.getUpdatesType() == UpdatesType.SEARCHES ) {
			searchWidget = new TwitterUpdatesSearchToolWidget();
			searchWidget.setCurrentFiler(currentFilter);
			
			searchWidget.setController(getController());
			searchWidget.init();
			topPanel.add(searchWidget);
			
			
		}

		
		toolBar.setController(getController());
		toolBar.init();
		toolContainer.add(toolBar);
		waitingImage.setVisible(false);
		toolContainer.add(waitingImage);
		topPanel.add(toolContainer);
		
		
		
		this.add(topPanel);
		
				
		
		
	}
	public void refresh() {
		if (currentFilter.getUpdatesType() == UpdatesType.SEARCHES) {
			searchWidget.refresh();
		}
		
	}


	public FilterCriteriaDTO getCurrentFilter() {
		return currentFilter;
	}

	public void setCurrentFilter(FilterCriteriaDTO currentFilter) {
		this.currentFilter = currentFilter;
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		waitingImage.setVisible(isUpdating);
		
	}

	
	
}
