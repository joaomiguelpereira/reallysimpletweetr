package org.nideasystems.webtools.zwitrng.client.controller;

import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.TwitterUserInfoWidget;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;



public class PopupManager {
	
	PersonaController personaController = null;
	TwitterUserInfoWidget userInfoPopup = null;
	
	public PopupManager(PersonaController currentPersonaController) {
	 personaController = currentPersonaController;
	}

	/**
	 * Used globally to show a UserInforPopup
	 */
	public void showDelayedUserInfoPopup(int left, int top, String userIdOrScreenName) {
		
		userInfoPopup = new TwitterUserInfoWidget(userIdOrScreenName, personaController.getTwitterAccountController());
		
		userInfoPopup.center();
		userInfoPopup.setPopupPosition(userInfoPopup.getAbsoluteLeft(), top);
		userInfoPopup.show();
		
		
	}

	public void destroyUserPopupPanel() {
		userInfoPopup.hide(true);
		
	}
	
	
	/**
	 * Helper method to destroy a user popup panel
	 */
/*	private void destroyUserPopupPanel() {
		if (userInfoPopup != null) {
			if (keepUserInfoWidgetTimer == null) {
				keepUserInfoWidgetTimer = new HideUserInfoPanelTimer();
			}
			keepUserInfoWidgetTimer.schedule(500);
		}
		if (showUserInfoWidgetTimer != null) {
			showUserInfoWidgetTimer.cancel();
			showUserInfoWidget = false;
			showUserInfoWidgetTimer = null;

		}

	}
*/

}
