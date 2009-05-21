package org.nideasystems.webtools.zwitrng.client.view.persona;



import org.nideasystems.webtools.zwitrng.client.view.AbstractTabbedPanel;


/**
 * This is teh Tabbed view of personas
 * @author jpereira
 *
 */
public class PersonasCompositeView extends AbstractTabbedPanel{

	/**
	 * Constructor
	 */
	public PersonasCompositeView() {
		super();
		
		
	}

	@Override
	public void init() {
		this.setWidth("750px");
		super.setAnimationEnabled(true);
		//Add the default AddPersonaPanel
		AddNewPersonaTabView addNewPersonaView = new AddNewPersonaTabView();
		addNewPersonaView.setController(getController());
		
		addNewPersonaView.init();
		
		super.add(addNewPersonaView, "+");
		// Return the content
		super.selectTab(0);
		super.ensureDebugId("cwTabPanel");
		//Add a selection listener to the tab
		
		super.addSelectionHandler(getController().getSelectionHandler());
		
	}
}
