package org.nideasystems.webtools.zwitrng.client.view.persona;

import org.nideasystems.webtools.zwitrng.client.Constants;


import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;


import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * This is the persona View that contains other subViews
 * 
 * @author jpereira
 * 
 */
public class PersonaView extends AbstractVerticalPanelView<PersonaController> {

	private PersonaDTO personaObj = null;
	// private SearchesCompositeView userUpdatetabPanel = new
	// SearchesCompositeView();
	
	private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	private String loginUrl = "none";
	final HorizontalPanel authentication = new HorizontalPanel();
	final HTML loginUrlLink = new HTML("You need to authenticate");
	private boolean isInitialized = false;
	/*HTML linkToLogin = new HTML("");
	HTML continueLink = new HTML("");;*/
	/**
	 * P c
	 * 
	 * @param persona
	 */
	public PersonaView(/*PersonaDTO persona*/) {
		//super();
		//this.personaObj = persona;

	}

	public void setPersonaObj(PersonaDTO personaObj) {
		this.personaObj = personaObj;
	}

	public PersonaDTO getPersonaObj() {
		return personaObj;
	}

	@Override
	public void init() {
		if (isInitialized) {
			throw new RuntimeException("You try to initialize personaView again?");
		}
		this.add(new HTML("Persona View"));
		isInitialized = true;
		
		
		
		/*if (personaObj.getTwitterAccount() != null && personaObj.getTwitterAccount().getIsOAuthenticated() ) {

			userInfoWidget = new PersonaInfoWidget(this.personaObj
					.getTwitterAccount());
			super.add(userInfoWidget);

			
			
			personaToolsWidget = AbstractController.createView(PersonaToolsWidget.class);
			
			personaToolsWidget.setController(getController());
			personaToolsWidget.init();

			//change this
			whatImDoing = new HTML("");
			whenIDidIt = new HTML("");
			super.add(whatImDoing);

			super.add(personaToolsWidget);

			//Send Updae must have a TwitterAccountController
			sendUpdateWidget = new SendUpdateWidget();
			sendUpdateWidget.setController(getController());
			sendUpdateWidget.setType(SendUpdateWidget.STATUS);
			sendUpdateWidget.setSendingTwitterAccount(this.personaObj.getTwitterAccount());
			sendUpdateWidget.init();
			super.add(sendUpdateWidget);
			waitingImg.setVisible(false);
			super.add(waitingImg);
			isInitialized = true;
			linkToLogin.setVisible(false);
			linkToLogin.setVisible(false);

			
		} else {
			//Hyperlink linkToLogin = new Hyperlink();
			
			linkToLogin.setVisible(true);
			linkToLogin.setVisible(true);
			linkToLogin.setHTML("Click here to open a new window to login in twitter."+personaObj.getTwitterAccount().getOAuthLoginUrl());
			continueLink.setHTML("After you have logged in close the window and click here to grant you access");
			
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
		}*/

	}

	public void refresh() {
		if (!isInitialized) {
			init();
		}
		//personaViewSelected.refresh();
		
		//if (this.)
		
		/*if (personaToolsWidget!=null) {
			personaToolsWidget.refresh();
		}
		
		lastStatus = personaObj.getTwitterAccount().getTwitterUpdateDto();
		if (lastStatus != null) {
			whatImDoing.setHTML("</span><span class=\"text\">"
					+ lastStatus.getText() + "<span>");

			whenIDidIt.setHTML("<span class=\"createdAt\">"
					+ lastStatus.getCreatedAt()
					+ "<span> from <span class=\"source\">"
					+ lastStatus.getSource() + "</span>");

		}*/
		//loginUrlLink.setHTML(this.loginUrl);

	}

	@Override
	public void isUpdating(boolean isUpdating) {
		// Window.alert("Is Updating");
		waitingImg.setVisible(isUpdating);

	}

	public void updateLastStatus(TwitterUpdateDTO lastUpdate) {
		personaObj.getTwitterAccount().setTwitterUpdateDto(lastUpdate);
		//this.lastStatus = lastUpdate;
	}

	
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

}
