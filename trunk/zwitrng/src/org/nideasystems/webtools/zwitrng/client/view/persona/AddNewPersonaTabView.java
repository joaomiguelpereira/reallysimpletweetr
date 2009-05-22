package org.nideasystems.webtools.zwitrng.client.view.persona;



import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddNewPersonaTabView extends AbstractVerticalPanelView {
	private final VerticalPanel formPPanel = new VerticalPanel();
	private final Label twPersonaNameLabel = new Label("Persona Name");
	private final Label twUserNameLabel = new Label("Twitter User Name");
	private final Label twPasswordLabel = new Label("Twiter Password");
	private final TextBox twPersonaName = new TextBox();
	private final TextBox twUserName = new TextBox();
	private final PasswordTextBox twPassword = new PasswordTextBox();
	
	public AddNewPersonaTabView() {
		super();
	}

	@Override
	public void init() {
		formPPanel.setSpacing(5);	
		formPPanel.add(twPersonaNameLabel);
		formPPanel.add(twPersonaName);
		formPPanel.add(twUserNameLabel);
		formPPanel.add(twUserName);
		formPPanel.add(twPasswordLabel);
		formPPanel.add(twPassword);

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
				twitterAccountObj.setTwitterScreenName(twUserName.getValue());
				twitterAccountObj.setTwitterPassword(twPassword.getValue());
				persona.setTwitterAccount(twitterAccountObj);

				try {
					getController().getServiceManager().getRPCService().createPersona(persona, new AsyncCallback<PersonaDTO>(){

						@Override
						public void onFailure(Throwable caught) {
							getController().getErrorHandler().addException(caught);
							
						}

						@Override
						public void onSuccess(PersonaDTO result) {
							getController().handleDataLoaded(result);
							
						}
						
					});
				} catch (Exception e) {
					getController().getErrorHandler().addException(e);
				}
			}

		});

		toolPanel.add(loginButton);

		this.add(toolPanel);
		this.ensureDebugId("cwVerticalPanel");
		
	}
}
