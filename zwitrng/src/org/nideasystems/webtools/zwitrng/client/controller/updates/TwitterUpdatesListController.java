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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TwitterUpdatesListController extends
		AbstractController<TwitterAccountDTO, TwitterUpdatesListView> implements
		AutoUpdatable {

	private Map<String, TwitterUpdatesController> twitterUpdatesControllers = new HashMap<String, TwitterUpdatesController>();

	TwitterUpdatesController friendsTwitterUpdatesController = null;
	TwitterUpdatesController mentionsTwitterUpdatesController = null;
	TwitterUpdatesController searchesTwitterUpdatesController = null;

	TwitterUpdatesController activeController = null;

	public TwitterUpdatesListController() {
		super();
	}

	public TwitterUpdatesController getActiveUpdatesController() {
		return activeController;
	}
	
	public void init() {
		setView(new TwitterUpdatesListView());
		getView().init();

		loadFriendsTweets();

	}

	private void loadFriendsTweets() {

		this.friendsTwitterUpdatesController = new TwitterUpdatesController();
		this.friendsTwitterUpdatesController
				.setMainController(getMainController());
		this.friendsTwitterUpdatesController.setParentController(this);
		this.friendsTwitterUpdatesController
				.setServiceManager(getServiceManager());
		
		FilterCriteriaDTO friendsFilter = new FilterCriteriaDTO();
		friendsFilter.setUpdatesType(UpdatesType.FRIENDS);
		friendsFilter.setSearchText("friends");
		this.friendsTwitterUpdatesController.setCurrentFilter(friendsFilter);

		this.friendsTwitterUpdatesController.init();
		getView()
				.add(this.friendsTwitterUpdatesController.getView(), "Friends");

		getView().selectTab(0);
		loadUpdateList(this.friendsTwitterUpdatesController);
		activeController=this.friendsTwitterUpdatesController;
		// Add mentions tab
		mentionsTwitterUpdatesController = new TwitterUpdatesController();
		this.mentionsTwitterUpdatesController
				.setMainController(getMainController());
		this.mentionsTwitterUpdatesController.setParentController(this);
		FilterCriteriaDTO mentionsfilter = new FilterCriteriaDTO();
		mentionsfilter.setUpdatesType(UpdatesType.MENTIONS);
		mentionsfilter.setSearchText("mentions");
		this.mentionsTwitterUpdatesController.setCurrentFilter(mentionsfilter);

		this.mentionsTwitterUpdatesController
				.setServiceManager(getServiceManager());
		this.mentionsTwitterUpdatesController.init();
		getView().add(this.mentionsTwitterUpdatesController.getView(),
				"Mentions");

		// Add search tab
		this.searchesTwitterUpdatesController = new TwitterUpdatesController();
		this.searchesTwitterUpdatesController
				.setMainController(getMainController());
		this.searchesTwitterUpdatesController.setParentController(this);
		this.searchesTwitterUpdatesController
				.setServiceManager(getServiceManager());
		FilterCriteriaDTO seachesFlter = new FilterCriteriaDTO();
		seachesFlter.setUpdatesType(UpdatesType.SEARCHES);
		seachesFlter.setSearchText("default");
		this.searchesTwitterUpdatesController.setCurrentFilter(seachesFlter);

		this.searchesTwitterUpdatesController.init();

		getView().add(this.searchesTwitterUpdatesController.getView(),
				"Searches");

		getView().addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if (event.getSelectedItem() == 1) {
					if (activeController != null) {
						activeController.pause();
					}
					if (mentionsTwitterUpdatesController.getModel() == null) {
						loadUpdateList(mentionsTwitterUpdatesController);
					}

					activeController = mentionsTwitterUpdatesController;
				} else if (event.getSelectedItem() == 0) {
					if (activeController != null) {
						activeController.pause();
					}
					if (friendsTwitterUpdatesController.getModel() == null) {
						loadUpdateList(friendsTwitterUpdatesController);
					}
					activeController = friendsTwitterUpdatesController;
				} else if (event.getSelectedItem() == 2) {
					if (activeController != null) {
						activeController.pause();
					}
					if (searchesTwitterUpdatesController.getModel() == null) {
						loadUpdateList(searchesTwitterUpdatesController);

					}
					activeController = searchesTwitterUpdatesController;
				}
				activeController.resume();

			}

		});
		this.twitterUpdatesControllers.put("friends", friendsTwitterUpdatesController);
		this.twitterUpdatesControllers.put("mentions", mentionsTwitterUpdatesController);
		this.twitterUpdatesControllers.put("searches",searchesTwitterUpdatesController);
		

	}

	private void loadUpdateList(
			final TwitterUpdatesController updatesController) {

		updatesController.startProcessing();

		try {
			getServiceManager().getRPCService().getTwitterUpdates(getModel(),
					updatesController.getCurrentFilter(),
					new AsyncCallback<TwitterUpdateDTOList>() {

						@Override
						public void onFailure(Throwable caught) {
							getMainController().addException(caught);
							GWT.log("error returned from getTwitterUpdates",
									caught);
							updatesController.endProcessing();

						}

						@Override
						public void onSuccess(TwitterUpdateDTOList result) {
							updatesController.handleDataLoaded(result);
							updatesController.endProcessing();

						}

					});
		} catch (Exception e) {
			updatesController.endProcessing();
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
		// get all controllers, and pause them
		for (TwitterUpdatesController controller : this.twitterUpdatesControllers
				.values()) {
			controller.pause();
		}

	}

	@Override
	public void resume() {
		for (TwitterUpdatesController controller : this.twitterUpdatesControllers
				.values()) {
			controller.resume();
		}
	}

	public TwitterAccountController getTwitterAccountController() {
		return ((PersonaController) getParentController())
				.getTwitterAccountController();

	}

	public void activateSearch(String searchText) {
		activeController = this.searchesTwitterUpdatesController;
		this.searchesTwitterUpdatesController.getCurrentFilter().setSearchText(searchText);
		this.searchesTwitterUpdatesController.getCurrentFilter().reset();
		
		
		getView().selectTab(2);
		Window.scrollTo(0, 0);
		this.searchesTwitterUpdatesController.reload();
	}

}
