package org.nideasystems.webtools.zwitrng.client.controller.search;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractCompositeController;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesController;
import org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdatesCompositeView;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SearchesCompositeController extends AbstractCompositeController {

	// This update controller belong to one persona
	private TwitterAccountDTO twitterAccount;


	//Controls if it's initialized
	private boolean initialized = false;

	private TwitterUpdatesCompositeView searchesCompositeView = null;
	
	//private Map<String, PersonaUpdatesTabView> updatesViews = null;

	public SearchesCompositeController() {
		super();
	}

	
	public void init() {
		if (!this.initialized) {
			searchesCompositeView = new TwitterUpdatesCompositeView();
			searchesCompositeView.setController(this);
			searchesCompositeView.init();
			this.view = searchesCompositeView;
			
			
			//Initialize the Home view
			TwitterUpdatesController twitterHomeController = new TwitterUpdatesController();
			twitterHomeController.setErrorHandler(getErrorHandler());
			twitterHomeController.setName("Home");
			twitterHomeController.setUpdatesType(UpdatesType.FRIENDS);
			twitterHomeController.setTwitterAccount(twitterAccount);
			twitterHomeController.setParentController(this);
			twitterHomeController.setServiceManager(getServiceManager());
			twitterHomeController.init();
			searchesCompositeView.add(twitterHomeController.getView().getAsWidget(),twitterHomeController.getName());
			
			
			//Initialize the Search View
			TwitterUpdatesSearchController defaultSearchController = new TwitterUpdatesSearchController();
			defaultSearchController.setErrorHandler(getErrorHandler());
			defaultSearchController.setName("Search_9192829192839372828372837465192H");
			defaultSearchController.setParentController(this);
			defaultSearchController.setServiceManager(getServiceManager());
			defaultSearchController.setTwitterAccount(twitterAccount);
			defaultSearchController.init();
			searchesCompositeView.add(defaultSearchController.getView().getAsWidget(),"Search");
			
			searchesCompositeView.selectTab(0);
						
		}

	}

	@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		Window.alert("SelectionHandler<Integer> getSelectionHandler() Searches");
		return null;
	}

	@Override
	public void handleDataLoaded(Object result) {
		Window.alert("getDataLoadedHandler(Object result)");
		
	}

	@Override
	public AsyncCallback<String> getDataRemovedCallBack() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTwitterAccount(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountDTO getTwitterAccount() {
		return twitterAccount;
	}


	@Override
	public void handleAction(String action, Object... args) {
		Window.alert("SeachsCompositeController ActionEvent Handler"+action);
		
	}

	

}
