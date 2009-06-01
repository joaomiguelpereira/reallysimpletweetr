package org.nideasystems.webtools.zwitrng.client.view.persona;




import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonasListController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractTabbedPanel;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;


/**
 * This is teh Tabbed view of personas
 * @author jpereira
 *
 */
public class PersonasListView extends AbstractTabbedPanel<PersonasListController>{

	
	
	//private final PersonasCompositeToolsWidget = new PersonasCompositeToolsWidget(); 
	/**
	 * Constructor
	 */
	public PersonasListView() {
		super();
		
		
	}

	public void myMethod() {
		
	}
	@Override
	public void init() {
		this.setWidth("720px");
		super.setAnimationEnabled(true);
		
		
		//Add the default AddPersonaPanel
		DefaultHomeView defaultHomeView = new DefaultHomeView();
		
		defaultHomeView.setController(getController());
		
		defaultHomeView.init();
		
		super.add(defaultHomeView, "+");
		// Return the content
		super.selectTab(0);
		super.ensureDebugId("cwTabPanel");
		//Add a selection listener to the tab

		super.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				getController().onTabSelectedChanged(event.getSelectedItem());
				
				
			}
			
		});
		
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		//Do noting
		

		
	}
}
