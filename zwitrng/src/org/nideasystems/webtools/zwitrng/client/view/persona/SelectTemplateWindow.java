package org.nideasystems.webtools.zwitrng.client.view.persona;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.view.configuration.ConfigurationListSelectListener;
import org.nideasystems.webtools.zwitrng.client.view.configuration.TemplatesConfigurationWidget;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
		mainPanel.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);
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
		
		String[] userNames = StringUtils.getUserNames(updateWidget.getText());
		String newTemplateText = obj.getTemplateText();

		if (userNames.length > 0) {
			for (int i = 0; i < userNames.length; i++) {
				newTemplateText = newTemplateText.replaceAll("\\Q{username_"
						+ i + "}\\E", userNames[i]);
			}

		}

		// Randomize if needed

		newTemplateText = StringUtils.randomizeString(newTemplateText);
		updateWidget.setTemplateText(newTemplateText);
		this.hide(true);
	}

}
