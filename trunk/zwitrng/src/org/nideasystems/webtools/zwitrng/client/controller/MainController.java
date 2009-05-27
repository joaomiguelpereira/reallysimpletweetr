package org.nideasystems.webtools.zwitrng.client.controller;



import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonasCompositeController;
import org.nideasystems.webtools.zwitrng.client.services.BasicServiceManager;
import org.nideasystems.webtools.zwitrng.client.services.IServiceManager;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

public class MainController extends AbstractCompositeController implements IMainController{
	private Panel mainPanel = RootPanel.get("main");
	private static MainController instance = null;
	private IServiceManager serviceManager = new BasicServiceManager();
	PersonasCompositeController personasCompositeController = null;
	private final GlobalToolsWidget toolsWidget = new GlobalToolsWidget();
	
	
	
	private MainController() {
		
	}
	
	public static MainController getInstance() {
		if ( instance == null ) {
			instance = new MainController();
		}
		return instance;
	}

	public void init() {
		//This will be the default error handle for this controller
		setErrorHandler(this);
		
		
		//Add the tools
		toolsWidget.setController(this);
		toolsWidget.init();
		mainPanel.add(toolsWidget);

		
		//Create the PesonasCompisiteController
		//This controller will control the Personas loaded for the logged user
		//Another reponsabilit, is to handle the renderization of the tabs for the personas
		personasCompositeController = new PersonasCompositeController();
		//The name of the controller will be assigned by the systems
		personasCompositeController.setName(AbstractController.generateDefaultName());
		//The controller will have this MainController and parent controleer
		personasCompositeController.setParentController(this);
		//The service manager used to get instance of remote services(In this case gRPC)
		personasCompositeController.setServiceManager(serviceManager);
		//The error handler of the controller will be this instance
		personasCompositeController.setErrorHandler(this);
		//Initialize
		personasCompositeController.init();
		//Get the corresponding view and add it as a widget to the main Root Panel
		mainPanel.add(personasCompositeController.getView().getAsWidget());
		
		
		
		
		
	}

	@Override
	public void addError(String errorMsg) {
		Window.alert(errorMsg);
		
	}

	@Override
	public void addException(Throwable tr) {
		Window.alert(tr.getLocalizedMessage());
		
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
	public void handleAction(String action, Object... args) {
		Window.alert("MainCOntroller ActionEvent Handler"+action);
		
	}

	@Override
	public void isProcessing(boolean isProcessing) {
		//Window.alert("isProcessing:"+isProcessing);
		toolsWidget.isUpdating(isProcessing);
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

	
}