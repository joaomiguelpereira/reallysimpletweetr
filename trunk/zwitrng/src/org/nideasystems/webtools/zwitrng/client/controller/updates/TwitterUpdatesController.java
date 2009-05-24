package org.nideasystems.webtools.zwitrng.client.controller.updates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdatesView;
import org.nideasystems.webtools.zwitrng.client.view.widgets.TwitterUpdateWidget;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class TwitterUpdatesController extends AbstractController {

	private TwitterAccountDTO twitterAccount;
	private UpdatesType updatesType;
	private TwitterUpdatesView updatesView = null;
	private Map<Long, TwitterUpdateDTO> updates = new HashMap<Long, TwitterUpdateDTO>();
	private long lastUpdateId = 1;
	private Timer timerForAutoUpdates = null;
	private int timeBeforeAutoUpdate = 10; // Seconds
	private int updatesPerPage = 20; //20 updates in a page

	public UpdatesType getUpdatesType() {
		return updatesType;
	}

	public void setUpdatesType(UpdatesType updatesType) {
		this.updatesType = updatesType;
	}

	@SuppressWarnings("unchecked")
	@Override
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

				TwitterUpdateWidget updateWidget = new TwitterUpdateWidget();
				updateWidget.setController(this);
				updateWidget.setTwitterUpdate(update);
				updateWidget.setStyleName("twitterUpdate");
				updateWidget.init();
				
				if (addOnTop) {
					updatesView.insert(updateWidget, i++);

				} else {
					updatesView.add(updateWidget);
				}

				updates.put(update.getId(), update);

			}

		}
		adjustPageSize();
		endProcessing();
	}

	

	@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	private void refresh() {
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
			getErrorHandler().addException(e);
			e.printStackTrace();

		}
	}

	@Override
	public void handleAction(String action, Object... args) {

		if (action.equals(IController.IActions.CHANGE_PAGE_SIZE)) {
			//Window.alert(args[0].toString());
			//Try to conver to int 
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

	}

	/**
	 * Check the current sise and update accordingly
	 */
	private void adjustPageSize() {
		// TODO Auto-generated method stub
		//With the subtraction I'm removing the widget that represents the tools (errrr)
		int widgetCount = updatesView.getWidgetCount()-1;
		if ( widgetCount > updatesPerPage ) {
			//Remove any remaining update
			//int updatesToRemove = updatesPerPage-newPageSize;
			for ( int i=widgetCount; i>updatesPerPage;i--) {
				TwitterUpdateWidget updateWidget = (TwitterUpdateWidget)updatesView.getWidget(i);
				updates.remove(updateWidget.getTwitterUpdate().getId());
				updatesView.remove(i);
				
				
				
			}
			assert(updates.size()==updatesPerPage);
			assert((updatesView.getWidgetCount()-1)==updatesPerPage);
		}
		
		
		//updatesPerPage = newPageSize;
		
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
		this.view = this.updatesView;
		// Window.alert("going to initialize now");
		FilterCriteriaDTO filter = new FilterCriteriaDTO();
		filter.setUpdatesType(this.getUpdatesType());

		try {
			getServiceManager().getRPCService().getTwitterUpdates(
					twitterAccount, filter, new TwitterUpdatesLoaded());
			startProcessing();
		} catch (Exception e) {
			getErrorHandler().addException(e);
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
			getErrorHandler().addException(caught);
			endProcessing();

		}

		@Override
		public void onSuccess(List<TwitterUpdateDTO> result) {
			handleDataLoaded(result);

		}

	}

	

}
