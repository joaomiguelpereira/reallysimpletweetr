package org.nideasystems.webtools.zwitrng.client.view;

import org.nideasystems.webtools.zwitrng.client.services.RPCService;
import org.nideasystems.webtools.zwitrng.client.utils.JSONUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddNewPersonaView extends VerticalPanel {
	private final VerticalPanel formPPanel = new VerticalPanel();
	private final Label twPersonaNameLabel = new Label("Persona Name");
	private final Label twUserNameLabel = new Label("Twitter User Name");
	private final Label twPasswordLabel = new Label("Twiter Password");
	private final TextBox twPersonaName = new TextBox();
	private final TextBox twUserName = new TextBox();
	private final PasswordTextBox twPassword = new PasswordTextBox();
	
	public AddNewPersonaView() {
		super();
		
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

				JSONObject jsonObject = JSONUtils.createNewPersonaJSonRequest(
						twPersonaName.getValue(), twUserName.getValue(),
						twPassword.getValue());

				RPCService.getInstance().createPersona(
						jsonObject.toString());
			}

		});

		toolPanel.add(loginButton);

		this.add(toolPanel);
		this.ensureDebugId("cwVerticalPanel");

	}
}
