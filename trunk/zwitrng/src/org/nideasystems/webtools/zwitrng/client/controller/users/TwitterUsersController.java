package org.nideasystems.webtools.zwitrng.client.controller.users;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;

import org.nideasystems.webtools.zwitrng.client.view.users.TwitterUsersView;

import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserType;

import com.google.gwt.user.client.Window;

public class TwitterUsersController extends AbstractController<TwitterUserDTOList, TwitterUsersView>{

	
	
	@Override
	public void init() {
		
		TwitterUserDTOList model = new TwitterUserDTOList();
		TwitterUserFilterDTO defaultFilter = new TwitterUserFilterDTO();
		defaultFilter.setPage(1);
		defaultFilter.setCount(100);
		defaultFilter.setType(TwitterUserType.FRIENDS);
		defaultFilter.setTwitterUserScreenName(MainController.getInstance().getCurrentPersonaController().getModel().getTwitterAccount().getTwitterScreenName());
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
		
		MainController.getInstance().getCurrentPersonaController().getTwitterAccountController().loadFriends(this, getModel().getFilter());
		
		
		
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

}
