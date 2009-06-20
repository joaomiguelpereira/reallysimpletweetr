package org.nideasystems.webtools.zwitrng.client.view.twitteraccount;

import java.util.List;

import org.apache.tools.ant.taskdefs.Sleep;
import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SelectSendingAccountWindow extends PopupPanel {

	private static final String WIDTH = "550px";
	private VerticalPanel mainPanel = null;

	private TwitterAccountController parentController = null;
	private SendUpdateWidget callingInstance = null;

	public SelectSendingAccountWindow(TwitterAccountController controller,
			SendUpdateWidget callingInstance) {
		this.parentController = controller;
		this.callingInstance = callingInstance;
	}

	public void init() {
		VerticalPanel outerPanel = new VerticalPanel();
		this.setAnimationEnabled(true);
		this.setWidth(WIDTH);
		this.setAutoHideEnabled(true);

		mainPanel = new VerticalPanel();
		mainPanel.setSpacing(2);
		mainPanel.setWidth("400px");

		ScrollPanel scrollPanel = new ScrollPanel(mainPanel);
		
		scrollPanel.setHeight("160px");

		
		
		
		outerPanel.add(scrollPanel);
		this.add(outerPanel);
		HTML closeLink = new HTML("close");
		closeLink.addStyleName("link");
		closeLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide(true);
			}
			
		});
		
		outerPanel.add(closeLink);
		
		
	}

	public class SelectableUser extends HorizontalPanel implements
			HasMouseOutHandlers, HasMouseOverHandlers {
		private TwitterAccountDTO currentAccount = null;
		private boolean isSelected = false;
		private HorizontalPanel contentPanel = null;
		private CheckBox selectUser = null;

		public SelectableUser(TwitterAccountDTO account) {

			this.currentAccount = account;
			this.contentPanel = new HorizontalPanel();

			selectUser = new CheckBox(account.getTwitterName() + " ("
					+ account.getTwitterScreenName() + ")");

			selectUser.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					// Get the parent SendUpdate Widget and add the account as
					// selected
					if (selectUser.getValue()) {
						callingInstance.addSendingAccount(currentAccount);
					} else {
						callingInstance.removeSendingAccount(currentAccount);
					}

				}

			});

			Image userImg = new Image(account.getTwitterImageUrl());
			this.contentPanel.add(userImg);
			this.contentPanel.add(selectUser);

			this.add(contentPanel);

			/*
			 * this.addMouseOverHandler(new MouseOverHandler() {
			 * 
			 * @Override public void onMouseOver(MouseOverEvent event) {
			 * contentPanel.addStyleName("currentTweet");
			 * 
			 * }
			 * 
			 * });
			 * 
			 * this.addMouseOutHandler(new MouseOutHandler() {
			 * 
			 * @Override public void onMouseOut(MouseOutEvent event) {
			 * contentPanel.removeStyleName("currentTweet");
			 * 
			 * }
			 * 
			 * });
			 */

		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
			selectUser.setValue(isSelected);
		}

		public boolean isSelected() {
			return isSelected;
		}

		@Override
		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {

			return addDomHandler(handler, MouseOutEvent.getType());
		}

		@Override
		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());
		}
	}

	public void load(List<String> currentTwitterScreenNames) {

		// Load the controller service
		List<TwitterAccountDTO> accountList = this.parentController
				.getMainController().getAllTwitterAccounts(this);

		for (TwitterAccountDTO account : accountList) {

			SelectableUser selUser = new SelectableUser(account);
			if (currentTwitterScreenNames.contains(account.getTwitterScreenName())) {
				selUser.setSelected(true);
			}

			mainPanel.add(selUser);
		}

	}

	public void show(int absoluteLeft, int absoluteTop) {
		this.setPopupPosition(absoluteLeft, absoluteTop);
		this.show();

	}

}
