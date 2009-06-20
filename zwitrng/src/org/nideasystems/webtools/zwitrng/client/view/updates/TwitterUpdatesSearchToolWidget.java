package org.nideasystems.webtools.zwitrng.client.view.updates;

import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;




import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class TwitterUpdatesSearchToolWidget extends AbstractVerticalPanelView<TwitterUpdatesController>{

	private FilterCriteriaDTO currentFilter = null;
	Button searchBt = new Button("Search");
	TextBox search = new TextBox();
	HTML currentSearch = null;
	HTML completedIn = null;
	HTML refreshUrl = null;
	@Override
	public void init() {
		this.addStyleName("search-panel");
		HorizontalPanel searchPanel = new HorizontalPanel();
		search.setWidth("250px");
		searchPanel.add(search);
		searchPanel.add(searchBt);
		if ( currentFilter != null ) {
			search.setValue(currentFilter.getSearchText());
			currentSearch = new HTML(currentFilter.getSearchText());
		}
		this.add(searchPanel);
		
		this.add(currentSearch);
		completedIn = new HTML();
		refreshUrl = new HTML();
		completedIn.setText(currentFilter.getCompletedIn()+" s");
		refreshUrl.setText(currentFilter.getRefreshUrl()+" url");
		
		this.add(completedIn);
		this.add(refreshUrl);
		
		searchBt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//Window.alert(search.getValue());
				//Window.alert(currentFiler.getSearchText());
				if (!search.getValue().isEmpty() && !search.getValue().equals(currentFilter.getSearchText())) {
					currentFilter.setSearchText(search.getValue());
					currentFilter.reset();
					
					getController().setCurrentFilter(currentFilter);
					getController().clearView();
					getController().reload();
					//refresh();
					
				}
				
			}
			
		});
		
	}

	
	@Override
	public void isUpdating(boolean isUpdating) {
		// TODO Auto-generated method stub
		
	}


	public void setCurrentFiler(FilterCriteriaDTO currentFiler) {
		this.currentFilter = currentFiler;
	}


	public FilterCriteriaDTO getCurrentFiler() {
		return currentFilter;
	}


	public void refresh() {
		search.setValue(currentFilter.getSearchText());
		currentSearch.setText(currentFilter.getSearchText());
		
		completedIn.setText(currentFilter.getCompletedIn()+" s");
		refreshUrl.setText(currentFilter.getRefreshUrl()+" url");
		
		
	}

}
