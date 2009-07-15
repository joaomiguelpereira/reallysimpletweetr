package org.nideasystems.webtools.zwitrng.client.view.persona;

import java.util.List;
import java.util.Map;

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
		List<String> lists = StringUtils.getFragmentLists(obj.getTemplateText());
		if (lists.size()>0) {
			loadListsContents(obj,lists);
		} else {
			updateTemplateText(obj);
			
		}
		
	}

	private void loadListsContents(final TemplateDTO obj,List<String> lists) {
		//call server
		this.updateWidget.isUpdating(true);
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
		updateTemplateText(obj);
		
	}

	private void replaceLoadedLists(TemplateDTO obj,
			Map<String, String> result) {
		
		String newvalue = StringUtils.replaceFragmentsLists(obj.getTemplateText(),result);
		obj.setTemplateText(newvalue);
		updateTemplateText(obj);
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

		newTemplateText = StringUtils.randomizeString(newTemplateText);
		updateWidget.setTemplateText(newTemplateText);
		this.hide(true);
		
	}

}
