package org.nideasystems.webtools.zwitrng.client.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.services.RPCService;
import org.nideasystems.webtools.zwitrng.client.view.PersonaTabView;
import org.nideasystems.webtools.zwitrng.client.view.PersonaUpdatesTabbedView;
import org.nideasystems.webtools.zwitrng.client.view.PersonaView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaObj;
import org.nideasystems.webtools.zwitrng.shared.model.TwittUpdate;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainController {

	
	/**
	 * The MainController has a PersonaTabView. The personatabview has tabs with the personas 
	 */
	private PersonaTabView tabPanel = null;
	private static MainController instance = null;
	
	private Map<String, UpdatesController> updatesControllers = new HashMap<String, UpdatesController>();
	private Map<String, Widget> personaTabs = new HashMap<String, Widget>();
	
	
	/**
	 * Static method to get the singletons instance
	 * @return
	 */
	public static MainController getInstance() {
		if ( instance == null ) {
			instance = new MainController();
		}
		return instance;
		
	}
	/**
	 * Private Constructor. Singleton.
	 */
	private MainController() {
		
	}
	
	/**
	 * Initialize the view where all tabs are places
	 */
	public void init() {
		this.tabPanel = new PersonaTabView();
		RootPanel.get("main").add(this.tabPanel);
	}

	public void loadPersonas() {
		//Call the service to update the list of personas
		//add an hendler for update 
		RPCService.getInstance().loadPersonas(new LoadPersonasCallBack());
		// TODO Auto-generated method stub
	}
	
	public class LoadPersonasCallBack implements DataLoadedCallBack{

		@Override
		public void onError(String error) {
			Window.alert(error);
			
		}

		@Override
		public void onSuccess() {
			//Get the actual personas from the PersonaService
			Map<String, PersonaObj> personas = RPCService.getInstance().getPersonas();
			
			Iterator<String> it = personas.keySet().iterator();

			while (it.hasNext()) {
				String name = it.next();
				PersonaObj persona = personas.get(name);
				PersonaView wid = new PersonaView(persona);
				PersonaUpdatesTabbedView updatestab = new PersonaUpdatesTabbedView(); 
				wid.add(updatestab);
				
				personaTabs.put(persona.getName(), wid);
				//Now render the tab
				tabPanel.add(wid,persona.getName());
				//Create a Updatecontroller for this persona
				
				
				UpdatesController personaUpdatesController = new UpdatesController(persona.getName(),updatestab);
				updatesControllers.put(persona.getName(), personaUpdatesController);
			}
			
			
		}
		
	}

	/**
	 * Return the 
	 * @param selectedItem
	 */
	public Widget getTabPanel(Integer selectedItem) {
		return this.tabPanel.getWidget(selectedItem);
		
	}
	public UpdatesController getUpdatesController(String name) {
		return this.updatesControllers.get(name);
		
	}
	public void addPersonaTab(PersonaObj persona) {
		Widget wid = new PersonaView(persona);
		this.personaTabs.put(persona.getName(), wid);
		this.tabPanel.add(wid, persona.getName());
		
	}
	public void removePersonaTab(String name) {
		
		Widget wid = this.personaTabs.get(name);
		if ( wid != null ) {
			this.tabPanel.remove(wid);
			this.tabPanel.selectTab(0);
			this.updatesControllers.remove(name);
		}
		
		
	}
	
	
	

}
