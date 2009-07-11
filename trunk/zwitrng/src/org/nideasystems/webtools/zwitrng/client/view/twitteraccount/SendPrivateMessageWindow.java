package org.nideasystems.webtools.zwitrng.client.view.twitteraccount;

import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.SendUpdateAsyncHandler;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SendPrivateMessageWindow extends DialogBox {
	private final static String WIDTH_DIALOG_BOX = "600px";
	private final static String HEIGH_DIALOG_BOX = "100px";
	
	private  VerticalPanel successPanel = new VerticalPanel(); 
	public SendPrivateMessageWindow(TwitterAccountDTO recipient,final SendUpdateWidget updateWidget) {

		HTML successMessage = new HTML("Your message was sent to "+recipient.getTwitterScreenName());

		successPanel.setWidth(WIDTH_DIALOG_BOX);
		successPanel.setHeight(HEIGH_DIALOG_BOX);
		successPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		successPanel.add(successMessage);
		HTML closeLink = new HTML("Close window");
		closeLink.addStyleName("link");
		successPanel.add(closeLink);
		closeLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide(false);
				
			}
			
		});
		setText("Send private message to " + recipient.getTwitterScreenName());
		VerticalPanel mainPanel = new VerticalPanel();
		setModal(true);
		mainPanel.add(updateWidget);
		InlineHTML closeWindowLink = new InlineHTML("Close");
		closeWindowLink.addStyleName("link");
		closeWindowLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide(true);
				
			}
			
		});
		mainPanel.add(closeWindowLink);
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
				
				setWidget(successPanel);
				
			}
			
		});

		
		setWidget(mainPanel);
	
		center();
	}
	public static SendPrivateMessageWindow create(
			TwitterAccountController twitterAccountController,
			TwitterAccountDTO user, int type) {

		SendUpdateWidget sendUpdateWidget = new SendUpdateWidget();
		sendUpdateWidget.setController(twitterAccountController);
		sendUpdateWidget
				.setSendingTwitterAccount(twitterAccountController.getModel());
		sendUpdateWidget.setShowUserImage(true);
		sendUpdateWidget
				.setInResponseToUserAccount(user);
		sendUpdateWidget
				.setType(type);
		
		TwitterUpdateDTO update = new TwitterUpdateDTO();
		update.setText(user.getTwitterStatusText());
		update.setTwitterAccount(user);
		sendUpdateWidget.setInResponseTo(update);
		sendUpdateWidget.init();
		sendUpdateWidget.refresh();

		SendPrivateMessageWindow sendPrivateMessageWindow = new SendPrivateMessageWindow(
				twitterAccountController.getModel(), sendUpdateWidget);
		sendPrivateMessageWindow.setAnimationEnabled(true);

		return sendPrivateMessageWindow;
	}

}
