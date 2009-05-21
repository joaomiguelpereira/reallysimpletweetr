package org.nideasystems.webtools.zwitrng.client.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.services.RPCService;

import org.nideasystems.webtools.zwitrng.client.view.PersonasCompositeView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class PersonasCompositeController extends AbstractCompositeController
		implements IDataLoadedHandler {

	// The controller view
	private PersonasCompositeView personasCompositeView = null;
	private Map<String, PersonaDTO> personas = null;
	private Map<String, Widget> personaTabs = new HashMap<String, Widget>();

	
	public PersonasCompositeController() {

	}

	/**
	 * Initialize the view where all tabs are places
	 */
	public void init() {

		// Initialize view
		// Create the view for this controller
		this.personasCompositeView = new PersonasCompositeView();
		// Tell the view who is its controller
		this.personasCompositeView.setController(this);
		// Initialize the view
		this.personasCompositeView.init();
		// Tell this controller what the view is
		this.view = this.personasCompositeView;

		// Initialize data structures
		this.personas = new HashMap<String, PersonaDTO>();

		
		this.loadData();
	}

	/**
	 * Load the Pseronas configured for the logged user
	 */
	private void loadData() {
		RPCService service = null;
		try {
			service = getServiceManager().getRPCService();
		} catch (Exception e) {
			// Get the default error handler for this controller and let it
			// handle the error
			getErrorHandler().addException(e);
		}
		if (service != null) {
			// Call the service and give it a callback to handle service async
			// call
			service.loadPersonas(new LoadPersonasCallBack());
		}

	}

	public class LoadPersonasCallBack implements
			AsyncCallback<List<PersonaDTO>> {

		
		@Override
		public void onFailure(Throwable caught) {
			// Ok, got an error, let's default error handle handle this
			getErrorHandler().addException(caught);

		}

		@Override
		public void onSuccess(List<PersonaDTO> result) {
			// Cool, let's add the persons :)
			// TODO: Create a IDataLoaded handler that is the controller
			getDataLoadedCallBack().onDataLoaded(result);
		}

	}

	private IDataLoadedHandler getDataLoadedCallBack() {
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDataLoaded(Object obj) {
		this.createPersonas((List<PersonaDTO>) obj);
	}

	private void createPersonas(List<PersonaDTO> result) {

		// If the user is not logged in, then it can be null
		if (result != null) {
			for (PersonaDTO persona : result) {
				createPersona(persona);


			}
		}

	}

	

	private class PersonaTabSelectionHandler implements
			SelectionHandler<Integer> {

		// When a tab is selected what should I do?
		// Initialize the tabs of updates, if not already initialized
		@Override
		public void onSelection(SelectionEvent<Integer> event) {
			Window.alert("Tab Selected");

		}

	}

	@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		return this.new PersonaTabSelectionHandler();

	}
	private void createPersona(PersonaDTO persona) {
		// Add the personas to the list
		// First, let's update the List of loaded personas
		this.personas.put(persona.getName(), persona);

		// Now, create a Controller for the persona
		PersonaController personaController = new PersonaController();
		// The persona controller default error handler will be this
		// controller
		personaController.setErrorHandler(getErrorHandler());
		// The Persona Object will be the loaded persona
		personaController.setPersona(persona);
		// Lets name the controller
		personaController.setName(persona.getName());
		// The Persona controller will be a child of this controller
		personaController.setParentController(this);
		// The default Service Manager (RPC) will the the parent's
		personaController.setServiceManager(getServiceManager());
		
		// Let's initialize the Persona Controller:
		// Responsabilites: Create the view
		personaController.init();
		// Add the Persona View to this view
		this.personasCompositeView.add(personaController.getView()
				.getAsWidget(),persona.getName());
		// Let's maintain the list of created widgets, by persona name.
		// This will serve to synchronize between The rendered tabs and
		// the Loaded personas
		this.personaTabs.put(persona.getName(), personaController
				.getView().getAsWidget());

	}

	private void removePersona(String personaName) {
		if (this.personas.get(personaName)!= null) {
			this.personas.remove(personaName);
		}
		if (this.personaTabs.get(personaName) != null) {
			this.personasCompositeView.remove(personaTabs.get(personaName));
			this.personasCompositeView.selectTab(0);
			this.personaTabs.remove(personaName);
			
			
		}
		
	}
	
	@Override
	public void getDataLoadedHandler(Object result) {
		//Hopefully, this is a PersonaDTO
		createPersona((PersonaDTO)result);
		
		
		
	}

	@Override
	public void getToolActionHandler(String string) {
		Window.alert("PersonaaCompositeController ActionEvent Handler"+string);
		
	}

	@Override
	public AsyncCallback<String> getDataRemovedCallBack() {
		return this.new PersonaRemovedCallBack();
	}
	
	private class PersonaRemovedCallBack implements AsyncCallback<String> {

		@Override
		public void onFailure(Throwable caught) {
			getErrorHandler().addException(caught);
			
		}

		@Override
		public void onSuccess(String result) {
			
			removePersona(result);
			
		}
		
	}

}