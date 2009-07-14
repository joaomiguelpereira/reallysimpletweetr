package org.nideasystems.webtools.zwitrng.client.view.twitteraccount;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.controller.TwitterAccountOperationCallBack;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;

import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;


import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;


import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TwitterUserInfoWidget extends PopupPanel implements
		HasMouseOverHandlers, HasMouseOutHandlers,
		TwitterAccountOperationCallBack {

	private boolean hasMouseOver = false;

	private TwitterUserInfoWidget instance = this;
	private static final String WIDTH = "550px";
	private static final String HEIGHT = "150px";
	private TwitterAccountController parentController = null;
	private InlineHTML followLink = new InlineHTML();
	private TwitterUserDTO twitterAccount = null;
	private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	private InlineHTML blockLink = new InlineHTML();

	public TwitterUserInfoWidget(String accountIdOrScreenName,
	/* TwitterUpdatesController */TwitterAccountController parentController) {
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.parentController = parentController;
		this.setWidth(WIDTH);

		HorizontalPanel tempPanel = new HorizontalPanel();
		tempPanel.add(waitingImg);
		this.setWidget(tempPanel);
		// this.setWidget(createTopPanel(account));

		/*
		 * this.addMouseOverHandler(new MouseOverHandler() {
		 * 
		 * @Override public void onMouseOver(MouseOverEvent event) {
		 * hasMouseOver = true; if (hidePanelTimer != null) {
		 * hidePanelTimer.cancel(); }
		 * 
		 * }
		 * 
		 * });
		 * 
		 * this.addMouseOutHandler(new MouseOutHandler() {
		 * 
		 * @Override public void onMouseOut(MouseOutEvent event) { hasMouseOver
		 * = false; if (hidePanelTimer == null) { hidePanelTimer = new
		 * HidePanelTime(); } hidePanelTimer.schedule(300); }
		 * 
		 * });
		 */
		// this.twitterAccount = account;
		parentController.getExtendedUserAccount(accountIdOrScreenName, this);
	}

	private VerticalPanel createMainPanel() {

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
				"<span class=\"userName\">"
						+ this.twitterAccount.getTwitterName()
						+ "</span> (<span class=\"userScreenName\">"
						+ this.twitterAccount.getTwitterScreenName()
						+ "</span>)");
		infoPanel.add(userNamePlusUserScreeName);

		if (this.twitterAccount.getTwitterLocation() != null
				&& this.twitterAccount.getTwitterLocation().length() > 0) {

			InlineHTML userLocation = new InlineHTML(
					"Location: <span class=\"userLocation\">"
							+ this.twitterAccount.getTwitterLocation()
							+ "</span>");
			infoPanel.add(userLocation);
		}

		if (this.twitterAccount.getTwitterWeb() != null
				&& this.twitterAccount.getTwitterWeb().length() > 1) {

			InlineHTML userUrl = new InlineHTML("<a href=\""
					+ this.twitterAccount.getTwitterWeb()
					+ "\" target=\"_blank\" class=\"userWeb\">"
					+ this.twitterAccount.getTwitterWeb() + "</a>");
			userUrl.addStyleName("link");
			userUrl.setTitle("Click to open the user page in a new window");
			infoPanel.add(userUrl);
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

		InlineHTML following = new InlineHTML("<a href=\"http://twitter.com/"+this.twitterAccount.getTwitterScreenName()+"/friends\" target=\"_blank\">"+this.twitterAccount
				.getTwitterFriends().toString()+"</a>");
		
		InlineHTML followers = new InlineHTML("<a href=\"http://twitter.com/"+this.twitterAccount.getTwitterScreenName()+"/followers\" target=\"_blank\">"+this.twitterAccount
				.getTwitterFollowers().toString()+"</a>");
		
		/*InlineHTML followers = new InlineHTML(this.twitterAccount
				.getTwitterFollowers().toString());*/
		
		
		
		InlineHTML updates = new InlineHTML(this.twitterAccount
				.getTwitterUpdates().toString());

		following.setTitle("Click to see who "
				+ this.twitterAccount.getTwitterScreenName() + " is following");
		followers.setTitle("Click to see who's following "
				+ this.twitterAccount.getTwitterScreenName());
		updates.setTitle("Click to see the updates for "
				+ this.twitterAccount.getTwitterScreenName());

		//following.addStyleName("link");
		//followers.addStyleName("link");
		updates.addStyleName("link");
		
		updates.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			/*	FilterCriteriaDTO newFilter = MainController.getInstance().getCurrentPersonaController().getTwitterUpdatesListController().getActiveUpdatesController().getCurrentFilter();
				newFilter.setSearchText("from:"+twitterAccount.getTwitterScreenName());
				MainController.getInstance().getCurrentPersonaController().getTwitterUpdatesListController().getActiveUpdatesController().setCurrentFilter(newFilter);*/
				hide(true);
				MainController.getInstance().getCurrentPersonaController().getTwitterUpdatesListController().activateSearch("from:"+twitterAccount.getTwitterScreenName());
			}
			
		});
		
		
				

		bottomLayout.setWidget(0, 0, following_label);
		bottomLayout.setWidget(0, 1, following);
		bottomLayout.setWidget(0, 2, followers_label);
		bottomLayout.setWidget(0, 3, followers);
		bottomLayout.setWidget(0, 4, updates_label);
		bottomLayout.setWidget(0, 5, updates);
		extendedUserInfo.add(bottomLayout);
		
		if (!this.twitterAccount.getTwitterScreenName().equals(
				parentController.getModel().getTwitterScreenName())) {
			HorizontalPanel optionPannel = new HorizontalPanel();

			optionPannel.addStyleName("user_options");

			// Add follow/unfollow user link
			setupFollowLink(this.twitterAccount.isImFollowing());
			followLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					waitingImg.setVisible(true);
					if ( twitterAccount.isImFollowing() ) {
						parentController.unfollowUser(
								twitterAccount.getId(), instance);	
					} else {
						parentController.followUser(
								twitterAccount.getId(), instance);	
					}
					
				}

			});
			optionPannel.add(followLink);

			// Add block/unblok
			setupBlockLink(this.twitterAccount.isImBlocking());
			blockLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					waitingImg.setVisible(true);
					parentController.blockUser(!twitterAccount.isImBlocking(),
							twitterAccount.getId(), instance);

				}

			});
			optionPannel.add(blockLink);

			// Start Set Up the Send Private Message Link
			if (twitterAccount.isMutualFriendShip()) {
				InlineHTML sendPm = new InlineHTML("Send private message");
				sendPm.addStyleName("link");
				sendPm.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						
						SendUpdateWidget sendUpdateWidget = new SendUpdateWidget();
						sendUpdateWidget.setController(parentController);
						sendUpdateWidget
								.setSendingTwitterAccount(parentController
										.getModel());
						sendUpdateWidget.setShowUserImage(true);
						sendUpdateWidget
								.setInResponseToUserAccount(twitterAccount);
						sendUpdateWidget
								.setType(SendUpdateWidget.PRIVATE_MESSAGE);
						sendUpdateWidget.init();

						SendPrivateMessageWindow sendPrivateMessageWindow = new SendPrivateMessageWindow(
								twitterAccount, sendUpdateWidget);
						sendPrivateMessageWindow.setAnimationEnabled(true);
						sendPrivateMessageWindow.show();
					}

				});

				optionPannel.add(sendPm);
			}
			// End Set Up the Send Private Message Link

			InlineHTML groups = new InlineHTML("Groups");
			groups.addStyleName("link");

			optionPannel.add(groups);
			optionPannel.add(waitingImg);
			waitingImg.setVisible(false);
			optionPannel.setSpacing(5);
			extendedUserInfo.add(optionPannel);
		} else {
			InlineHTML showMeNewFollowers = new InlineHTML("New followers");
			HorizontalPanel optionPannel = new HorizontalPanel();
			optionPannel.add(showMeNewFollowers);
			showMeNewFollowers.addStyleName("link");
			
			showMeNewFollowers.addClickHandler(new ClickHandler()  {

				@Override
				public void onClick(ClickEvent event) {
					parentController.showNewFriends(twitterAccount.getTwitterScreenName());
					hide(true);
				}
				
			});
			
			
			
			extendedUserInfo.add(optionPannel);
			
			 
		}
			

		mainPanel.add(topPanel);
		mainPanel.add(userDescription);
		mainPanel.add(extendedUserInfo);
		return mainPanel;
	}

	/**
	 * Create block/unblock link
	 * 
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
	 * 
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
		this.hide(true);

	}

	@Override
	public void onTwitterAccountLoadSuccess(TwitterUserDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
		this.setWidget(createMainPanel());

	}

	@Override
	public void onBlockUserError(String error) {
		waitingImg.setVisible(false);

	}

	@Override
	public void onBlockUserSuccess(Object result) {
		waitingImg.setVisible(false);
		twitterAccount.setImBlocking(
				!twitterAccount.isImBlocking());
		setupBlockLink(twitterAccount.isImBlocking());
	}

	@Override
	public void onFollowUserError(String error) {
		// Window.alert("Follow fail "+error);
		waitingImg.setVisible(false);
	}

	@Override
	public void onFollowUserSuccess(Object result) {
		waitingImg.setVisible(false);
		twitterAccount.setImFollowing(
				!twitterAccount.isImFollowing());
		setupFollowLink(twitterAccount.isImFollowing());

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
