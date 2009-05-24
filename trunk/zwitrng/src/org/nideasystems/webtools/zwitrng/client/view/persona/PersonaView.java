package org.nideasystems.webtools.zwitrng.client.view.persona;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.client.view.widgets.PersonaInfoWidget;
import org.nideasystems.webtools.zwitrng.client.view.widgets.PersonaToolsWidget;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * This is the persona View that contains other subViews
 * 
 * @author jpereira
 * 
 */
public class PersonaView extends AbstractVerticalPanelView {

	private PersonaDTO personaObj = null;
	// private SearchesCompositeView userUpdatetabPanel = new
	// SearchesCompositeView();
	private TwitterUpdateDTO lastStatus = null;
	private PersonaInfoWidget userInfoWidget = null;
	private PersonaToolsWidget personaToolsWidget = null;
	private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	private HTML whatImDoing = null;
	private HTML whenIDidIt = null;
	private String loginUrl = "none";
	final HorizontalPanel authentication = new HorizontalPanel();
	final HTML loginUrlLink = new HTML("You need to authenticate");

	/**
	 * P c
	 * 
	 * @param persona
	 */
	public PersonaView(PersonaDTO persona) {
		super();
		this.personaObj = persona;

	}

	public void setPersonaObj(PersonaDTO personaObj) {
		this.personaObj = personaObj;
	}

	public PersonaDTO getPersonaObj() {
		return personaObj;
	}

	@Override
	public void init() {
		// check if it's authenticated
		// http://zwitrng.appspot.com/
		// Check if is Authenticate

		
		
		if (personaObj.getTwitterAccount() != null && personaObj.getTwitterAccount().getIsOAuthenticated() ) {

			userInfoWidget = new PersonaInfoWidget(this.personaObj
					.getTwitterAccount());
			super.add(userInfoWidget);

			
			personaToolsWidget = new PersonaToolsWidget();
			personaToolsWidget.setController(getController());
			personaToolsWidget.init();

			whatImDoing = new HTML("");
			whenIDidIt = new HTML("");
			super.add(whatImDoing);

			super.add(personaToolsWidget);
			waitingImg.setVisible(false);
			super.add(waitingImg);
		} else {
			//Hyperlink linkToLogin = new Hyperlink();
			HTML linkToLogin = new HTML("Click here to open a new window to login in twitter."+personaObj.getTwitterAccount().getOAuthLoginUrl());
			HTML continueLink = new HTML("After you have logged in close the window and click here to grant you access");
			
			linkToLogin.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
					getController().handleAction(IController.IActions.START_LOGIN);
					
				}
				
			});
			
			continueLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getController().handleAction(IController.IActions.CONTINUE_LOGIN);
					
				}
				
			});
			super.add(linkToLogin);
			super.add(continueLink);
		}
		/*
		 * if (!isOath) {
		 * 
		 * 
		 * loginUrlLink.addClickHandler(new ClickHandler() {
		 * 
		 * @Override public void onClick(ClickEvent event) { try {
		 * BasicAutehnticationService authService =
		 * (BasicAutehnticationService)getController
		 * ().getServiceManager().getAuthenticationService();
		 * authService.doAuthentication(loginUrl);
		 * Window.alert("Open new window with URL "+loginUrl); } catch
		 * (Exception e) { getController().getErrorHandler().addException(e);
		 * e.printStackTrace(); }
		 * 
		 * }
		 * 
		 * }); authentication.add(loginUrlLink); // You're not authenticated
		 * //Window.alert("You're not authenticated"); //get the login URL from
		 * server try { //BasicAutehnticationService authService =
		 * (BasicAutehnticationService
		 * )getController().getServiceManager().getAuthenticationService();
		 * //String url = authService.getAuthenticationUrl();
		 * //authentication.add(new HTML(url));
		 * 
		 * isUpdating(true);
		 * getController().getServiceManager().getRPCService().
		 * getLoginUrl(personaObj.getTwitterAccount(), new
		 * AsyncCallback<String>() {
		 * 
		 * @Override public void onFailure(Throwable caught) {
		 * isUpdating(false);
		 * //getController().getErrorHandler().addException(caught);
		 * caught.printStackTrace();
		 * 
		 * }
		 * 
		 * @Override public void onSuccess(String result) {
		 * //Window.alert(result); authentication.add(new HTML(result));
		 * 
		 * }
		 * 
		 * }); } catch (Exception e) { isUpdating(false);
		 * getController().getErrorHandler().addException(e);
		 * e.printStackTrace(); } super.add(authentication); } else {
		 */

		// }

	}

	public void refresh() {
		personaToolsWidget.refresh();
		if (lastStatus != null) {
			whatImDoing.setHTML("</span><span class=\"text\">"
					+ lastStatus.getText() + "<span>");

			whenIDidIt.setHTML("<span class=\"createdAt\">"
					+ lastStatus.getCreatedAt()
					+ "<span> from <span class=\"source\">"
					+ lastStatus.getSource() + "</span>");

		}
		loginUrlLink.setHTML(this.loginUrl);

	}

	@Override
	public void isUpdating(boolean isUpdating) {
		// Window.alert("Is Updating");
		waitingImg.setVisible(isUpdating);

	}

	public void setLastStatus(TwitterUpdateDTO lastStatus) {
		this.lastStatus = lastStatus;
	}

	public TwitterUpdateDTO getLastStatus() {
		return lastStatus;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

}
