package org.nideasystems.webtools.zwitrng.client.view.configuration;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.configuration.ConfigurationController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;

import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CampaignsConfigurationView extends AbstractVerticalPanelView<ConfigurationController>{

	private ExpandablePanel selectedPanel = null;
	@Override
	public void init() {
		this.setSpacing(1);
		assert(getController()!=null);
		//Add a tab
		DecoratedTabPanel options = new DecoratedTabPanel();
		options.setAnimationEnabled(true);
		options.add(new InlineHTML("tabe"), "Campaigns");
		options.add(new InlineHTML("tabe"), "Templates");
		options.add(new InlineHTML("tabe"), "Lists");
		options.add(new InlineHTML("tabe"), "Feeds");
		
		
		this.add(options);
		
		
		
		
		ExpandablePanel templates = new ExpandablePanel();
		templates.setTitleText("Configure Templates");
		
		TemplatesConfigurationWidget templatesConfWidget = new TemplatesConfigurationWidget();
		//templatesConfWidget.setController(getController());
		templates.setConfigWidget(templatesConfWidget);
		this.add(templates);
		
		
		ExpandablePanel recommendations = new ExpandablePanel();
		recommendations.setTitleText("Configure template fragments");
		TemplateFragmentsConfigurationWidget templatesFragmentsConfigWidget = new TemplateFragmentsConfigurationWidget();
		//templatesFragmentsConfigWidget.setController(getController());
		recommendations.setConfigWidget(templatesFragmentsConfigWidget);
		this.add(recommendations);
		
		
		ExpandablePanel feedSet = new ExpandablePanel();
		feedSet.setTitleText("Configure Feeds");
		FeedSetConfigurationWidget feedSetcw = new FeedSetConfigurationWidget();
		feedSet.setConfigWidget(feedSetcw);
		this.add(feedSet);
		
		ExpandablePanel campaingns = new ExpandablePanel();
		campaingns.setTitleText("Configure campaigns");
		CampaignsConfigurationWidget ccw = new CampaignsConfigurationWidget();
		campaingns.setConfigWidget(ccw);
		this.add(campaingns);

		

	}

	@Override
	public void isUpdating(boolean isUpdating) {
		// TODO Auto-generated method stub
		
	}
	
	private class ExpandablePanel extends VerticalPanel  {
		AbstractListConfigurationWidget configWidget = null;
		HorizontalPanel header = null;
		String headerText = "";
		InlineHTML headerTextHtml = null;
		Image headerImg = null;
		VerticalPanel mainContent = null;
		boolean isInitialized = false;
		
		//static final String WIDTH = "650px";
		
		public ExpandablePanel() {
			
			header = new HorizontalPanel();
			header.addStyleName("expandable_panel_header");
			
			FlexTable headerTable = new FlexTable();
			
			header.setWidth(Constants.CONFIGURATION_PANEL_WIDTH);
			header.setSpacing(1);
			header.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			headerImg = new Image(Constants.TEMPLATES_ICON_IMAGE);
			
			headerTable.setWidget(0, 0, headerImg);
			

			headerTextHtml = new InlineHTML();
			headerTextHtml.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			headerTextHtml.setHTML(headerText);
			headerTable.setWidget(0, 1, headerTextHtml);
			header.add(headerTable);
			
			this.add(header);
			headerTextHtml.addStyleName("link");
			headerTextHtml.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					selectPanel();
					
					
				}
				
			});
			mainContent = new VerticalPanel();
			mainContent.setVisible(false);
			
			this.add(mainContent);
			
		}
		
		public void setConfigWidget(
				AbstractListConfigurationWidget configurationWidget) {
			this.configWidget = configurationWidget;
			
		}

		private void selectPanel() {
			
			if ( !isInitialized) {
				init();
			}
			
			
			mainContent.setVisible(!mainContent.isVisible());
			if (mainContent.isVisible()) {
				addStyleName("expandable_panel");
				header.addStyleName("expandable_panel_header_selected");
				selectedPanel = this;
				configWidget.loadData();
				
			} else {
				removeStyleName("expandable_panel");
				header.removeStyleName("expandable_panel_header_selected");
				selectedPanel = null;
			
			}
		}
		private void init() {
			configWidget.init();
			mainContent.add(configWidget);
			isInitialized = true;
			
		}

		public void setTitleText(String text) {
			headerTextHtml.setHTML(text);
			
		}
	}

}
