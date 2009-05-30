package org.nideasystems.webtools.zwitrng.client.controller.persona;

import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;

import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaView;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonasListView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PersonasListController extends
		AbstractController<PersonaDTOList, PersonasListView> {
	
	private Map<String, PersonaView> personaViews = new HashMap<String, PersonaView>();
	
	PersonaController currentPersonaController = null;
	

	/**
	 * Constructor
	 */
	public PersonasListController() {

	}

	/**
	 * Initialize the view where all tabs are placed
	 */
	public void init() {
		super.init();
		//Create the View
		setView(new PersonasListView());
		//Set the controller for the view
		getView().setController(this);
		//Initialize the view
		getView().init();
		//Load the personas
		loadPersonas(getModel());
		// create the view
	
	}
	/**
	 * Load a PersonaDTOList into the controller
	 */
	private void loadPersonas(PersonaDTOList result) {

		// If the user is not logged in, then it can be null
		//For each persona create a new Tab
		if (result != null) {
			for (PersonaDTO persona : result.getPersonaList()) {
				loadPersona(persona,false);
			}
		}

	}


	/**
	 * called when a given persona is selecets
	 */
	public void onTabSelectedChanged(int selectedTab) {
		GWT.log("You selected TAB"+selectedTab,null);
		
		
		if (currentPersonaController != null) {
			currentPersonaController.pause();
		}

		if (selectedTab != 0) {
			
			final PersonaView personaViewSelected = (PersonaView) this.getView()
					.getWidget(selectedTab);
			
			currentPersonaController = personaViewSelected.getController();
			if ( currentPersonaController.hasTwitterUpdatesListControllerInitialized() ) {
				currentPersonaController.resume();
			} else {
				currentPersonaController.initializeUpdatesListController();
			}
			

		}
	}

	

	/**
	 * Load a persona into the controller and create a view for it
	 * @param persona
	 */
	private void loadPersona(PersonaDTO persona, boolean selecteAfterCreated) {
		
		PersonaController personaController = new PersonaController();
		personaController.setMainController(getMainController());
		personaController.setServiceManager(getServiceManager());
		personaController.setParentController(this);
		personaController.setModel(persona);
		personaController.init();
		getView().add(personaController.getView(),persona.getName());
		this.personaViews.put(persona.getName(), personaController.getView());
		
		if ( selecteAfterCreated ) {
			getView().selectTab(getView().getWidgetCount()-1);
		}
	}

	/**
	 * This will unloa the persona from the list and any reference to it
	 * @param personaName
	 */
	private void unloadPersona(String personaName) {
		
		if (this.personaViews.get(personaName) != null) {
			getView().remove(personaViews.get(personaName));
			getView().selectTab(0);
			this.personaViews.remove(personaName);

		}

	}
	
	/**
	 * Call the service to remove a persona. After success call unloadPersona
	 * @param persona
	 */
	public void deletePersona(PersonaDTO persona) {
		
		if ( Window.confirm("Are you sure you want to delete this Persona?") ) {
			//make the server call
			startProcessing();
			try {
				getServiceManager().getRPCService().deletePersona(persona.getName(), new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						endProcessing();
						getMainController().addException(caught);
						
						
					}

					@Override
					public void onSuccess(String result) {
						endProcessing();
						unloadPersona(result);
						
					}
					
				});
			} catch (Exception e) {
				getMainController().addException(e);
				endProcessing();
			}
			
		}
		
	}



	@Override
	public void endProcessing() {
		
		getMainController().isProcessing(false);

	}

	@Override
	public void startProcessing() {
		getMainController().isProcessing(true);
		
	}

	@Override
	public void handleAction(String action, Object... args) {

	}

	/**
	 * Create a new Persona. Call service to store persona, then add a new
	 * persona to the list of personas
	 * 
	 * @param persona
	 */
	public void createPersona(PersonaDTO persona) {

		try {
			startProcessing();
			getServiceManager().getRPCService().createPersona(persona,
					new AsyncCallback<PersonaDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							endProcessing();
							getMainController().addException(caught);

						}

						@Override
						public void onSuccess(PersonaDTO result) {
							endProcessing();
							loadPersona(result,true);

						}

					});
		} catch (Exception e) {
			endProcessing();
			getMainController().addException(e);
		}
	}

	@Override
	public void reload() {

	}

}
