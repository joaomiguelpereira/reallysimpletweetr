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

	private FilterCriteriaDTO currentFiler = null;
	Button searchBt = new Button("Search");
	TextBox search = new TextBox();
	HTML currentSearch = null;
	@Override
	public void init() {
		this.addStyleName("search-panel");
		HorizontalPanel searchPanel = new HorizontalPanel();
		search.setWidth("250px");
		searchPanel.add(search);
		searchPanel.add(searchBt);
		if ( currentFiler != null ) {
			search.setValue(currentFiler.getSearchText());
			currentSearch = new HTML(currentFiler.getSearchText());
		}
		this.add(searchPanel);
		this.add(currentSearch);
		searchBt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//Window.alert(search.getValue());
				//Window.alert(currentFiler.getSearchText());
				if (!search.getValue().isEmpty() && !search.getValue().equals(currentFiler.getSearchText())) {
					currentFiler.setSearchText(search.getValue());
					//getController().setCurrentFilter(currentFiler);
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
		this.currentFiler = currentFiler;
	}


	public FilterCriteriaDTO getCurrentFiler() {
		return currentFiler;
	}


	public void refresh() {
		search.setValue(currentFiler.getSearchText());
		currentSearch.setText(currentFiler.getSearchText());
		
	}

}
