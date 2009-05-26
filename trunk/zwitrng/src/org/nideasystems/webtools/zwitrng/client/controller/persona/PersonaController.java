package org.nideasystems.webtools.zwitrng.client.controller.persona;



import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesCompositeController;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaView;

import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PersonaController extends AbstractController {

	private PersonaView personaView = null;
	private PersonaDTO persona = null;
	//private Map<String, SearchesCompositeController> searchesCompositeControllers = new HashMap<String, SearchesCompositeController>();
	TwitterUpdatesCompositeController twitterUpdatesCompositeController = null;
	//private OAuthInfoDTO oAuthInfo = null;
	
	private void initializeUpdatesController() {
		
		/*SearchesCompositeController searchesCompositeController = this.searchesCompositeControllers
				.get(persona.getName());
		*/
		
		if (twitterUpdatesCompositeController == null && persona.getTwitterAccount() != null && persona.getTwitterAccount().getIsOAuthenticated() ) {

			twitterUpdatesCompositeController = new TwitterUpdatesCompositeController();
			twitterUpdatesCompositeController.setErrorHandler(getErrorHandler());
			twitterUpdatesCompositeController.setName(AbstractController
					.generateDefaultName());
			twitterUpdatesCompositeController.setParentController(this);
			twitterUpdatesCompositeController.setTwitterAccount(persona.getTwitterAccount());
			twitterUpdatesCompositeController.setServiceManager(getServiceManager());
			twitterUpdatesCompositeController.init();
			
			// Add the created view of the controller to this view
			this.personaView.add(twitterUpdatesCompositeController.getView()
					.getAsWidget());
			/*this.searchesCompositeControllers.put(personaView
					.getPersonaObj().getName(), searchesCompositeController);*/

		}

	}

	@Override
	public void init() {
		//Let's create the persona View
		personaView = new PersonaView(persona);
		//The the controller for the view
		personaView.setController(this);
		//Initialize the view
		personaView.init();
	
			
		
		
		//Now, tell the controller what view to use
		this.view = personaView;
		
	}
	
	/**
	 * Set the Persona DTO
	 * @param persona
	 */
	public void setPersona(PersonaDTO persona) {
		this.persona = persona;
	}

	/**
	 * Get the Persona DTO
	 * @return
	 */
	public PersonaDTO getPersona() {
		return persona;
	}

	@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleDataLoaded(Object result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleAction(String action, Object...args) {

		if (action.equals(IController.IActions.CONTINUE_LOGIN)) {
			//check if the window is still open
			//Try to authorize the User
			
			try {
				getServiceManager().getRPCService().authenticateUser(this.persona, new AsyncCallback<TwitterAccountDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						getErrorHandler().addException(caught);
						
					}

					@Override
					public void onSuccess(TwitterAccountDTO result) {
						
						
						if (result.getIsOAuthenticated()) {
							persona.setTwitterAccount(result);
							
							//init();
							//Re-initialize
							
							personaView.refresh();
							initializeUpdatesController();
							
							
						}
						
					}
					
				});
			} catch (Exception e) {
				getErrorHandler().addException(e);
				e.printStackTrace();
			}
		}
		if (action.equals(IController.IActions.START_LOGIN)) {
			Window.open(persona.getTwitterAccount().getOAuthLoginUrl(), "_login_", "");
			
		}

		
		if (action.equals(IController.IActions.DELETE)) {
			try {
				startProcessing();
				getParentController().handleAction(IController.IActions.DELETE, this.persona.getName());
				//getServiceManager().getRPCService().deletePersona(this.persona.getName(), getParentController().getDataRemovedCallBack());
			} catch (Exception e) {
				endProcessing();
				getErrorHandler().addException(e);
			}
		}
		if (action.endsWith(IController.IActions.UPDATE_LAST_UPDATE)) {
			TwitterUpdateDTO update =(TwitterUpdateDTO)args[0];
			updateLastStatus(update);
		}
		/*
		if ( action.equals(IController.IActions.TWEET_THIS)) {
			Window.alert("deprecated here");
			try {
				startProcessing();
				if (args[0]!= null && args[0] instanceof TwitterUpdateDTO )
				getServiceManager().getRPCService().postUpdate(this.persona.getTwitterAccount(), (String)args[0], new AsyncCallback<TwitterUpdateDTO>(){

					@Override
					public void onFailure(Throwable caught) {
						endProcessing();
						getErrorHandler().addException(caught);
						
					}

					@Override
					public void onSuccess(TwitterUpdateDTO result) {
						endProcessing();
						updateLastStatus(result);
						//Window.alert("Sent: "+result.getText());
						
					}

					
				
				});
				
			} catch (Exception e) {
				endProcessing();
				getErrorHandler().addException(e);
				e.printStackTrace();
			}
			
		}
*/			
	}
	private void updateLastStatus(TwitterUpdateDTO result) {
		personaView.updateLastStatus(result);
		personaView.refresh();
		
	}
	
	
	@Override
	public void endProcessing() {
		getErrorHandler().isProcessing(false);
		super.endProcessing();
	}

	@Override
	public void startProcessing() {
		getErrorHandler().isProcessing(true);
		super.startProcessing();
	}

	@Override
	public void reload() {
		//Reload
		initializeUpdatesController();
		
	}
	

	

}
