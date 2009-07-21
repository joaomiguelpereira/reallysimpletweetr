package org.nideasystems.webtools.zwitrng.client.view.users;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.controller.users.TwitterUsersController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.SendPrivateMessageWindow;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;

import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserType;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

public class TwitterUsersView extends
		AbstractVerticalPanelView<TwitterUsersController> {

	Image waitingImg = new Image(Constants.WAITING_IMAGE);
	VerticalPanel contents;
	HorizontalPanel bottomToolbar;
	Hyperlink nextPageLink;
	Hyperlink previousPageLink;


	@Override
	public void init() {

		waitingImg.setVisible(false);
		this.add(waitingImg);
		final AutoFollowConfigurationPanel autoFollowPanel = new AutoFollowConfigurationPanel();
		DisclosurePanel optionsPanel = new DisclosurePanel("Configure auto follower");
		optionsPanel.setAnimationEnabled(true);
		optionsPanel.setContent(autoFollowPanel);
		optionsPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {

			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				autoFollowPanel.loadRule();
				
			}
			
		});
		
		this.add(optionsPanel);
		this.add(new TwitterUsersViewToolBar(this));
		HorizontalPanel filterPanel = new HorizontalPanel();
		filterPanel.setSpacing(5);

		// Types of users
		final ListBox userTypeList = new ListBox(false);
		userTypeList.addItem(TwitterUserType.FRIENDS.type(),
				TwitterUserType.FRIENDS.toString());
		userTypeList.addItem(TwitterUserType.FOLLOWERS.type(),
				TwitterUserType.FOLLOWERS.toString());
		filterPanel.add(userTypeList);

		// of Label
		Label ofLabel = new Label("of");
		filterPanel.add(ofLabel);

		// for userName
		final TextBox twitterScreenNameTB = new TextBox();
		twitterScreenNameTB.setText(getController().getModel().getFilter()
				.getTwitterUserScreenName());
		filterPanel.add(twitterScreenNameTB);

		Button activateFilter = new Button("Search");

		activateFilter.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isUpdating(true);

				getController().getModel().getFilter().setType(
						TwitterUserType.valueOf(userTypeList
								.getValue(userTypeList.getSelectedIndex())));
				getController().getModel().getFilter()
						.setTwitterUserScreenName(
								twitterScreenNameTB.getValue());
				getController().reload();
				contents.clear();
			}
		});

		filterPanel.add(activateFilter);

		contents = new VerticalPanel();
		this.add(filterPanel);
		this.add(contents);
		bottomToolbar = new HorizontalPanel();
		bottomToolbar.setSpacing(4);
		this.add(bottomToolbar);
	}



	/**
	 * 
	 * @author jpereira
	 * 
	 */
	public class TwitterUsersViewToolBar extends HorizontalPanel {
		TwitterUsersView view;

		public TwitterUsersViewToolBar(TwitterUsersView theView) {
			this.view = theView;
			this.setSpacing(4);
			InlineHTML synch = new InlineHTML("Synchronize");
			synch.addStyleName("link");
			this.add(synch);
			synch.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getController().syncronize();
				}

			});
		}
	}

	

	private void updatePaging() {

		bottomToolbar.clear();
		final int actualPage = getController().getModel().getFilter().getPage();

		if (nextPageLink == null) {
			nextPageLink = new Hyperlink();
			nextPageLink.setHTML("Next Page");
			nextPageLink.setTitle("Next Page");
			nextPageLink.addStyleName("link");
			nextPageLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// isUpdating(true);
					// int actualPage =
					// getController().getModel().getFilter().getPage();
					getController().getModel().getFilter()
							.setPage(
									getController().getModel().getFilter()
											.getPage() + 1);
					getController().reload();

				}

			});
		}

		if (previousPageLink == null) {
			previousPageLink = new Hyperlink();
			previousPageLink.setHTML("Previus Page");
			previousPageLink.setTitle("Previus Page");
			previousPageLink.addStyleName("link");
			previousPageLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// isUpdating(true);
					// int actualPage =
					// getController().getModel().getFilter().getPage();
					getController().getModel().getFilter()
							.setPage(
									getController().getModel().getFilter()
											.getPage() - 1);
					getController().reload();

				}

			});
		}

		if (actualPage > 1) {
			bottomToolbar.add(previousPageLink);

		}
		bottomToolbar.add(new InlineHTML("(Page: " + actualPage + ")"));
		bottomToolbar.add(nextPageLink);

	}

	@Override
	public void isUpdating(boolean isUpdating) {
		waitingImg.setVisible(isUpdating);

	}

	public void refresh() {
		if (getController().getModel().getAccounts() != null
				&& getController().getModel().getAccounts().size() > 0) {
			for (TwitterUserDTO user : getController().getModel()
					.getAccounts()) {
				TwitterInfoListItem item = new TwitterInfoListItem(user, this);
				contents.add(item);
			}
		}
		updatePaging();
	}

	public void clear() {
		contents.clear();
	}

	private class TwitterUserToolBar extends HorizontalPanel implements
			UserListener {
		TwitterUserDTO user;
		Image waitingImage = new Image(Constants.WAITING_IMAGE);
		TwitterUsersView view;
		InlineHTML followLink;
		InlineHTML unfollowLink;
		InlineHTML blockUser;
		InlineHTML blockLink;
		InlineHTML unblockLink;
		InlineHTML sendDirectMessageLink;
		Image canSendDMImg = new Image(Constants.SMALL_CHECK_IMG);
		Image imFollowingDMImg = new Image(Constants.SMALL_CHECK_IMG);
		SendUpdateWidget sendUpdateW = null;
		TwitterInfoListItem parentItem;
		

		public TwitterUserToolBar(TwitterUserDTO user, TwitterInfoListItem item) {
			this.user = user;
			this.view = item.view;
			this.parentItem = item;
			this.setSpacing(4);
			waitingImage.setVisible(false);
			this.add(waitingImage);
			this.add(imFollowingDMImg);
			followLink = createFollowActionMenu();
			this.add(followLink);

			unfollowLink = createUnfollowActionMenu();

			this.add(unfollowLink);
			this.add(createBlockActionMenu());
			this.add(createUnblockActionMenu());
			this.add(createRetweetLastMenu());
			this.add(createSendDirectMessageMenu());
			
			this.add(canSendDMImg);
			
			
			
			updateFollowingStatus();
			
			canSendDMImg.setWidth(Constants.MENU_ICON_WIDTH);
			canSendDMImg.setHeight(Constants.MENU_ICON_HEIGH);
			
			

			imFollowingDMImg.setWidth(Constants.MENU_ICON_WIDTH);
			imFollowingDMImg.setHeight(Constants.MENU_ICON_HEIGH);
			
			
			
			
			
			
		
			
		}

		
		private Widget createRetweetLastMenu() {
			InlineHTML retweetLast = new InlineHTML("Retweet last");
			retweetLast.addStyleName("link");
			retweetLast.setTitle("Retweet user status: "+user.getTwitterStatusText());
			
			retweetLast.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					parentItem.showUpdateWidget();
					
					

				}
				
			});
			
			return retweetLast;
		}

		private void updateFollowingStatus() {
			
			if ( user.isMutualFriendShip() && 
					view.getController().getModel().getFilter().getType().equals(TwitterUserType.FRIENDS)) {
				this.canSendDMImg.setVisible(true);
			} else {
				this.canSendDMImg.setVisible(false);
			} 
			
			if (user.isImFollowing() && 
					view.getController().getModel().getFilter().getType().equals(TwitterUserType.FOLLOWERS)) {
				this.imFollowingDMImg.setVisible(true);
			} else {
				this.imFollowingDMImg.setVisible(false);
			}
			
			
		}
		private Widget createSendDirectMessageMenu() {

			this.sendDirectMessageLink = new InlineHTML("Send Direct Message");
			this.sendDirectMessageLink.addStyleName("link");
			this.sendDirectMessageLink.setTitle("Send a Direct Message to "+user.getTwitterScreenName());
			this.sendDirectMessageLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					openSendDirectMessageWindow();

				}

			});
			if (user.isMutualFriendShip()
					|| view.getController().getModel().getFilter().getType() == TwitterUserType.FOLLOWERS) {
				this.sendDirectMessageLink.setVisible(true);
			} else {
				this.sendDirectMessageLink.setVisible(false);
			}

			return this.sendDirectMessageLink;
		}

		private void openSendDirectMessageWindow() {

			SendPrivateMessageWindow wnd = SendPrivateMessageWindow.create(
					MainController.getInstance().getCurrentPersonaController()
							.getTwitterAccountController(), user, SendUpdateWidget.PRIVATE_MESSAGE);
			wnd.show();

		}

		private Widget createUnblockActionMenu() {
			unblockLink = new InlineHTML("Unblock");
			unblockLink.addStyleName("link");
			unblockLink.setTitle("Unblock user "+user.getTwitterScreenName());
			unblockLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					unblockUser();

				}

			});

			if (!user.isImBlocking()) {
				unblockLink.setVisible(false);
			}
			return unblockLink;
		}

		private InlineHTML createBlockActionMenu() {
			blockLink = new InlineHTML("Block");
			blockLink.addStyleName("link");
			blockLink.setTitle("Block user "+user.getTwitterScreenName());

			blockLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					blockUser();

				}

			});

			if (user.isImBlocking()) {
				blockLink.setVisible(false);
			}
			return blockLink;
		}

		private void isProcessing(boolean b) {
			this.waitingImage.setVisible(b);
		}

		private InlineHTML createUnfollowActionMenu() {
			InlineHTML followAction = new InlineHTML();

			followAction.setText("Stop following");
			followAction.setTitle("Stop following "
					+ user.getTwitterScreenName());
			followAction.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					stopFollowing();

				}

			});

			followAction.addStyleName("link");

			if (!user.isImFollowing()) {
				followAction.setVisible(false);
			}

			return followAction;
		}

		private InlineHTML createFollowActionMenu() {
			InlineHTML followAction = new InlineHTML();

			followAction.setText("Start following");
			followAction.setTitle("Start following "
					+ user.getTwitterScreenName());
			followAction.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					startFollowing();

				}

			});

			if (user.isImFollowing()) {
				followAction.setVisible(false);
			}
			followAction.addStyleName("link");
			return followAction;
		}

		private void unblockUser() {
			isProcessing(true);
			view.getController().unblockUser(user, this);

		}

		private void blockUser() {
			isProcessing(true);
			view.getController().blockUser(user, this);

		}

		protected void startFollowing() {
			isProcessing(true);
			view.getController().followUser(user, true, this);

		}

		protected void stopFollowing() {
			isProcessing(true);
			view.getController().stopFollowUser(user, this);

		}

		@Override
		public void onBlockSucess() {
			isProcessing(false);
			user.setImBlocking(true);
			this.unblockLink.setVisible(true);
			this.blockLink.setVisible(false);
			user.setNew(false);
			parentItem.updateIsNew();


		}

		@Override
		public void onError(Throwable tr) {
			isProcessing(false);
			MainController.getInstance().addException(tr);

		}

		@Override
		public void onFollowSucess() {
			isProcessing(false);
			user.setImFollowing(true);
			this.unfollowLink.setVisible(true);
			this.followLink.setVisible(false);
			updateFollowingStatus();
			user.setNew(false);
			parentItem.updateIsNew();


		}

		@Override
		public void onUnFollowSucess() {
			isProcessing(false);
			user.setImFollowing(false);
			
			this.unfollowLink.setVisible(false);
			this.followLink.setVisible(true);
			updateFollowingStatus();
			user.setNew(false);
			parentItem.updateIsNew();
			
		}

		@Override
		public void onUnblockSucess() {
			isProcessing(false);
			user.setImBlocking(false);
			this.unblockLink.setVisible(false);
			this.blockLink.setVisible(true);
			user.setNew(false);
			parentItem.updateIsNew();


		}

	};

	public class TwitterInfoListItem extends VerticalPanel implements
			HasMouseOutHandlers, HasMouseOverHandlers {
		private TwitterUsersView view;
		private SendUpdateWidget sendUpdateWidget;
		private TwitterUserDTO user;
		private HorizontalPanel isNewPanel;
	
		

		public TwitterInfoListItem(TwitterUserDTO user, TwitterUsersView view) {
			this.view = view;
			this.user = user;
			isNewPanel = new HorizontalPanel();
			InlineHTML isNewtText = new InlineHTML("Is New ");
			isNewtText.addStyleName("bolder");
			Image isNewImg = new Image(Constants.SMALL_CHECK_IMG);
			isNewImg.setWidth(Constants.MENU_ICON_WIDTH);	
			isNewImg.setHeight(Constants.MENU_ICON_HEIGH);
			
			
			isNewPanel.add(isNewtText);
			isNewPanel.add(isNewImg);
			this.add(isNewPanel);
			
			FlexTable table = new FlexTable();
			FlexCellFormatter formatter = table.getFlexCellFormatter();

			// User Image
			Image userImg = new Image(user.getTwitterImageUrl());
			userImg.setWidth(Constants.USER_IMAGE_MEDIUM_WIDTH);
			userImg.setHeight(Constants.USER_IMAGE_MEDIUM_HEIGHT);

			table.setWidget(0, 0, userImg);
			formatter.setWidth(0, 0, "50px");
			VerticalPanel userInfo1 = new VerticalPanel();
			userInfo1.add(new InlineHTML(buildUserLineHtml(user)));
			userInfo1.add(new InlineHTML(buildLocationHtml(user)));
			
			//userInfo1.add(userLine)
			userInfo1.add(new InlineHTML(buildUserHomePageHtml(user)));
			
			
			table.setWidget(0, 1, userInfo1);
			this.add(table);

			this.add(new InlineHTML(buildUserBioHtml(user)));
			// HTML line = new HTML("&nbsp;");
			// this.add(line);
			this.add(new InlineHTML(
					buildStatusLine(user.getTwitterStatusText())));

			// add Info
			this.add(buildUserPopularityPanel(user));

			TwitterUserToolBar toolBar = new TwitterUserToolBar(user,this);
			this.add(toolBar);
			this.setCellHorizontalAlignment(toolBar, HasHorizontalAlignment.ALIGN_RIGHT);
			
			this.setWidth(Constants.MAIN_LIST_ITEM_WIDTH);
			this.setHeight(Constants.MAIN_LIST_ITEM_MIN_HEIGHT);
			this.addStyleName("list_item");
			addMouseHandlers();
			updateIsNew();


		}


		public void updateIsNew() {
			if (user.isNew()) {
				isNewPanel.setVisible(true);
			} else {
				isNewPanel.setVisible(false);
			}
			
		}


		public void showUpdateWidget() {
			if (this.sendUpdateWidget == null ) {
				this.sendUpdateWidget = new SendUpdateWidget();
				TwitterAccountController controller = MainController.getInstance().getCurrentPersonaController().getTwitterAccountController();
				sendUpdateWidget.setController(controller);
				sendUpdateWidget
						.setSendingTwitterAccount(controller.getModel());
				sendUpdateWidget.setShowUserImage(true);
				sendUpdateWidget
						.setInResponseToUserAccount(user);
				sendUpdateWidget
						.setType(SendUpdateWidget.RETWEET);
				TwitterUpdateDTO update = new TwitterUpdateDTO();
				update.setText(user.getTwitterStatusText());
				
				update.setTwitterUser(user);
				
				sendUpdateWidget.setInResponseTo(update);
				sendUpdateWidget.init();
				sendUpdateWidget.setVisible(false);
				this.add(sendUpdateWidget);
			}
			sendUpdateWidget.setVisible(!sendUpdateWidget.isVisible());
			sendUpdateWidget.refresh();
			
		}

		private void addMouseHandlers() {
			this.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					addStyleName("list_item_over");

				}

			});
			this.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					removeStyleName("list_item_over");

				}

			});

		}

		private String buildStatusLine(String status) {
			return TwitterUsersHtmlUtils.buildStatusLine(status);
			/*return "<span class=\"inlineStatus\"><span class=\"label\">Last status:</span> "
					+ StringUtils.jsParseText(status) + "</span>";*/

		}

		private Widget buildUserPopularityPanel(final TwitterUserDTO user) {
			return TwitterUsersHtmlUtils.buildUserPopularityPanel(user,true);
			
/*			HorizontalPanel panel = new HorizontalPanel();
			panel.setSpacing(4);
			InlineHTML following_label = new InlineHTML("Following: ");
			InlineHTML followers_label = new InlineHTML("Followers: ");
			InlineHTML updates_label = new InlineHTML("Updates: ");

			InlineHTML followingFollowersRatioLabel = new InlineHTML(
					"Following/Followers Ratio:");

			InlineHTML following = new InlineHTML(
					"<a href=\"http://twitter.com/"
							+ user.getTwitterScreenName()
							+ "/friends\" target=\"_blank\">"
							+ user.getTwitterFriends().toString() + "</a>");

			InlineHTML followers = new InlineHTML(
					"<a href=\"http://twitter.com/"
							+ user.getTwitterScreenName()
							+ "/followers\" target=\"_blank\">"
							+ user.getTwitterFollowers().toString() + "</a>");

			InlineHTML updates = new InlineHTML(user.getTwitterUpdates()
					.toString());

			float ratio = Float.valueOf(user.getTwitterFriends())
					/ Float.valueOf(user.getTwitterFollowers());
			InlineHTML followingFollowersRatio = new InlineHTML(StringUtils
					.formatPercentage(ratio, 2));
			followingFollowersRatio.addStyleName("label");
			panel.add(following_label);
			panel.add(following);

			panel.add(followers_label);
			panel.add(followers);

			panel.add(followingFollowersRatioLabel);
			panel.add(followingFollowersRatio);

			updates.addStyleName("link");
			panel.add(updates_label);
			panel.add(updates);

			updates.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					MainController.getInstance().getCurrentPersonaController()
							.getTwitterUpdatesListController().activateSearch(
									"from:" + user.getTwitterScreenName());

				}

			});

			return panel;
*/
		}

		private String buildUserBioHtml(TwitterUserDTO user) {
			return TwitterUsersHtmlUtils.buildUserBioHtml(user);
			
			
		}

		private String buildUserHomePageHtml(TwitterUserDTO user) {

			return TwitterUsersHtmlUtils.buildUserHomePageHtml(user);
			
					}

		private String buildLocationHtml(TwitterUserDTO user) {
			return TwitterUsersHtmlUtils.buildLocationHtml(user);
			
		}

		private String buildUserLineHtml(TwitterUserDTO user) {
			return TwitterUsersHtmlUtils.buildUserLineHtml(user);

		}

		@Override
		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return addDomHandler(handler, MouseOutEvent.getType());
		}

		@Override
		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());
		}
	}


}
