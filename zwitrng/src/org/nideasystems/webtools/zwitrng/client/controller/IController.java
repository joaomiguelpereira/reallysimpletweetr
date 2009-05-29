package org.nideasystems.webtools.zwitrng.client.controller;

import org.nideasystems.webtools.zwitrng.client.services.IServiceManager;
import org.nideasystems.webtools.zwitrng.client.view.IView;
import org.nideasystems.webtools.zwitrng.shared.model.IModel;



public interface IController<M extends IModel, V extends IView<?>> {
	
	
	
	
	/**
	 * Set the Parent Controller
	 * TODO: make it typed
	 * @param parentController
	 */
	public void setParentController(IController<?, ?> parentController);

	/**
	 * Get the parent Controller
	 * @return
	 */
	public IController<?, ?> getParentController();
	
	/**
	 * Set the model for this controller
	 * @param model
	 */
	public void setModel(M model);
	
	/**
	 * Get the model configured for this controller
	 * @return
	 */
	public M getModel();
	/**
	 * Call this when a remote call was made and we are waiting for data
	 */
	public void startProcessing();
	/**
	 * Call when call returns
	 */
	public void endProcessing();

	/**
	 * Set the view for this controller
	 * @param <V>
	 * @param view
	 */
	public void setView(V view);
	/**
	 * Get the View for this controller
	 * @return
	 */
	public V getView();

	/**
	 * Initialize the controller
	 */
	public void init();
	/**
	 * Get the Service Manager used to make remote calls
	 * @return
	 */
	public IServiceManager getServiceManager();
	/**
	 * Set the Service Manager to make remote Calls
	 * @param serviceManager
	 */
	public void setServiceManager(IServiceManager serviceManager);
	/**
	 * Set the Error Handler for this this Controller
	 * @param errorHandler
	 */
	public void setMainController(IMainController errorHandler);
	/**
	 * Get The Error Handler registered with this controller
	 * @return
	 */
	public IMainController getMainController();
	
	/**
	 * TODO: Move for composite controllers with aspect Tab
	 * Return the handler of a tab selection
	 * @return
	 *//*
	public SelectionHandler<Integer> getSelectionHandler();*/
	/**
	 * Handel the data loaded
	 * @param result
	 *//*
	public void handleDataLoaded(Object result);
	*/
	
	/**
	 * If this controller has tools (action associated)
	 * @param string
	 */
	public void handleAction(String action, Object...args);
	
	/**
	 * When data is removed 
	 * @return
	 */
	//public AsyncCallback<String> getDataRemovedCallBack();
	

	
	public interface IActions {
		public final String DELETE="DELETE";
		public final String SEARCH="SEARCH";
		public final String REFRESH_TWEETS="REFRESH_TWEETS";
		public final String ENABLE_AUTO_UPDATE = "ENABLE_AUTO_UPDATE";
		public final String DISABLE_AUTO_UPDATE = "DISABLE_AUTO_UPDATE";
		public final String CHANGE_PAGE_SIZE = "CHANGE_PAGE_SIZE";
		public final String CREATE = "CREATE";
		public final String TWEET_THIS = "TWEET_THIS";
		public final String START_LOGIN = "START_LOGIN";
		public final String CONTINUE_LOGIN = "CONTINUE_LOGIN";
		public final String UPDATE_LAST_UPDATE = "UPDATE_LAST_STATUS";
		public final String PAUSE_AUTO_UPDATE = "PAUSE_AUTO_UPDATE";
		public final String RESUME_AUTO_UPDATE = "RESUME_AUTO_UPDATE";
		public final String PERSONA_SELECTED = "PERSONA_SELECTED";
		
		
	}

	public void reload();
}
