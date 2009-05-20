package org.nideasystems.webtools.zwitrng.client.controller;


import org.nideasystems.webtools.zwitrng.client.view.PersonaView;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;


public class PersonaTabSelectCallBack implements SelectionHandler<Integer>{

	//When a tab is selected what should I do?
	//Initialize the tabs of updates, if not already initialized
	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		
		Widget view = MainController.getInstance().getTabPanel(event.getSelectedItem());
		
		if (view != null && (view instanceof PersonaView)) {
			PersonaView personaView = (PersonaView)view;
			
			//Now create the
			MainController.getInstance().getUpdatesController(personaView.getPersonaObj().getName()).init();
			
		}
		
		
		
	}
	

}
