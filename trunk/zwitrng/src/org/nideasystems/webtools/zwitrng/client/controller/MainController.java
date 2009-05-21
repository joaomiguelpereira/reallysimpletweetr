package org.nideasystems.webtools.zwitrng.client.controller;



import org.nideasystems.webtools.zwitrng.client.services.BasicServiceManager;
import org.nideasystems.webtools.zwitrng.client.services.IServiceManager;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

public class MainController extends AbstractCompositeController implements IErrorHandler{
	private Panel mainPanel = RootPanel.get();
	private static MainController instance = null;
	private IServiceManager serviceManager = new BasicServiceManager();
	PersonasCompositeController personasCompositeController = null;
	
	
	
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
	public void getDataLoadedHandler(Object result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getToolActionHandler(String string) {
		Window.alert("MainCOntroller ActionEvent Handler"+string);
		
	}

	@Override
	public AsyncCallback<String> getDataRemovedCallBack() {
		// TODO Auto-generated method stub
		return null;
	}

}
