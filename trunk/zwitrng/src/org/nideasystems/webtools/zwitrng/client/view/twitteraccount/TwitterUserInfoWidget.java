package org.nideasystems.webtools.zwitrng.client.view.twitteraccount;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.TwitterAccountOperationCallBack;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesController;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

public class TwitterUserInfoWidget extends PopupPanel implements
		HasMouseOverHandlers, HasMouseOutHandlers, TwitterAccountOperationCallBack {

	private boolean hasMouseOver = false;
	private Timer hidePanelTimer = null;
	private TwitterUserInfoWidget instance = this;
	private static final String WIDTH = "550px";
	private static final String HEIGHT = "150px";
	private TwitterUpdatesController parentController = null;
	private InlineHTML followLink = new InlineHTML();
	private TwitterAccountDTO twitterAccount= null;
	private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	private InlineHTML blockLink = new InlineHTML();

	public TwitterUserInfoWidget(TwitterAccountDTO account,
			TwitterUpdatesController parentController) {
		this.setAnimationEnabled(true);
		this.parentController = parentController;
		this.setWidth(WIDTH);
		
		HorizontalPanel tempPanel = new HorizontalPanel();
		tempPanel.add(waitingImg);
		this.setWidget(tempPanel);
		// this.setWidget(createTopPanel(account));

		this.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				hasMouseOver = true;
				if (hidePanelTimer != null) {
					hidePanelTimer.cancel();
				}

			}

		});

		this.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				hasMouseOver = false;
				if (hidePanelTimer == null) {
					hidePanelTimer = new HidePanelTime();
				}
				hidePanelTimer.schedule(300);
			}

		});
		this.twitterAccount = account;
		parentController.getExtendedUserInfo(account.getId(), this);
	}

	private VerticalPanel createTopPanel() {
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSpacing(2);
		mainPanel.setWidth(WIDTH);
		// mainPanel.setHeight(HEIGHT);

		HorizontalPanel topPanel = new HorizontalPanel();
		topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Image userImg = new Image(this.twitterAccount.getTwitterImageUrl());
		userImg.setWidth(Constants.USER_IMAGE_MEDIUM_WIDTH);
		userImg.setHeight(Constants.USER_IMAGE_MEDIUM_HEIGHT);
		topPanel.add(userImg);

		VerticalPanel infoPanel = new VerticalPanel();
		infoPanel.setSpacing(0);

		InlineHTML userNamePlusUserScreeName = new InlineHTML(
				"<span class=\"userName\">" + this.twitterAccount.getTwitterName()
						+ "</span> (<span class=\"userScreenName\">"
						+ this.twitterAccount.getTwitterScreenName() + "</span>)");
		infoPanel.add(userNamePlusUserScreeName);

		if (this.twitterAccount.getTwitterLocation() != null
				&& this.twitterAccount.getTwitterLocation().length() > 0) {

			InlineHTML userLocation = new InlineHTML(
					"Location: <span class=\"userLocation\">"
							+ this.twitterAccount.getTwitterLocation() + "</span>");
			infoPanel.add(userLocation);
		}

		if (this.twitterAccount.getTwitterWeb() != null
				&& this.twitterAccount.getTwitterWeb().length() > 1) {

			InlineHTML userWeb = new InlineHTML("<a href=\""
					+ this.twitterAccount.getTwitterWeb()
					+ "\" target=\"_blank\" class=\"userWeb\">"
					+ this.twitterAccount.getTwitterWeb() + "</a>");
			userWeb.addStyleName("link");
			infoPanel.add(userWeb);
		}

		topPanel.add(infoPanel);
		InlineHTML userDescription = new InlineHTML("<span class=\"userBio\">"
				+ this.twitterAccount.getTwitterDescription() + "</span>");

		VerticalPanel extendedUserInfo = new VerticalPanel();

		
		extendedUserInfo.setWidth(WIDTH);
		FlexTable bottomLayout = new FlexTable();

		// HorizontalPanel activity = new HorizontalPanel();
		InlineHTML following_label = new InlineHTML("Following: ");
		InlineHTML followers_label = new InlineHTML("Followers: ");
		InlineHTML updates_label = new InlineHTML("Updates: ");

		InlineHTML following = new InlineHTML(this.twitterAccount.getTwitterFriends()
				.toString());
		InlineHTML followers = new InlineHTML(this.twitterAccount.getTwitterFollowers()
				.toString());
		InlineHTML updates = new InlineHTML(this.twitterAccount.getTwitterUpdates()
				.toString());

		following.setTitle("Click to see who " + this.twitterAccount.getTwitterScreenName()
				+ " is following");
		followers.setTitle("Click to see who's following "
				+ this.twitterAccount.getTwitterScreenName());
		updates.setTitle("Click to see the updates for "
				+ this.twitterAccount.getTwitterScreenName());

		following.addStyleName("link");
		followers.addStyleName("link");
		updates.addStyleName("link");

		bottomLayout.setWidget(0, 0, following_label);
		bottomLayout.setWidget(0, 1, following);
		bottomLayout.setWidget(0, 2, followers_label);
		bottomLayout.setWidget(0, 3, followers);
		bottomLayout.setWidget(0, 4, updates_label);
		bottomLayout.setWidget(0, 5, updates);
		
		

		// Add follow/unfollow user link
		setupFollowLink(this.twitterAccount.getExtendedUserAccount().isImFollowing());		
		followLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				waitingImg.setVisible(true);
				parentController.followUser(!twitterAccount.getExtendedUserAccount().isImFollowing(), twitterAccount.getId(), instance);
			}
			
		});

		// Add block/unblok
		setupBlockLink(this.twitterAccount.getExtendedUserAccount().isImBlocking());
		blockLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				waitingImg.setVisible(true);
				parentController.blockUser(!twitterAccount.getExtendedUserAccount().isImBlocking(), twitterAccount.getId(), instance);
				
				
			}
			
		});
		
		InlineHTML sendPm = new InlineHTML("Send private message");
		sendPm.addStyleName("style");
		InlineHTML groups = new InlineHTML("Groups");
		groups.addStyleName("link");
		HorizontalPanel optionPannel = new HorizontalPanel();
		
		
		optionPannel.addStyleName("user_options");
		optionPannel.add(followLink);
		optionPannel.add(blockLink);
		optionPannel.add(sendPm);
		optionPannel.add(groups);
		optionPannel.add(waitingImg);
		waitingImg.setVisible(false);
		optionPannel.setSpacing(5);
		
		
		/*
		 * activity.setSpacing(5); activity.add(following);
		 * activity.add(followers); activity.add(updates);
		 */
		extendedUserInfo.add(bottomLayout);
		extendedUserInfo.add(optionPannel);
		mainPanel.add(topPanel);
		mainPanel.add(userDescription);
		mainPanel.add(extendedUserInfo);
		return mainPanel;
	}

	/**
	 * Create block/unblock link
	 * @param isBlocking
	 */
	private void setupBlockLink(boolean isBlocking) {
		if (isBlocking) {
			blockLink.setHTML("Unblock");
			blockLink.setTitle("Click to unblock"
					+ this.twitterAccount.getTwitterScreenName());

		} else {
			blockLink.setHTML("Block");
			blockLink.setTitle("Click to block "
					+ this.twitterAccount.getTwitterScreenName());

		}
		blockLink.addStyleName("link");
		
	}
	/**
	 * Set up the Follow/unfollow link
	 * @param isFollowing
	 */
	private void setupFollowLink(boolean isFollowing) {
		if (isFollowing) {
			followLink.setText("Stop following");
			followLink.setTitle("Click to stop following "
					+ this.twitterAccount.getTwitterScreenName());

		} else {
			followLink.setText("Start following");
			followLink.setTitle("Click to start following "
					+ this.twitterAccount.getTwitterScreenName());

		}
		followLink.addStyleName("link");
	}
	/**
	 * Timer to hide the panel
	 * 
	 * @author jpereira
	 * 
	 */
	private class HidePanelTime extends Timer {

		@Override
		public void run() {

			instance.hide(true);

		}

	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());

	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	@Override
	public void hide(boolean autoClosed) {
		if (!hasMouseOver) {
			super.hide(autoClosed);
		}

	}

	@Override
	public void onTwitterAccountLoadError(String error) {
		// Ignore because error has been handled in controller, hopefully

	}

	@Override
	public void onTwitterAccountLoadSuccess(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
		this.setWidget(createTopPanel());

	}

	@Override
	public void onBlockUserError(String error) {
		waitingImg.setVisible(false);
		
	}

	@Override
	public void onBlockUserSuccess(Object result) {
		waitingImg.setVisible(false);
		twitterAccount.getExtendedUserAccount().setImBlocking(!twitterAccount.getExtendedUserAccount().isImBlocking());
		setupBlockLink(twitterAccount.getExtendedUserAccount().isImBlocking());
	}

	@Override
	public void onFollowUserError(String error) {
		//Window.alert("Follow fail "+error);
		waitingImg.setVisible(false);
	}

	@Override
	public void onFollowUserSuccess(Object result) {
		waitingImg.setVisible(false);
		twitterAccount.getExtendedUserAccount().setImFollowing(!twitterAccount.getExtendedUserAccount().isImFollowing());
		setupFollowLink(twitterAccount.getExtendedUserAccount().isImFollowing());
		
	}

	@Override
	public void onPrivateMessageError(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPrivateMessageSuccess(Object result) {
		// TODO Auto-generated method stub
		
	}

}