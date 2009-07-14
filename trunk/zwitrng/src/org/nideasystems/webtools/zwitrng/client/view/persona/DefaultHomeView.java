package org.nideasystems.webtools.zwitrng.client.view.persona;




import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonasListController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DefaultHomeView extends AbstractVerticalPanelView<PersonasListController> {
	private final VerticalPanel formPPanel = new VerticalPanel();
	private final Label twPersonaNameLabel = new Label("Persona Name");

	private final TextBox twPersonaName = new TextBox();

	private VerticalPanel twitterAccountsPanel = new VerticalPanel();
	private VerticalPanel messages = new VerticalPanel();
	
	public DefaultHomeView() {
		super();
	}

	@Override
	public void init() {
		
		
		this.add(messages);
		//Add My Persona's twitter account listView
		this.add(new InlineHTML("<h2>Your accounts</h2>"));
		this.add(twitterAccountsPanel);
		/////
		
		formPPanel.setSpacing(5);	
		formPPanel.add(new HTML("Create new Persona"));
		formPPanel.add(new HTML("Enter the name of the persona. Later you'll be asked to login into a twitter account."));
		formPPanel.add(twPersonaNameLabel);
		formPPanel.add(twPersonaName);
		
		
		// Return the content
		formPPanel.ensureDebugId("cwVerticalPanel");
		this.add(formPPanel);

		HorizontalPanel toolPanel = new HorizontalPanel();
		Button addNewTwitterAccountButton = new Button("Add new Twitter Account");

		addNewTwitterAccountButton.setWidth("180px");
		addNewTwitterAccountButton.setHeight("25px");

		addNewTwitterAccountButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				// Create a new Object to send to the Server
				PersonaDTO persona = new PersonaDTO();
				persona.setName(twPersonaName.getValue());
				TwitterAccountDTO twitterAccountObj = new TwitterAccountDTO();
				//twitterAccountObj.setTwitterScreenName(twUserName.getValue());
				//twitterAccountObj.setTwitterPassword(twPassword.getValue());
				persona.setTwitterAccount(twitterAccountObj);
				
				//Pass to controller. The controller will create the persona
				getController().createPersona(persona);
				twPersonaName.setValue("");
				
				

			}

		});

		toolPanel.add(addNewTwitterAccountButton);

		this.add(toolPanel);
		
		
		
		this.ensureDebugId("cwVerticalPanel");
		
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		Window.alert("Is Updating");
	}
	
	public void removePersona(String personaName) {
		EditableTwitterAccountItem widR = personaWidgets.get(personaName);
		
		if ( widR!= null ) {
			this.twitterAccountsPanel.remove(widR);
		}
		
	}

	private Map<String, EditableTwitterAccountItem> personaWidgets = new HashMap<String, EditableTwitterAccountItem>();
	public void createTwitterAccounts(List<PersonaDTO> personas) {
		
		
		this.twitterAccountsPanel.clear();
		for (PersonaDTO account: personas) {
			//EditableTwitterAccountItem wid = new EditableTwitterAccountItem(account,this);
			//this.twitterAccountsPanel.add(wid);
			//personaWidgets.put(account.getName(), wid);
			addPersona(account);
			
		}
		if ( personas.size()==0 ) {
			this.twitterAccountsPanel.add(new InlineHTML("<h3>You have no accounts</h3>"));
		}
		
		
	}

	public void addPersona(PersonaDTO persona) {
		if (persona.getTwitterAccount()!= null && persona.getTwitterAccount().getIsOAuthenticated() ) {
			EditableTwitterAccountItem wid = new EditableTwitterAccountItem(persona,this);
			this.twitterAccountsPanel.add(wid);
			personaWidgets.put(persona.getName(), wid);
			clearMessages();
		}
		addMessage("You have accounts that need to be authenticated.");
		
	}

	private void clearMessages() {
		messages.clear();
		
	}

	private void addMessage(String string) {
		messages.add(new InlineHTML(string));
		
	}

}
