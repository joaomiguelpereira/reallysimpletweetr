package org.nideasystems.webtools.zwitrng.client.view.updates;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;


import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;

import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class TwitterUpdatesToolWidget extends AbstractVerticalPanelView<TwitterUpdatesController> {
	
	private final CheckBox autoUpdate = new CheckBox("Auto Update?");
	//private final Image refreshImg = new Image(Constants.REFRESH_IMAGE_LOCATION);
	private final ListBox updatesPerPage = new ListBox();
	
	@Override
	public void init() {
		HorizontalPanel container = new HorizontalPanel();
		container.setSpacing(5);
		InlineHTML refreshLink = new InlineHTML("Update?");
		refreshLink.addStyleName("link");
		
		//PushButton refresheBt = new PushButton(refreshImg);		
		//refresheBt.setStyleName("bottonIcon");
		refreshLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getController().reload();
				//getController().handleAction(IController.IActions.REFRESH_TWEETS);
			}
			
		});
		container.add(refreshLink);
		//Create auto update 
		
		autoUpdate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if ( autoUpdate.getValue() ) {
					
					getController().enableAutoUpdate();
				} else {
					getController().disableAutoUpdate();
				}
				
				
			}
			
		});
		Label itemPerPageLb = new Label("Updates per page ");
		container.add(autoUpdate);
		updatesPerPage.addItem("10");
		updatesPerPage.addItem("20");
		updatesPerPage.addItem("30");
		updatesPerPage.addItem("50");
		updatesPerPage.setSelectedIndex(1);
		updatesPerPage.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				Integer param = Integer.valueOf(updatesPerPage.getItemText(updatesPerPage.getSelectedIndex()));
				
				getController().changePageSize(param);
				
			}
			
		});
		container.add(itemPerPageLb);
		container.add(updatesPerPage);
		this.add(container);
		
		
	}
	@Override
	public void isUpdating(boolean isUpdating) {
		Window.alert("Is Updating");
		
	}

}
