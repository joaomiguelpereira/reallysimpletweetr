package org.nideasystems.webtools.zwitrng.client.controller;

import org.nideasystems.webtools.zwitrng.client.services.IServiceManager;
import org.nideasystems.webtools.zwitrng.client.view.IView;



import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IController {
	/**
	 * Call this when a remote call was made and we are waiting for data
	 */
	public void startProcessing();
	/**
	 * Call when call returns
	 */
	public void endProcessing();

	/**
	 * Get the Corresponding View
	 * @return
	 */
	public IView getView();
	/**
	 * Set the Parent Controller
	 * @param parentController
	 */
	public void setParentController(IController parentController);
	/**
	 * Get the Parent Controller
	 * @return
	 */
	public IController getParentController();
	/**
	 * Set the name of the controller
	 * @param name
	 */
	public void setName(String name);
	/**
	 * Get the name Of the controller
	 * @return
	 */
	public String getName();
	/**
	 * Initialize. Do any View creation here
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
	public void setErrorHandler(IErrorHandler errorHandler);
	/**
	 * Get The Error Handler registered with this controller
	 * @return
	 */
	public IErrorHandler getErrorHandler();
	
	/**
	 * TODO: Move for composite controllers with aspect Tab
	 * Return the handler of a tab selection
	 * @return
	 */
	public SelectionHandler<Integer> getSelectionHandler();
	/**
	 * Handel the data loaded
	 * @param result
	 */
	public void handleDataLoaded(Object result);
	
	
	/**
	 * If this controller has tools (action associated)
	 * @param string
	 */
	public void handleAction(String action, Object...args);
	
	/**
	 * When data is removed 
	 * @return
	 */
	public AsyncCallback<String> getDataRemovedCallBack();
	

	
	public interface IActions {
		public final String DELETE="DELETE";
		public final String SEARCH="SEARCH";
		public final String REFRESH_TWEETS="REFRESH_TWEETS";
		public final String ENABLE_AUTO_UPDATE = "ENABLE_AUTO_UPDATE";
		public final String DISABLE_AUTO_UPDATE = "DISABLE_AUTO_UPDATE";
		
		
	}
}
