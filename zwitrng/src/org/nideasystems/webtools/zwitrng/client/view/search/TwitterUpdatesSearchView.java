package org.nideasystems.webtools.zwitrng.client.view.search;


import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesListController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TwitterUpdatesSearchView extends AbstractVerticalPanelView{
	
	private final VerticalPanel topPanel = new VerticalPanel();
	TwitterUpdatesListController controller = null;
	
	private final Button seachCriteriaBt = new Button("Search");
	private final TextBox seachCriteriaTb = new TextBox();
	
	public TwitterUpdatesSearchView(){
		super();
		
	}
	
	
	//Handle the click on search
	private class SearchClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			FilterCriteriaDTO filter = new FilterCriteriaDTO();
			filter.setSearchText("general");
			getController().handleAction("SEARCH",filter);
			
			
		}
		
	}


	@Override
	public void init() {
		topPanel.setSpacing(5);
		topPanel.add(seachCriteriaTb);
		topPanel.add(seachCriteriaBt);
		
		seachCriteriaBt.addClickHandler(new SearchClickHandler());
		super.add(topPanel);

		
	}


	@Override
	public void isUpdating(boolean isUpdating) {
		Window.alert("Is Updating");
		
	}


}
