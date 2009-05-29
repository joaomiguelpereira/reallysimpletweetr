package org.nideasystems.webtools.zwitrng.client.controller.updates;

import java.util.HashMap;
import java.util.Map;
import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.AutoUpdatable;
import org.nideasystems.webtools.zwitrng.client.controller.search.TwitterUpdatesSearchController;
import org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdatesCompositeView;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;

public class TwitterUpdatesListController extends AbstractController<TwitterUpdateDTOList, TwitterUpdatesListView> implements AutoUpdatable {

	// This update controller belong to one persona
	private TwitterAccountDTO twitterAccount;


	//Controls if it's initialized
	private boolean initialized = false;

	private TwitterUpdatesCompositeView twitterUpdatesCompositeView = null;
	private Map<String, TwitterUpdatesController> twitterUpdatesControllers = new HashMap<String, TwitterUpdatesController>();
	
	//private Map<String, PersonaUpdatesTabView> updatesViews = null;

	public TwitterUpdatesListController() {
		super();
	}

	
	public void init() {
		if (!this.initialized) {
			twitterUpdatesCompositeView = new TwitterUpdatesCompositeView();
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
			twitterUpdatesCompositeView.selectTab(0);
						
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

	

	public void setTwitterAccount(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountDTO getTwitterAccount() {
		return twitterAccount;
	}


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


	

	

}
