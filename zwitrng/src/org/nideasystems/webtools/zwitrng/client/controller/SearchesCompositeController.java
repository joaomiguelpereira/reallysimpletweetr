package org.nideasystems.webtools.zwitrng.client.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.nideasystems.webtools.zwitrng.client.view.SearchTabbedView;
import org.nideasystems.webtools.zwitrng.client.view.PersonaUpdatesTabView;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SearchesCompositeController extends AbstractCompositeController {

	// This update controller belong to one persona
	private PersonaDTO personaObj = null;

	private boolean initialized = false;

	private SearchTabbedView userUpdatetabPanel = null;
	private Map<String, PersonaUpdatesTabView> updatesViews = null;

	public SearchesCompositeController(PersonaDTO personaObj,
			SearchTabbedView tabbedView) {
		this.personaObj = personaObj;
		this.userUpdatetabPanel = tabbedView;
		updatesViews = new HashMap<String, PersonaUpdatesTabView>();
	}

	public void addFilterTab(FilterCriteriaDTO filter) {

		if (!updatesViews.containsKey(filter.getName())) {
			PersonaUpdatesTabView view = new PersonaUpdatesTabView(filter);
			this.userUpdatetabPanel.add(view, filter.getName());
			this.updatesViews.put(filter.getName(), view);
		}

	}

	public void init() {
		/*if (!this.initialized) {
			String name = personaObj.getName();
			// Get from Service all Tabs;
			RPCService.getInstance().getPersonaFilters(name,
					new FiltersLoadedCallBack());
			
			// Create the default Tab
			SearchTabView defaultSearch = new SearchTabView(this);
			defaultSearch.setName("Default");
			
			//Create the default search
			SearchController searchController = new SearchController();
			
			searchController.setView(defaultSearch);
			searchController.setModel(personaObj);
			searchController.setParentController(this);
			

			this.userUpdatetabPanel.add(defaultSearch, defaultSearch.getName());
			this.userUpdatetabPanel.selectTab(0);

		}*/

	}

	public void search(FilterCriteriaDTO filter) {
		
	/*	RPCService.getInstance().search(filter, personaObj.getTwitterAccount(),new AsyncCallback<List<StatusDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				PersonasCompositeController.getInstance().addError(caught.getLocalizedMessage());
				
			}

			@Override
			public void onSuccess(List<StatusDTO> result) {
				Window.alert("OK return searchs");
				
			}
			
		});*/
	}

	private class FiltersLoadedCallBack implements
			AsyncCallback<List<FilterCriteriaDTO>> {

		@Override
		public void onFailure(Throwable caught) {
			//PersonasCompositeController.getInstance().addError(caught.getLocalizedMessage());

		}

		@Override
		public void onSuccess(List<FilterCriteriaDTO> result) {
			Window.alert("Passed");
			// get all Filters for persona
			personaObj.setFilters(result);

			if (result != null) {
				for (FilterCriteriaDTO filter : result) {
					addFilterTab(filter);
				}
				initialized = true;
			}

		}

	}

	@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		Window.alert("SelectionHandler<Integer> getSelectionHandler() Searches");
		return null;
	}

	@Override
	public void getDataLoadedHandler(Object result) {
		Window.alert("getDataLoadedHandler(Object result)");
		
	}

	@Override
	public void getToolActionHandler(String string) {
		Window.alert("SeachsCompositeController ActionEvent Handler"+string);
		
	}

	@Override
	public AsyncCallback<String> getDataRemovedCallBack() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
