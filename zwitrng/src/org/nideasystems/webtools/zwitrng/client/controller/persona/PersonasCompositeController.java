package org.nideasystems.webtools.zwitrng.client.controller.persona;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractCompositeController;
import org.nideasystems.webtools.zwitrng.client.controller.GlobalToolsWidget;
import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.controller.IDataLoadedHandler;
import org.nideasystems.webtools.zwitrng.client.services.RPCService;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaView;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonasCompositeView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class PersonasCompositeController extends AbstractCompositeController
		implements IDataLoadedHandler {

	// The controller view
	private PersonasCompositeView personasCompositeView = null;
	GlobalToolsWidget toolsWidget = null;
	private Map<String, PersonaDTO> personas = null;
	private Map<String, Widget> personaTabs = new HashMap<String, Widget>();
	//private Map<String, SearchesCompositeController> searchesCompositeControllers = new HashMap<String, SearchesCompositeController>();

	/**
	 * Constructor
	 */
	public PersonasCompositeController() {

	}

	/**
	 * Initialize the view where all tabs are placed
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
			try {
				startProcessing();
				service.loadPersonas(new LoadPersonasCallBack());
			} catch (Exception e) {
				getErrorHandler().addException(e);
				e.printStackTrace();
				endProcessing();
			}
		}

	}

	public class LoadPersonasCallBack implements
			AsyncCallback<List<PersonaDTO>> {

		@Override
		public void onFailure(Throwable caught) {
			// Ok, got an error, let's default error handle handle this
			getErrorHandler().addException(caught);
			endProcessing();

		}

		@Override
		public void onSuccess(List<PersonaDTO> result) {
			endProcessing();
			// Cool, let's add the persons :)
			// TODO: Create a IDataLoaded handler that is the controller
			createPersonas(result);
			//getDataLoadedCallBack().onDataLoaded(result);
		}

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

	/**
	 * When a Tab is selected
	 * 
	 * @author jpereira
	 * 
	 */
	private class PersonaTabSelectionHandler implements
			SelectionHandler<Integer> {

		// When a tab is selected what should I do?
		@Override
		public void onSelection(SelectionEvent<Integer> event) {
			// Check if the user is authenticated with OAuth

			onTabSelectedChanged(event.getSelectedItem());

		}

	}

/*	private void initializeFullPersonaView(PersonaView personaView) {
		
		SearchesCompositeController searchesCompositeController = this.searchesCompositeControllers
				.get(personaView.getPersonaObj().getName());
		
		
		if (searchesCompositeController == null) {

			searchesCompositeController = new SearchesCompositeController();
			searchesCompositeController.setErrorHandler(getErrorHandler());
			searchesCompositeController.setName(AbstractController
					.generateDefaultName());
			searchesCompositeController.setParentController(this);
			searchesCompositeController.setTwitterAccount(personaView
					.getPersonaObj().getTwitterAccount());
			searchesCompositeController.setServiceManager(getServiceManager());
			searchesCompositeController.init();
			// Add the created view of the controller to this view
			personaView.add(searchesCompositeController.getView()
					.getAsWidget());
			this.searchesCompositeControllers.put(personaView
					.getPersonaObj().getName(), searchesCompositeController);

		}

	}
*/
	
	private void onTabSelectedChanged(int selectedTab) {
		if (selectedTab != 0) {
			// TODO:Huum!!
			final PersonaView personaViewSelected = (PersonaView) this.personasCompositeView
					.getWidget(selectedTab);

			//PersonaDTO
			personaViewSelected.getController().reload();
			personaViewSelected.refresh();
			
			// Check if the person is logged
			/*if (!personaViewSelected.getPersonaObj().getTwitterAccount()
					.getIsOAuthenticated()) {
				//do nothing??
				// personaViewSelected.setVisible(false);

			} else {
				
				personaViewSelected.refresh();//initializeFullPersonaView(personaViewSelected);
				
				// Try to get the controller
				SearchesCompositeController searchesCompositeController = this.searchesCompositeControllers
						.get(personaViewSelected.getPersonaObj().getName());
				if (searchesCompositeController == null) {

					searchesCompositeController = new SearchesCompositeController();
					searchesCompositeController
							.setErrorHandler(getErrorHandler());
					searchesCompositeController.setName(AbstractController
							.generateDefaultName());
					searchesCompositeController.setParentController(this);
					searchesCompositeController
							.setTwitterAccount(personaViewSelected
									.getPersonaObj().getTwitterAccount());
					searchesCompositeController
							.setServiceManager(getServiceManager());
					searchesCompositeController.init();
					// Add the created view of the controller to this view
					personaViewSelected.add(searchesCompositeController
							.getView().getAsWidget());
					this.searchesCompositeControllers.put(personaViewSelected
							.getPersonaObj().getName(),
							searchesCompositeController);

				}

			}*/
			// Not authen
			/*
			 * try { personaViewSelected.isUpdating(true);
			 * getServiceManager().getRPCService
			 * ().getOAuthInfo(personaViewSelected
			 * .getPersonaObj().getTwitterAccount(), new
			 * AsyncCallback<OAuthInfoDTO>() {
			 * 
			 * @Override public void onFailure(Throwable caught) {
			 * 
			 * personaViewSelected.isUpdating(false);
			 * getErrorHandler().addException(caught); caught.printStackTrace();
			 * 
			 * }
			 * 
			 * @Override public void onSuccess(OAuthInfoDTO result) {
			 * //Window.alert(result); personaViewSelected.isUpdating(false);
			 * //personaViewSelected.add(new HTML(result));
			 * personaViewSelected.setLoginUrl(result.getLoginUrl());
			 * Window.alert("URL: "+result.getLoginUrl());
			 * Window.alert("Token: "+result.getToken());
			 * Window.alert("Token Secret: "+result.getTokenSecret());
			 * personaViewSelected.refresh();
			 * 
			 * 
			 * //authentication.add(new HTML(result));
			 * 
			 * }
			 * 
			 * }); } catch (Exception e) {
			 * personaViewSelected.isUpdating(false);
			 * getErrorHandler().addException(e); e.printStackTrace(); } }
			 */

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
				.getAsWidget(), persona.getName());
		// Let's maintain the list of created widgets, by persona name.
		// This will serve to synchronize between The rendered tabs and
		// the Loaded personas
		this.personaTabs.put(persona.getName(), personaController.getView()
				.getAsWidget());

	}

	private void removePersona(String personaName) {
		if (this.personas.get(personaName) != null) {
			this.personas.remove(personaName);
		}
		if (this.personaTabs.get(personaName) != null) {
			this.personasCompositeView.remove(personaTabs.get(personaName));
			this.personasCompositeView.selectTab(0);
			this.personaTabs.remove(personaName);

		}

	}

	@Override
	public void handleDataLoaded(Object result) {
		// Hopefully, this is a PersonaDTO
		createPersona((PersonaDTO) result);

	}

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
		super.endProcessing();
		getErrorHandler().isProcessing(false);

	}

	@Override
	public void startProcessing() {
		getErrorHandler().isProcessing(true);
		super.startProcessing();
	}

	@Override
	public void handleAction(String action, Object... args) {

		
		if (action.equals(IController.IActions.DELETE)) {
			String personaName = (String) args[0];

			try {
				startProcessing();
				getServiceManager().getRPCService().deletePersona(personaName,
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								endProcessing();
								getErrorHandler().addException(caught);
							}

							@Override
							public void onSuccess(String result) {
								endProcessing();
								removePersona(result);

							}

						});
			} catch (Exception e) {
				endProcessing();
				getErrorHandler().addException(e);
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
								getErrorHandler().addException(caught);

							}

							@Override
							public void onSuccess(PersonaDTO result) {
								endProcessing();
								handleDataLoaded(result);

							}

						});
			} catch (Exception e) {
				endProcessing();
				getErrorHandler().addException(e);
			}
		}
		// Window.alert("PersonaaCompositeController ActionEvent Handler"+action);

	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

}