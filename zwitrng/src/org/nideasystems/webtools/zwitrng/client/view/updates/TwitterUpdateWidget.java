package org.nideasystems.webtools.zwitrng.client.view.updates;

import java.sql.Time;
import java.util.Iterator;

import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesController;
import org.nideasystems.webtools.zwitrng.client.view.SendUpdateAsyncHandler;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.TwitterUserInfoWidget;
import org.nideasystems.webtools.zwitrng.client.view.widgets.SendUpdateWidget;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * This is a wifget rendering a single update from
 * 
 * @author jpereira
 * 
 */
public class TwitterUpdateWidget extends VerticalPanel implements
		HasMouseOverHandlers, HasMouseOutHandlers, SendUpdateAsyncHandler {

	private TwitterUpdateDTO twitterUpdate;
	private TwitterUpdatesController parentController;
	private final HorizontalPanel actionPanel = new HorizontalPanel();
	private final HorizontalPanel sendUpdateContainer = new HorizontalPanel();
	private SendUpdateWidget sendUpdateWidget = null;
	private TwitterUserInfoWidget userInfoPopup = null;
	private Timer keepUserInfoWidgetTimer = null;
	private ShowUserInfoPanelTimer showUserInfoWidgetTimer = null;
	private boolean showUserInfoWidget = false;

	private Image userImg = null;

	// final private HorizontalPanel tweetQuickOptions = new HorizontalPanel();

	public TwitterUpdateWidget(TwitterUpdatesController theParentController, /*
																			 * TwitterAccountDTO
																			 * twitterAccount
																			 * ,
																			 */
			TwitterUpdateDTO twitterUpdateDTO) {
		super();

		this.parentController = theParentController;
		// this.twitterAccount = twitterAccount;
		this.twitterUpdate = twitterUpdateDTO;
		super.setWidth("630px");

		FlexTable tweetLayout = new FlexTable();
		FlexCellFormatter tweetLayoutFormatter = tweetLayout
				.getFlexCellFormatter();
		tweetLayout.setWidth("630px");

		// Create the user image
		userImg = new Image(twitterUpdate.getTwitterAccount()
				.getTwitterImageUrl());
		userImg.setWidth("48px");
		userImg.setHeight("48px");

		tweetLayout.setWidget(0, 0, userImg);
		tweetLayoutFormatter.setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		tweetLayoutFormatter.setRowSpan(0, 0, 1);
		tweetLayoutFormatter.setWidth(0, 1, "50px");

		// Create the update Text
		// TODO: Parse html
		VerticalPanel tweetInfo = new VerticalPanel();
		tweetInfo.setWidth("630px");

		Widget updateText = getUpdateTextHtml(twitterUpdate);
		updateText.addStyleName("tweet");

		tweetInfo.add(updateText);

		HTML tweetUpdateMetaData = getUpdateMetaInfoHtml(twitterUpdate);
		tweetUpdateMetaData.addStyleName("twitterUpdateMetaData");
		tweetInfo.add(tweetUpdateMetaData);

		tweetLayout.setWidget(0, 1, tweetInfo);
		tweetLayoutFormatter.setColSpan(0, 1, 2);
		tweetLayoutFormatter.setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);

		// tweetLayout.setWidget(1, 1, tweetUpdateMetaData);

		// Now add the action

		// HTML followUser = new HTML("unfollow user (do later)");
		HTML retweet = new HTML("Retweet");
		HTML reply = new HTML("Reply");
		HTML moreOptions = new HTML("More options");

		moreOptions.addStyleName("link");
		retweet.addStyleName("link");
		reply.addStyleName("link");

		actionPanel.setSpacing(5);
		actionPanel.add(retweet);
		actionPanel.add(reply);
		actionPanel.add(moreOptions);

		tweetLayout.setWidget(1, 1, actionPanel);
		tweetLayoutFormatter.setStyleName(1, 1, "tweetOptions");
		tweetLayoutFormatter.setHeight(1, 1, "25px");
		tweetLayoutFormatter.setAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		super.add(tweetLayout);
		super.add(sendUpdateContainer);
		sendUpdateContainer.setVisible(false);

		this.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				addStyleName("currentTweet");
				parentController.pause();

			}

		});
		this.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				removeStyleName("currentTweet");
				parentController.resume();

			}

		});

		/***********
		 * Add Retweet functionality
		 */

		retweet.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showSendUpdate(SendUpdateWidget.RETWEET);
			}

		});
		/***********
		 * Add Reply functionality
		 */

		reply.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showSendUpdate(SendUpdateWidget.REPLY);
			}

		});
		setupUserPanel();

	}

	private class HideUserInfoPanelTimer extends Timer {

		@Override
		public void run() {
			if (userInfoPopup != null) {
				userInfoPopup.hide(true);
			}
		}

	}

	private class ShowUserInfoPanelTimer extends Timer {

		int left;
		int top;

		@Override
		public void run() {
			showUserInfoWidget = true;
			createUserPopupPanel(left,top);
		}
	}

	
	private void createUserPopupPanel(int left, int top) {
		if (!showUserInfoWidget) {
			if (showUserInfoWidgetTimer == null) {
				showUserInfoWidgetTimer = new ShowUserInfoPanelTimer();
				showUserInfoWidgetTimer.left = left;
				showUserInfoWidgetTimer.top = top;
				showUserInfoWidgetTimer.schedule(500);

			}
		} else {
			if (userInfoPopup == null) {
				userInfoPopup = new TwitterUserInfoWidget(twitterUpdate
						.getTwitterAccount());

			}
			if (keepUserInfoWidgetTimer != null) {
				keepUserInfoWidgetTimer.cancel();
			}

			userInfoPopup.setPopupPosition(left, top);
			userInfoPopup.show();

		}

	}

	private void destroyUserPopupPanel() {
		if (userInfoPopup != null) {
			if (keepUserInfoWidgetTimer == null) {
				keepUserInfoWidgetTimer = new HideUserInfoPanelTimer();
			}
			keepUserInfoWidgetTimer.schedule(500);
		}
		if ( showUserInfoWidgetTimer!= null) {
			showUserInfoWidgetTimer.cancel();
			showUserInfoWidget = false;
			showUserInfoWidgetTimer = null;
			
			
		}

	}

	private void setupUserPanel() {
		// Add mouse over
		userImg.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				createUserPopupPanel(userImg.getAbsoluteLeft() + 48, userImg
						.getAbsoluteTop());

			}

		});
		userImg.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				destroyUserPopupPanel();
			}

		});
	}

	private void showSendUpdate(int type) {
		if (sendUpdateWidget == null) {
			// Create new
			sendUpdateWidget = parentController.getTwitterAccountController()
					.createSendUpdateWidget(twitterUpdate, type, true);
			sendUpdateContainer.add(sendUpdateWidget);
			sendUpdateWidget.addAsyncHandler(this);
			sendUpdateContainer.setVisible(true);
			sendUpdateWidget.refresh();
		} else if (sendUpdateWidget.getType() != type) {
			sendUpdateWidget.setType(type);
			sendUpdateWidget.refresh();
		}

	}

	private Widget getUpdateTextHtml(TwitterUpdateDTO twitterUpdate) {
	

		FlowPanel update = new FlowPanel();

		final InlineHTML screenName = new InlineHTML();
		

		final InlineHTML updateText = new InlineHTML();

		screenName.setHTML("<span class=\"userScreenName\">"
				+ twitterUpdate.getTwitterAccount().getTwitterScreenName()
				+ " </span>");

		updateText.setHTML("<span class=\"text\">" + twitterUpdate.getText()
				+ "</span>");

		// updateText.addStyleName("html-noBlock");

		update.add(screenName);
		update.add(updateText);

		screenName.addStyleName("link");
		screenName.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createUserPopupPanel(screenName.getAbsoluteLeft(), screenName
						.getAbsoluteTop() + 20);
				
			}
			
		});
		screenName.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				createUserPopupPanel(screenName.getAbsoluteLeft(), screenName
						.getAbsoluteTop() + 20);

			}

		});
		screenName.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				destroyUserPopupPanel();

			}

		});
		return update;

		/*
		 * updateTextPanel.add(screenName); updateTextPanel.add(updateText);
		 * return updateTextPanel;
		 */

		/*
		 * return new HTML("<span class=\"userScreenName\">" +
		 * twitterUpdate.getTwitterAccount().getTwitterScreenName() +
		 * " </span><span class=\"text\">" + twitterUpdate.getText() +
		 * "</span>");
		 */

	}

	private HTML getUpdateMetaInfoHtml(TwitterUpdateDTO twitterUpdate) {
		return new HTML("<span class=\"createdAt\">"
				+ twitterUpdate.getCreatedAt()
				+ "<span> from <span class=\"source\">"
				+ twitterUpdate.getSource() + "</span>");

	}

	/*
	 * private native void publishNativeJSCode(TwitterUpdateWidget instance) -{
	 * $wnd.testA = function () {
	 * instance.@org.nideasystems.webtools.zwitrng.client
	 * .view.updates.TwitterUpdateWidget::doSomething()(); };
	 * 
	 * }-;
	 * 
	 * 
	 * public void doSomething() { Window.alert(twitterUpdate.getText()); }
	 */
	private void hasReply(TwitterUpdateDTO result) {

		HorizontalPanel replyPannel = new HorizontalPanel();

		Image userImg = new Image(result.getTwitterAccount()
				.getTwitterImageUrl());

		userImg.setWidth("32px");
		userImg.setHeight("32px");
		userImg.addStyleName("userImageReply");
		replyPannel.add(userImg);
		// replyPannel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		FlexTable layout = new FlexTable();
		layout.setCellSpacing(0);
		layout.setCellSpacing(0);

		Widget updateText = getUpdateTextHtml(result);

		updateText.addStyleName("tweetReply");
		layout.setWidget(0, 0, updateText);

		HTML updateMetaInfoHtml = getUpdateMetaInfoHtml(result);

		updateMetaInfoHtml.addStyleName("tweetReplyMetadata");

		layout.setWidget(1, 0, updateMetaInfoHtml);

		replyPannel.add(layout);

		this.add(replyPannel);
		// this.sendUpdate.setVisible(false);
	}

	public void setTwitterUpdate(TwitterUpdateDTO twitterUpdate) {
		this.twitterUpdate = twitterUpdate;
	}

	public TwitterUpdateDTO getTwitterUpdate() {
		return twitterUpdate;
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
	public void onFailure(Throwable tr) {
		removeSendUpdate();
	}

	@Override
	public void onSuccess(Object arg) {
		TwitterUpdateDTO update = (TwitterUpdateDTO) arg;
		if (update.getInReplyToStatusId() == this.twitterUpdate.getId()) {
			hasReply(update);
		}

		removeSendUpdate();
	}

	@Override
	public void onCancel() {
		removeSendUpdate();
	}

	private void removeSendUpdate() {
		this.sendUpdateContainer.remove(this.sendUpdateWidget);
		this.sendUpdateContainer.setVisible(false);
		this.sendUpdateWidget = null;

	}

}
