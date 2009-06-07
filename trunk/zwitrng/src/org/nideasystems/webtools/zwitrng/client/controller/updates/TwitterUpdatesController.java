package org.nideasystems.webtools.zwitrng.client.controller.updates;

import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.AutoUpdatable;
import org.nideasystems.webtools.zwitrng.client.controller.TwitterAccountOperationCallBack;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.TwitterUserInfoWidget;
import org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdateWidget;
import org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdatesView;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TwitterUpdatesController extends AbstractController<TwitterUpdateDTOList, TwitterUpdatesView> implements
		AutoUpdatable {

	private TwitterAccountDTO twitterAccount;

	//private TwitterUpdatesView updatesView = null;
	private Map<Long, TwitterUpdateDTO> updates = new HashMap<Long, TwitterUpdateDTO>();
	private Timer timerForAutoUpdates = null;
	private int timeBeforeAutoUpdate = 60; // Seconds
	private int updatesPerPage = 20; // 20 updates in a page
	private Map<Long, TwitterUpdateWidget> updateWidgets = new HashMap<Long, TwitterUpdateWidget>();
	private FilterCriteriaDTO currentFilter = null;
	// If is paused (tab invisible, tweet selected, answering tweet, etc) don't
	// update automatically

	private boolean isPaused = false;

	@Override
	public void init() {
		//create the view 
		setView(new TwitterUpdatesView());
		getView().setController(this);
		getView().setCurrentFilter(currentFilter);
		getView().init();
		this.twitterAccount = ((TwitterUpdatesListController)getParentController()).getModel();
	
		
	}

	

	

	
	public void handleDataLoaded(TwitterUpdateDTOList twitterUpdates) {
		
		
		assert (twitterUpdates != null);
		boolean addOnTop = false;
		boolean updateNeeded = false;
		if (currentFilter.getSinceId() > 1) {
			addOnTop = true;
		}

		if (twitterUpdates.getTwitterUpdatesList().size() > 0) {
			long newUpdateId = twitterUpdates.getTwitterUpdatesList().get(0).getId();
			if (newUpdateId != currentFilter.getSinceId()) {
				updateNeeded = true;
				currentFilter.setSinceId(newUpdateId);
				currentFilter.setSinceId(newUpdateId);
			}

		}

		if (updateNeeded) {
			int i = 1;
				
			
			
			for (TwitterUpdateDTO update : twitterUpdates.getTwitterUpdatesList()) {

				if (addOnTop) {
					// updatesView.insert(updateWidget, i++);
					addUpdateWidget(update, i++);

				} else {
					addUpdateWidget(update, -1);
					// updatesView.add(updateWidget);
				}

				

			}

		}
		adjustPageSize();
	}

	private void addUpdateWidget(TwitterUpdateDTO update, int pos) {
		
		TwitterUpdateWidget updateWidget = new TwitterUpdateWidget(this,update);
		
		updateWidget.setStyleName("twitterUpdate");
		//updateWidget.init();

		if (pos > -1) {
			getView().insert(updateWidget, pos);

		} else {
			getView().add(updateWidget);
		}

		updateWidgets.put(update.getId(), updateWidget);
		updates.put(update.getId(), update);
	}



	@Override
	public void reload() {
		if (!isPaused) {
			startProcessing();
			
			/*FilterCriteriaDTO filter = new FilterCriteriaDTO();
			filter.setSinceId(lastUpdateId);
			filter.setUpdatesType(this.getCurrentFilter().getUpdatesType());*/
			// Let's update the tweets
			try {
				getServiceManager().getRPCService().getTwitterUpdates(
						twitterAccount, this.getCurrentFilter(), new AsyncCallback<TwitterUpdateDTOList>() {

							@Override
							public void onFailure(Throwable caught) {
								GWT.log("Error returned from service", caught);
								endProcessing();
								getMainController().addException(caught);
								
							}

							@Override
							public void onSuccess(TwitterUpdateDTOList result) {
								endProcessing();
								handleDataLoaded(result);
								
							}
							
						});
			} catch (Exception e) {
				endProcessing();
				getMainController().addException(e);
				GWT.log("Error calling serive", e);

			}
			
		}

	}

	
	

	@Override
	public void handleAction(String action, final Object... args) {

	

	}

	public void changePageSize(int newPageSize) {
		GWT.log("Changing page size to "+newPageSize, null);
		updatesPerPage = newPageSize;
		adjustPageSize();
	}
	
	public void enableAutoUpdate() {
		GWT.log("Enabling auto update", null);
		if (timerForAutoUpdates == null) {
			timerForAutoUpdates = new AutoUpdatesUpdateTimer();
		}

		timerForAutoUpdates.scheduleRepeating(1000 * timeBeforeAutoUpdate);
	}
	public void disableAutoUpdate() {
		GWT.log("Disabling auto update", null);
		if (timerForAutoUpdates != null) {
			timerForAutoUpdates.cancel();
		}
	}
	/**
	 * Check the current sise and update accordingly
	 */
	private void adjustPageSize() {
		// TODO Auto-generated method stub
		// With the subtraction I'm removing the widget that represents the
		// tools (errrr)
		int widgetCount = getView().getWidgetCount() - 1;
		if (widgetCount > updatesPerPage) {
			// Remove any remaining update
			// int updatesToRemove = updatesPerPage-newPageSize;
			for (int i = widgetCount; i > updatesPerPage; i--) {
				TwitterUpdateWidget updateWidget = (TwitterUpdateWidget) getView()
						.getWidget(i);
				updates.remove(updateWidget.getTwitterUpdate().getId());
				updateWidgets.remove(updateWidget.getTwitterUpdate().getId());
				getView().remove(i);

			}
			assert (updates.size() == updatesPerPage);
			assert ((getView().getWidgetCount() - 1) == updatesPerPage);
		}

		// updatesPerPage = newPageSize;

	}

	private class AutoUpdatesUpdateTimer extends Timer {

		@Override
		public void run() {

			if (!isPaused) {
				reload();
			}
			

		}

	}

	
	public void setTwitterAccount(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountDTO getTwitterAccount() {
		return twitterAccount;
	}

	
	@Override
	public void pause() {
		this.isPaused = true;

	}

	@Override
	public void resume() {
		this.isPaused = false;
	}


	public TwitterAccountController getTwitterAccountController() {
		return ((TwitterUpdatesListController)getParentController()).getTwitterAccountController();
		
	}



	public void setCurrentFilter(FilterCriteriaDTO currentFilter) {
		this.currentFilter = currentFilter;
	}



	public FilterCriteriaDTO getCurrentFilter() {
		return currentFilter;
	}
	
/*	public PersonaController getPersonaController() {
		return (PersonaController)getTwitterAccountController().getParentController();
	}
*/


	/**
	 * Delegate this request to parent controller
	 * @param accountIdorScreenName
	 * @param callback
	 *//*
	public void getExtendedUserInfo(String accountIdorScreenName,
			TwitterAccountOperationCallBack callback) {
		
		getTwitterAccountController().getExtendedUserAccount(accountIdorScreenName,callback);
		
	}*/


/*
	*//**
	 * Delegate followUser to parent controller
	 * @param follow
	 * @param id
	 * @param callback
	 *//*
	public void followUser(boolean follow, Integer id, TwitterAccountOperationCallBack callback) {
		
		getTwitterAccountController().followUser(follow, id, callback);
		
	}*/



	/**
	 * Delegate block user to parent controller
	 * @param block
	 * @param id
	 * @param callback
	 *//*
	public void blockUser(boolean block, Integer id, TwitterUserInfoWidget callback) {
		
		((TwitterUpdatesListController)getParentController()).getTwitterAccountController().blockUser(block, id, callback);
		
	}
*/
	
	


	

}
