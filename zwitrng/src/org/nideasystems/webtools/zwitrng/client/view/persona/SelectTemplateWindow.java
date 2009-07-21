package org.nideasystems.webtools.zwitrng.client.view.persona;

import java.util.ArrayList;
import java.util.List;


import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.view.configuration.ConfigurationListSelectListener;
import org.nideasystems.webtools.zwitrng.client.view.configuration.TemplatesConfigurationWidget;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SelectTemplateWindow extends PopupPanel implements
		ConfigurationListSelectListener<TemplateDTO> {

	private TemplatesConfigurationWidget templatesConfWidget;
	private SendUpdateWidget updateWidget;

	

	public void loadTemplates() {
		templatesConfWidget.loadData();
	}

	public void init() {

		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);

		templatesConfWidget = new TemplatesConfigurationWidget();
		templatesConfWidget.setMaxHeight("200px");
		templatesConfWidget.setEditable(false);

		templatesConfWidget.addSelectListener(this);
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth(Constants.MAIN_LIST_ITEM_WIDTH);
		mainPanel.add(templatesConfWidget);
		InlineHTML closeLink = new InlineHTML("Close");
		closeLink.addStyleName("link");
		closeLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide(true);
			}

		});
		mainPanel.add(closeLink);
		this.setWidget(mainPanel);
		templatesConfWidget.init();

	

	}

	
	public void setSendTwitterUpdateWidget(SendUpdateWidget aWidget) {
		this.updateWidget = aWidget;

	}

	

	@Override
	public void onItemSelected(TemplateDTO obj) {
		//check if it contains any list that needs to be loaded

		//Window.alert(this.updateWidget.getText());
		String[] userNames = StringUtils.getUserNames(this.updateWidget.getText())  ;
		//List<String> lists = StringUtils.getFragmentLists(obj.getTemplateText());
		List<String> userNamesList = new ArrayList<String>();
		for (String uName: userNames ) {
			userNamesList.add(uName.trim());
		}
		buildTweetFromTemplate(obj,userNamesList);
		/*
		if (lists.size()>0) {
			
			buildTweetFromTemplate(obj,lists);
		
		} else {
			Window.alert("Selected");
			updateTemplateText(obj);
			
		}*/
		
	}

	private void buildTweetFromTemplate(final TemplateDTO template,List<String> userNames) {
		//call server
		this.updateWidget.isUpdating(true);
		try {
			
			MainController.getInstance().getCurrentPersonaController().buildTweetFromTemplate(template, userNames, new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Failed");
					updateWidget.isUpdating(false);
					MainController.getInstance().addException(caught);
					replaceTemplate(template.getTemplateText());
				}

				@Override
				public void onSuccess(String result) {
					updateWidget.isUpdating(false);
					replaceTemplate(result);
					
				}

				
				
			});
		} catch (Exception e) {
			updateWidget.isUpdating(false);
			MainController.getInstance().addException(e);
			replaceTemplate(template.getTemplateText());
			e.printStackTrace();
		}

		
		/*
		try {
			MainController.getInstance().getCurrentPersonaController().loadTemplateFragmentsLists(lists, new AsyncCallback<Map<String, String>>() {

				@Override
				public void onFailure(Throwable caught) {
					updateWidget.isUpdating(false);
					MainController.getInstance().addException(caught);
					updateTemplateText(obj);
				}

				@Override
				public void onSuccess(Map<String, String> result) {
					updateWidget.isUpdating(false);
					replaceLoadedLists(obj,result);
					
				}

				
				
			});
		} catch (Exception e) {
			updateWidget.isUpdating(false);
			MainController.getInstance().addException(e);
			updateTemplateText(obj);
			e.printStackTrace();
		}
		*/
		
		
	}

	private void replaceTemplate(String result) {
		updateWidget.setTemplateText(result);
		//updateTemplateText(result);
		hide(true);
	}
	private void updateTemplateText(TemplateDTO obj) {
		String[] userNames = StringUtils.getUserNames(updateWidget.getText());
		String newTemplateText = obj.getTemplateText();

		if (userNames.length > 0) {
			for (int i = 0; i < userNames.length; i++) {
				newTemplateText = newTemplateText.replace("{username_"
						+ i + "}", userNames[i]);
			}

		}

		// Randomize if needed

		newTemplateText = StringUtils.randomizeTemplate(newTemplateText);
		updateWidget.setTemplateText(newTemplateText);
		this.hide(true);
		
	}

}
