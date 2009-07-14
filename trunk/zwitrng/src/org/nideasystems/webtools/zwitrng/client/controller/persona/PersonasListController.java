package org.nideasystems.webtools.zwitrng.client.controller.persona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.persona.EditableTwitterAccountItem;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaView;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonasListView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class PersonasListController extends
		AbstractController<PersonaDTOList, PersonasListView> {

	private Map<String, PersonaView> personaViews = new HashMap<String, PersonaView>();

	private PersonaController currentPersonaController = null;
	private Map<String, PersonaController> personaControllers = new HashMap<String, PersonaController>();

	

	// private DefaultHomeView homeView;

	/**
	 * Constructor
	 */
	public PersonasListController() {

	}

	public PersonaController getCurrentPersonaController() {
		return currentPersonaController;
	}

	/**
	 * Initialize the view where all tabs are placed
	 */
	public void init() {
		super.init();
		// Create the View
		setView(new PersonasListView());
		// Set the controller for the view
		getView().setController(this);
		// Initialize the view
		getView().init();
		MainController.getInstance().setHomeView(getView().getHomeView());
		// this.homeView = getView().getHomeView();
		// Load the personas
		loadPersonas(getModel());
		// create the view

	}

	/**
	 * Load a PersonaDTOList into the controller
	 */
	private void loadPersonas(PersonaDTOList result) {

		// If the user is not logged in, then it can be null
		// For each persona create a new Tab
		List<PersonaDTO> personas = new ArrayList<PersonaDTO>();

		if (result != null) {
			for (PersonaDTO persona : result.getPersonaList()) {
				loadPersona(persona, false);
				TwitterAccountDTO account = persona.getTwitterAccount();
				personas.add(persona);
			}
		}

		// Update list of accounts in home

		MainController.getInstance().getHomeView().createTwitterAccounts(
				personas);

	}

	/**
	 * called when a given persona is selecets
	 */
	public void onTabSelectedChanged(int selectedTab) {
		GWT.log("You selected TAB" + selectedTab, null);

		if (currentPersonaController != null) {
			currentPersonaController.pause();
		}

		if (selectedTab != 0) {

			final PersonaView personaViewSelected = (PersonaView) this
					.getView().getWidget(selectedTab);

			currentPersonaController = personaViewSelected.getController();
			if (currentPersonaController
					.hasTwitterUpdatesListControllerInitialized()) {
				currentPersonaController.resume();
			} else {
				currentPersonaController.initializeUpdatesListController();
			}

		}
	}

	/**
	 * Load a persona into the controller and create a view for it
	 * 
	 * @param persona
	 */
	private void loadPersona(PersonaDTO persona, boolean selecteAfterCreated) {

		PersonaController personaController = new PersonaController();
		personaController.setMainController(getMainController());
		personaController.setServiceManager(getServiceManager());
		personaController.setParentController(this);
		personaController.setModel(persona);
		personaController.init();
		String title = persona.getName();

		HorizontalPanel tabTitle = new HorizontalPanel();

		String imgUrl = "http://static.twitter.com/images/default_profile_bigger.png";
		Image titleImg = null;

		if (persona.getTwitterAccount() != null
				&& persona.getTwitterAccount().getTwitterScreenName() != null) {

			title = persona.getTwitterAccount().getTwitterName() + " ("
					+ persona.getTwitterAccount().getTwitterScreenName() + ")";
			imgUrl = persona.getTwitterAccount().getTwitterImageUrl();
		}
		titleImg = new Image(imgUrl);

		titleImg.setWidth("24px");
		titleImg.setHeight("24px");

		tabTitle.add(titleImg);
		titleImg.setTitle(title);

		getView().add(personaController.getView(), tabTitle);
		this.personaViews.put(persona.getName(), personaController.getView());

		if (selecteAfterCreated) {
			getView().selectTab(getView().getWidgetCount() - 1);
		}

		personaControllers.put(persona.getName(), personaController);
		
		MainController.getInstance().addPersonaTabTitle(persona.getName(), tabTitle);
		
		MainController.getInstance().getHomeView().addPersona(persona);
	}

	/**
	 * This will unloa the persona from the list and any reference to it
	 * 
	 * @param personaName
	 */
	private void unloadPersona(String personaName) {

		if (this.personaViews.get(personaName) != null) {
			getView().remove(personaViews.get(personaName));
			getView().selectTab(0);
			this.personaViews.remove(personaName);
			personaControllers.remove(personaName);
		}
		//Get the Home view and remove it also
		MainController.getInstance().getHomeView().removePersona(personaName);

	}

	/**
	 * Call the service to remove a persona. After success call unloadPersona
	 * 
	 * @param persona
	 */
	public void deletePersona(PersonaDTO persona) {

		if (Window.confirm("Are you sure you want to remove the account "+persona.getTwitterAccount().getTwitterScreenName()+" from the system?")) {
			// make the server call
			startProcessing();
			try {
				getServiceManager().getRPCService().deletePersona(
						persona.getName(), new AsyncCallback<String>() {

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
							loadPersona(result, true);

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

	public TwitterAccountController getTwitterAccountController(
			String userScreenName) {
		for (PersonaController pController : this.personaControllers.values()) {
			if (pController.getTwitterAccountController().getModel()
					.getTwitterScreenName().endsWith(userScreenName)) {
				return pController.getTwitterAccountController();
			}
		}

		return null;
	}

	public void getPersonaInfo(String personaName,
			final EditableTwitterAccountItem callback) {

		try {

			getServiceManager().getRPCService().getPersona(personaName,
					new AsyncCallback<PersonaDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							callback.onReloadPersonaError(caught);

						}

						@Override
						public void onSuccess(PersonaDTO result) {
							callback.onReloadPersonaSucess(result);

						}

					});
		} catch (Exception e) {

			callback.onReloadPersonaError(e);
		}

	}

	public void synchronize(PersonaDTO persona,
			final EditableTwitterAccountItem callback) {
		try {

			getServiceManager().getRPCService().synchronizeTwitterAccount (persona,
					new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							callback.onSynchronizePersonaError(caught);

						}

						@Override
						public void onSuccess(Void result) {
							callback.onSynchronizePersonaSucess();

						}

					});
		} catch (Exception e) {

			callback.onSynchronizePersonaError(e);
		}

	}

}
