package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTODTOList;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.FilterOperator;
import org.nideasystems.webtools.zwitrng.shared.model.TimeUnits;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

public class CampaignsConfigurationWidget extends
		AbstractListConfigurationWidget<CampaignDTO, CampaignDTODTOList> {

	@Override
	public void init() {
		super.init();
		if (isEditable) {
			super.addToolBarNewItemMenu("New Campaign");
		}

	}

	@Override
	protected EditableItem<CampaignDTO, CampaignDTODTOList> createEditableItem() {
		return new EditableCampaign(this);
	}

	@Override
	public void loadData() {
		isCreatingNew = false;
		loadCampaingns();

	}

	private void loadCampaingns() {
		isProcessing(true);
		contentPanel.clear();
		MainController.getInstance().getCurrentPersonaController()
				.loadCampaigns(this);

	}

	@Override
	protected SelectableItem<CampaignDTO, CampaignDTODTOList> createSelectableItem(
			CampaignDTO obj,
			AbstractListConfigurationWidget<CampaignDTO, CampaignDTODTOList> parent) {

		return new SelectableCampaign(obj, parent, true);
	}

	@Override
	public void onSuccessLoadObjects(CampaignDTODTOList list) {

		isProcessing(false);
		for (CampaignDTO campaign : list.getCampaigns()) {
			SelectableItem<CampaignDTO, CampaignDTODTOList> item = createSelectableItem(
					campaign, this);
			addListItem(item);
		}

	}

	@Override
	protected void removeItem(
			SelectableItem<CampaignDTO, CampaignDTODTOList> item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveObject(CampaignDTO object) {
		// TODO Auto-generated method stub

	}

	
	/**
	 * 
	 * @author jpereira
	 *
	 */
	private class EditableCampaign extends EditableItem<CampaignDTO, CampaignDTODTOList> {
	
		private TextBox campaignName; 
		private TextBox filterByTags; 
		private ListBox filterOperator;
		private TextBox filterByText;
		private TextBox startDate;
		private TextBox endDate;
		private TextBox timeBetweenTweets;
		private ListBox timeUnits;
		private TextBox maxUsageOfTemplate;
		
		public EditableCampaign(AbstractListConfigurationWidget<CampaignDTO, CampaignDTODTOList> theParent) {
			super(theParent);
			
			
			InlineHTML textLabel = new InlineHTML("Name: ");
			campaignName = new TextBox();
			contentPanel.add(textLabel);
			contentPanel.add(campaignName);
			
			FlexTable table = new FlexTable();
			
			InlineHTML containingTagsLabel = new InlineHTML("Use templates containing tags:");
			filterByTags = new TextBox();
			table.setWidget(0, 0, containingTagsLabel);
			table.setWidget(1, 0, filterByTags);
			InlineHTML operatorLabel = new InlineHTML();
			filterOperator = new ListBox();
			filterOperator.addItem(FilterOperator.AND.toString(), FilterOperator.AND.name());
			filterOperator.addItem(FilterOperator.OR.toString(), FilterOperator.OR.name());
			
			table.setWidget(0, 1, operatorLabel);
			table.setWidget(1, 1, filterOperator);
			
	
			InlineHTML filterByTextLabel = new InlineHTML("Templates containing text:");
			filterByText = new TextBox();
			table.setWidget(0, 2, filterByTextLabel);
			table.setWidget(1, 2, filterByText);
			filterByText.setWidth("220px");
			filterByTags.setWidth("220px");
			

			contentPanel.add(table);
			/////////
			
			InlineHTML startDateLabel = new  InlineHTML("Start date:");
			startDate = new TextBox();
			startDate.setWidth("220px");
			table.setWidget(2, 0, startDateLabel);
			table.setWidget(3, 0, startDate);
			
			
			InlineHTML endDateLabel = new  InlineHTML("Start date:");
			endDate = new TextBox();
			endDate.setWidth("220px");
			table.setWidget(2, 2, endDateLabel);
			table.setWidget(3, 2, endDate);
						
			
			
			
			///////////////
			
			
			
			FlexTable limitsTable = new FlexTable();
			FlexCellFormatter formatterLimitsTable = limitsTable.getFlexCellFormatter();
			InlineHTML timeBetweenTweetsLabel = new InlineHTML("Wait time between tweets");
			timeBetweenTweets = new TextBox();
			timeBetweenTweets.setWidth("50px");
			limitsTable.setWidget(0, 0, timeBetweenTweetsLabel);
			formatterLimitsTable.setColSpan(0, 0, 2);
			timeUnits = new ListBox();
			timeUnits.addItem(TimeUnits.MINUTES.toString(),TimeUnits.MINUTES.name());
			timeUnits.addItem(TimeUnits.HOURS.toString(),TimeUnits.HOURS.name());
			timeUnits.addItem(TimeUnits.DAYS.toString(),TimeUnits.DAYS.name());
			limitsTable.setWidget(1, 0, timeBetweenTweets);
			limitsTable.setWidget(1, 1, timeUnits);
			
			contentPanel.add(limitsTable);
			
			
			FlexTable maximumTweetsTable = new FlexTable();
			InlineHTML maxUsageOfTemplateLabel = new InlineHTML("Use each template maximum times");
			maxUsageOfTemplate = new TextBox();
			maxUsageOfTemplate.setWidth("50px");
			
			maximumTweetsTable.setWidget(0, 0, maxUsageOfTemplateLabel);

			maximumTweetsTable.setWidget(1,0,maxUsageOfTemplate);
			
			contentPanel.add(maximumTweetsTable);
			
			
		}

		@Override
		public void focus() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void refresh() {
			this.campaignName.setValue(dataObject.getName());
			
			if ( dataObject.getId()!=-1) {
				this.campaignName.setEnabled(false);
			}
			
			DateTimeFormat dtf = DateTimeFormat.getFormat(Constants.DATE_FORMAT);
			this.endDate.setValue(dtf.format(dataObject.getEndDate()));
			this.startDate.setValue(dtf.format(dataObject.getStartDate()));
			this.timeBetweenTweets.setValue(dataObject.getTimeBetweenTweets()+"");
			this.filterByTags.setValue(dataObject.getFilterByTemplateTags());
			this.filterByText.setValue(dataObject.getFilterByTemplateText());
			this.maxUsageOfTemplate.setValue(dataObject.getMaxTweetsPerTemplate()+"");
			
			//Operator & timeunits hack
			if ( dataObject.getFilterOperator().equals(FilterOperator.OR)) {
				
				this.filterOperator.setSelectedIndex(1);	
			} else {
				this.filterOperator.setSelectedIndex(0);	
			}
			
			if ( dataObject.getTimeUnit().equals(TimeUnits.DAYS)) {
				this.timeUnits.setSelectedIndex(2);
			} else if (dataObject.getTimeUnit().equals(TimeUnits.HOURS)) {
				this.timeUnits.setSelectedIndex(1);
			} else {
				this.timeUnits.setSelectedIndex(0);
			}
			
			
			
			
			
			
		
		}

		@Override
		protected void save() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLinksShortened(Map<String, String> result) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	/**
	 * 
	 * @author jpereira
	 *
	 */
	private class SelectableCampaign extends
			SelectableItem<CampaignDTO, CampaignDTODTOList> {

		private InlineHTML nameText;
		private InlineHTML filterText;
		private InlineHTML scheduleText;
		private InlineHTML limitsText;
		private InlineHTML statusText;
		

		public SelectableCampaign(
				CampaignDTO theCampaign,
				AbstractListConfigurationWidget<CampaignDTO, CampaignDTODTOList> theParent,
				boolean isEditable) {
			// Set the parent
			super(theParent, isEditable);
			setDataObject(theCampaign);

			HorizontalPanel nameLine = new HorizontalPanel();
			InlineHTML nameLabel = new InlineHTML("Name: ");
			nameLabel.addStyleName("label");
			nameLine.add(nameLabel);
			nameText = new InlineHTML();
			nameLine.add(nameText);
			// Filter

			content.add(nameLine);
			filterText = new InlineHTML();
			content.add(filterText);

			scheduleText = new InlineHTML();
			content.add(scheduleText);

			limitsText = new InlineHTML();
			content.add(limitsText);
			statusText = new InlineHTML();
			content.add(statusText);
			toolBar = createMenuOptions();
			content.add(toolBar);
			refresh();

		}

		@Override
		public String getSearchableText() {

			return dataObject.getName() + " "
					+ dataObject.getFilterByTemplateTags() + " "
					+ dataObject.getFilterByTemplateText();
		}

		@Override
		protected void refresh() {
			this.nameText.setText(dataObject.getName());
			// this.filterOperator.setText(" "+dataObject.getFilterOperatorAsText()+" ");
			// this.filterTags.setText(dataObject.getFilterByTemplateTags());
			this.filterText.setHTML(getCampaignDescriptionText(dataObject));
			this.scheduleText.setHTML(getCampaignScheduleText(dataObject));
			this.limitsText.setHTML(getCampaignLimitsText(dataObject));
			this.statusText.setHTML(getCampaignStatusText(dataObject));
		}

		private String getCampaignStatusText(CampaignDTO dataObject) {
			StringBuffer sb = new StringBuffer();
			sb.append("Status: ");
			sb.append("<span class=\"label\">");
			sb.append(dataObject.getStatus().toString());
			sb.append("</span>. " );
			
			if (!dataObject.getStatus().equals(CampaignStatus.NOT_STARTED)) {
				sb.append(" Sent: ");
				sb.append("<span class=\"label\">");
				sb.append(dataObject.getTweetsSent());
				sb.append("</span> tweets");
					
			}
			return sb.toString();
			
		}

		private String getCampaignLimitsText(CampaignDTO dataObject) {
			StringBuffer sb = new StringBuffer();

			sb.append("Wait ");
			sb.append("<span class=\"label\">");
			sb.append(dataObject.getTimeBetweenTweets());
			sb.append(" </span> ");
			if ( dataObject.getTimeUnit().equals(TimeUnits.DAYS)) {
				sb.append("days");
			} else if (dataObject.getTimeUnit().equals(TimeUnits.HOURS)) {
				sb.append("hours");
			} else {
				sb.append("minutes");
			}
			
			sb.append("between tweets. ");
			
			sb.append(" Use each template a maximum of  ");
			sb.append("<span class=\"label\">");
			sb.append(dataObject.getMaxTweetsPerTemplate());
			sb.append("</span> times.");

			return sb.toString();

		}

		private String getCampaignDescriptionText(CampaignDTO theCampaign) {
			StringBuffer sb = new StringBuffer();

			if (theCampaign.getFilterByTemplateTags() != null
					&& !theCampaign.getFilterByTemplateTags().trim().isEmpty()) {
				sb.append("Use templates containing tags :\"");
				sb.append("<span class=\"label\">");

				sb.append(theCampaign.getFilterByTemplateTags());
				sb.append("\"</span> ");

				if (theCampaign.getFilterByTemplateText() != null
						&& !theCampaign.getFilterByTemplateText().trim()
								.isEmpty()) {
					sb.append(theCampaign.getFilterOperatorAsText());
				}
			}

			if (theCampaign.getFilterByTemplateText() != null
					&& !theCampaign.getFilterByTemplateText().trim().isEmpty()) {

				if (theCampaign.getFilterByTemplateTags() != null
						&& !theCampaign.getFilterByTemplateTags().trim()
								.isEmpty()) {
					sb.append(" containing text: \"");
				} else {
					sb.append("Use templates containing text: \"");
				}

				sb.append("<span class=\"label\">");
				sb.append(theCampaign.getFilterByTemplateText());
				sb.append("\"</span>");

			}
			return sb.toString();
		}

		private String getCampaignScheduleText(CampaignDTO dataObject) {

			StringBuffer sb = new StringBuffer();
			
			DateTimeFormat sdf = DateTimeFormat.getFormat(Constants.DATE_FORMAT);
			// new DateTimeFormat("EEE, MMM d, yyyy");
			sb.append("Starts  ");
			sb.append("<span class=\"label\">");
			sb.append(sdf.format(dataObject.getStartDate()));
			sb.append("</span>");
			sb.append(" and ends  ");
			sb.append("<span class=\"label\">");

			sb.append(sdf.format(dataObject.getEndDate()));
			sb.append("</span>");

			return sb.toString();

		}

	}

}
