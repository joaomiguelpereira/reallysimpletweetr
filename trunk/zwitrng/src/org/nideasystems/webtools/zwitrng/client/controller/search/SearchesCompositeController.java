package org.nideasystems.webtools.zwitrng.client.controller.search;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractCompositeController;
import org.nideasystems.webtools.zwitrng.client.view.search.SearchesCompositeView;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SearchesCompositeController extends AbstractCompositeController {

	// This update controller belong to one persona
	private TwitterAccountDTO twitterAccount;


	//Controls if it's initialized
	private boolean initialized = false;

	private SearchesCompositeView searchesCompositeView = null;
	
	//private Map<String, PersonaUpdatesTabView> updatesViews = null;

	public SearchesCompositeController() {
		super();
	}

	
	public void init() {
		if (!this.initialized) {
			searchesCompositeView = new SearchesCompositeView();
			searchesCompositeView.setController(this);
			searchesCompositeView.init();
			this.view = searchesCompositeView;
			
			

		}

	}

	@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		Window.alert("SelectionHandler<Integer> getSelectionHandler() Searches");
		return null;
	}

	@Override
	public void getDataLoadedHandler(Object result) {
		Window.alert("getDataLoadedHandler(Object result)");
		
	}

	@Override
	public void getToolActionHandler(String string) {
		Window.alert("SeachsCompositeController ActionEvent Handler"+string);
		
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

	

}
