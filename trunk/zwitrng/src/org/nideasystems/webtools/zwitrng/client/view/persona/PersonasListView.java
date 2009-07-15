package org.nideasystems.webtools.zwitrng.client.view.persona;




import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonasListController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractTabbedPanel;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Image;


/**
 * This is teh Tabbed view of personas
 * @author jpereira
 *
 */
public class PersonasListView extends AbstractTabbedPanel<PersonasListController>{

	private DefaultHomeView defaultHomeView;
	
	//private final PersonasCompositeToolsWidget = new PersonasCompositeToolsWidget(); 
	/**
	 * Constructor
	 */
	public PersonasListView() {
		super();
		
		
	}


	@Override
	public void init() {
		this.setWidth(Constants.MAIN_TABBED_PANEL_WIDTH);
		super.setAnimationEnabled(true);
		
		
		//Add the default AddPersonaPanel
		defaultHomeView = new DefaultHomeView();
		
		defaultHomeView.setController(getController());
		
		defaultHomeView.init();
		Image homeTabImage = new Image(Constants.HOME_TAB_IMG);
		homeTabImage.setTitle("Home");
		homeTabImage.setHeight(Constants.MINI_TOOLBAR_ICON_HEIGHT);
		homeTabImage.setWidth(Constants.MINI_TOOLBAR_ICON_WIDTH);
		homeTabImage.setTitle("Home");
		
		
		super.add(defaultHomeView, homeTabImage);
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

	public DefaultHomeView getHomeView() {
		
		return this.defaultHomeView;
	}
}
