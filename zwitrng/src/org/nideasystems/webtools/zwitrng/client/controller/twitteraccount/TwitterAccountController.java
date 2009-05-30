package org.nideasystems.webtools.zwitrng.client.controller.twitteraccount;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.TwitterAccountView;
import org.nideasystems.webtools.zwitrng.client.view.widgets.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TwitterAccountController extends AbstractController<TwitterAccountDTO, TwitterAccountView>{

	
	@Override
	public void init() {
		
		super.init();
		//Create the view for the TwitterAccount
		setView(createView(TwitterAccountView.class));
		getView().setController(this);
		getView().init();
		
	
	}

	@Override
	public void handleAction(String action, Object... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

	public void finishTwitterLogin() {
		startProcessing();
		assert(getParentController() instanceof PersonaController );
		PersonaDTO personaDto = ((PersonaController)getParentController()).getModel();
		
		try {
			getServiceManager().getRPCService().authenticateUser(personaDto, new AsyncCallback<TwitterAccountDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("retuned an error from service call", caught);
					getMainController().addException(caught);
					endProcessing();
					getView().onAuthenticationError();
				}

				@Override
				public void onSuccess(TwitterAccountDTO result) {
					((PersonaController)getParentController()).getModel().setTwitterAccount(result);
					setModel(result);
					getView().onAuthenticationSuccess();
					if (!((PersonaController)getParentController()).hasTwitterUpdatesListControllerInitialized()){
						((PersonaController)getParentController()).initializeUpdatesListController();
					}
					endProcessing();
				}
				
			});
		} catch (Exception e) {
			endProcessing();
			getMainController().addException(e);
			GWT.log("Error while calling authenticate User", e);
		}
		
	}

	public SendUpdateWidget createSendUpdateWidget(TwitterUpdateDTO inResponseTo, int type, boolean showUserImg) {
		SendUpdateWidget updateWidget = new SendUpdateWidget();
		updateWidget.setSendingTwitterAccount(getModel());
		updateWidget.setInResponseTo(inResponseTo);
		updateWidget.setType(type);
		updateWidget.setShowUserImage(showUserImg);
		updateWidget.setController(this);
		updateWidget.init();
		return updateWidget;
	}
	

	

	public void sendUpdate(TwitterUpdateDTO twitterUpdate,
			final SendUpdateWidget instance) {
		
		try {
			getServiceManager().getRPCService().postUpdate(twitterUpdate, new AsyncCallback<TwitterUpdateDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("received error from postUpdate", caught);
					getMainController().addException(caught);
					instance.onFailure(caught);
					
					
				}

				@Override
				public void onSuccess(TwitterUpdateDTO result) {
					getView().updateLastStatus(result);
					instance.onSuccess(result);
					
				}
				
			});
		} catch (Exception e) {
			GWT.log("Error Calling postUpdate", e);
			getMainController().addException(e);
		}
		
	}
	

}
