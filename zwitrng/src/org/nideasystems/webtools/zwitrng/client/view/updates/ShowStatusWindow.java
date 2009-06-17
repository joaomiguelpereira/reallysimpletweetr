package org.nideasystems.webtools.zwitrng.client.view.updates;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.DialogBoxesConstants;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ShowStatusWindow extends DialogBox {

	private Image waitingImage = new Image(Constants.WAITING_IMAGE);
	private TwitterAccountController controller = null;
	private String tweetId = null;
	private VerticalPanel vPanel = new VerticalPanel();
	private VerticalPanel contentPanel = new VerticalPanel();
	
	public ShowStatusWindow(TwitterAccountController controller) {
		this.setController(controller);
		this.setTitle("Show status");
		this.setText("Show status ");
		
		//TODO: Refactor this duplicated code
		this.setAnimationEnabled(true);
		vPanel = new VerticalPanel();
		vPanel.setWidth(DialogBoxesConstants.WIDTH);
		vPanel.setHeight(DialogBoxesConstants.HEIGHT);
		waitingImage.setVisible(false);
		vPanel.add(waitingImage);
		/**Content goes here*/
		vPanel.add(contentPanel);
		/**End Content */	
		//TODO: Refactor this duplicated code
		HorizontalPanel toolsPanel = new HorizontalPanel();
		InlineHTML closeOption = new InlineHTML("Close");
		closeOption.addStyleName("link");
		closeOption.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide(true);
				
			}
			
		});
		
		
		//add tools panel
		toolsPanel.add(closeOption);
		vPanel.add(toolsPanel);
		this.add(vPanel);
		this.center();

	}
	public void setController(TwitterAccountController controller) {
		this.controller = controller;
	}
	public TwitterAccountController getController() {
		return controller;
	}
	public void isProcessing(boolean b) {
		this.waitingImage.setVisible(b);
		
		
		
	}
	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}
	public String getTweetId() {
		return tweetId;
	}
	public void load() {
		this.isProcessing(true);
		controller.loadTwitterUpdate(this,getTweetId());
		
	}
	public void onError(Throwable caught) {
		isProcessing(false);
		controller.getMainController().addException(caught);
		
	}
	public void onSuccess(TwitterUpdateDTOList result) {
		isProcessing(false);
		if ( result.getTwitterUpdatesList().size() == 0 ) {
			controller.getMainController().addError("Tweet not found");
		} else {
			//Window.alert("Loaded Tweet: "+result.getTwitterUpdatesList().get(0).getText());
			TwitterUpdateWidget update = new TwitterUpdateWidget(controller,result.getTwitterUpdatesList().get(0));
			contentPanel.add(update);
			
			
		}
		
		
	}
}
