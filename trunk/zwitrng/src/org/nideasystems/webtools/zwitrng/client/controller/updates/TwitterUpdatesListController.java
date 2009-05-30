package org.nideasystems.webtools.zwitrng.client.controller.updates;

import java.util.HashMap;
import java.util.Map;
import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.AutoUpdatable;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.controller.search.TwitterUpdatesSearchController;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdatesCompositeView;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TwitterUpdatesListController extends AbstractController<TwitterAccountDTO, TwitterUpdatesListView> implements AutoUpdatable {


	//Controls if it's initialized
	private boolean initialized = false;

	//private TwitterUpdatesCompositeView twitterUpdatesCompositeView = null;
	private Map<String, TwitterUpdatesController> twitterUpdatesControllers = new HashMap<String, TwitterUpdatesController>();
	
	//private Map<String, PersonaUpdatesTabView> updatesViews = null;

	TwitterUpdatesController friendsTwitterUpdatesController = null;
	public TwitterUpdatesListController() {
		super();
	}

	
	public void init() {
		setView(createView(TwitterUpdatesListView.class));
		getView().init();
		
		loadFriendsTweets();
		
		
		
		//Load Tweets
		
		
		//Create the tabs
		 
		
		//Create the view 
		if (!this.initialized) {
			/*twitterUpdatesCompositeView = new TwitterUpdatesCompositeView();
			twitterUpdatesCompositeView.setController(this);
			twitterUpdatesCompositeView.init();
			//this.view = twitterUpdatesCompositeView;
			
			
			//Initialize the Home view
			TwitterUpdatesController twitterHomeController = new TwitterUpdatesController();
			twitterHomeController.setMainController(getMainController());
			//twitterHomeController.setName("Home");
			twitterHomeController.setUpdatesType(UpdatesType.FRIENDS);
			twitterHomeController.setTwitterAccount(twitterAccount);
			//twitterHomeController.setParentController(this);
			twitterHomeController.setServiceManager(getServiceManager());
			twitterHomeController.init();
			//twitterUpdatesCompositeView.add(twitterHomeController.getView().getAsWidget(),twitterHomeController.getName());
			//twitterUpdatesControllers.put(twitterHomeController.getName(), twitterHomeController);
			
			//Initialize the Search View
			TwitterUpdatesSearchController defaultSearchController = new TwitterUpdatesSearchController();
			defaultSearchController.setMainController(getMainController());
			//defaultSearchController.setName("Search_9192829192839372828372837465192H");
			//defaultSearchController.setParentController(this);
			defaultSearchController.setServiceManager(getServiceManager());
			defaultSearchController.setTwitterAccount(twitterAccount);
			defaultSearchController.init();
			twitterUpdatesCompositeView.add(defaultSearchController.getView().getAsWidget(),"Search");
			//twitterUpdatesControllers.put(defaultSearchController.getName(), defaultSearchController);
			twitterUpdatesCompositeView.selectTab(0);*/
						
		}

	}

	private void loadFriendsTweets() {
		
		this.friendsTwitterUpdatesController = AbstractController.createController(TwitterUpdatesController.class);
		this.friendsTwitterUpdatesController.setMainController(getMainController());
		this.friendsTwitterUpdatesController.setParentController(this);
		this.friendsTwitterUpdatesController.setServiceManager(getServiceManager());
		this.friendsTwitterUpdatesController.setUpdatesType(UpdatesType.FRIENDS);
		this.friendsTwitterUpdatesController.init();	
		getView().add(this.friendsTwitterUpdatesController.getView(), "Friends");
		getView().selectTab(0);
		friendsTwitterUpdatesController.startProcessing();
		GWT.log("Loading Friends Tweets", null);
		FilterCriteriaDTO filter = new FilterCriteriaDTO();
		filter.setUpdatesType(UpdatesType.FRIENDS);

		
		try {
			getServiceManager().getRPCService().getTwitterUpdates(getModel(), filter, new AsyncCallback<TwitterUpdateDTOList>() {

				@Override
				public void onFailure(Throwable caught) {
					getMainController().addException(caught);
					GWT.log("error returned from getTwitterUpdates", caught);
					friendsTwitterUpdatesController.endProcessing();
					
				}

				@Override
				public void onSuccess(TwitterUpdateDTOList result) {
					friendsTwitterUpdatesController.handleDataLoaded(result);
					friendsTwitterUpdatesController.endProcessing();
					
					
					
				}
				
			});
		} catch (Exception e) {
			friendsTwitterUpdatesController.endProcessing();
			getMainController().addException(e);
			GWT.log("error callint getTwitterUpdates", e);
		}
		
	}


	/*@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		Window.alert("SelectionHandler<Integer> getSelectionHandler() Searches");
		return null;
	}
*/
/*	@Override
	public void handleDataLoaded(Object result) {
		Window.alert("getDataLoadedHandler(Object result)");
		
	}*/

	

	/*public void setTwitterAccount(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountDTO getTwitterAccount() {
		return twitterAccount;
	}*/


	@Override
	public void handleAction(String action, Object... args) {
		getParentController().handleAction(action, args);
		//Window.alert("SeachsCompositeController ActionEvent Handler"+action);
		
	}


	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void pause() {
		//get all controllers, and pause them
		for (TwitterUpdatesController controller: this.twitterUpdatesControllers.values() ) {
			controller.pause();
		}
		
	}


	@Override
	public void resume() {
		for (TwitterUpdatesController controller: this.twitterUpdatesControllers.values() ) {
			controller.resume();
		}		
	}


	public TwitterAccountController getTwitterAccountController() {
		return ((PersonaController)getParentController()).getTwitterAccountController();
		
	}


	

	

}
