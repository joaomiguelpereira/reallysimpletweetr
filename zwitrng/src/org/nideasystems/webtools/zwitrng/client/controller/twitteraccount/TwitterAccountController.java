package org.nideasystems.webtools.zwitrng.client.controller.twitteraccount;

import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.controller.TwitterAccountOperationCallBack;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.controller.users.TwitterUsersController;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.TwitterUserInfoWidget;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.UsersWindow;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.client.view.updates.ShowStatusWindow;
import org.nideasystems.webtools.zwitrng.client.view.users.TwitterAccountView;
import org.nideasystems.webtools.zwitrng.client.view.users.TwitterUsersView;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserType;

import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TwitterAccountController extends
		AbstractController<TwitterAccountDTO, TwitterAccountView> {

	private Map<String, TwitterUserDTO> extendedUsersInfo = null;

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

	public void finishTwitterLogin(String pinCode) {
		startProcessing();
		assert (getParentController() instanceof PersonaController);
		
		PersonaDTO personaDto = ((PersonaController) getParentController())
				.getModel();

		try {
			getServiceManager().getRPCService().authenticateUser(personaDto,
					pinCode, new AsyncCallback<TwitterAccountDTO>() {

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

		PersonaDTO persona = MainController.getInstance().getCurrentPersonaController().getModel();
		try {
			getServiceManager().getRPCService().postUpdate(persona,twitterUpdate,
					new AsyncCallback<TwitterUpdateDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							GWT.log("received error from postUpdate", caught);
							getMainController().addException(caught);
							if (instance != null) {
								instance.onFailure(caught);
							}

						}

						@Override
						public void onSuccess(TwitterUpdateDTO result) {
							getView().updateLastStatus(result);
							if (instance != null) {
								instance.onSuccess(result);
							}

						}

					});
		} catch (Exception e) {
			GWT.log("Error Calling postUpdate", e);
			getMainController().addException(e);
		}

	}

	/**
	 * Get from service extended information about a Twitter User
	 * 
	 * @param accountIdOrScreenName
	 * @param callback
	 */
	public void getExtendedUserAccount(final String accountIdOrScreenName,
			final TwitterAccountOperationCallBack callback) {

		PersonaDTO currentPersona = MainController.getInstance().getCurrentPersonaController().getModel();
		if (this.extendedUsersInfo != null
				&& this.extendedUsersInfo.containsKey(accountIdOrScreenName)) {
			callback.onTwitterAccountLoadSuccess(this.extendedUsersInfo
					.get(accountIdOrScreenName));
		} else {
			try {
				getServiceManager().getRPCService().getUserInfo(
						currentPersona, accountIdOrScreenName,
						new AsyncCallback<TwitterUserDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								getMainController().addException(caught);
								callback.onTwitterAccountLoadError(caught
										.getMessage());
								GWT.log("Error calling service", caught);
							}

							@Override
							public void onSuccess(TwitterUserDTO result) {
								if (extendedUsersInfo == null) {
									extendedUsersInfo = new HashMap<String, TwitterUserDTO>();
								}
								extendedUsersInfo.put(accountIdOrScreenName,
										result);
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
	 * 
	 * @param follow
	 *            true to follow, false to unfollow
	 * @param id
	 *            the id of the user to follow/unfollow
	 * @param callback
	 *            the callback to be notified
	 */
	public void followUser(Integer id,
			final TwitterAccountOperationCallBack callback) {
		TwitterUserDTO user = new TwitterUserDTO();
		user.setId(id);
		
		PersonaDTO currentPersona = MainController.getInstance().getCurrentPersonaController().getModel();
		
		try {
			getServiceManager().getRPCService().followUser(currentPersona,
					user, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							GWT.log("error returned from followUser method",
									caught);
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
	public void unfollowUser(Integer id,
			final TwitterAccountOperationCallBack callback) {
		TwitterUserDTO user = new TwitterUserDTO();
		user.setId(id);
		
		PersonaDTO currentPersona = MainController.getInstance().getCurrentPersonaController().getModel();
		
		try {
			getServiceManager().getRPCService().unfollowUser(currentPersona,
					user, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							GWT.log("error returned from followUser method",
									caught);
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
	 * 
	 * @param block
	 * @param id
	 * @param callback
	 */
	public void blockUser(final boolean block, final Integer id,
			final TwitterUserInfoWidget callback) {

		TwitterUserDTO user = new TwitterUserDTO();
		user.setId(id);
		PersonaDTO currentPersona = MainController.getInstance().getCurrentPersonaController().getModel();
		try {
			getServiceManager().getRPCService().blockUser(currentPersona, user, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							GWT.log("Error returned from service", caught);
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

	public void showNewFriends(String twitterScreenName) {
		TwitterUserFilterDTO filter = new TwitterUserFilterDTO();
		filter.setType(TwitterUserType.FRIENDS);
		filter.setTwitterUserScreenName(twitterScreenName);

		UsersWindow friendsWindow = new UsersWindow(this, filter);
		friendsWindow.show();
		friendsWindow.isUpdating(true);
		// loadFriends(friendsWindow, filter);
	}

	public void loadFriends(final TwitterUsersController callback,
			TwitterUserFilterDTO currentFilter) {

		try {
			getServiceManager().getRPCService().getUsers(
					MainController.getInstance().getCurrentPersonaController()
							.getModel(), currentFilter,
					new AsyncCallback<TwitterUserDTOList>() {

						@Override
						public void onFailure(Throwable caught) {
							callback.onLoadError(caught);
						}

						@Override
						public void onSuccess(TwitterUserDTOList result) {
							callback.onLoadSuccess(result);

						}

					});
		} catch (Exception e) {
			callback.onLoadError(e);

		}

	}

	/*
	 * public void loadTwitterUpdate(final ShowStatusWindow showStatusWindow,
	 * String tweetId) {
	 * 
	 * FilterCriteriaDTO filter = new FilterCriteriaDTO();
	 * 
	 * filter.setStatusId(Long.parseLong(tweetId));
	 * filter.setUpdatesType(UpdatesType.SINGLE); //
	 * filter.setUniqueResult(true);
	 * 
	 * try { getServiceManager().getRPCService().getTwitterUpdates(getModel(),
	 * filter, new AsyncCallback<TwitterUpdateDTOList>() {
	 * 
	 * @Override public void onFailure(Throwable caught) {
	 * showStatusWindow.onError(caught);
	 * 
	 * }
	 * 
	 * @Override public void onSuccess(TwitterUpdateDTOList result) {
	 * showStatusWindow.onSuccess(result);
	 * 
	 * }
	 * 
	 * }); } catch (Exception e) { showStatusWindow.onError(e); }
	 * 
	 * }
	 */
	public void loadTwitterConversation(
			final ShowStatusWindow showStatusWindow, long id, UpdatesType type) {
		FilterCriteriaDTO filter = new FilterCriteriaDTO();

		filter.setStatusId(id);
		filter.setUpdatesType(type);
		// filter.setUniqueResult(true);
		PersonaDTO persona = MainController.getInstance().getCurrentPersonaController().getModel();

		try {
			getServiceManager().getRPCService().getTwitterUpdates(persona,
					filter, new AsyncCallback<TwitterUpdateDTOList>() {

						@Override
						public void onFailure(Throwable caught) {
							showStatusWindow.onError(caught);

						}

						@Override
						public void onSuccess(TwitterUpdateDTOList result) {
							showStatusWindow.onSuccess(result);

						}

					});
		} catch (Exception e) {
			showStatusWindow.onError(e);
		}

		// TODO Auto-generated method stub

	}

	public void sendUpdate(TwitterUpdateDTO twitterUpdate) {
		this.sendUpdate(twitterUpdate, null);

	}

	public void synchronize(final TwitterUsersController twitterUsersController) {

		try {
			getServiceManager().getRPCService().synchronizeTwitterAccount(
					MainController.getInstance().getCurrentPersonaController()
							.getModel(),
					new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							twitterUsersController.onSynchronizeError(caught);
						}

						@Override
						public void onSuccess(Void result) {
							twitterUsersController.onSynchronizeSuccess();

						}

					});
		} catch (Exception e) {
			twitterUsersController.onSynchronizeError(e);

		}

	}

	public void sendDM(TwitterUpdateDTO twitterUpdate, final SendUpdateWidget instance) {
		PersonaDTO persona = MainController.getInstance().getCurrentPersonaController().getModel();
		try {
			getServiceManager().getRPCService().sendDM(persona,twitterUpdate,
					new AsyncCallback<TwitterUpdateDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							GWT.log("received error from postUpdate", caught);
							getMainController().addException(caught);
							if (instance != null) {
								instance.onFailure(caught);
							}

						}

						@Override
						public void onSuccess(TwitterUpdateDTO result) {
							getView().updateLastStatus(result);
							if (instance != null) {
								instance.onSuccess(result);
							}

						}

					});
		} catch (Exception e) {
			GWT.log("Error Calling postUpdate", e);
			getMainController().addException(e);
		}
		
	}

}
