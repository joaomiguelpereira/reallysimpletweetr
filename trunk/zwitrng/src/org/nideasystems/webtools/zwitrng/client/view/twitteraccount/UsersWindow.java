package org.nideasystems.webtools.zwitrng.client.view.twitteraccount;


import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.DialogBoxesConstants;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserType;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserFilterDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import com.google.gwt.user.client.ui.VerticalPanel;

public class UsersWindow extends DialogBox  {

	/*private static final String WIDTH = "600px";
	private static final String HEIGHT = "150px";*/
	private TwitterUserFilterDTO currentFilter = null;
	private TwitterAccountController twitterAccountController = null;
	private TwitterUserDTOList model;
	private Image waitingImage = new Image(Constants.WAITING_IMAGE);
	
	public UsersWindow(TwitterAccountController controller, TwitterUserFilterDTO filter) {
		this.setCurrentFilter(filter);
		this.setTwitterAccountController(controller);
		
		this.setTitle(filter.getType()==TwitterUserType.FRIENDS?("Friends of "+currentFilter.getTwitterUserScreenName()):"followers of");
		this.setText(filter.getType()==TwitterUserType.FRIENDS?("Friends of "+currentFilter.getTwitterUserScreenName()):"followers of");
		this.setAnimationEnabled(true);
		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth(DialogBoxesConstants.WIDTH);
		vPanel.setHeight(DialogBoxesConstants.HEIGHT);
		waitingImage.setVisible(false);
		vPanel.add(waitingImage);
		
	
		
		HorizontalPanel filterPanel = new HorizontalPanel();
		filterPanel.setSpacing(5);
		
		//Types of users
		final ListBox userTypeList = new ListBox(false);
		
		userTypeList.addItem(TwitterUserType.FRIENDS.type(),TwitterUserType.FRIENDS.toString());
		userTypeList.addItem(TwitterUserType.FOLLOWERS.type(),TwitterUserType.FOLLOWERS.toString());
		filterPanel.add(userTypeList);
		//of Label
		Label ofLabel = new Label("of");
		filterPanel.add(ofLabel);
		
		//for userName
		final TextBox twitterScreenNameTB = new TextBox();
		twitterScreenNameTB.setText(currentFilter.getTwitterUserScreenName());
		filterPanel.add(twitterScreenNameTB);
		
		Button activateFilter = new Button("Search");
		
		activateFilter.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//Check current filter
				currentFilter.setType(TwitterUserType.valueOf(userTypeList.getValue(userTypeList.getSelectedIndex())));
				currentFilter.setTwitterUserScreenName(twitterScreenNameTB.getValue());
				reload();
			}

			
			
		});
		
		filterPanel.add(activateFilter);
		vPanel.add(filterPanel);
		
		
		HorizontalPanel toolsPanel = new HorizontalPanel();
		InlineHTML closeOption = new InlineHTML("Close");
		closeOption.addStyleName("link");
		closeOption.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide(true);
				
			}
			
		});
		
		
		//add tools panel
		toolsPanel.add(closeOption);
		vPanel.add(toolsPanel);
		this.add(vPanel);
		this.center();
	
		
	}

	private void reload() {
		GWT.log("Reloading for type "+currentFilter.getType().type(), null);
		GWT.log("Reloading for user "+currentFilter.getTwitterUserScreenName(), null);
		//twitterAccountController.loadFriends(this, currentFilter);
		
		
		
	}
	@Override
	public void setTitle(String title) {
		super.setTitle(title);
	}
	
	@Override
	public void show() {
		super.show();
	}

	public void setCurrentFilter(TwitterUserFilterDTO currentFilter) {
		this.currentFilter = currentFilter;
	}

	public TwitterUserFilterDTO getCurrentFilter() {
		return currentFilter;
	}

	public void setTwitterAccountController(TwitterAccountController twitterAccountController) {
		this.twitterAccountController = twitterAccountController;
	}

	public TwitterAccountController getTwitterAccountController() {
		return twitterAccountController;
	}

	public void onLoadError(Throwable caught) {
		twitterAccountController.getMainController().addException(caught);
		isUpdating(false);
		
	}

	public void onLoadSuccess(TwitterUserDTOList result) {
		this.setModel(result);
		isUpdating(false);
		Window.alert("Size "+result.getAccounts().size());
	}

	public void setModel(TwitterUserDTOList model) {
		this.model = model;
	}

	public TwitterUserDTOList getModel() {
		return model;
	}

	public void isUpdating(boolean b) {
		waitingImage.setVisible(b);
		
	}
	
}
