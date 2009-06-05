package org.nideasystems.webtools.zwitrng.client.view.twitteraccount;

import org.nideasystems.webtools.zwitrng.client.view.SendUpdateAsyncHandler;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

public class SendPrivateMessageWindow extends DialogBox {

	
	public SendPrivateMessageWindow(TwitterAccountDTO recipient,final SendUpdateWidget updateWidget) {

		setText("Send private message to " + recipient.getTwitterScreenName());
		VerticalPanel mainPanel = new VerticalPanel();
		setModal(true);
		mainPanel.add(updateWidget);
		updateWidget.addAsyncHandler(new SendUpdateAsyncHandler() {

			@Override
			public void onCancel() {
				hide(true);
				
			}

			@Override
			public void onFailure(Throwable tr) {
				updateWidget.getController().getMainController().addException(tr);
				
				
			}

			@Override
			public void onSuccess(Object arg) {
				Window.alert("Message sent");
				
			}
			
		});

		
		setWidget(mainPanel);
	
		center();
	}

}
