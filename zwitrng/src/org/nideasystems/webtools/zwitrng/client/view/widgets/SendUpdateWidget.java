package org.nideasystems.webtools.zwitrng.client.view.widgets;

import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
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
import com.google.gwt.user.client.ui.Image;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

public class SendUpdateWidget extends AbstractVerticalPanelView {

	protected static final int STATUS = 0;
	protected static final int REPLY = 1;
	protected static final int RETWEET = 2;
	private static final Integer DEFAULT_TWEET_SIZE = 140;

	private final HTML remainingChars = new HTML(DEFAULT_TWEET_SIZE.toString());
	private final HTML pub = new HTML("Innovation with twitter :)");
	final TextArea update = new TextArea();
	final Button send = new Button("Send");
	private int type = STATUS;
	private TwitterAccountDTO sendingTwitterAccount;
	private TwitterUpdateDTO inResponseTotwitterUpdate;

	@Override
	public void init() {

		
		update.setWidth("650px");
		//update.setHeight("35px");
		update.addStyleName("input");
		update.setVisibleLines(2);
		
		
		FlexTable bottomLayout = new FlexTable();
		FlexCellFormatter formater = bottomLayout.getFlexCellFormatter();
		bottomLayout.setCellSpacing(0);
		Image sendingUserImage = new Image(sendingTwitterAccount.getTwitterImageUrl());
		sendingUserImage.setWidth("32px");
		sendingUserImage.setHeight("32px");
		bottomLayout.setWidget(0, 0, sendingUserImage);
		
		bottomLayout.setWidget(0, 1, update);
		formater.setColSpan(0, 1, 2);
		formater.setWidth(0, 1, "650px");
		
		
		remainingChars.setWidth("35px");
		bottomLayout.setWidget(1, 0, remainingChars);
		bottomLayout.setWidget(1, 1, pub);
		bottomLayout.setWidget(1, 2, send);
		
		formater.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		formater.setAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		formater.setAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		formater.setAlignment(1, 2, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);

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

		send.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				TwitterUpdateDTO twitterUpdate = new TwitterUpdateDTO();
				twitterUpdate.setText(update.getValue());
				getController().handleAction(IController.IActions.TWEET_THIS,
						twitterUpdate);

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
		// TODO Auto-generated method stub

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

}
