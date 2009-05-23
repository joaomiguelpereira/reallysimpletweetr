package org.nideasystems.webtools.zwitrng.client.view.updates;

import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

public class TwitterUpdatesToolWidget extends AbstractVerticalPanelView {
	
	private final CheckBox autoUpdate = new CheckBox("Auto Update?");
	private final Image refreshImg = new Image("/images/refresh_bt_3.gif");
	@Override
	public void init() {
		HorizontalPanel container = new HorizontalPanel();
		container.setSpacing(5);
		
		PushButton refresheBt = new PushButton(refreshImg);		
		refresheBt.setStyleName("bottonIcon");
		refresheBt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getController().handleAction(IController.IActions.REFRESH_TWEETS);
			}
			
		});
		container.add(refresheBt);
		//Create auto update 
		
		autoUpdate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if ( autoUpdate.getValue() ) {
					getController().handleAction(IController.IActions.ENABLE_AUTO_UPDATE);
				} else {
					getController().handleAction(IController.IActions.DISABLE_AUTO_UPDATE);
				}
				
				
			}
			
		});
		container.add(autoUpdate);
		this.add(container);
		
		
	}
	@Override
	public void isUpdating(boolean isUpdating) {
		Window.alert("Is Updating");
		
	}

}
