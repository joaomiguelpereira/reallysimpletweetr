package org.nideasystems.webtools.zwitrng.client.controller.updates;

import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.AutoUpdatable;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdateWidget;
import org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdatesView;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;


import com.google.gwt.user.client.rpc.AsyncCallback;

public class TwitterUpdatesController extends
		AbstractController<TwitterUpdateDTOList, TwitterUpdatesView> implements
		AutoUpdatable {

	private static final int MORE_RESULTS_PER_PAGE = 10;

	private TwitterAccountDTO twitterAccount;

	// private TwitterUpdatesView updatesView = null;
	private Map<Long, TwitterUpdateDTO> updates = new HashMap<Long, TwitterUpdateDTO>();
	private Map<Long, TwitterUpdateWidget> updateWidgets = new HashMap<Long, TwitterUpdateWidget>();
	private Timer timerForAutoUpdates = null;
	private int timeBeforeAutoUpdate = 60; // Seconds
	private int updatesPerPage = 20; // 20 updates in a page
	
	private FilterCriteriaDTO currentFilter = null;
	// If is paused (tab invisible, tweet selected, answering tweet, etc) don't
	// update automatically

	private boolean isPaused = false;

	@Override
	public void init() {
		// create the view
		setView(new TwitterUpdatesView());
		getView().setController(this);
		getView().setCurrentFilter(currentFilter);
		getView().init();
		this.twitterAccount = ((TwitterUpdatesListController) getParentController())
				.getModel();

	}

	public void handleDataLoaded(TwitterUpdateDTOList twitterUpdates) {

		assert (twitterUpdates != null);
		boolean addOnTop = false;
		boolean updateNeeded = false;
		if (currentFilter.getSinceId() >= 1) {
			addOnTop = true;
		}
		//Window.alert("Processing "+twitterUpdates.getTwitterUpdatesList().size()+" elements");
		if (twitterUpdates.getTwitterUpdatesList().size() > 0) {
			long newUpdateId = twitterUpdates.getTwitterUpdatesList().get(0)
					.getId();

			if (newUpdateId != currentFilter.getSinceId()) {
				updateNeeded = true;
				currentFilter.setSinceId(newUpdateId);
				currentFilter.setCompletedIn(twitterUpdates.getFilter()
						.getCompletedIn());
				currentFilter.setRefreshUrl(twitterUpdates.getFilter()
						.getRefreshUrl());
				// currentFilter = twitterUpdates.getFilter();
			}

		}

		if (updateNeeded) {
			int i = 0;

			for (TwitterUpdateDTO update : twitterUpdates
					.getTwitterUpdatesList()) {

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
		getView().refresh();
	}

	private void addUpdateWidget(TwitterUpdateDTO update, int pos) {

		TwitterUpdateWidget updateWidget = new TwitterUpdateWidget(this.getTwitterAccountController(), update);
		updateWidget.init();

		updateWidget.setStyleName("twitterUpdate");
		// updateWidget.init();

		if (pos > -1) {
			
			//getView().insert(updateWidget, pos);
			getView().addUpdate(updateWidget, pos);

		} else {
			//getView().add(updateWidget);
			getView().addUpdate(updateWidget);
		}

		updateWidgets.put(update.getId(), updateWidget);
		updates.put(update.getId(), update);
	}

	
	
	public void reload(FilterCriteriaDTO filter) {
		
		if (!isPaused) {
			startProcessing();

			/*
			 * FilterCriteriaDTO filter = new FilterCriteriaDTO();
			 * filter.setSinceId(lastUpdateId);
			 * filter.setUpdatesType(this.getCurrentFilter().getUpdatesType());
			 */
			// Let's update the tweets
			FilterCriteriaDTO usedfilter = null;
			if (filter != null ) {
				usedfilter = filter;
			} else {
				usedfilter = currentFilter;
			}
			try {
				getServiceManager().getRPCService().getTwitterUpdates(
						twitterAccount, usedfilter,
						new AsyncCallback<TwitterUpdateDTOList>() {

							@Override
							public void onFailure(Throwable caught) {
								GWT.log("Error returned from service", caught);
								endProcessing();
								getView().refresh();
								getMainController().addException(caught);

							}

							@Override
							public void onSuccess(TwitterUpdateDTOList result) {
								endProcessing();
								handleDataLoaded(result);

								// getView().refresh();
							}

						});
			} catch (Exception e) {
				endProcessing();
				getMainController().addException(e);
				GWT.log("Error calling serive", e);
				getView().refresh();

			}

		}

	}

	@Override
	public void handleAction(String action, final Object... args) {

	}

	public void changePageSize(int newPageSize) {
		GWT.log("Changing page size to " + newPageSize, null);
		updatesPerPage = newPageSize;
		currentFilter.setResultsPerPage(newPageSize);
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
		int widgetCount = getView().getUpdateCount();
		if (widgetCount > updatesPerPage) {
			// Remove any remaining update
			// int updatesToRemove = updatesPerPage-newPageSize;
			for (int i = widgetCount; i > updatesPerPage; i--) {
				
				TwitterUpdateWidget updateWidget = getView().getUpdateWidget(i-1);
				
				updates.remove(updateWidget.getTwitterUpdate().getId());
				updateWidgets.remove(updateWidget.getTwitterUpdate().getId());
				getView().removeUpdate(updateWidget);
			}

			
		}
		
		//TwitterUpdateWidget updateWidget = getView().getUpdateWidget(getView().getUpdateCount()-1);
		//currentFilter.setMaxId(updateWidget.getTwitterUpdate().getId());


	}

	private class AutoUpdatesUpdateTimer extends Timer {

		@Override
		public void run() {

			reload();

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
		return ((TwitterUpdatesListController) getParentController())
				.getTwitterAccountController();

	}

	public void setCurrentFilter(FilterCriteriaDTO currentFilter) {
		this.currentFilter = currentFilter;
	}

	public FilterCriteriaDTO getCurrentFilter() {
		return currentFilter;
	}

	public void clearView() {



		for (TwitterUpdateWidget wid: this.updateWidgets.values() ) {
			getView().removeUpdate(wid);
		}
		this.updates.clear();
		this.updateWidgets.clear();
		
		
	}

	public void changePage(int page) {
		currentFilter.reset();
		currentFilter.setPage(page);
		clearView();
		reload();
	}

	public void loadMoreUpdates() {
		//get the last id
		TwitterUpdateWidget updateWidget = getView().getUpdateWidget(getView().getUpdateCount()-1);
		long maxId = updateWidget.getTwitterUpdate().getId();
		
		FilterCriteriaDTO filter = new FilterCriteriaDTO();
		filter.reset();
		filter.setPage(1);
		
		filter.setResultsPerPage(MORE_RESULTS_PER_PAGE);
		filter.setMaxId(maxId);
		//filter.setSinceId(currentFilter.getSinceId());
		reload(filter);
		//Window.alert("calling reloading");
		
		
		
	}

	@Override
	public void reload() {
		reload(null);
		
	}

	
}
