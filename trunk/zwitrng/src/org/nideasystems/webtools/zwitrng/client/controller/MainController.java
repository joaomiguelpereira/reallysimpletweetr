package org.nideasystems.webtools.zwitrng.client.controller;



import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonasListController;
import org.nideasystems.webtools.zwitrng.client.services.BasicServiceManager;
import org.nideasystems.webtools.zwitrng.client.services.IServiceManager;

import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;


import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

public class MainController implements IMainController{
	private Panel mainPanel = RootPanel.get("main");
	private static MainController instance = null;
	final private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	private IServiceManager serviceManager = new BasicServiceManager();
	PersonasListController personasListController = null;
	
	
	
	private MainController() {
			
	}
	
	public static MainController getInstance() {
		if ( instance == null ) {
			instance = new MainController();
			
		}
		
		return instance;

	}
	
	private void loadPersonas() throws Exception {
		isProcessing(true);
		serviceManager.getRPCService().loadPersonas(new AsyncCallback<PersonaDTOList>() {

			@Override
			public void onFailure(Throwable caught) {
				isProcessing(false);
				addException(caught);
				
			}

			@Override
			public void onSuccess(PersonaDTOList result) {
				isProcessing(false);
				if ( personasListController == null ) {
					
					//personasListController = new PersonasListController();
					
					personasListController = AbstractController.createController(PersonasListController.class);
					
					personasListController.setModel(result);
					personasListController.setServiceManager(serviceManager);
					personasListController.init();
					mainPanel.add(personasListController.getView());
					
					
					
				}
				
				
			}

			
			
		});
		

	}

	public void init() {
		//This will be the default error handle for this controller
		//Load personas from Service;
		//Add processing image
		waitingImg.setVisible(false);
		mainPanel.add(waitingImg);
		try {

			loadPersonas();
		} catch (Exception e) {
			addException(e);
			isProcessing(false);
		} 
		
				
		/*
		//Add the tools
		toolsWidget.setController(this);
		toolsWidget.init();
		mainPanel.add(toolsWidget);

		
		//Create the PesonasCompisiteController
		//This controller will control the Personas loaded for the logged user
		//Another reponsabilit, is to handle the renderization of the tabs for the personas
		
		
		personasListController = new PersonasListController();
		//The name of the controller will be assigned by the systems
		personasListController.setName(AbstractController.generateDefaultName());
		//The controller will have this MainController and parent controleer
		//personasCompositeController.setParentController(this);
		//The service manager used to get instance of remote services(In this case gRPC)
		personasListController.setServiceManager(serviceManager);
		//The error handler of the controller will be this instance
		personasListController.setMainController(this);
		//Initialize
		personasListController.init();
		//Get the corresponding view and add it as a widget to the main Root Panel
		mainPanel.add(personasListController.getView().getAsWidget());
		*/
		
		
		
		
	}

	@Override
	public void addError(String errorMsg) {
		Window.alert(errorMsg);
		
	}

	@Override
	public void addException(Throwable tr) {
		Window.alert(tr.getLocalizedMessage());
		
	}

	/*@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		// TODO Auto-generated method stub
		return null;
	}*/

	/*@Override
	public void handleDataLoaded(Object result) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void handleAction(String action, Object... args) {
		Window.alert("MainCOntroller ActionEvent Handler"+action);
		
	}*/

	@Override
	public void isProcessing(boolean isProcessing) {
		waitingImg.setVisible(isProcessing);
	}

	/*@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}*/

	
}
