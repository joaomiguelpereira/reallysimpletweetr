package org.nideasystems.webtools.zwitrng.client.controller.users;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;

import org.nideasystems.webtools.zwitrng.client.view.users.TwitterUsersView;
import org.nideasystems.webtools.zwitrng.client.view.users.UserListener;


import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserType;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TwitterUsersController extends
		AbstractController<TwitterUserDTOList, TwitterUsersView> {

	@Override
	public void init() {

		TwitterUserDTOList model = new TwitterUserDTOList();
		TwitterUserFilterDTO defaultFilter = new TwitterUserFilterDTO();
		defaultFilter.setPage(1);
		defaultFilter.setCount(100);
		defaultFilter.setType(TwitterUserType.FRIENDS);
		defaultFilter.setTwitterUserScreenName(MainController.getInstance()
				.getCurrentPersonaController().getModel().getTwitterAccount()
				.getTwitterScreenName());
		model.setFilter(defaultFilter);

		this.setModel(model);
		super.init();
		TwitterUsersView view = new TwitterUsersView();
		view.setController(this);
		view.init();

		setView(view);

	}

	@Override
	public void handleAction(String action, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reload() {
		getView().clear();
		getView().isUpdating(true);
		MainController.getInstance().getCurrentPersonaController()
				.getTwitterAccountController().loadFriends(this,
						getModel().getFilter());

	}

	public void onLoadError(Throwable caught) {
		getView().isUpdating(false);
		MainController.getInstance().addException(caught);

	}

	public void onLoadSuccess(TwitterUserDTOList result) {
		getView().isUpdating(false);
		setModel(result);
		getView().refresh();

	}

	public void syncronize() {
		getView().isUpdating(true);
		MainController.getInstance().getCurrentPersonaController()
				.getTwitterAccountController().synchronize(this);

	}

	public void onSynchronizeError(Throwable caught) {
		getView().isUpdating(false);
		MainController.getInstance().addException(caught);

	}

	public void onSynchronizeSuccess() {
		getView().isUpdating(false);
		reload();

	}

	public void followUser(TwitterUserDTO user, boolean synch, final UserListener callback) {
		PersonaDTO currentPersona = MainController.getInstance()
				.getCurrentPersonaController().getModel();

		try {
			getServiceManager().getRPCService().followUser(currentPersona,
					user, synch, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							callback.onError(caught);

						}

						@Override
						public void onSuccess(Void result) {
							callback.onFollowSucess();

						}

					});
		} catch (Exception e) {
			callback.onError(e);

		}

	}

	public void stopFollowUser(TwitterUserDTO user,
			final UserListener callback) {
		PersonaDTO currentPersona = MainController.getInstance()
				.getCurrentPersonaController().getModel();

		try {
			getServiceManager().getRPCService().unfollowUser(currentPersona,
					user, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							callback.onError(caught);

						}

						@Override
						public void onSuccess(Void result) {
							callback.onUnFollowSucess();

						}

					});
		} catch (Exception e) {
			callback.onError(e);

		}

	}

	public void blockUser(TwitterUserDTO user,
			final UserListener callback) {
		
		PersonaDTO currentPersona = MainController.getInstance()
		.getCurrentPersonaController().getModel();
		try {
			getServiceManager().getRPCService().blockUser(currentPersona,
					user, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							callback.onError(caught);

						}

						@Override
						public void onSuccess(Void result) {
							callback.onBlockSucess();

						}

					});
		} catch (Exception e) {
			callback.onError(e);

		}

	}

	public void unblockUser(TwitterUserDTO user,
			final UserListener callback) {
		
		PersonaDTO currentPersona = MainController.getInstance()
		.getCurrentPersonaController().getModel();
		try {
			getServiceManager().getRPCService().unblockUser(currentPersona,
					user, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							callback.onError(caught);

						}

						@Override
						public void onSuccess(Void result) {
							callback.onUnblockSucess();

						}

					});
		} catch (Exception e) {
			callback.onError(e);

		}

	}

}
