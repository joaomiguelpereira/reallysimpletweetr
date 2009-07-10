package org.nideasystems.webtools.zwitrng.client.view.users;

import java.util.ArrayList;
import java.util.List;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.controller.users.TwitterUsersController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;

import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
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
					isUpdating(true);
					// int actualPage =
					// getController().getModel().getFilter().getPage();
					getController().getModel().getFilter()
							.setPage(
									getController().getModel().getFilter()
											.getPage() + 1);
					getController().reload();
					contents.clear();
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
					isUpdating(true);
					// int actualPage =
					// getController().getModel().getFilter().getPage();
					getController().getModel().getFilter()
							.setPage(
									getController().getModel().getFilter()
											.getPage() - 1);
					getController().reload();
					contents.clear();
				}

			});
		}

		// InlineHTML nextPageLink = new InlineHTML("Next page");

		/*
		 * if ( actualPage>1 && pageLinks.size()<actualPage-1) {
		 * 
		 * 
		 * for (int i=pageLinks.size();i<actualPage-1;i++) { Hyperlink pageLink
		 * = new Hyperlink(); pageLink.setText(""+(i+1));
		 * pageLink.setTitle("Go to Page "+(i+1));
		 * pageLink.addStyleName("link"); final int page = i+1;
		 * pageLink.addClickHandler(new ClickHandler() {
		 * 
		 * @Override public void onClick(ClickEvent event) { isUpdating(true);
		 * //int actualPage = getController().getModel().getFilter().getPage();
		 * getController().getModel().getFilter().setPage(page);
		 * getController().reload(); contents.clear(); } });
		 * pageLinks.add(pageLink);
		 * 
		 * } }
		 */
		if (actualPage > 1) {
			bottomToolbar.add(previousPageLink);

		}
		/*
		 * for (Hyperlink pageLink: pageLinks) {
		 * Window.alert("ActualPage:"+actualPage);
		 * Window.alert("Link Page:"+Integer.valueOf(pageLink.getText()));
		 * 
		 * if ( actualPage == Integer.valueOf(pageLink.getText()) ) {
		 * 
		 * bottomToolbar.add(new InlineHTML(""+actualPage)); } else {
		 * bottomToolbar.add(pageLink); }
		 * 
		 * }
		 */
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
				TwitterInfoListItem item = new TwitterInfoListItem(user);
				contents.add(item);
			}
		}
		updatePaging();
	}

	public class TwitterInfoListItem extends VerticalPanel {

		public TwitterInfoListItem(TwitterAccountDTO user) {
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
		//	HTML line = new HTML("&nbsp;");
		//	this.add(line);
			this.add(new InlineHTML(buildStatusLine(user.getTwitterStatusText())));
			
			// add Info
			this.add(buildUserPopularityPanel(user));
			
			this.add(buildToolsPanel(user));

			this.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);
			this.setHeight(Constants.EDITABLE_TEMPLATE_MIN_HEIGHT);
			this.addStyleName("list_item");

		}

		private Widget buildToolsPanel(TwitterAccountDTO user) {
			HorizontalPanel panel = new HorizontalPanel();
			panel.setSpacing(4);
			
			InlineHTML followAction = new InlineHTML();
			if ( user.getExtendedUserAccount().isImFollowing() ) {
				followAction.setText("Stop following");
			} else {
				followAction.setText("Start following");
			}
			
			return null;
		}

		private String buildStatusLine(String status) {
			return "<span class=\"inlineStatus\"><span class=\"label\">Last status:</span> "+StringUtils.jsParseText(status)+"</span>";
			
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
			
			float ratio = Float.valueOf(user.getTwitterFriends())/Float.valueOf(user.getTwitterFollowers());
			InlineHTML followingFollowersRatio = new InlineHTML(StringUtils.formatPercentage(ratio, 2));
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
					MainController.getInstance().getCurrentPersonaController().getTwitterUpdatesListController().activateSearch("from:"+user.getTwitterScreenName());
					
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
