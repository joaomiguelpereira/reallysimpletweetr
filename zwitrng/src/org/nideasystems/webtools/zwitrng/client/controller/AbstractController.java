package org.nideasystems.webtools.zwitrng.client.controller;


import org.nideasystems.webtools.zwitrng.client.services.IServiceManager;
import org.nideasystems.webtools.zwitrng.client.view.IView;

import org.nideasystems.webtools.zwitrng.shared.model.IDTO;




public abstract class AbstractController<M extends IDTO, V extends IView<?>>
		implements IController<M, V> {

	private static int controllerCount = 0;
	// private static int controllerCount = 0;
	private V view;
	private IController<?,?> parentController;
	
	// private String name;
	private IServiceManager serviceManager;
	private IMainController mainController;
	// private IController<IModel, IView<?>> parent;

	private M model;

	public void init() {
		if (serviceManager == null) {
			throw new RuntimeException(
					"The controller has a null Sevice Manager. Have you forgot to set the service Manager?");
		}

		if (mainController == null) {
			throw new RuntimeException(
					"This controller has no IMainControllerInstance. Have you forgot to setMainController?");
		}

		if (model == null) {
			throw new RuntimeException(
					"This controller has no Model. Have you forgot to call setModel?");
		}
	}

	
	

	/*public <C extends IController<M, ?,?>> C createChildController(
			Class<C> clazz, M model) {
		C controller = GWT.create(clazz);
		controller.setParentController(this);
		controller.setModel(model);

		return controller;
	}
*/
	/*
	 * @Override public V createView(Class<V> clazz) { V view =
	 * GWT.create(clazz); //view.setController(this); return view; }
	 */



	@Override
	public IController<?,?> getParentController() {
		// TODO Auto-generated method stub
		return parentController;
	}




	@Override
	public void setParentController(
			IController<?, ?> parentController) {
		this.parentController = parentController;
		
	}




	@Override
	public IMainController getMainController() {
		return this.mainController;
	}

	@Override
	public void setMainController(IMainController errorHandler) {
		this.mainController = errorHandler;

	}

	@Override
	public M getModel() {
		return model;
	}

	@Override
	public void setModel(M model) {
		this.model = model;

	}

	/*
	 * @Override public String getName() { return this.name; }
	 * 
	 * @Override public void setName(String name) { this.name = name; }
	 */

	@Override
	public V getView() {
		return this.view;
	}

	@Override
	public void setView(V view) {
		this.view = view;

	}

	public static String generateDefaultName() {
		return "controller_" + (controllerCount++);
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
	public void endProcessing() {
		view.isUpdating(false);
		

	}

	@Override
	public void startProcessing() {
		view.isUpdating(true);

	}

	/*public static <V extends IView<?>> V createView(Class<V> clazz) {
		V view = GWT.create(clazz);
		
		//view.setController(this);
		return view;
	}

	public static <C extends IController<?, ?>> C createController(
			Class<C> clazz) {
		C controller = GWT.create(clazz);
		// controller.setModel(model);
		//controller.setParentController(parent);
		controller.setMainController(MainController.getInstance());

		// controller.init();
		return controller;
	}*/

}
