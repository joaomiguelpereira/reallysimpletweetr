package org.nideasystems.webtools.zwitrng.client.view.widgets;

import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * This is a wifget rendering a single update from
 * 
 * @author jpereira
 * 
 */
public class TwitterUpdateWidget extends AbstractVerticalPanelView implements
		HasMouseOverHandlers, HasMouseOutHandlers {

	private TwitterAccountDTO twitterAccount;
	private TwitterUpdateDTO twitterUpdate;
	private final SendUpdateWidget sendUpdate = new SendUpdateWidget();
	private boolean isSendUpdateVisible = false;
	private final HorizontalPanel actionPanel = new HorizontalPanel();

	private Image userImg = null;

	// final private HorizontalPanel tweetQuickOptions = new HorizontalPanel();

	public TwitterUpdateWidget() {
		super();
		super.setWidth("700px");

	}

	private HTML getUpdateTextHtml(TwitterUpdateDTO twitterUpdate) {
		return new HTML("<span class=\"userScreenName\">"
				+ twitterUpdate.getTwitterAccount().getTwitterScreenName()
				+ " </span><span class=\"text\">" + twitterUpdate.getText()
				+ "</span>");
	}

	private HTML getUpdateMetaInfoHtml(TwitterUpdateDTO twitterUpdate) {
		return new HTML("<span class=\"createdAt\">"
				+ twitterUpdate.getCreatedAt()
				+ "<span> from <span class=\"source\">"
				+ twitterUpdate.getSource() + "</span>");

	}

	@Override
	public void init() {
		FlexTable tweetLayout = new FlexTable();
		FlexCellFormatter tweetLayoutFormatter = tweetLayout
				.getFlexCellFormatter();
		tweetLayout.setWidth("700px");

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
		tweetInfo.setWidth("650px");

		HTML updateText = getUpdateTextHtml(twitterUpdate);
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
		this.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				addStyleName("currentTweet");
				getController().handleAction(
						IController.IActions.PAUSE_AUTO_UPDATE);
				// actionPanel.setVisible(true);

			}

		});
		this.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				removeStyleName("currentTweet");
				getController().handleAction(
						IController.IActions.RESUME_AUTO_UPDATE);
				// actionPanel.setVisible(false);

			}

		});
		sendUpdate.setController(getController());
		sendUpdate.setSendingTwitterAccount(twitterAccount);

		sendUpdate.init();
		sendUpdate.setVisible(false);

		this.add(sendUpdate);

		retweet.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (sendUpdate.getType() == SendUpdateWidget.RETWEET
						&& sendUpdate.isVisible()) {
					isSendUpdateVisible = false;

				} else {
					isSendUpdateVisible = true;
				}
				sendUpdate.setVisible(isSendUpdateVisible);
				sendUpdate.setType(SendUpdateWidget.RETWEET);
				sendUpdate.setInResponseTo(twitterUpdate);

				sendUpdate.refresh();

			}

		});

		reply.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (sendUpdate.getType() == SendUpdateWidget.REPLY
						&& sendUpdate.isVisible()) {
					isSendUpdateVisible = false;

				} else {
					isSendUpdateVisible = true;
				}
				sendUpdate.setVisible(isSendUpdateVisible);
				sendUpdate.setType(SendUpdateWidget.REPLY);
				sendUpdate.setInResponseTo(twitterUpdate);

				sendUpdate.refresh();

			}

		});

	}

	public void hasReply(TwitterUpdateDTO result) {

		HorizontalPanel replyPannel = new HorizontalPanel();
		
		Image userImg = new Image(result.getTwitterAccount()
				.getTwitterImageUrl());

		
		userImg.setWidth("32px");
		userImg.setHeight("32px");
		userImg.addStyleName("userImageReply");
		replyPannel.add(userImg);
		//replyPannel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		

		FlexTable layout = new FlexTable();
		layout.setCellSpacing(0);
		layout.setCellSpacing(0);
		
		HTML updateText = getUpdateTextHtml(result);
		
		updateText.addStyleName("tweetReply");
		layout.setWidget(0, 0, updateText);

		HTML updateMetaInfoHtml = getUpdateMetaInfoHtml(result);
		
		updateMetaInfoHtml.addStyleName("tweetReplyMetadata");
		
		layout.setWidget(1, 0, updateMetaInfoHtml);
		
		replyPannel.add(layout);
		
		this.add(replyPannel);
		this.sendUpdate.setVisible(false);
	}

	@Override
	public void setVisible(boolean visible) {
		this.isSendUpdateVisible = false;
		super.setVisible(visible);
	}

	public void setTwitterUpdate(TwitterUpdateDTO twitterUpdate) {
		this.twitterUpdate = twitterUpdate;
	}

	public TwitterUpdateDTO getTwitterUpdate() {
		return twitterUpdate;
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		Window.alert("Is Updating");

	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());

	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {

		return addDomHandler(handler, MouseOutEvent.getType());
	}

	public void setTwitterAccount(TwitterAccountDTO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountDTO getTwitterAccount() {
		return twitterAccount;
	}

}
