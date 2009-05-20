package org.nideasystems.webtools.zwitrng.client.view;

import org.nideasystems.webtools.zwitrng.client.view.widgets.PersonaInfoWidget;
import org.nideasystems.webtools.zwitrng.client.view.widgets.PersonaToolsWidget;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaObj;

import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * This is the persona View that contains other subViews
 * @author jpereira
 *
 */
public class PersonaView extends VerticalPanel {
	
	
	
	private PersonaObj personaObj = null;
	private PersonaUpdatesTabbedView userUpdatetabPanel = new PersonaUpdatesTabbedView();
	
	private PersonaInfoWidget userInfoWidget = null;
	private PersonaToolsWidget personaToolsWidget = null;
	
	/**
	 * P c
	 * @param persona
	 */
	public PersonaView(PersonaObj persona) {
		super();
		setPersonaObj(persona);
		userInfoWidget = new PersonaInfoWidget(persona.getTwitterAccount());
		super.add(userInfoWidget);
		
		personaToolsWidget = new PersonaToolsWidget(persona);
		
		super.add(personaToolsWidget);
		
		//userUpdatetabPanel = new PersonaUpdatesTabbedView();
		
		
		//Build the default filter "home"
		//FilterCriteria homeFilter = new FilterCriteria();
		/*userUpdates = new PersonaUpdatesView(homeFilter);
		homeFilter.setName("home");
		
		userUpdatetabPanel.add(userUpdates,homeFilter.getName());
		userUpdatetabPanel.selectTab(0);*/
		//super.add(userUpdatetabPanel);
		
		//On activate this tab, populate first tab of updates
	}
	
	
	/*public void addUpdate(TwittUpdate twittUpdate) {
		
		userUpdates.add(new TwitterUpdateWidget(twittUpdate));
		
		
		
	}*/
	public void setPersonaObj(PersonaObj personaObj) {
		this.personaObj = personaObj;
	}

	public PersonaObj getPersonaObj() {
		return personaObj;
	}

	

	/**
	 * This a tabbed view with updated of the user
	 * @author jpereira
	 *
	 */
/*	private class PersonaUpdatesTabbedView extends DecoratedTabPanel { 
		Map<String, VerticalPanel> tabs = new HashMap<String, VerticalPanel>();
		private PersonaUpdatesTabbedView() {
			super();
			super.setWidth("700px");
			super.setAnimationEnabled(true);
		}
		
	}
*/	/**
	 * This contains all updates
	 * @author jpereira
	 *
	 */
	/*private class PersonaUpdatesView extends VerticalPanel {
		
		private PersonaUpdatesView() {
			super();
		}
	}
	*/

		
	}
	
	

	 
	
	
	
	
	

