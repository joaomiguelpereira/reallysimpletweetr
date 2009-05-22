package org.nideasystems.webtools.zwitrng.client.controller.updates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdatesView;
import org.nideasystems.webtools.zwitrng.client.view.widgets.TwitterUpdateWidget;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TwitterUpdatesController extends AbstractController {


	private TwitterAccountDTO twitterAccount;
	private UpdatesType updatesType;
	private TwitterUpdatesView updatesView = null;	
	private Map<Long, TwitterUpdateDTO> updates = new HashMap<Long, TwitterUpdateDTO>();
	

	public UpdatesType getUpdatesType() {
		return updatesType;
	}

	public void setUpdatesType(UpdatesType updatesType) {
		this.updatesType = updatesType;
	}

	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleDataLoaded(Object result) {
		List<TwitterUpdateDTO> twitterUpdates = (List<TwitterUpdateDTO>)result;
		//Now start building the TwitterUpdateWidget
		assert(result!=null);
		
		for ( TwitterUpdateDTO update: twitterUpdates) {
			TwitterUpdateWidget updateWidget = new TwitterUpdateWidget();
			updateWidget.setController(this);
			updateWidget.setTwitterUpdate(update);
			updateWidget.setStyleName("twitterUpdate");			
			updateWidget.init();
			
			updatesView.add(updateWidget);
			updates.put(update.getId(), update);
			
		}
		Window.alert("Dont mess with me:)");
	}

	@Override
	public AsyncCallback<String> getDataRemovedCallBack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleAction(String action, Object... args) {
		Window.alert("handleAction");
		
		
	}

	@Override
	public void init() {
		updatesView = new TwitterUpdatesView();
		updatesView.setController(this);
		updatesView.init();
		this.view = this.updatesView;
		Window.alert("going to initialize now");
		
		
		try {
			getServiceManager().getRPCService().getTwitterUpdates(this.getUpdatesType(),twitterAccount, new TwitterUpdatesLoaded());
		} catch (Exception e) {
			getErrorHandler().addException(e);
			e.printStackTrace();
		}
		
		//Load friends time line
		
	}

	public void setTwitterAccount(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountDTO getTwitterAccount() {
		return twitterAccount;
	}
	/**
	 * Call Back to handle loading of twitter updates
	 * @author jpereira
	 *
	 */
	private final class TwitterUpdatesLoaded implements  AsyncCallback<List<TwitterUpdateDTO>> {

		@Override
		public void onFailure(Throwable caught) {
			getErrorHandler().addException(caught);
			
		}

		@Override
		public void onSuccess(List<TwitterUpdateDTO> result) {
			handleDataLoaded(result);
			
		}
		
	}
	
}
