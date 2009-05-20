package org.nideasystems.webtools.zwitrng.client.view.widgets;

import org.nideasystems.webtools.zwitrng.client.services.RPCService;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaObj;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PersonaToolsWidget extends VerticalPanel {
	Button deleteBt = null;
	HorizontalPanel buttons = null;
	HorizontalPanel newStatus= null;
	TextArea newStatusTxa = null;
	Button sendBt = null;
	private PersonaObj persona;
	
	public PersonaToolsWidget(final PersonaObj personaObj) {
		super();
		this.persona = personaObj;
		buttons = new HorizontalPanel();
		newStatus = new HorizontalPanel();
		this.deleteBt = new Button();
		this.deleteBt.setText("Delete");
		this.deleteBt.setTitle("Delete");
		this.deleteBt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RPCService.getInstance()
				.deletePersona(persona.getName());
			}
			
		});
		
		 
		buttons.add(deleteBt);
		
		newStatusTxa = new TextArea();
		newStatusTxa.setWidth("400px");
		
		newStatusTxa.setHeight("50px");
		
		
		sendBt = new Button();
		sendBt.setText("Send");
		sendBt.setTitle("Send");
		
		newStatus.add(newStatusTxa);
		newStatus.add(sendBt);
		
		super.add(buttons);
		super.add(newStatus);
		
		
	}
}
