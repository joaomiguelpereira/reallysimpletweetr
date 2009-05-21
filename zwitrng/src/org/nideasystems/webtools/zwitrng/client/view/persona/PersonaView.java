package org.nideasystems.webtools.zwitrng.client.view.persona;

import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.client.view.widgets.PersonaInfoWidget;
import org.nideasystems.webtools.zwitrng.client.view.widgets.PersonaToolsWidget;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;


/**
 * This is the persona View that contains other subViews
 * 
 * @author jpereira
 * 
 */
public class PersonaView extends AbstractVerticalPanelView{

	private PersonaDTO personaObj = null;
	//private SearchesCompositeView userUpdatetabPanel = new SearchesCompositeView();

	private PersonaInfoWidget userInfoWidget = null;
	private PersonaToolsWidget personaToolsWidget = null;

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
		userInfoWidget = new PersonaInfoWidget(this.personaObj.getTwitterAccount());
		super.add(userInfoWidget);
		personaToolsWidget = new PersonaToolsWidget();
		personaToolsWidget.setController(getController());
		personaToolsWidget.init();
		
		
		super.add(personaToolsWidget);
		
	}

	

}
