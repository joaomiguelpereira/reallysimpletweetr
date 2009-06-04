package org.nideasystems.webtools.zwitrng.client.controller.search;


import java.util.List;

import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesController;
import org.nideasystems.webtools.zwitrng.client.view.search.TwitterUpdatesSearchView;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class TwitterUpdatesSearchController extends TwitterUpdatesController{

	private TwitterUpdatesSearchView searchView = null;
	private TwitterAccountDTO twitterAccount = null;

	@Override
	public void init() {
		
		searchView = new TwitterUpdatesSearchView();
		searchView.setController(this);
		//searchView.setName(getName());
		searchView.init();
		//this.view = searchView;
			
	}

	/*@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		// TODO Auto-generated method stub
		return null;
	}
*/
	


	
	

	@Override
	public void handleAction(String action, Object... args) {
		if ( action.endsWith(IController.IActions.SEARCH) ) {
			assert(args.length>0);
			FilterCriteriaDTO filter = (FilterCriteriaDTO)args[0]; 
			try {
				getServiceManager().getRPCService().search(filter, twitterAccount, new SearchCallback());
			} catch (Exception e) {
				getMainController().addException(e);
			}
		}
		
	}
	private void updateSearchResults(List<TwitterUpdateDTO> result) {
		Window.alert("Searcj OK");
		
	}
	
	public void setTwitterAccount(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountDTO getTwitterAccount() {
		return twitterAccount;
	}

	private class SearchCallback implements AsyncCallback<List<TwitterUpdateDTO>> {

		@Override
		public void onFailure(Throwable caught) {
			getMainController().addException(caught);
			
		}

		@Override
		public void onSuccess(List<TwitterUpdateDTO> result) {
			updateSearchResults(result);
			
		}
		
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}