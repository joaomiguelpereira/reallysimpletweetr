package org.nideasystems.webtools.zwitrng.client.controller;

import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.TwitterUserInfoWidget;
import org.nideasystems.webtools.zwitrng.client.view.updates.ShowStatusWindow;

public class PopupManager {

	PersonaController personaController = null;
	TwitterUserInfoWidget userInfoPopup = null;
	ShowStatusWindow showStatusWindow = null;

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

	public void showStatus(String originalId, String id) {
		// If the window is already opened, then just add the tweet to the
		// pannel
		if (showStatusWindow == null) {
			showStatusWindow = new ShowStatusWindow(personaController
					.getTwitterAccountController());
			
			showStatusWindow.show();
			

		}
		if (showStatusWindow.isEmpty() ) {
			showStatusWindow.load(originalId);
		}
		
		
		// showStatusWindow.addTweetId(id);

		showStatusWindow.show();

		showStatusWindow.load(id);

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
