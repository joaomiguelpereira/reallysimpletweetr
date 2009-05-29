package org.nideasystems.webtools.zwitrng.client.controller.updates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.AutoUpdatable;
import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.view.AsyncHandler;
import org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdatesView;
import org.nideasystems.webtools.zwitrng.client.view.widgets.TwitterUpdateWidget;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TwitterUpdatesController extends AbstractController<TwitterUpdateDTOList, TwitterUpdatesView> implements
		AutoUpdatable {

	private TwitterAccountDTO twitterAccount;
	private UpdatesType updatesType;
	private TwitterUpdatesView updatesView = null;
	private Map<Long, TwitterUpdateDTO> updates = new HashMap<Long, TwitterUpdateDTO>();
	private long lastUpdateId = 1;
	private Timer timerForAutoUpdates = null;
	private int timeBeforeAutoUpdate = 10; // Seconds
	private int updatesPerPage = 20; // 20 updates in a page
	Map<Long, TwitterUpdateWidget> updateWidgets = new HashMap<Long, TwitterUpdateWidget>();
	// If is paused (tab invisible, tweet selected, answering tweet, etc) don't
	// update automatically

	private boolean isPaused = false;

	public UpdatesType getUpdatesType() {
		return updatesType;
	}

	public void setUpdatesType(UpdatesType updatesType) {
		this.updatesType = updatesType;
	}

	
	public void handleDataLoaded(Object result) {
		List<TwitterUpdateDTO> twitterUpdates = (List<TwitterUpdateDTO>) result;
		// Now start building the TwitterUpdateWidget
		assert (result != null);
		boolean addOnTop = false;
		boolean updateNeeded = false;
		if (lastUpdateId > 1) {
			addOnTop = true;
		}

		if (twitterUpdates.size() > 0) {
			long newUpdateId = twitterUpdates.get(0).getId();
			if (newUpdateId != lastUpdateId) {
				updateNeeded = true;
				lastUpdateId = newUpdateId;
			}

		}

		if (updateNeeded) {
			int i = 1;
			for (TwitterUpdateDTO update : twitterUpdates) {

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
		endProcessing();
	}

	private void addUpdateWidget(TwitterUpdateDTO update, int pos) {
		TwitterUpdateWidget updateWidget = new TwitterUpdateWidget();
		updateWidget.setTwitterAccount(getTwitterAccount());
		updateWidget.setController(this);
		updateWidget.setTwitterUpdate(update);
		updateWidget.setStyleName("twitterUpdate");
		updateWidget.init();

		if (pos > -1) {
			updatesView.insert(updateWidget, pos);

		} else {
			updatesView.add(updateWidget);
		}

		updateWidgets.put(update.getId(), updateWidget);
		updates.put(update.getId(), update);
	}

	private void handleTweetedUpdate(TwitterUpdateDTO result) {
		// Check if the tweet is in reply to any rendered
		Long inRelyTo = result.getInReplyToStatusId();

		// Check if I have the tweet on my list
		TwitterUpdateWidget widget = this.updateWidgets.get(inRelyTo);
		if (widget != null) {
			// find get the widget
			widget.hasReply(result);
		} else {
			// Add to top
			// updatesView.ad
		}

	}

	/*
	 * @Override public SelectionHandler<Integer> getSelectionHandler() { //
	 * TODO Auto-generated method stub return null; }
	 */

	private void refresh() {
		if (!isPaused) {

			startProcessing();
			FilterCriteriaDTO filter = new FilterCriteriaDTO();
			filter.setSinceId(lastUpdateId);
			filter.setUpdatesType(this.getUpdatesType());
			// Let's update the tweets
			try {
				getServiceManager().getRPCService().getTwitterUpdates(
						twitterAccount, filter, new TwitterUpdatesLoaded());
			} catch (Exception e) {
				endProcessing();
				getMainController().addException(e);
				e.printStackTrace();

			}
		}
	}

	private void notifyViews(Object object) {
		
		if (object!= null && (object instanceof AsyncHandler)) {
			AsyncHandler handler = (AsyncHandler)object;
			handler.onSuccess(object);
		}
	}

	@Override
	public void handleAction(String action, final Object... args) {

		if (action.equals(IController.IActions.TWEET_THIS)) {

			// Window.alert("Here");

			if (args[0] != null && args[0] instanceof TwitterUpdateDTO) {
				TwitterUpdateDTO update = (TwitterUpdateDTO) args[0];
				//update.setTwitterAccount(this.twitterAccount);

				try {
					startProcessing();
					getServiceManager().getRPCService().postUpdate(update,
							new AsyncCallback<TwitterUpdateDTO>() {

								@Override
								public void onFailure(Throwable caught) {
									endProcessing();
									notifyViews(args[args.length-1]);
									getMainController().addException(caught);

								}

								@Override
								public void onSuccess(TwitterUpdateDTO result) {
									endProcessing();
									notifyViews(args[args.length-1]);
									getParentController()
											.handleAction(
													IController.IActions.UPDATE_LAST_UPDATE,
													result);
									handleTweetedUpdate(result);

								}

							});
				} catch (Exception e) {
					endProcessing();
					getMainController().addException(e);
					e.printStackTrace();
				}
			}

		}
		if (action.equals(IController.IActions.CHANGE_PAGE_SIZE)) {
			// Window.alert(args[0].toString());
			// Try to conver to int
			updatesPerPage = Integer.valueOf(args[0].toString());
			adjustPageSize();
		}
		if (action.equals(IController.IActions.REFRESH_TWEETS)) {
			refresh();

		}
		if (action.equals(IController.IActions.ENABLE_AUTO_UPDATE)) {
			// Create the timmer
			if (timerForAutoUpdates == null) {
				timerForAutoUpdates = new AutoUpdatesUpdateTimer();
			}

			timerForAutoUpdates.scheduleRepeating(1000 * timeBeforeAutoUpdate);

		}

		if (action.equals(IController.IActions.DISABLE_AUTO_UPDATE)) {
			// Create the timmer

			if (timerForAutoUpdates != null) {
				timerForAutoUpdates.cancel();
			}
		}
		if (action.equals(IController.IActions.PAUSE_AUTO_UPDATE)) {
			this.isPaused = true;
		}
		if (action.equals(IController.IActions.RESUME_AUTO_UPDATE)) {
			this.isPaused = false;
		}

	}

	/**
	 * Check the current sise and update accordingly
	 */
	private void adjustPageSize() {
		// TODO Auto-generated method stub
		// With the subtraction I'm removing the widget that represents the
		// tools (errrr)
		int widgetCount = updatesView.getWidgetCount() - 1;
		if (widgetCount > updatesPerPage) {
			// Remove any remaining update
			// int updatesToRemove = updatesPerPage-newPageSize;
			for (int i = widgetCount; i > updatesPerPage; i--) {
				TwitterUpdateWidget updateWidget = (TwitterUpdateWidget) updatesView
						.getWidget(i);
				updates.remove(updateWidget.getTwitterUpdate().getId());
				updateWidgets.remove(updateWidget.getTwitterUpdate().getId());
				updatesView.remove(i);

			}
			assert (updates.size() == updatesPerPage);
			assert ((updatesView.getWidgetCount() - 1) == updatesPerPage);
		}

		// updatesPerPage = newPageSize;

	}

	private class AutoUpdatesUpdateTimer extends Timer {

		@Override
		public void run() {

			refresh();

		}

	}

	@Override
	public void init() {
		updatesView = new TwitterUpdatesView();
		updatesView.setController(this);
		updatesView.init();
		//this.view = this.updatesView;

		FilterCriteriaDTO filter = new FilterCriteriaDTO();
		filter.setUpdatesType(this.getUpdatesType());

		try {
			getServiceManager().getRPCService().getTwitterUpdates(
					twitterAccount, filter, new TwitterUpdatesLoaded());
			startProcessing();
		} catch (Exception e) {
			getMainController().addException(e);
			e.printStackTrace();
		}
		// Load friends time line

	}

	public void setTwitterAccount(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountDTO getTwitterAccount() {
		return twitterAccount;
	}

	/**
	 * Call Back to handle loading of twitter updates
	 * 
	 * @author jpereira
	 * 
	 */
	private final class TwitterUpdatesLoaded implements
			AsyncCallback<List<TwitterUpdateDTO>> {

		@Override
		public void onFailure(Throwable caught) {
			getMainController().addException(caught);
			endProcessing();

		}

		@Override
		public void onSuccess(List<TwitterUpdateDTO> result) {
			handleDataLoaded(result);

		}

	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		this.isPaused = true;

	}

	@Override
	public void resume() {
		this.isPaused = false;
	}

}
