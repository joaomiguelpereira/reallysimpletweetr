package org.nideasystems.webtools.zwitrng.client.controller.updates;

import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.AutoUpdatable;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdatesListView;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TwitterUpdatesListController extends AbstractController<TwitterAccountDTO, TwitterUpdatesListView> implements AutoUpdatable {





	private Map<String, TwitterUpdatesController> twitterUpdatesControllers = new HashMap<String, TwitterUpdatesController>();
	

	TwitterUpdatesController friendsTwitterUpdatesController = null;
	public TwitterUpdatesListController() {
		super();
	}

	
	public void init() {
		setView(new TwitterUpdatesListView());
		getView().init();
		
		loadFriendsTweets();
		
		
		

	}

	private void loadFriendsTweets() {
		
		this.friendsTwitterUpdatesController = new TwitterUpdatesController();
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


	@Override
	public void handleAction(String action, Object... args) {
		getParentController().handleAction(action, args);
		
		
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
