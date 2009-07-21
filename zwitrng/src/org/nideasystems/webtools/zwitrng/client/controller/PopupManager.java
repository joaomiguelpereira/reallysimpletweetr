package org.nideasystems.webtools.zwitrng.client.controller;

import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.view.persona.CreateTemplateWindow;
import org.nideasystems.webtools.zwitrng.client.view.persona.SelectTemplateWindow;
import org.nideasystems.webtools.zwitrng.client.view.persona.ObjectList;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.TwitterUserInfoWidget;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.client.view.updates.ShowStatusWindow;

import com.google.gwt.user.client.ui.PopupPanel;

public class PopupManager {

	PersonaController personaController = null;
	TwitterUserInfoWidget userInfoPopup = null;
	ShowStatusWindow statusWindow = null;
	ShowStatusWindow conversationWindow = null;

	public PopupManager(PersonaController currentPersonaController) {
		personaController = currentPersonaController;
	}

	/**
	 * Used globally to show a UserInforPopup
	 */
	public void showDelayedUserInfoPopup(int left, int top,
			String userIdOrScreenName) {

		userInfoPopup = new TwitterUserInfoWidget(userIdOrScreenName,
				personaController.getTwitterAccountController());

		userInfoPopup.center();
		userInfoPopup.setPopupPosition(userInfoPopup.getAbsoluteLeft(), top);
		userInfoPopup.show();

	}

	public void destroyUserPopupPanel() {
		userInfoPopup.hide(true);

	}

	public void showConversation(long id, int top) {
		if (conversationWindow == null) {
			conversationWindow = new ShowStatusWindow(personaController
					.getTwitterAccountController());
		}
		//conversationWindow.setText("conversation");
		conversationWindow.center();
		conversationWindow.show();
		conversationWindow.setTop(top);
		conversationWindow.loadConversation(id);
		
	}

	public void showStatus(long id,int top) {
		// If the window is already opened, then just add the tweet to the
		// pannel

		if (statusWindow == null) {
			statusWindow = new ShowStatusWindow(personaController
					.getTwitterAccountController());
		}
		
		statusWindow.clear();
		//statusWindow.setText("Status");
		statusWindow.center();
		statusWindow.show();
		if ( statusWindow.getTop() == -1 ) {
			statusWindow.setTop(top);
		}
		
		
		statusWindow.load(id);

	}

	public PopupPanel showSelectTemplateWindow(SendUpdateWidget widget, int left, int top) {
		
		SelectTemplateWindow wnd = new SelectTemplateWindow();
		wnd.setSendTwitterUpdateWidget(widget);
		wnd.init();
		wnd.center();
		wnd.setPopupPosition(wnd.getAbsoluteLeft(), top);
		wnd.show();
		wnd.loadTemplates();
		return wnd;
		
		
		
	}

	public void showCreateTemplateWindow(PersonaController controller, ObjectList templateList, int left, int top) {
		CreateTemplateWindow wnd = new CreateTemplateWindow(controller,templateList);
		wnd.init();
		wnd.center();
		wnd.setPopupPosition(wnd.getAbsoluteLeft(), top);
		wnd.show();
		
		
	}

	/**
	 * Helper method to destroy a user popup panel
	 */
	/*
	 * private void destroyUserPopupPanel() { if (userInfoPopup != null) { if
	 * (keepUserInfoWidgetTimer == null) { keepUserInfoWidgetTimer = new
	 * HideUserInfoPanelTimer(); } keepUserInfoWidgetTimer.schedule(500); } if
	 * (showUserInfoWidgetTimer != null) { showUserInfoWidgetTimer.cancel();
	 * showUserInfoWidget = false; showUserInfoWidgetTimer = null;
	 * 
	 * }
	 * 
	 * }
	 */

}
