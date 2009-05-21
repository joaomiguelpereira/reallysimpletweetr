package org.nideasystems.webtools.zwitrng.client.controller;

import org.nideasystems.webtools.zwitrng.client.services.IServiceManager;
import org.nideasystems.webtools.zwitrng.client.view.IView;


public abstract class AbstractController implements IController {

	private static int controllerCount = 0;
	
	private IController parentController;
	protected IView view;
	private String name;
	private IServiceManager serviceManager;
	private IErrorHandler errorHandler;
	
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}

	@Override
	public IController getParentController() {
		return this.parentController;
	}

	@Override
	public IView getView() {
		return this.view;
	}

	@Override
	public void setParentController(IController parentController) {
		this.parentController = parentController;
	}


	public static String generateDefaultName() {
		return "controller_"+(controllerCount++);
	}

	@Override
	public IServiceManager getServiceManager() {
		return this.serviceManager;
	}



	@Override
	public void setServiceManager(IServiceManager serviceManager) {
		this.serviceManager = serviceManager;
		
	}

	@Override
	public IErrorHandler getErrorHandler() {
		return this.errorHandler;
	}

	

	@Override
	public void setErrorHandler(IErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
		
	}

}
