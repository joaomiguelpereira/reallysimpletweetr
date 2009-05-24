package org.nideasystems.webtools.zwitrng.client.view.widgets;



import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;


public class PersonaToolsWidget extends AbstractVerticalPanelView{
	Button deleteBt = null;
	HorizontalPanel buttons = null;
	HorizontalPanel newStatus= null;
	TextArea newStatusTxa = null;
	Button sendBt = null;
	private final Label remainingChars = new Label("140");
	private final Label remainingCharsLabel = new Label("Remaining Characters :");
	
	
	
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
				getController().handleAction(IController.IActions.DELETE);
			}
			
		});
		
		 
		buttons.add(deleteBt);
		
		//Build the statuc update (max 140 chars)
		newStatusTxa = new TextArea();
		newStatusTxa.setWidth("700px");
		newStatusTxa.setHeight("35px");
		newStatusTxa.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				Integer remainingCharsVal =140 - newStatusTxa.getValue().length(); 
				//Window.alert(remainingCharsVal.toString());
				remainingChars.setText(remainingCharsVal.toString());
				if ( remainingCharsVal < 0 ) {
					remainingChars.setStyleName("error");
				} else {
					remainingChars.removeStyleName("error");
				}
				
			}
			
		});
				
		
		
		newStatus.add(newStatusTxa);
		//newStatus.add(sendBt);
		
		super.add(buttons);
		super.add(newStatus);
		
		//info about the status
		HorizontalPanel updateInfo = new HorizontalPanel();
		updateInfo.setSpacing(5);
		sendBt = new Button();
		sendBt.setText("Send");
		sendBt.setTitle("Send");
		sendBt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getController().handleAction(IController.IActions.TWEET_THIS, newStatusTxa.getValue());
				
			}
			
		});
		HorizontalPanel leftPanel = new HorizontalPanel();
		leftPanel.add(remainingCharsLabel);
		leftPanel.add(remainingChars);
		leftPanel.setWidth("200px");
		updateInfo.add(leftPanel);
		
		
		//Middle pannel here
		HorizontalPanel middelPannel = new HorizontalPanel();
		
		middelPannel.setWidth("400px");
		middelPannel.add(new HTML("Innovation with twitter :)"));
		updateInfo.add(middelPannel);
		
		HorizontalPanel rightPanel = new HorizontalPanel();
		rightPanel.add(sendBt);
		updateInfo.add(rightPanel);
		
		//HTML whatImDoing = new HTML("Im dskdj ksdf sdkfj sdfk");
		
		super.add(updateInfo);		
		//super.add(whatImDoing);
		
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		Window.alert("Is Updating");
		
	}

	public void refresh() {
		newStatusTxa.setValue("");
		remainingChars.setText("140");
		
	}

	
}
