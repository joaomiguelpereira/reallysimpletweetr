package org.nideasystems.webtools.zwitrng.client.view.widgets;


import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;


public class PersonaToolsWidget extends AbstractVerticalPanelView{
	Button deleteBt = null;
	HorizontalPanel buttons = null;
	HorizontalPanel newStatus= null;
	TextArea newStatusTxa = null;
	Button sendBt = null;
	
	
	public PersonaToolsWidget() {
		super();
		
		
		
	}

	@Override
	public void init() {
		
		buttons = new HorizontalPanel();
		newStatus = new HorizontalPanel();
		this.deleteBt = new Button();
		this.deleteBt.setText("Delete");
		this.deleteBt.setTitle("Delete");
		this.deleteBt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getController().handleAction("DELETE");
				
/*				RPCService.getInstance()
				.deletePersona(persona.getName());*/
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
