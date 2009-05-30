package org.nideasystems.webtools.zwitrng.client.controller.persona;



import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.AutoUpdatable;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesListController;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaToolsWidget;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

public class PersonaController extends AbstractController<PersonaDTO, PersonaView> implements AutoUpdatable{

	
	TwitterUpdatesListController twitterUpdatesListController = null;
	TwitterAccountController twitterAccountController = null;
	PersonaToolsWidget tools = null;
	
	
	/*private void initializeUpdatesController() {
		
		
		
		if (twitterUpdatesListController == null && getModel().getTwitterAccount() != null && getModel().getTwitterAccount().getIsOAuthenticated() ) {

			twitterUpdatesCompositeController = new TwitterUpdatesListController();
			twitterUpdatesCompositeController.setMainController(getMainController());
			twitterUpdatesCompositeController.setName(AbstractController
					.generateDefaultName());
			//twitterUpdatesCompositeController.setParentController(this);
			twitterUpdatesCompositeController.setTwitterAccount(persona.getTwitterAccount());
			twitterUpdatesCompositeController.setServiceManager(getServiceManager());
			twitterUpdatesCompositeController.init();
			
			// Add the created view of the controller to this view
			this.personaView.add(twitterUpdatesCompositeController.getView()
					.getAsWidget());
			this.searchesCompositeControllers.put(personaView
					.getPersonaObj().getName(), searchesCompositeController);

		}

	}*/

	@Override
	public void init() {
		
		setView(new PersonaView());
		getView().setController(this);
		getView().init();
		//Add the Tools for the Persona
		tools = new PersonaToolsWidget();
		tools.setController(this);
		tools.init();
		getView().add(tools);
		
		
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
		
		//twitterUpdatesListController = AbstractController.createController(TwitterUpdatesListController.class);
		//twitterUpdatesListController.setParentController(this);
		//twitterUpdatesListController.setModel(null);
		
		
		
		
		/*//Let's create the persona View
		personaView = new PersonaView(persona);
		//The the controller for the view
		personaView.setController(this);
		//Initialize the view
		personaView.init();
		
		//Initialize the Controller to Send updates
		
		SendUpdateWidgetController sendUpdateWidgetController = this.createChildController(SendUpdateWidgetController.class, persona.getTwitterAccount());
		
		
		sendUpdateWidgetController.setParentController(this);
		
		sendUpdateWidgetController.setMainController(this.getMainController());
		sendUpdateWidgetController.setServiceManager(getServiceManager());
		
		sendUpdateWidgetController
	
			
		
		
		//Now, tell the controller what view to use
		this.view = personaView;*/
		
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
		
		/*if (action.endsWith(IController.IActions.TWEET_THIS)) {
			TwitterUpdateDTO update = (TwitterUpdateDTO)args[0];
			final SendUpdateAsyncHandler handle = (SendUpdateAsyncHandler)args[args.length-1];
			//startProcessing();
			try {
				getServiceManager().getRPCService().postUpdate(update, new AsyncCallback<TwitterUpdateDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						//endProcessing();
						handle.onFailure(caught);
						getMainController().addException(caught);
						
					}

					@Override
					public void onSuccess(TwitterUpdateDTO result) {
						//endProcessing();
						handle.onSuccess(result);
						
						updateLastStatus(result);
						
						
					}
					
				});
			} catch (Exception e) {
				endProcessing();
				endProcessing();
				e.printStackTrace();
			}
		}
		if (action.equals(IController.IActions.CONTINUE_LOGIN)) {
			//check if the window is still open
			//Try to authorize the User
			
			try {
				getServiceManager().getRPCService().authenticateUser(getModel(), new AsyncCallback<TwitterAccountDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						getMainController().addException(caught);
						
					}

					@Override
					public void onSuccess(TwitterAccountDTO result) {
						
						
						if (result.getIsOAuthenticated()) {
							getModel().setTwitterAccount(result);
							
							//init();
							//Re-initialize
							
							getView().refresh();
							initializeUpdatesController();
							
							
						}
						
					}
					
				});
			} catch (Exception e) {
				getMainController().addException(e);
				e.printStackTrace();
			}
		}
		if (action.equals(IController.IActions.START_LOGIN)) {
			Window.open(getModel().getTwitterAccount().getOAuthLoginUrl(), "_login_", "");
			
		}

		
		if (action.equals(IController.IActions.DELETE)) {
			try {
				startProcessing();
				getParentController().handleAction(IController.IActions.DELETE, getModel().getName());
				//getServiceManager().getRPCService().deletePersona(this.persona.getName(), getParentController().getDataRemovedCallBack());
			} catch (Exception e) {
				endProcessing();
				getMainController().addException(e);
			}
		}
		if (action.endsWith(IController.IActions.UPDATE_LAST_UPDATE)) {
			TwitterUpdateDTO update =(TwitterUpdateDTO)args[0];
			updateLastStatus(update);
		}
	*/
	}
	/*private void updateLastStatus(TwitterUpdateDTO result) {
		getView().updateLastStatus(result);
		getView().refresh();
		
	}*/
	
	
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
