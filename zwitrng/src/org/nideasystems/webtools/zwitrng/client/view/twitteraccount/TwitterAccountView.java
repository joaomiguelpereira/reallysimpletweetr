package org.nideasystems.webtools.zwitrng.client.view.twitteraccount;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;

import org.nideasystems.webtools.zwitrng.client.view.widgets.PersonaInfoWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TwitterAccountView extends
		AbstractVerticalPanelView<TwitterAccountController> {

	final private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	private PersonaInfoWidget personaInfo = null;
	private VerticalPanel loginInfoPanel = null;

	@Override
	public void init() {
		add(new HTML("TwitterAccountView"));

		add(waitingImg);

		if (!getController().getModel().getIsOAuthenticated()) {
			loginInfoPanel = getLoginInstructions();
			add(loginInfoPanel);
		} else {
			personaInfo = new PersonaInfoWidget(getController().getModel());
			add(personaInfo);
		}
		waitingImg.setVisible(false);

	}

	/**
	 * Show instructions on how to login
	 */
	private VerticalPanel getLoginInstructions() {
		final VerticalPanel loginInstuctions = new VerticalPanel();

		final HTML loginMessage = new HTML(
				"You did not gave access to any Twitter account yet. To give access to a Twitter account, click the image bellow.");
		final HTML continueLoginMessage = new HTML(
				"After you give access to your twitter account, click the image bellow to finish giving access to your account");
		final Image signWithTwitterImg = new Image(
				Constants.SIGN_WITH_TWITTER_IMG);

		final Image continueSignWithTwitterImg = new Image(
				Constants.CONTINUE_SIGN_WITH_TWITTER_IMG);

		signWithTwitterImg.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(getController().getModel().getOAuthLoginUrl(),
						"_OAuthLogin", "width=700,height=400");
				loginInstuctions.remove(signWithTwitterImg);
				loginInstuctions.remove(loginMessage);
				loginInstuctions.add(continueLoginMessage);
				loginInstuctions.add(continueSignWithTwitterImg);
			}
		});
		loginInstuctions.add(loginMessage);
		loginInstuctions.add(signWithTwitterImg);
		continueSignWithTwitterImg.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				getController().finishTwitterLogin();

			}

		});
		return loginInstuctions;

	}

	@Override
	public void isUpdating(boolean isUpdating) {
		waitingImg.setVisible(isUpdating);

	}
	public void onAuthenticationError() {
		remove(loginInfoPanel);
		loginInfoPanel = getLoginInstructions();
		add(loginInfoPanel);
	}
	public void onAuthenticationSuccess() {
		remove(loginInfoPanel);
		personaInfo = new PersonaInfoWidget(getController().getModel());
		add(personaInfo);
		
	}

}
