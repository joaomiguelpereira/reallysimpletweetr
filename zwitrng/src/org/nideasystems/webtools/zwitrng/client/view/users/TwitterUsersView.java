package org.nideasystems.webtools.zwitrng.client.view.users;


import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.controller.users.TwitterUsersController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.SendPrivateMessageWindow;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;

import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
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
			for (TwitterAccountDTO user : getController().getModel()
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

	public class TwitterInfoListItem extends VerticalPanel {
		private TwitterUsersView view;

		public TwitterInfoListItem(TwitterAccountDTO user, TwitterUsersView view) {
			this.view = view;
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

			this.add(new TwitterUserToolBar(user, view));

			this.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);
			this.setHeight(Constants.EDITABLE_TEMPLATE_MIN_HEIGHT);
			this.addStyleName("list_item");

		}

		private class TwitterUserToolBar extends HorizontalPanel implements
				UserListener {
			TwitterAccountDTO user;
			Image waitingImage = new Image(Constants.WAITING_IMAGE);
			TwitterUsersView view;
			InlineHTML followLink;
			InlineHTML unfollowLink;
			InlineHTML blockUser;
			InlineHTML blockLink;
			InlineHTML unblockLink;
			InlineHTML sendDirectMessageLink;
			

			public TwitterUserToolBar(TwitterAccountDTO user,
					TwitterUsersView view) {
				this.user = user;
				this.view = view;
				this.setSpacing(4);
				waitingImage.setVisible(false);
				this.add(waitingImage);
				followLink = createFollowActionMenu();
				this.add(followLink);

				unfollowLink = createUnfollowActionMenu();
				
				
				this.add(unfollowLink);
				this.add(createBlockActionMenu());
				this.add(createUnblockActionMenu());
				this.add(createSendDirectMessageMenu());
			}


			private Widget createSendDirectMessageMenu() {

				this.sendDirectMessageLink = new InlineHTML("Send Direct Message");
				this.sendDirectMessageLink.addStyleName("link");
				this.sendDirectMessageLink.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						openSendDirectMessageWindow();
						
					}
					
				});
				if (!user.getExtendedUserAccount().isMutualFriendShip() || view.getController().getModel().getFilter().getType()==TwitterUserType.FOLLOWERS) {
					this.sendDirectMessageLink.setVisible(true);
				} else {
					this.sendDirectMessageLink.setVisible(false);
				}
					
				return this.sendDirectMessageLink;
			}

			private void openSendDirectMessageWindow() {
				
				
				SendPrivateMessageWindow wnd = SendPrivateMessageWindow.create(MainController.getInstance().getCurrentPersonaController().getTwitterAccountController(),user);
//				
//				SendUpdateWidget sendUpdateWidget = new SendUpdateWidget();
//				sendUpdateWidget.setController(MainController.getInstance().getCurrentPersonaController().getTwitterAccountController());
//				sendUpdateWidget
//						.setSendingTwitterAccount(MainController.getInstance().getCurrentPersonaController().getModel().getTwitterAccount());
//				sendUpdateWidget.setShowUserImage(true);
//				sendUpdateWidget
//						.setInResponseToUserAccount(user);
//				sendUpdateWidget
//						.setType(SendUpdateWidget.PRIVATE_MESSAGE);
//				sendUpdateWidget.init();
//
//				SendPrivateMessageWindow sendPrivateMessageWindow = new SendPrivateMessageWindow(
//						MainController.getInstance().getCurrentPersonaController().getModel().getTwitterAccount(), sendUpdateWidget);
//				sendPrivateMessageWindow.setAnimationEnabled(true);
				wnd.show();
				
			}
			
			private Widget createUnblockActionMenu() {
				unblockLink = new InlineHTML("Unblock");
				unblockLink.addStyleName("link");
				
				unblockLink.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						unblockUser();
						
					}

					

					
				});
				
				if (!user.getExtendedUserAccount().isImBlocking()) {
					unblockLink.setVisible(false);
				}
				return unblockLink;
			}
			private InlineHTML createBlockActionMenu() {
				blockLink = new InlineHTML("Block");
				blockLink.addStyleName("link");
				
				blockLink.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						blockUser();
						
					}

					
				});
				
				if (user.getExtendedUserAccount().isImBlocking()) {
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
				
				if (!user.getExtendedUserAccount().isImFollowing() ) {
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

				if (user.getExtendedUserAccount().isImFollowing() ) {
					followAction.setVisible(false);
				}
				followAction.addStyleName("link");
				return followAction;
			}

			private void unblockUser() {
				isProcessing(true);
				view.getController().unblockUser(user,this);
				
			}
			private void blockUser() {
				isProcessing(true);
				view.getController().blockUser(user,this);
				
			}
			protected void startFollowing() {
				isProcessing(true);
				view.getController().followUser(user, this);

			}

			protected void stopFollowing() {
				isProcessing(true);
				view.getController().stopFollowUser(user, this);

			}

			@Override
			public void onBlockSucess() {
				isProcessing(false);
				user.getExtendedUserAccount().setImBlocking(true);
				this.unblockLink.setVisible(true);
				this.blockLink.setVisible(false);
				


			}

			@Override
			public void onError(Throwable tr) {
				isProcessing(false);
				MainController.getInstance().addException(tr);

			}

			@Override
			public void onFollowSucess() {
				isProcessing(false);
				user.getExtendedUserAccount().setImFollowing(true);
				this.unfollowLink.setVisible(true);
				this.followLink.setVisible(false);
				

			}

			@Override
			public void onUnFollowSucess() {
				isProcessing(false);
				user.getExtendedUserAccount().setImFollowing(false);
				this.unfollowLink.setVisible(false);
				this.followLink.setVisible(true);			}



			@Override
			public void onUnblockSucess() {
				isProcessing(false);
				user.getExtendedUserAccount().setImBlocking(false);
				this.unblockLink.setVisible(false);
				this.blockLink.setVisible(true);

				
			}

		};

		private String buildStatusLine(String status) {
			return "<span class=\"inlineStatus\"><span class=\"label\">Last status:</span> "
					+ StringUtils.jsParseText(status) + "</span>";

		}

		

		private Widget buildUserPopularityPanel(final TwitterAccountDTO user) {
			HorizontalPanel panel = new HorizontalPanel();
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

		}

		private String buildUserBioHtml(TwitterAccountDTO user) {
			String userBio = "<span class=\"userBio\">"
					+ user.getTwitterDescription() + "</span>";
			return userBio;
		}

		private String buildUserHomePageHtml(TwitterAccountDTO user) {

			String userUrl = "<a href=\"" + user.getTwitterWeb()
					+ "\" target=\"_blank\" class=\"userWeb\">"
					+ user.getTwitterWeb() + "</a>";

			return userUrl;
		}

		private String buildLocationHtml(TwitterAccountDTO user) {
			String location = "Location: <span class=\"userLocation\">"
					+ user.getTwitterLocation() + "</span>";

			return location;
		}

		private String buildUserLineHtml(TwitterAccountDTO user) {
			String userNamePlusUserScreeName = "<span class=\"userName\">"
					+ user.getTwitterName()
					+ "</span> (<span class=\"userScreenName\">"
					+ user.getTwitterScreenName() + "</span>)";

			return userNamePlusUserScreeName;

		}
	}

}
