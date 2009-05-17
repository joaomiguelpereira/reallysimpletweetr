package org.nideasystems.webtools.zwitrng.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.objects.PersonaObj;
import org.nideasystems.webtools.zwitrng.client.utils.JSONUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WindowManager {

	private static WindowManager instance = null;
	private DecoratedTabPanel tabPanel = null;
	private Map<String, Widget> tabs = null;

	private WindowManager() {
		tabs = new HashMap<String, Widget>();
	}

	public static WindowManager getInstance() {
		if (instance == null) {
			instance = new WindowManager();
		}
		return instance;
	}

	public void createPersonasTab(Map<String, PersonaObj> personas) {

		Iterator<String> it = personas.keySet().iterator();

		while (it.hasNext()) {

			String name = it.next();
			this.addPersonaTab(personas.get(name));
		}
	}

	/**
	 * Create the tabbed panel
	 */
	public void createTabbedPannel() {

		tabPanel = new DecoratedTabPanel();
		tabPanel.setWidth("750px");
		tabPanel.setAnimationEnabled(true);

		/*
		 * String[] personaNames = personas.keySet().toArray(new
		 * String[personas.size()]);
		 * 
		 * for (String personaName : personaNames) {
		 * 
		 * tabPanel.add(getPanelForPersona(personas.get(personaName)),
		 * personaName); }
		 */

		tabPanel.add(getDefaultTabPanel(), "+");
		// Return the content
		tabPanel.selectTab(0);
		tabPanel.ensureDebugId("cwTabPanel");
		RootPanel.get("main").add(tabPanel);

	}

	/**
	 * Crete the add tab panel
	 * 
	 * @return
	 */
	private Panel getDefaultTabPanel() {
		Panel returnPanel = null;
		VerticalPanel mainPanel = new VerticalPanel();

		VerticalPanel formPPanel = new VerticalPanel();
		formPPanel.setSpacing(5);
		final Label twPersonaNameLabel = new Label("Persona Name");
		final Label twUserNameLabel = new Label("Twitter User Name");
		final Label twPasswordLabel = new Label("Twiter Password");
		final TextBox twPersonaName = new TextBox();
		final TextBox twUserName = new TextBox();
		final PasswordTextBox twPassword = new PasswordTextBox();

		formPPanel.add(twPersonaNameLabel);
		formPPanel.add(twPersonaName);
		formPPanel.add(twUserNameLabel);
		formPPanel.add(twUserName);
		formPPanel.add(twPasswordLabel);
		formPPanel.add(twPassword);

		// Return the content
		formPPanel.ensureDebugId("cwVerticalPanel");
		mainPanel.add(formPPanel);

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

				PersonaService.getInstance().createPersona(
						jsonObject.toString());
			}

		});

		toolPanel.add(loginButton);

		mainPanel.add(toolPanel);
		mainPanel.ensureDebugId("cwVerticalPanel");

		returnPanel = mainPanel;

		return returnPanel;

	}

	private Widget getPanelForPersona(final PersonaObj personaObj) {
		// Create the main Panel
		VerticalPanel mainPanel = new VerticalPanel();

		// Create the panel with user Info
		HorizontalPanel userInfoPanel = new HorizontalPanel();

		if (personaObj.getTwitterAccount() != null) {
			userInfoPanel.add(new Image(personaObj.getTwitterAccount()
					.getTwitterImageUrl()));
			userInfoPanel.add(new HTML(personaObj.getTwitterAccount()
					.getTwitterScreenName()));
			userInfoPanel.add(new HTML(personaObj.getTwitterAccount()
					.getTwitterUserName()));

			mainPanel.add(userInfoPanel);

		} else {
			userInfoPanel.add(new HTML("The account is invalid or null"));

			mainPanel.add(userInfoPanel);
		}
		// Add the options panel
		HorizontalPanel tools = new HorizontalPanel();
		Button deleteAccountButton = new Button();
		deleteAccountButton.setText("Delete");
		deleteAccountButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PersonaService.getInstance()
						.deletePersona(personaObj.getName());

			}

		});

		tools.add(deleteAccountButton);
		mainPanel.add(tools);
		return mainPanel;

	}

	public void addPersonaTab(PersonaObj persona) {
		Widget wid = getPanelForPersona(persona);
		this.tabs.put(persona.getName(), wid);
		this.tabPanel.add(wid, persona.getName());

	}

	public void removePersonaTab(String name) {
	
		Widget wid = this.tabs.get(name);
		if ( wid != null ) {
			this.tabPanel.remove(wid);
			this.tabPanel.selectTab(0);
		}
		
	}

}
