package org.nideasystems.webtools.zwitrng.client.view.twitteraccount;

import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SendPrivateMessageWindow extends DialogBox {

	private static final String WIDTH = "400px";
	private static final String WIDTH_TEXT_AREA = "390px";
	private static final String HEIGHT_TEXT_AREA = "80px";
	private TextArea messageTextArea = new TextArea();
	
	public SendPrivateMessageWindow(TwitterAccountDTO recipient) {
		
		setText("Send private message to "+recipient.getTwitterScreenName());
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth(WIDTH);
		setWidget(mainPanel);
		setUpTextBox();
		mainPanel.add(messageTextArea);
		setUpTools();
		center();
	}
	
	private void setUpTools() {
		
		
	}

	private void setUpTextBox() {
		
		messageTextArea.setWidth(WIDTH_TEXT_AREA);
		messageTextArea.setHeight(HEIGHT_TEXT_AREA);
		
	}
	
	
}
