package org.nideasystems.webtools.zwitrng.client.view.updates;

import java.util.ArrayList;
import java.util.List;

import org.nideasystems.webtools.zwitrng.client.Constants;

import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.client.view.SendUpdateAsyncHandler;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

public class SendUpdateWidget extends AbstractVerticalPanelView<TwitterAccountController> implements
		 SendUpdateAsyncHandler {

	public static final int STATUS = 0;
	public static final int REPLY = 1;
	public static final int RETWEET = 2;
	private static final Integer DEFAULT_TWEET_SIZE = 140;

	private boolean showUserImage = true;
	private final HTML remainingChars = new HTML(DEFAULT_TWEET_SIZE.toString());
	private final HTML pub = new HTML("Innovation with twitter :)");
	final TextArea update = new TextArea();
	final Button send = new Button("Send");
	private int type = STATUS;
	private TwitterAccountDTO sendingTwitterAccount;
	private TwitterUpdateDTO inResponseTotwitterUpdate;
	private final Image waitingImage = new Image(Constants.WAITING_IMAGE);
	private SendUpdateWidget instance;
	private List<SendUpdateAsyncHandler> asynHadlers = null;

	@Override
	public void init() {
		instance = this;
		this.add(waitingImage);
		waitingImage.setVisible(false);

		update.setWidth("620px");
		// update.setHeight("35px");
		update.addStyleName("input");
		update.setVisibleLines(2);

		FlexTable bottomLayout = new FlexTable();
		FlexCellFormatter formater = bottomLayout.getFlexCellFormatter();
		bottomLayout.setCellSpacing(0);
		
		Image sendingUserImage = new Image(sendingTwitterAccount
				.getTwitterImageUrl());
		
		sendingUserImage.setWidth("32px");
		sendingUserImage.setHeight("32px");
		sendingUserImage.setVisible(showUserImage);
		bottomLayout.setWidget(0, 0, sendingUserImage);
		HTML cancelLink = new HTML("Cancel");
		
		
		formater.setWidth(0, 0, "48px");
		
		bottomLayout.setWidget(0, 1, update);
		formater.setColSpan(0, 1, 3);
		//formater.setWidth(0, 1, "650px");

		remainingChars.setWidth("35px");
		bottomLayout.setWidget(1, 0, new HTML(""));
		
		bottomLayout.setWidget(1, 1, remainingChars);
		bottomLayout.setWidget(1, 2, pub);
		HorizontalPanel toolsPanel = new HorizontalPanel();
		toolsPanel.setSpacing(4);
		toolsPanel.add(cancelLink);
		toolsPanel.add(send);
		
		bottomLayout.setWidget(1, 3, toolsPanel);
		//bottomLayout.setWidget(1, 4, send);
		

		formater.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
	
		formater.setAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		
		
		formater.setAlignment(1, 2, HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		
		formater.setAlignment(1, 3, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		
		/*formater.setAlignment(1, 4, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);*/

		// Add handlers
		update.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				Integer remaining = DEFAULT_TWEET_SIZE
						- update.getValue().length();
				remainingChars.setText(remaining.toString());
				updateRemainingCharsClass(remaining);
			}

		});

		cancelLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onCancel();
				
			}
			
		});
		send.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				TwitterUpdateDTO twitterUpdate = new TwitterUpdateDTO();
				twitterUpdate.setText(update.getValue());
				twitterUpdate.setTwitterAccount(getSendingTwitterAccount());
				if (type == REPLY ) {
					twitterUpdate.setInReplyToStatusId(inResponseTotwitterUpdate
							.getId());	
				}
				
				isUpdating(true);
				getController().sendUpdate(twitterUpdate,instance);
				
				//getController().handleAction(IController.IActions.TWEET_THIS,
				//		twitterUpdate,instance);
			}

		});
		super.add(bottomLayout);

	}

	private void updateRemainingCharsClass(Integer remaining) {
		if (remaining < 0) {
			remainingChars.addStyleName("error");
		} else {
			remainingChars.removeStyleName("error");
		}

	}

	@Override
	public void isUpdating(boolean isUpdating) {

		this.waitingImage.setVisible(isUpdating);

	}

	public void setType(int type) {
		this.type = type;

	}

	public void setInResponseTo(TwitterUpdateDTO twitterUpdate) {
		this.inResponseTotwitterUpdate = twitterUpdate;

	}

	public void refresh() {

		remainingChars.setText(DEFAULT_TWEET_SIZE.toString());

		if (this.type == REPLY) {
			assert (this.inResponseTotwitterUpdate != null);
			this.update.setFocus(true);
			this.update.setValue("@"
					+ this.inResponseTotwitterUpdate.getTwitterAccount()
							.getTwitterScreenName() + " ");

		} else if (this.type == RETWEET) {
			assert (this.inResponseTotwitterUpdate != null);
			this.update.setFocus(true);

			this.update.setValue("RT @"
					+ this.inResponseTotwitterUpdate.getTwitterAccount()
							.getTwitterScreenName() + ":"
					+ this.inResponseTotwitterUpdate.getText());

		}

		Integer remainingLength = DEFAULT_TWEET_SIZE
				- this.update.getValue().length();

		this.remainingChars.setText(remainingLength.toString());
		updateRemainingCharsClass(remainingLength);

	}

	public int getType() {
		return this.type;
	}

	public void setSendingTwitterAccount(TwitterAccountDTO sendingTwitterAccount) {
		this.sendingTwitterAccount = sendingTwitterAccount;
	}

	public TwitterAccountDTO getSendingTwitterAccount() {
		return sendingTwitterAccount;
	}
	
	public void addAsyncHandler(SendUpdateAsyncHandler asyncHandler) {
		if (this.asynHadlers==null) {
			this.asynHadlers = new ArrayList<SendUpdateAsyncHandler>();
		}
		this.asynHadlers.add(asyncHandler);
	}

	@Override
	public void onSuccess(Object result) {
		isUpdating(false);
		update.setValue("");
		this.remainingChars.setText(DEFAULT_TWEET_SIZE.toString());
		if (this.asynHadlers!= null ) {
			for (SendUpdateAsyncHandler handler : this.asynHadlers) {
				handler.onSuccess(result);
			}
			this.asynHadlers = null;
		}
		
	}
	@Override
	public void onFailure(Throwable tr) {
		isUpdating(false);
		if (this.asynHadlers!= null ) {
			for (SendUpdateAsyncHandler handler : this.asynHadlers) {
				handler.onFailure(tr);
			}
			this.asynHadlers = null;
		}

		
	}

	public void setShowUserImage(boolean showUserImg) {
		this.showUserImage = showUserImg;
		
	}

	@Override
	public void onCancel() {
		update.setValue("");
		if (this.asynHadlers!= null ) {
			for (SendUpdateAsyncHandler handler : this.asynHadlers) {
				handler.onCancel();
			}
			this.asynHadlers = null;
		}
		
	}

}
