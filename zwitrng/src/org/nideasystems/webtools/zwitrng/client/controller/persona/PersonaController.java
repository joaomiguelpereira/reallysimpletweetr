package org.nideasystems.webtools.zwitrng.client.controller.persona;



import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.AutoUpdatable;
import org.nideasystems.webtools.zwitrng.client.controller.HasParent;
import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesListController;
import org.nideasystems.webtools.zwitrng.client.view.AsyncHandler;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaToolsWidget;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaView;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonasListView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PersonaController extends AbstractController<PersonaDTO, PersonaView> implements AutoUpdatable{

	//private PersonaView personaView = null;
	
	//private PersonasListController parent;
	//private PersonaDTO persona = null;
	//private Map<String, SearchesCompositeController> searchesCompositeControllers = new HashMap<String, SearchesCompositeController>();
	TwitterUpdatesListController twitterUpdatesCompositeController = null;
	TwitterAccountController twitterAccount = null;
	PersonaToolsWidget tools = null;
	//private OAuthInfoDTO oAuthInfo = null;
	
	private void initializeUpdatesController() {
		
		
		
		if (twitterUpdatesCompositeController == null && getModel().getTwitterAccount() != null && getModel().getTwitterAccount().getIsOAuthenticated() ) {

			/*twitterUpdatesCompositeController = new TwitterUpdatesListController();
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
*/
		}

	}

	@Override
	public void init() {
		
		setView(createView(PersonaView.class));
		getView().setController(this);
		getView().init();
		//Add the Tools for the Persona
		tools = new PersonaToolsWidget();
		tools.setController(this);
		tools.init();
		getView().add(tools);
		
		
		//Create a controller for the TwitterAccount
		twitterAccount = AbstractController.createController(TwitterAccountController.class);
		twitterAccount.setModel(getModel().getTwitterAccount());
		twitterAccount.setMainController(getMainController());
		twitterAccount.setServiceManager(getServiceManager());
		
		twitterAccount.setParentController(this);
		twitterAccount.init();
		//Add the view of TwitterAccount to this persona
		getView().add(twitterAccount.getView());
		
		
		
		
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
		
		if (action.endsWith(IController.IActions.TWEET_THIS)) {
			TwitterUpdateDTO update = (TwitterUpdateDTO)args[0];
			final AsyncHandler handle = (AsyncHandler)args[args.length-1];
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
	
	}
	private void updateLastStatus(TwitterUpdateDTO result) {
		getView().updateLastStatus(result);
		getView().refresh();
		
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
		initializeUpdatesController();
		
	}

	@Override
	public void pause() {
		
		if ( this.twitterUpdatesCompositeController != null ) {
			this.twitterUpdatesCompositeController.pause();
		}
		
		
	}

	@Override
	public void resume() {
		if ( this.twitterUpdatesCompositeController != null ) {
			this.twitterUpdatesCompositeController.resume();
		}
		
	}

	
	
	

	

}
