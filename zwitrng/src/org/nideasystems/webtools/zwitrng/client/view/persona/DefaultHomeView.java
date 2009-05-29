package org.nideasystems.webtools.zwitrng.client.view.persona;




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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DefaultHomeView extends AbstractVerticalPanelView<PersonasListController> {
	private final VerticalPanel formPPanel = new VerticalPanel();
	private final Label twPersonaNameLabel = new Label("Persona Name");

	private final TextBox twPersonaName = new TextBox();

	
	
	public DefaultHomeView() {
		super();
	}

	@Override
	public void init() {
		formPPanel.setSpacing(5);	
		formPPanel.add(new HTML("Create new Persona"));
		formPPanel.add(new HTML("Enter the name of the persona. Later you'll be asked to login into a twitter account."));
		formPPanel.add(twPersonaNameLabel);
		formPPanel.add(twPersonaName);
		
		
		// Return the content
		formPPanel.ensureDebugId("cwVerticalPanel");
		this.add(formPPanel);

		HorizontalPanel toolPanel = new HorizontalPanel();
		Button loginButton = new Button("Create this account");

		loginButton.setWidth("153px");
		loginButton.setHeight("26px");

		loginButton.addClickHandler(new ClickHandler() {
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
				
				

			}

		});

		toolPanel.add(loginButton);

		this.add(toolPanel);
		
		
		
		this.ensureDebugId("cwVerticalPanel");
		
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		Window.alert("Is Updating");
		
		
	}
}
