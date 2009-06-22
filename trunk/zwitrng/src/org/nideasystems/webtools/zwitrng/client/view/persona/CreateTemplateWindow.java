package org.nideasystems.webtools.zwitrng.client.view.persona;

import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.controller.updates.ShortLinksListenerCallBack;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CreateTemplateWindow extends DialogBox implements ShortLinksListenerCallBack, CreateTemplateCallBack {

	private PersonaController controller;
	private TextArea templateText = null;
	private InlineHTML remainingChars = null;
	private CreateTemplateWindow instance = null;
	private TemplateList templateList = null;

	public CreateTemplateWindow(PersonaController theController, TemplateList theTemplateList ) {
		this.setController(theController);
		this.templateList  = theTemplateList;
	}
	
	public void init() {
		this.setText("Create new template for "+controller.getModel().getName());
		this.setTitle("Create new template for "+controller.getModel().getName());
		this.setAnimationEnabled(true);
		this.setModal(true);
		instance = this;
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(new InlineHTML("Inser template text:"));
		templateText = new TextArea();
		templateText.setWidth("620px");
		// update.setHeight("35px");
		templateText.addStyleName("input");
		templateText.setVisibleLines(2);
		mainPanel.add(templateText);

		remainingChars = new InlineHTML("140");
		mainPanel.add(remainingChars);

		
		InlineHTML shortLinksLink = new InlineHTML("Short Links");
		shortLinksLink.addStyleName("link");
		mainPanel.add(shortLinksLink);
		
		
		mainPanel.add(new InlineHTML("Add tags separaded by spaces:"));
		final TextBox templateTags = new TextBox();
		mainPanel.add(templateTags);
		
		HorizontalPanel toolsPanel = new HorizontalPanel();
		toolsPanel.setSpacing(5);
		
				
		InlineHTML closeLink = new InlineHTML("Cancel");
		closeLink.addStyleName("link");
		closeLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide(true);
				
			}
			
		});
		Button saveButton = new Button("Save");
		toolsPanel.add(closeLink);
		toolsPanel.add(saveButton);
		mainPanel.add(toolsPanel);
		
		this.add(mainPanel);
		
		//Add handlers
		
		
		templateText.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				updateRemainingChars();
				
			}

		});
		
		shortLinksLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SendUpdateWidget.shortLinks(templateText.getValue(), controller, instance);
			}
			
		});
		
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if ( templateText.getValue().trim().length()>0 ) {
					controller.createTemplate(templateText.getValue(), templateTags.getValue(), instance);
				}
			}
			
		});
	}

	private void updateRemainingChars() {
		int remainingCharsNbr =  SendUpdateWidget.DEFAULT_TWEET_SIZE-this.templateText.getValue().length();
		remainingChars.setHTML(""+remainingCharsNbr);
		
	}
	
	public void setController(PersonaController controller) {
		this.controller = controller;
	}

	public PersonaController getController() {
		return controller;
	}

	@Override
	public void onLinksShortened(Map<String, String> result) {
		String newStr = HTMLHelper.get().replaceText(templateText.getValue(), result);
		templateText.setFocus(true);
		templateText.setValue(newStr);
		updateRemainingChars();
		
	}

	@Override
	public void onFailCreateTemplate(Throwable ex) {
		getController().getMainController().addException(ex);
		
	}

	@Override
	public void onSuccessCreateTemplate(TemplateDTO template) {
		getController().getMainController().addInfoMessage("Template created with sucess");
		this.hide(true);
		if (templateList!=null) {
			templateList.onNewTemplate(template);
		}
		
	}
}
