package org.nideasystems.webtools.zwitrng.client.controller.twitteraccount;

import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.TwitterAccountOperationCallBack;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.TwitterAccountView;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.TwitterUserInfoWidget;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TwitterAccountController extends
		AbstractController<TwitterAccountDTO, TwitterAccountView> {

	private Map<Integer, TwitterAccountDTO> extendedUsersInfo = null;

	@Override
	public void init() {

		super.init();
		// Create the view for the TwitterAccount
		setView(new TwitterAccountView());
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
		assert (getParentController() instanceof PersonaController);
		PersonaDTO personaDto = ((PersonaController) getParentController())
				.getModel();

		try {
			getServiceManager().getRPCService().authenticateUser(personaDto,
					new AsyncCallback<TwitterAccountDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							GWT.log("retuned an error from service call",
									caught);
							getMainController().addException(caught);
							endProcessing();
							getView().onAuthenticationError();
						}

						@Override
						public void onSuccess(TwitterAccountDTO result) {
							((PersonaController) getParentController())
									.getModel().setTwitterAccount(result);
							setModel(result);
							getView().onAuthenticationSuccess();
							if (!((PersonaController) getParentController())
									.hasTwitterUpdatesListControllerInitialized()) {
								((PersonaController) getParentController())
										.initializeUpdatesListController();
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

	public SendUpdateWidget createSendUpdateWidget(
			TwitterUpdateDTO inResponseTo, int type, boolean showUserImg) {
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
			getServiceManager().getRPCService().postUpdate(twitterUpdate,
					new AsyncCallback<TwitterUpdateDTO>() {

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

	public void getExtendedUserAccount(final Integer accountId,
			final TwitterAccountOperationCallBack callback) {

		if (this.extendedUsersInfo != null
				&& this.extendedUsersInfo.containsKey(accountId)) {
			callback.onTwitterAccountLoadSuccess(this.extendedUsersInfo.get(accountId));
		} else {
			try {
				getServiceManager().getRPCService().getExtendedUser(
						this.getModel(), accountId,
						new AsyncCallback<TwitterAccountDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								getMainController().addException(caught);
								callback.onTwitterAccountLoadError(caught.getMessage());
								GWT.log("Error calling service", caught);
							}

							@Override
							public void onSuccess(TwitterAccountDTO result) {
								if (extendedUsersInfo == null ) {
									extendedUsersInfo = new HashMap<Integer, TwitterAccountDTO>();
								}
								extendedUsersInfo.put(accountId, result);
								callback.onTwitterAccountLoadSuccess(result);

							}

						});
			} catch (Exception e) {
				getMainController().addException(e);
				GWT.log("Error calling service", e);
			}

		}
	}

	/**
	 * Follow/Ufollow a User
	 * @param follow true to follow, false to unfollow
	 * @param id the id of the user to follow/unfollow
	 * @param callback the callback to be notified
	 */
	public void followUser(boolean follow, Integer id,
			final TwitterAccountOperationCallBack callback) {

		try {
			getServiceManager().getRPCService().followUser(this.getModel(),follow, id, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("error returned from followUser method", caught);
					getMainController().addException(caught);
					callback.onFollowUserError(caught.getMessage());
					
				}

				@Override
				public void onSuccess(Void result) {
					callback.onFollowUserSuccess(result);
					
				}
				
			});
		} catch (Exception e) {
			GWT.log("error calling followUser method", e);
			getMainController().addException(e);
			callback.onFollowUserError(e.getMessage());
		}
		
	}

	/**
	 * Block/unblock user
	 * @param block
	 * @param id
	 * @param callback
	 */
	public void blockUser(final boolean block, final Integer id,
			final TwitterUserInfoWidget callback) {
		
		try {
			getServiceManager().getRPCService().blockUser(getModel(), block, id, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Error returned from service" , caught);
					getMainController().addException(caught);
					callback.onBlockUserError(caught.getMessage());
					
				}

				@Override
				public void onSuccess(Void result) {
					callback.onBlockUserSuccess(result);
					
				}
				
			});
		} catch (Exception e) {
		GWT.log("Error calling service", e);
			getMainController().addException(e);
		}
		
	}

}