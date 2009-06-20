package org.nideasystems.webtools.zwitrng.client.view.updates;

import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;

public class DirectMessagesSearchToolWidget extends AbstractVerticalPanelView<TwitterUpdatesController>{
	
	private FilterCriteriaDTO currentFilter = null;

	@Override
	public void init() {
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(new InlineHTML("See Messages: "));
		final ListBox messagesType = new ListBox();
		messagesType.addItem("Received", "RECEIVED");
		messagesType.addItem("Sent", "SENT");
		panel.add(messagesType);
		this.add(panel);
		
		//Add the change handler
		messagesType.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				
				String seleString = messagesType.getValue(messagesType.getSelectedIndex());
				
				//Type selected
					if ( seleString.equals("RECEIVED")) {
						currentFilter.setUpdatesType(UpdatesType.DIRECT_RECEIVED);
						
					} else {
						currentFilter.setUpdatesType(UpdatesType.DIRECT_SENT);
						
					}
					
					currentFilter.reset();
					
					getController().setCurrentFilter(currentFilter);
					
					getController().clearView();
					getController().reload();
					//messagesType.setFocus(false);
					
					//refresh();
					
				
	
			}
			
		});
		
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		// TODO Auto-generated method stub
		
	}

	public void setCurrentFilter(FilterCriteriaDTO currentFilter) {
		this.currentFilter = currentFilter;
	}

	public FilterCriteriaDTO getCurrentFilter() {
		return currentFilter;
	}

}
 