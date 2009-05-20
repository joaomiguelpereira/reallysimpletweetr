package org.nideasystems.webtools.zwitrng.client.view;

import org.nideasystems.webtools.zwitrng.client.controller.PersonaTabSelectCallBack;

import com.google.gwt.user.client.ui.DecoratedTabPanel;


/**
 * This is teh Tabbed view of personas
 * @author jpereira
 *
 */
public class PersonaTabView extends DecoratedTabPanel{

	/**
	 * Constructor
	 */
	public PersonaTabView() {
		super();
		this.setWidth("750px");
		super.setAnimationEnabled(true);

		//Add the default AddPersonaPanel
		AddNewPersonaView addNewPersonaView = new AddNewPersonaView();
		super.add(addNewPersonaView, "+");
		// Return the content
		super.selectTab(0);
		super.ensureDebugId("cwTabPanel");
		
		//Add a selection listener to the tab
		super.addSelectionHandler(new PersonaTabSelectCallBack());
		
	}
}
