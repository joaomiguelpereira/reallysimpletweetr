package org.nideasystems.webtools.zwitrng.client.controller;

import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.view.PersonaUpdatesTabbedView;
import org.nideasystems.webtools.zwitrng.client.view.PersonaUpdatesView;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteria;



public class UpdatesController {
	
	//This update controller belong to one persona
	private String personaName = null;
	private boolean initialized = false;
	
	private PersonaUpdatesTabbedView userUpdatetabPanel = null;
	private Map<String, PersonaUpdatesView> updatesViews = null;

	
	public UpdatesController(String personaName, PersonaUpdatesTabbedView tabbedView) {
		this.personaName = personaName;
		this.userUpdatetabPanel = tabbedView;
		updatesViews = new HashMap<String, PersonaUpdatesView>();
	}
	
	public void addFilterTab(FilterCriteria filter) {

		if ( !updatesViews.containsKey(filter.getName())) {
			PersonaUpdatesView view = new PersonaUpdatesView(filter);
			this.userUpdatetabPanel.add(view,filter.getName());
			this.updatesViews.put(filter.getName(),view);
		}
		
		
	}

	public void init() {
		if ( !this.initialized ) {
			FilterCriteria homeFilter = new FilterCriteria();
			homeFilter.setName("home");
			this.addFilterTab(homeFilter);
			this.initialized = true;
		}
		
	}
}
