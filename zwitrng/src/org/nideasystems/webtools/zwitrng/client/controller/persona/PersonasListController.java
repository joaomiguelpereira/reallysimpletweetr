package org.nideasystems.webtools.zwitrng.client.controller.persona;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.internal.ole.win32.ISpecifyPropertyPages;
import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.GlobalToolsWidget;
import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.controller.IDataLoadedHandler;

import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaView;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonasListView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class PersonasListController extends
		AbstractController<PersonaDTOList, PersonasListView> {

	// The controller view
	//private PersonasListView personasListView = null;
	GlobalToolsWidget toolsWidget = null;
	
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
		Window.alert("initializing");
		//Create the View
		setView(createView(PersonasListView.class));
		//Set the controller for the view
		getView().setController(this);
		//Initialize the view
		getView().init();
		//Load the personas
		loadPersonas(getModel());
		// create the view

		/*
		 * this.view = createView(PersonasListView.class);
		 * 
		 * // Initialize view // Create the view for this controller
		 * this.personasListView = new PersonasListView(); // Tell the view who
		 * is its controller this.personasListView.setController(this); //
		 * Initialize the view this.personasListView.init(); // Tell this
		 * controller what the view is this.view = this.personasListView; //
		 * Initialize data structures this.personas = new HashMap<String,
		 * PersonaDTO>();
		 * 
		 * this.loadData();
		 */
	}

	/**
	 * Load the Pseronas configured for the logged user
	 */
	private void loadData() {
		/*
		 * RPCService service = null; try { service =
		 * getServiceManager().getRPCService(); } catch (Exception e) { // Get
		 * the default error handler for this controller and let it // handle
		 * the error getMainController().addException(e); } if (service != null)
		 * { // Call the service and give it a callback to handle service async
		 * // call try { startProcessing(); service.loadPersonas(new
		 * LoadPersonasCallBack()); } catch (Exception e) {
		 * getMainController().addException(e); e.printStackTrace();
		 * endProcessing(); } }
		 */

	}

	/*
	 * public class LoadPersonasCallBack implements
	 * AsyncCallback<List<PersonaDTO>> {
	 * 
	 * @Override public void onFailure(Throwable caught) { // Ok, got an error,
	 * let's default error handle handle this
	 * getMainController().addException(caught); endProcessing();
	 * 
	 * }
	 * 
	 * @Override public void onSuccess(List<PersonaDTO> result) {
	 * endProcessing(); // Cool, let's add the persons :) // TODO: Create a
	 * IDataLoaded handler that is the controller createPersonas(result); //
	 * getDataLoadedCallBack().onDataLoaded(result); }
	 * 
	 * }
	 */
	
	
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
	 * When a Tab is selected
	 * 
	 * @author jpereira
	 * 
	 */
	/*
	 * private class PersonaTabSelectionHandler implements
	 * SelectionHandler<Integer> {
	 * 
	 * // When a tab is selected what should I do?
	 * 
	 * @Override public void onSelection(SelectionEvent<Integer> event) { //
	 * Check if the user is authenticated with OAuth
	 * 
	 * onTabSelectedChanged(event.getSelectedItem());
	 * 
	 * }
	 * 
	 * }
	 */

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
			
			
			// PersonaDTO
			//personaViewSelected.getController().reload();
			//personaViewSelected.refresh();

		}
	}

	

	/**
	 * Load a persona into the controller and create a view for it
	 * @param persona
	 */
	private void loadPersona(PersonaDTO persona, boolean selecteAfterCreated) {
		
		PersonaController personaController = AbstractController
				.createController(PersonaController.class);
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

	/*@Override
	public void handleDataLoaded(Object result) {
		// Hopefully, this is a PersonaDTO
		loadPersona((PersonaDTO) result);

	}
*/
	/*
	 * @Override public AsyncCallback<String> getDataRemovedCallBack() { return
	 * this.new PersonaRemovedCallBack(); }
	 * 
	 * private class PersonaRemovedCallBack implements AsyncCallback<String> {
	 * 
	 * @Override public void onFailure(Throwable caught) {
	 * getErrorHandler().addException(caught);
	 * 
	 * }
	 * 
	 * @Override public void onSuccess(String result) {
	 * 
	 * removePersona(result);
	 * 
	 * }
	 * 
	 * }
	 */

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

		/*if (action.equals(IController.IActions.PERSONA_SELECTED)) {
			Integer selectedItem = (Integer) args[0];
			onTabSelectedChanged(selectedItem);
		}

		if (action.equals(IController.IActions.DELETE)) {
			String personaName = (String) args[0];

			try {
				startProcessing();
				getServiceManager().getRPCService().deletePersona(personaName,
						new AsyncCallback<String>() {

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
				endProcessing();
				getMainController().addException(e);
				e.printStackTrace();
			}

		}

		if (action.equals(IController.IActions.CREATE)) {
			PersonaDTO persona = (PersonaDTO) args[0];
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
								loadPersona(result);

							}

						});
			} catch (Exception e) {
				endProcessing();
				getMainController().addException(e);
			}
		}
		// Window.alert("PersonaaCompositeController ActionEvent Handler"+action);
*/
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
		// TODO Auto-generated method stub

	}

	/*@Override
	public void onDataLoaded(Object obj) {
		// TODO Auto-generated method stub

	}*/

}
