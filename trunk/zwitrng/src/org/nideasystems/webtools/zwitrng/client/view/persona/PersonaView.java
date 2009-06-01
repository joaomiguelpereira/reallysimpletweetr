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
		isInitialized = true;
		
		

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
