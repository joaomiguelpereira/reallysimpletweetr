package org.nideasystems.webtools.zwitrng.client.view.updates;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.SendUpdateAsyncHandler;

import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.SendPrivateMessageWindow;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;

import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
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
	// private TwitterUpdatesController parentController;
	private TwitterAccountController parentController;
	private HorizontalPanel actionPanel = new HorizontalPanel();
	private final HorizontalPanel sendUpdateContainer = new HorizontalPanel();
	private SendUpdateWidget sendUpdateWidget = null;
	private boolean showConversationEnabled = false;

	// private ShowUserInfoPanelTimer showUserInfoWidgetTimer = null;
	// private static Image refLeftImage = null;

	public boolean isShowConversationEnabled() {
		return showConversationEnabled;
	}

	public void setShowConversationEnabled(boolean showConversationEnabled) {
		this.showConversationEnabled = showConversationEnabled;
	}

	private Image userImg = null;

	// final private HorizontalPanel tweetQuickOptions = new HorizontalPanel();

	public void init() {
		this.setWidth(Constants.MAIN_LIST_ITEM_WIDTH);
		
		FlexTable tweetLayout = new FlexTable();
		FlexCellFormatter tweetLayoutFormatter = tweetLayout
				.getFlexCellFormatter();
		//tweetLayout.setWidth("630px");
		
		// Create the user image
		userImg = new Image(twitterUpdate.getTwitterUser().getTwitterImageUrl());
		userImg.setWidth(Constants.USER_IMAGE_MEDIUM_WIDTH);
		userImg.setHeight(Constants.USER_IMAGE_MEDIUM_HEIGHT);

		// if ( refLeftImage == null ) {
		// refLeftImage = userImg;
		// }
		
		tweetLayout.setWidget(0, 0, userImg);
		tweetLayoutFormatter.setWidth(0, 0, "50px");
		
		// Create the update Text
		// TODO: Parse html
		VerticalPanel tweetInfo = new VerticalPanel();
		//tweetInfo.setWidth("630px");

		Widget updateText = getUpdateTextWidget(twitterUpdate);
		//updateText.addStyleName("list_item");
		
		tweetInfo.add(updateText);

		HTML tweetUpdateMetaData = getUpdateMetaInfoHtml(twitterUpdate,
				showConversationEnabled);
		tweetUpdateMetaData.addStyleName("twitterUpdateMetaData");
		tweetInfo.add(tweetUpdateMetaData);

		tweetLayout.setWidget(0, 1, tweetInfo);

		// Create Reguldate action panel
		if (twitterUpdate.getType().equals(UpdatesType.DIRECT_RECEIVED)
				|| twitterUpdate.getType().equals(UpdatesType.DIRECT_SENT)) {
			actionPanel = createDMActionPanel();
		} else {
			actionPanel = createDefaultActionPanel();
		}

		/*tweetLayout.setWidget(1, 0, actionPanel);
		tweetLayoutFormatter.setStyleName(1, 0, "tweetOptions");
		tweetLayoutFormatter.setHeight(1, 0, "25px");
		tweetLayoutFormatter.setAlignment(1, 0,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);*/
		
		super.add(tweetLayout);
		
		super.add(actionPanel);
		super.setCellWidth(actionPanel, Constants.MAIN_LIST_ITEM_WIDTH);
		super.setCellHorizontalAlignment(actionPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		super.add(sendUpdateContainer);
		sendUpdateContainer.setVisible(false);
		addStyleName("list_item");
		this.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				addStyleName("list_item_over");
				// parentController.pause();

				MainController.getInstance().getCurrentPersonaController()
						.getTwitterUpdatesListController()
						.getActiveUpdatesController().pause();

			}

		});
		this.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				removeStyleName("list_item_over");
				// parentController.resume();
				MainController.getInstance().getCurrentPersonaController()
						.getTwitterUpdatesListController()
						.getActiveUpdatesController().resume();

			}

		});

		// setupUserPanel();

	}

	private HorizontalPanel createDMActionPanel() {
		HorizontalPanel tmpPanel = new HorizontalPanel();
		
		
		if (this.twitterUpdate.getInReplyToUserId() == this.parentController
				.getModel().getId()) {
			HTML reply = new HTML("Reply");
			tmpPanel.setSpacing(5);

			tmpPanel.add(reply);

			reply.addStyleName("link");

			/***********
			 * Add Reply functionality
			 */

			reply.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					showSendDM(twitterUpdate.getTwitterUser(),
							SendUpdateWidget.PRIVATE_MESSAGE);
				}

			});

		}
		return tmpPanel;
	}

	private HorizontalPanel createDefaultActionPanel() {
		HorizontalPanel tmpPanel = new HorizontalPanel();
		HTML retweet = new HTML("Retweet");
		HTML reply = new HTML("Reply");
		HTML markRead = new HTML("Hide");
		markRead.addStyleName("link");
		tmpPanel.setSpacing(5);
		tmpPanel.add(retweet);
		tmpPanel.add(reply);
		
		retweet.addStyleName("link");
		reply.addStyleName("link");

		if (showConversationEnabled) {
			final HTML showConversation = new HTML("Show Conversation");

			showConversation.addStyleName("link");
			showConversation.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					MainController.getInstance().getPopupManager()
							.showConversation(twitterUpdate.getId(),
									showConversation.getAbsoluteTop() + 20);

				}

			});

			tmpPanel.add(showConversation);
		}
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
		tmpPanel.add(markRead);
		markRead.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setVisible(false);
				removeFromParent();
				MainController.getInstance().getCurrentPersonaController()
				.getTwitterUpdatesListController()
				.getActiveUpdatesController().resume();
				
			}
			
		});
		return tmpPanel;

	}

	public TwitterUpdateWidget(
			/* TwitterUpdatesController theParentController */TwitterAccountController theParentController,
			TwitterUpdateDTO aTwitterUpdateDTO) {
		super();
		publishNativeJSCode(this);

		this.parentController = theParentController;
		// this.twitterAccount = twitterAccount;
		this.twitterUpdate = aTwitterUpdateDTO;
		super.setWidth("630px");

		if (twitterUpdate.getInReplyToStatusId() > 0
				|| twitterUpdate.getInReplyToUserId() > 0) {
			this.setShowConversationEnabled(true);
		}

	}

	/**
	 * Publish javascript native functions
	 * 
	 * @param twitterUpdateWidget
	 * 
	 * @param result
	 */

	private native void publishNativeJSCode(
			TwitterUpdateWidget twitterUpdateWidget) /*-{
		$wnd.showUserPanel = function (username, elementId) {

			@org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdateWidget::jsShowTwitterUserInfoPanel(Ljava/lang/String;Ljava/lang/String;)(username,elementId); 

		};

		$wnd.hideUserPanel = function (elementId) {


			@org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdateWidget::jsHideTwitterUserInfoPanel(Ljava/lang/String;)(elementId); 

		};
		$wnd.processHashTag = function(hash) {

			@org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdateWidget::jsOpenSearch(Ljava/lang/String;)(hash);
		}

		$wnd.showStatus = function(id, elId) {
			//$wnd.alert(elId);
			@org.nideasystems.webtools.zwitrng.client.view.updates.TwitterUpdateWidget::jsShowStatus(Ljava/lang/String;Ljava/lang/String;)(id,elId);
		}
	}-*/;

	public static void jsShowStatus(String id, String elId) {

		Element el = DOM.getElementById(elId);
		assert (el != null);
		MainController.getInstance().getPopupManager().showStatus(
				Long.parseLong(id), el.getAbsoluteTop() + 20);
	}

	public static void jsHideTwitterUserInfoPanel(String id) {
		MainController.getInstance().getPopupManager().destroyUserPopupPanel();
	}

	public static void jsOpenSearch(String hash) {

		MainController.getInstance().getCurrentPersonaController()
				.getTwitterUpdatesListController().activateSearch(hash);

	}

	public static void jsShowTwitterUserInfoPanel(String screenName, String id) {
		Element el = DOM.getElementById(id);

		MainController.getInstance().getPopupManager()
				.showDelayedUserInfoPopup(0, el.getAbsoluteTop() + 20,
						screenName);

	}

	private void showSendDM(TwitterUserDTO twitterAccount, int type) {

		SendUpdateWidget sendUpdateWidget = new SendUpdateWidget();
		sendUpdateWidget.setController(parentController);
		sendUpdateWidget.setSendingTwitterAccount(parentController.getModel());
		sendUpdateWidget.setShowUserImage(true);
		sendUpdateWidget.setInResponseToUserAccount(twitterAccount);
		sendUpdateWidget.setType(SendUpdateWidget.PRIVATE_MESSAGE);
		sendUpdateWidget.init();

		SendPrivateMessageWindow sendPrivateMessageWindow = new SendPrivateMessageWindow(
				twitterAccount, sendUpdateWidget);
		sendPrivateMessageWindow.setAnimationEnabled(true);
		sendPrivateMessageWindow.show();

	}

	/**
	 * Show the send update widget
	 * 
	 * @param type
	 */
	private void showSendUpdate(int type) {

		if (sendUpdateWidget == null) {
			// Create new
			/*
			 * sendUpdateWidget = parentController.getTwitterAccountController()
			 * .createSendUpdateWidget(twitterUpdate, type, true);
			 */
			assert (parentController != null);
			assert (twitterUpdate != null);

			sendUpdateWidget = parentController.createSendUpdateWidget(
					twitterUpdate, type, true);
			// if (twitterAccount != null) {
			// sendUpdateWidget.setInResponseToUserAccount(twitterAccount);
			// }
			sendUpdateContainer.add(sendUpdateWidget);
			sendUpdateWidget.addAsyncHandler(this);
			sendUpdateContainer.setVisible(true);
			sendUpdateWidget.refresh();
		} else if (sendUpdateWidget.getType() != type) {
			sendUpdateWidget.setType(type);
			sendUpdateWidget.refresh();
		}

	}

	/**
	 * Helper method to get the UpdateText as a widget
	 * 
	 * @param twitterUpdate
	 * @return
	 */
	private Widget getUpdateTextWidget(final TwitterUpdateDTO twitterUpdate) {

		FlowPanel update = new FlowPanel();

		final InlineHTML screenName = new InlineHTML();

		final InlineHTML updateText = new InlineHTML();

		screenName.setHTML("<span class=\"userScreenName\">"
				+ twitterUpdate.getTwitterUser().getTwitterScreenName()
				+ " </span>");

		// in the update find http:// or https://


		/*
		 * String htmlText =
		 * helper.getParsedUpdateHtml(twitterUpdate.getText());
		 */

		String htmlText = StringUtils.jsParseText(twitterUpdate.getText());
		updateText.setHTML("<span class=\"text\">" + htmlText + "</span>");

		// updateText.addStyleName("html-noBlock");

		update.add(screenName);
		update.add(updateText);

		screenName.addStyleName("link");
		screenName.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				MainController.getInstance().getPopupManager()
						.showDelayedUserInfoPopup(
								screenName.getAbsoluteLeft(),
								screenName.getAbsoluteTop() + 20,
								twitterUpdate.getTwitterUser()
										.getTwitterScreenName());
				// createUserPopupPanel(screenName.getAbsoluteLeft(), screenName
				// .getAbsoluteTop() + 20,null);

			}

		});

		return update;

	}

	/**
	 * get the updateMetaInfo HTML widget
	 * 
	 * @param twitterUpdate
	 * @return
	 */
	private HTML getUpdateMetaInfoHtml(TwitterUpdateDTO twitterUpdate,
			boolean showConversationOptions) {
		return new HTML(HTMLHelper.get().getParsedMetaDataHtml(twitterUpdate,
				showConversationOptions));

	}

	/**
	 * Publish javascript native functions
	 * 
	 * @param result
	 */
	/*
	 * 
	 * private native void publishNativeJSCode(TwitterUpdateWidget instance) -{
	 * $wnd.showUserPanel = function (username) {
	 * instance.@org.nideasystems.webtools
	 * .zwitrng.client.view.updates.TwitterUpdateWidget
	 * ::jsShowTwitterUserInfoPanel(Ljava/lang/String;)(username);
	 * 
	 * }; }-;
	 * 
	 * 
	 * public void jsShowTwitterUserInfoPanel(String screenName) {
	 * Window.alert(screenName);
	 * parentController.getTwitterAccountController().getExtendedUserAccount
	 * (screenName, this);
	 * Window.alert("ID:"+this.twitterUpdate.getTwitterAccount().getId()); }
	 */

	private void hasReply(TwitterUpdateDTO result) {

		HorizontalPanel replyPannel = new HorizontalPanel();

		Image userImg = new Image(result.getTwitterUser().getTwitterImageUrl());

		userImg.setWidth("32px");
		userImg.setHeight("32px");
		userImg.addStyleName("userImageReply");
		replyPannel.add(userImg);
		// replyPannel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		FlexTable layout = new FlexTable();
		layout.setCellSpacing(0);
		layout.setCellSpacing(0);

		Widget updateText = getUpdateTextWidget(result);

		updateText.addStyleName("tweetReply");
		layout.setWidget(0, 0, updateText);

		HTML updateMetaInfoHtml = getUpdateMetaInfoHtml(result,
				showConversationEnabled);

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
		assert (update != null);
		assert (this.twitterUpdate != null);
		if (update.getInReplyToStatusId() == this.twitterUpdate.getId()) {
			hasReply(update);
		}
		Window.alert("on sucess");
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
