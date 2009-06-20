package org.nideasystems.webtools.zwitrng.client.view.updates;

import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.DialogBoxesConstants;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTOList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ShowStatusWindow extends /*DialogBox*/ PopupPanel {

	private Image waitingImage = new Image(Constants.WAITING_IMAGE);
	private TwitterAccountController controller = null;

	private VerticalPanel vPanel = new VerticalPanel();
	private VerticalPanel contentPanel = new VerticalPanel();
	private ScrollPanel scrollPannel = null;
	

	Map<String, TwitterUpdateDTO> loadedUpdates = new HashMap<String, TwitterUpdateDTO>();
	private int top = -1;

	public ShowStatusWindow(TwitterAccountController controller) {
		this.setController(controller);
		
		
		// TODO: Refactor this duplicated code
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		vPanel = new VerticalPanel();
		vPanel.setWidth(DialogBoxesConstants.WIDTH);
		vPanel.setHeight("150px");
		waitingImage.setVisible(false);
		vPanel.add(waitingImage);
		/** Content goes here */

		scrollPannel = new ScrollPanel(contentPanel);
		// scrollPannel.setWidth(DialogBoxesConstants.WIDTH);
		scrollPannel.setHeight("150px");

		// scrollPannel.add(contentPanel);

		vPanel.add(scrollPannel);
		/** End Content */
		// TODO: Refactor this duplicated code
		HorizontalPanel toolsPanel = new HorizontalPanel();
		InlineHTML closeOption = new InlineHTML("Close");
		closeOption.addStyleName("link");
		closeOption.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setTop(-1);
				hide(true);

			}

		});

		// add tools panel
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

	@Override
	public void hide(boolean autoClosed) {
		this.loadedUpdates.clear();
		contentPanel.clear();
		super.hide(autoClosed);

	}

	@Override
	public void clear() {
		this.contentPanel.clear();
		this.loadedUpdates.clear();
		
	}
	public void onError(Throwable caught) {
		isProcessing(false);
		controller.getMainController().addException(caught);

	}

	public void onSuccess(TwitterUpdateDTOList result) {
		isProcessing(false);

		if (result.getTwitterUpdatesList().size() == 0) {
			controller.getMainController().addError("Tweet not found");
		} else {
			// Window.alert("Loaded Tweet: "+result.getTwitterUpdatesList().get(0).getText());
			for ( TwitterUpdateDTO update : result.getTwitterUpdatesList() ) {
				TwitterUpdateWidget updateWidget = new TwitterUpdateWidget(controller,update);
				updateWidget.setShowConversationEnabled(false);
				updateWidget.init();
				this.loadedUpdates.put(update.getId()
						+ "", update);
				addUpdate(updateWidget);
	
			}
			
		}

	}

	private void addUpdate(TwitterUpdateWidget update) {
		contentPanel.add(update);
		scrollPannel.scrollToBottom();
		
		


	}

	public boolean isEmpty() {

		return (this.loadedUpdates.size() == 0);
	}

	public void load(long id) {

		if (this.loadedUpdates.get(id) == null) {
			this.isProcessing(true);
			controller.loadTwitterConversation(this, id, UpdatesType.SINGLE);
		} else {

			// Just scroll to the leaded
		}

	}

	public void loadConversation(long id) {
		this.isProcessing(true);
		controller.loadTwitterConversation(this,id, UpdatesType.CONVERSATION);
		
	}

	public void setTop(int top) {
		
		int left = this.getAbsoluteLeft()+20;
		this.setPopupPosition(left, top);
		this.top  = top;
		
	}

	public int getTop() {
		
		return top;
	}
}
