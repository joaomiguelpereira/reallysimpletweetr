package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.Date;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTODTOList;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.TimeUnits;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.datepicker.client.DateBox;

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

		setCreatingNew(false);
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

		MainController.getInstance().getCurrentPersonaController()
				.removeCampaign(item.dataObject, item);

	}

	@Override
	public void saveObject(CampaignDTO object) {
		if (object.getId() == -1) {
			createNewCampaign(object);
		} else {
			saveCampaign(object);
		}

	}

	private void saveCampaign(CampaignDTO object) {
		MainController.getInstance().getCurrentPersonaController()
				.saveCampaign(object, getSelectedItem());

	}

	private void createNewCampaign(CampaignDTO object) {
		MainController.getInstance().getCurrentPersonaController()
				.createCampaign(object, this);

	}

	/**
	 * 
	 * @author jpereira
	 * 
	 */
	private class EditableCampaign extends
			EditableItem<CampaignDTO, CampaignDTODTOList> {

		private TextBox campaignName;
		private TextBox filterByTags;
		// private ListBox filterOperator;
		// private TextBox filterByText;
		private DateBox startDate;
		private DateBox endDate;
		private TextBox timeBetweenTweets;
		private ListBox timeUnits;
		private TextBox maxTweets;
		private InlineHTML runningDays;
		private ListBox endHourOfTheDay;
		private ListBox startHourOfTheDay;

		public EditableCampaign(
				AbstractListConfigurationWidget<CampaignDTO, CampaignDTODTOList> theParent) {
			super(theParent);

			InlineHTML textLabel = new InlineHTML("Name: ");
			campaignName = new TextBox();
			contentPanel.add(textLabel);
			contentPanel.add(campaignName);

			FlexTable table = new FlexTable();

			InlineHTML containingTagsLabel = new InlineHTML(
					"Use templates containing tags:");
			filterByTags = new TextBox();
			table.setWidget(0, 0, containingTagsLabel);
			table.setWidget(1, 0, filterByTags);
		
			filterByTags.setWidth("220px");

			contentPanel.add(table);
			// ///////

			InlineHTML startDateLabel = new InlineHTML("Start date:");
			// startDate = new TextBox();
			startDate = new DateBox();
			startDate.setValue(new Date());
			startDate.setWidth("220px");
			table.setWidget(2, 0, startDateLabel);
			table.setWidget(3, 0, startDate);

			InlineHTML endDateLabel = new InlineHTML("Start date:");
			endDate = new DateBox();
			endDate.setWidth("220px");
			endDate.setValue(new Date());
			table.setWidget(2, 2, endDateLabel);
			table.setWidget(3, 2, endDate);
			runningDays = new InlineHTML();
			runningDays.addStyleName("inline-form-inline-help");
			contentPanel.add(runningDays);

			// /////////////

			FlexTable limitsTable = new FlexTable();
			FlexCellFormatter formatterLimitsTable = limitsTable
					.getFlexCellFormatter();
			InlineHTML timeBetweenTweetsLabel = new InlineHTML(
					"Wait time between tweets");
			timeBetweenTweets = new TextBox();
			timeBetweenTweets.setValue("20");
			timeBetweenTweets.setWidth("50px");
			limitsTable.setWidget(0, 0, timeBetweenTweetsLabel);
			formatterLimitsTable.setColSpan(0, 0, 2);
			timeUnits = new ListBox();
			timeUnits.addItem(TimeUnits.MINUTES.toString(), TimeUnits.MINUTES
					.name());
			timeUnits.addItem(TimeUnits.HOURS.toString(), TimeUnits.HOURS
					.name());
			timeUnits.addItem(TimeUnits.DAYS.toString(), TimeUnits.DAYS.name());
			limitsTable.setWidget(1, 0, timeBetweenTweets);
			limitsTable.setWidget(1, 1, timeUnits);

			limitsTable.setWidget(0, 1, new InlineHTML(
					"Run between hours of Day"));
			formatterLimitsTable.setColSpan(0, 1, 3);
			startHourOfTheDay = new ListBox();
			for (int i = 0; i < 24; i++) {
				startHourOfTheDay.addItem(i + ":00", i + "");
			}
			endHourOfTheDay = new ListBox();
			for (int i = 1; i < 24; i++) {
				endHourOfTheDay.addItem(i + ":59", i + "");
			}

			startHourOfTheDay.setSelectedIndex(0);
			endHourOfTheDay.setSelectedIndex(22);

			limitsTable.setWidget(1, 2, startHourOfTheDay);
			limitsTable.setWidget(1, 3, endHourOfTheDay);

			contentPanel.add(limitsTable);

			FlexTable maximumTweetsTable = new FlexTable();
			InlineHTML maxUsageOfTemplateLabel = new InlineHTML(
					"Send max of Tweets");
			maxTweets = new TextBox();
			maxTweets.setWidth("50px");
			maxTweets.setValue("10");

			maximumTweetsTable.setWidget(0, 0, maxUsageOfTemplateLabel);

			maximumTweetsTable.setWidget(1, 0, maxTweets);

			contentPanel.add(maximumTweetsTable);

			this.startDate.setFormat(new DateFormatter());
			this.endDate.setFormat(new DateFormatter());

			this.startDate
					.addValueChangeHandler(new ValueChangeHandler<Date>() {

						@Override
						public void onValueChange(ValueChangeEvent<Date> event) {
							updateRunningDays();

						}

					});

			this.endDate.addValueChangeHandler(new ValueChangeHandler<Date>() {

				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					updateRunningDays();

				}

			});

		}

		private void updateRunningDays() {
			StringBuffer sb = new StringBuffer();
			// "Running for: xx days";
			if (this.startDate.getValue() == null
					|| this.endDate.getValue() == null) {
				this.runningDays.setText("");
			}
			if (this.startDate.getValue().after(this.endDate.getValue())) {

				this.runningDays
						.setText("Error: End date is greater than start date");
			} else if (this.startDate.getValue().compareTo(
					this.endDate.getValue()) == 0) {
				this.runningDays
						.setText("Error: End date and start date are the same");
			} else {
				long iDays = this.endDate.getValue().getTime()
						- this.startDate.getValue().getTime();

				long days = iDays / (60 * 60 * 24 * 1000);
				sb.append("Run during ");
				sb.append(days);
				sb.append(" days");

				this.runningDays.setText(sb.toString());

			}

		}

		private class DateFormatter implements DateBox.Format {
			@Override
			public String format(DateBox dateBox, Date date) {
				DateTimeFormat dtf = DateTimeFormat
						.getFormat(Constants.DATE_FORMAT);
				String retValue = "";
				if (date != null) {
					retValue = dtf.format(date);
				}
				return retValue;

			}

			@Override
			public Date parse(DateBox dateBox, String text, boolean reportError) {
				return new DateBox.DefaultFormat().parse(dateBox, text,
						reportError);

			}

			@Override
			public void reset(DateBox dateBox, boolean abandon) {
				new DateBox.DefaultFormat().reset(dateBox, abandon);

			}
		}

		@Override
		public void focus() {
			if (dataObject != null) {
				this.filterByTags.setFocus(true);
			} else {
				this.campaignName.setFocus(true);
			}

		}

		@Override
		protected void save() {
			boolean isValid = true;

			if (this.campaignName.getValue().trim().isEmpty()) {
				MainController.getInstance().addError(
						"Please provide a name for the campaign");
				isValid = false;
			}

			if (this.filterByTags.getValue().trim().isEmpty()
			/* && this.filterByText.getValue().trim().isEmpty() */) {
				MainController
						.getInstance()
						.addError(
								"Please provide some tags so we can find your templates");
				isValid = false;
			}
			if (this.campaignName.getValue().trim().isEmpty()) {
				MainController.getInstance().addError(
						"Please provide a name for the campaign.");
				isValid = false;
			}

			if (startDate.getValue() == null || endDate.getValue() == null) {
				MainController
						.getInstance()
						.addError(
								"Please provide a start and end date for the campaign.");
				isValid = false;

			}

			if (this.startDate.getValue() == null
					|| this.endDate.getValue() == null
					|| this.startDate.getValue().after(this.endDate.getValue())
					|| (this.startDate.getValue().compareTo(
							this.endDate.getValue()) == 0)) {
				MainController.getInstance().addError(
						"Please provide a end date later than the start date.");
				isValid = false;
			}

			if (this.timeBetweenTweets.getValue().trim().isEmpty()) {
				MainController
						.getInstance()
						.addError(
								"Please provide the time we should wait between your tweets.");
				isValid = false;
			} else {

				try {
					Integer.valueOf(this.maxTweets.getValue());
				} catch (NumberFormatException e) {
					MainController
							.getInstance()
							.addError(
									"Please provide a valid number for the time we should wait between tweets!");
					isValid = false;

				}

			}

			if (this.maxTweets.getValue().trim().isEmpty()) {
				MainController
						.getInstance()
						.addError(
								"Please provide the number of times we should reuse each of the templates.");
				isValid = false;
			} else {

				try {
					Integer.valueOf(this.maxTweets.getValue());
				} catch (NumberFormatException e) {
					MainController
							.getInstance()
							.addError(
									"Please provide a valid number for the usage of each template!");
					isValid = false;

				}

				if (isValid) {
					CampaignDTO campaign = new CampaignDTO();
					if (dataObject != null) {
						campaign.setId(dataObject.getId());
					}
					campaign.setName(this.campaignName.getValue());

				}

			}

			if ((this.timeUnits.getValue(this.timeUnits.getSelectedIndex()))
					.equals(TimeUnits.MINUTES.name())) {
				// Validate multiple of 10

				Integer val = 5;
				try {
					val = Integer.valueOf(this.timeBetweenTweets.getValue());
				} catch (NumberFormatException e) {

					// validated before
					isValid = false;

				}

				if ((val % 5) > 0) {

					MainController
							.getInstance()
							.addError(
									"The interval in minutes we use to send tweets is allways multiples of 5.\nProvide a multiple of 5!");
					isValid = false;

				}
			}

			if (Integer.valueOf(this.startHourOfTheDay
					.getValue(this.startHourOfTheDay.getSelectedIndex())) > Integer
					.valueOf(this.endHourOfTheDay.getValue(this.endHourOfTheDay
							.getSelectedIndex()))) {
				isValid = false;
				MainController
						.getInstance()
						.addError(
								"The start hour of the day is later than the end hour. This Campaign will not run.");

			}
			if (isValid) {
				CampaignDTO campaign = new CampaignDTO();
				if (dataObject != null) {
					campaign.setId(dataObject.getId());
				}
				campaign.setName(this.campaignName.getValue());
				campaign.setEndDate(this.endDate.getValue());
				campaign.setFilterByTemplateTags(this.filterByTags.getValue());
				// campaign.setFilterByTemplateText(this.filterByText.getValue());
				/*
				 * campaign.setFilterOperator(FilterOperator
				 * .valueOf(this.filterOperator .getValue(this.filterOperator
				 * .getSelectedIndex())));
				 */
				campaign.setMaxTweets(Integer
						.valueOf(this.maxTweets.getValue()));
				campaign.setStartDate(this.startDate.getValue());
				// campaign.setStatus(status)
				campaign.setTimeBetweenTweets(Integer
						.valueOf(this.timeBetweenTweets.getValue()));
				campaign.setTimeUnit(TimeUnits.valueOf(this.timeUnits
						.getValue(this.timeUnits.getSelectedIndex())));

				campaign.setStartHourOfTheDay(Integer
						.valueOf(this.startHourOfTheDay
								.getValue(this.startHourOfTheDay
										.getSelectedIndex())));
				campaign.setEndHourOfTheDay(Integer
						.valueOf(this.endHourOfTheDay
								.getValue(this.endHourOfTheDay
										.getSelectedIndex())));
				setUpdating(true);
				parent.saveObject(campaign);

			}

		}

		@Override
		public void refresh() {
			this.campaignName.setValue(dataObject.getName());

			if (dataObject.getId() != -1) {
				this.campaignName.setEnabled(false);
			}

			this.endDate.setValue(dataObject.getEndDate());
			this.startDate.setValue(dataObject.getStartDate());
			this.timeBetweenTweets.setValue(dataObject.getTimeBetweenTweets()
					+ "");
			this.filterByTags.setValue(dataObject.getFilterByTemplateTags());
			// this.filterByText.setValue(dataObject.getFilterByTemplateText());
			this.maxTweets.setValue(dataObject.getMaxTweets() + "");

			// Operator & timeunits hack
			/*
			 * if (dataObject.getFilterOperator().equals(FilterOperator.OR)) {
			 * 
			 * this.filterOperator.setSelectedIndex(1); } else {
			 * this.filterOperator.setSelectedIndex(0); }
			 */

			if (dataObject.getTimeUnit().equals(TimeUnits.DAYS)) {
				this.timeUnits.setSelectedIndex(2);
			} else if (dataObject.getTimeUnit().equals(TimeUnits.HOURS)) {
				this.timeUnits.setSelectedIndex(1);
			} else {
				this.timeUnits.setSelectedIndex(0);
			}

			this.startHourOfTheDay.setSelectedIndex(dataObject
					.getStartHourOfTheDay());
			this.endHourOfTheDay.setSelectedIndex(dataObject
					.getEndHourOfTheDay() - 1);
			updateRunningDays();

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
					+ dataObject.getStatus().toString();
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
			sb.append("</span>. ");

			if (!dataObject.getStatus().equals(CampaignStatus.NOT_STARTED)) {
				sb.append(" Sent: ");
				sb.append("<span class=\"label\">");
				sb.append(dataObject.getTweetsSent());
				sb.append("</span> tweets.");
				DateTimeFormat dtf = DateTimeFormat
						.getFormat(Constants.DATE_TIME_FORMAT);
				if (dataObject.getLastRun() != null) {

					sb.append(" Last tweet sent at: "
							+ dtf.format(dataObject.getLastRun()));
				}

				if (dataObject.getNextRun() != null) {
					sb.append(" Next tweet will be sent at: "
							+ dtf.format(dataObject.getNextRun()));
				}

			}
			return sb.toString();

		}

		private String getCampaignLimitsText(CampaignDTO dataObject) {
			StringBuffer sb = new StringBuffer();

			sb.append("Wait ");
			sb.append("<span class=\"label\">");
			sb.append(dataObject.getTimeBetweenTweets());
			sb.append(" </span> ");
			if (dataObject.getTimeUnit().equals(TimeUnits.DAYS)) {
				sb.append("days ");
			} else if (dataObject.getTimeUnit().equals(TimeUnits.HOURS)) {
				sb.append("hours ");
			} else {
				sb.append("minutes ");
			}

			sb.append(" between tweets. ");

			sb.append(" Don't send more than ");
			sb.append("<span class=\"label\">");
			//

			sb.append(dataObject.getMaxTweets());

			sb.append("</span> tweets.");

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

				/*
				 * if (theCampaign.getFilterByTemplateText() != null &&
				 * !theCampaign.getFilterByTemplateText().trim() .isEmpty()) {
				 * sb.append(theCampaign.getFilterOperatorAsText()); }
				 */
			}

			/*
			 * if (theCampaign.getFilterByTemplateText() != null &&
			 * !theCampaign.getFilterByTemplateText().trim().isEmpty()) {
			 * 
			 * if (theCampaign.getFilterByTemplateTags() != null &&
			 * !theCampaign.getFilterByTemplateTags().trim() .isEmpty()) {
			 * sb.append(" containing text: \""); } else {
			 * sb.append("Use templates containing text: \""); }
			 * 
			 * sb.append("<span class=\"label\">");
			 * sb.append(theCampaign.getFilterByTemplateText());
			 * sb.append("\"</span>");
			 * 
			 * }
			 */
			return sb.toString();
		}

		private String getCampaignScheduleText(CampaignDTO dataObject) {

			StringBuffer sb = new StringBuffer();

			DateTimeFormat sdf = DateTimeFormat
					.getFormat(Constants.DATE_FORMAT);
			// new DateTimeFormat("EEE, MMM d, yyyy");
			sb.append("Starts  ");
			sb.append("<span class=\"label\">");
			sb.append(sdf.format(dataObject.getStartDate()));
			sb.append("</span>");
			sb.append(" and ends  ");
			sb.append("<span class=\"label\">");

			sb.append(sdf.format(dataObject.getEndDate()));
			sb.append("</span>. ");

			// If it have hours
			sb.append("Run from <span class=\"label\">");

			// Calendar.getInstance().DAY_OF_WEEK
			sb.append(dataObject.getStartHourOfTheDay());

			sb.append(":00</span> to <span class=\"label\"> ");

			sb.append(dataObject.getEndHourOfTheDay());

			sb.append(":59</span> hours");
			return sb.toString();

		}

	}

}
