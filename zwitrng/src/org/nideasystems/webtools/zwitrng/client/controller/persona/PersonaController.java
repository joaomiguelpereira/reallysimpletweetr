package org.nideasystems.webtools.zwitrng.client.controller.persona;



import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.AutoUpdatable;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesListController;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaToolsWidget;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;

public class PersonaController extends AbstractController<PersonaDTO, PersonaView> implements AutoUpdatable{

	
	TwitterUpdatesListController twitterUpdatesListController = null;
	TwitterAccountController twitterAccountController = null;
	
	
	@Override
	public void init() {
		
		setView(new PersonaView());
		getView().setController(this);
		getView().init();
		//Add the Tools for the Persona
				
		
		//Create a controller for the TwitterAccount
		twitterAccountController = new TwitterAccountController();
		twitterAccountController.setModel(getModel().getTwitterAccount());
		twitterAccountController.setMainController(getMainController());
		twitterAccountController.setServiceManager(getServiceManager());
		
		twitterAccountController.setParentController(this);
		twitterAccountController.init();
		//Add the view of TwitterAccount to this persona
		getView().add(twitterAccountController.getView());
		//Add twitter updates list
		getView().add(new HTML("Update list"));
		
		
		
	}
	public TwitterUpdatesListController getTwitterUpdatesListController() {
		
		return this.twitterUpdatesListController;
	}
	
	
	

	/**
	 * Set the Persona DTO
	 * @param persona
	 *//*
	public void setPersona(PersonaDTO persona) {
		this.persona = persona;
	}

	*//**
	 * Get the Persona DTO
	 * @return
	 *//*
	public PersonaDTO getPersona() {
		return persona;
	}
*/
	
	

	@Override
	public void handleAction(String action, Object...args) {
		
	}
	
	
	public void delete() {
		assert(getParentController() instanceof PersonasListController);
		((PersonasListController)getParentController()).deletePersona(getModel());
		
	}
	
	@Override
	public void endProcessing() {
		getMainController().isProcessing(false);
		super.endProcessing();
	}

	@Override
	public void startProcessing() {
		getMainController().isProcessing(true);
		super.startProcessing();
	}

	@Override
	public void reload() {
		//Reload
		//initializeUpdatesController();
		
	}

	@Override
	public void pause() {
		
		if ( this.twitterUpdatesListController != null ) {
			this.twitterUpdatesListController.pause();
		}
		
		
	}

	@Override
	public void resume() {
		if ( this.twitterUpdatesListController != null ) {
			this.twitterUpdatesListController.resume();
		}
		
	}

	public boolean hasTwitterUpdatesListControllerInitialized() {
		return (this.twitterUpdatesListController != null);
		
	}

	public void initializeUpdatesListController() {
		GWT.log("Initializing UpdatesListController...", null);
		//initialize only if it's authenticated
		if ( getModel().getTwitterAccount().getIsOAuthenticated() ) {
			this.twitterUpdatesListController = new TwitterUpdatesListController();
			this.twitterUpdatesListController.setMainController(getMainController());
			this.twitterUpdatesListController.setModel(getModel().getTwitterAccount());
			this.twitterUpdatesListController.setParentController(this);
			this.twitterUpdatesListController.setServiceManager(getServiceManager());
			this.twitterUpdatesListController.init();
			getView().add(this.twitterUpdatesListController.getView());			
		}
		
	}

	public TwitterAccountController getTwitterAccountController() {
		return this.twitterAccountController;
		
	}

	
	
	

	

}
