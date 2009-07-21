package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.TimeUnits;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.datepicker.client.DateBox;

public class CampaignsConfigurationWidget extends
		AbstractListConfigurationWidget<CampaignDTO, CampaignDTOList> {

	@Override
	public void init() {

		super.init();
		if (isEditable) {
			super.addToolBarNewItemMenu("New Campaign");
		}

	}

	@Override
	protected EditableItem<CampaignDTO, CampaignDTOList> createEditableItem() {
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
	protected SelectableItem<CampaignDTO, CampaignDTOList> createSelectableItem(
			CampaignDTO obj,
			AbstractListConfigurationWidget<CampaignDTO, CampaignDTOList> parent) {

		return new SelectableCampaign(obj, parent, true);
	}

	@Override
	public void onSuccessLoadObjects(CampaignDTOList list) {

		isProcessing(false);
		for (CampaignDTO campaign : list.getCampaigns()) {
			SelectableItem<CampaignDTO, CampaignDTOList> item = createSelectableItem(
					campaign, this);
			addListItem(item);
		}

	}

	@Override
	protected void removeItem(SelectableItem<CampaignDTO, CampaignDTOList> item) {

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
			EditableItem<CampaignDTO, CampaignDTOList> implements
			StringListLoadedCallBack {

		private TextBox campaignName;
		// private TextBox filterByTags;<<<<<
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
		private CheckBox useTemplatesRandomly;
		private CheckBox repeatTemplates;
		private CheckBox limitTweets;
		private CheckBox trackLinks;
		private InlineHTML status;

		private MultiSelectListWidget templateNamesSelect;

		public EditableCampaign(
				AbstractListConfigurationWidget<CampaignDTO, CampaignDTOList> theParent) {
			super(theParent);

			if (parent.isCreatingNew()) {
				contentPanel
						.add(new InlineHTML("<h3>Create new Campaign</h3>"));
			} else {
				contentPanel.add(new InlineHTML("<h3>Edit Campaign</h3>"));
				status = new InlineHTML();
				contentPanel.add(status);

			}

			InlineHTML textLabel = new InlineHTML("Name: ");
			campaignName = new TextBox();
			contentPanel.add(textLabel);
			contentPanel.add(campaignName);

			templateNamesSelect = new MultiSelectListWidget();
			templateNamesSelect.setAvailableItemsTitle("Available Templates");
			templateNamesSelect.setSelectedItemsTitle("Selected Templates");
			contentPanel.add(templateNamesSelect);
			useTemplatesRandomly = new CheckBox("Use templates randomly.");
			contentPanel.add(useTemplatesRandomly);
			repeatTemplates = new CheckBox("Allow to repeat templates.");
			contentPanel.add(repeatTemplates);
			trackLinks = new CheckBox(
					"Track clicks on links sent during this campaign?");
			contentPanel.add(trackLinks);

			FlexTable table = new FlexTable();

			// ///////
			InlineHTML startDateLabel = new InlineHTML("Start date:");
			// startDate = new TextBox();
			startDate = new DateBox();
			startDate.setValue(new Date());
			startDate.setWidth("11em");
			table.setWidget(0, 0, startDateLabel);
			table.setWidget(1, 0, startDate);

			InlineHTML endDateLabel = new InlineHTML("End date:");
			endDate = new DateBox();
			endDate.setWidth("11em");
			endDate.setValue(new Date());
			table.setWidget(0, 1, endDateLabel);
			table.setWidget(1, 1, endDate);
			contentPanel.add(table);
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

			FlexTable additionalOptionsTable = new FlexTable();
			limitTweets = new CheckBox("Limit the number of tweets sent?");
			additionalOptionsTable.setWidget(0, 0, limitTweets);

			InlineHTML maxUsageOfTemplateLabel = new InlineHTML(
					"Send max of Tweets");
			maxTweets = new TextBox();
			maxTweets.setWidth("50px");
			maxTweets.setValue("10");

			additionalOptionsTable.setWidget(1, 0, maxUsageOfTemplateLabel);

			additionalOptionsTable.setWidget(2, 0, maxTweets);

			contentPanel.add(additionalOptionsTable);

			this.startDate.setFormat(new DateFormatter());
			this.endDate.setFormat(new DateFormatter());
			this.maxTweets.setEnabled(limitTweets.getValue());
			this.limitTweets
					.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

						@Override
						public void onValueChange(
								ValueChangeEvent<Boolean> event) {
							maxTweets.setEnabled(event.getValue());
						}

					});
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

			/*
			 * //Load available template names ArrayList<String> avaItems = new
			 * ArrayList<String>(); avaItems.add("Item 1");
			 * avaItems.add("Item 2"); avaItems.add("Item 3");
			 */
			// this.templateNamesSelect.setAvailableItemsList();
			loadAvailableTemplatesNames();
		}

		private void loadAvailableTemplatesNames() {
			MainController.getInstance().getCurrentPersonaController()
					.loadTemplateNames(this);

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
				// this.filterByTags.setFocus(true);
			} else {
				this.campaignName.setFocus(true);
			}

		}

		@Override
		protected void save() {
			boolean isValid = true;
			CampaignDTO campaign = new CampaignDTO();
			if (dataObject != null) {
				campaign.setStatus(dataObject.getStatus());
			} else {
				campaign.setStatus(CampaignStatus.NOT_STARTED);
			}

			// Check Name
			if (this.campaignName.getValue().trim().isEmpty()) {
				MainController.getInstance().addError(
						"Please provide a name for the campaign");
				isValid = false;

			} else {
				campaign.setName(this.campaignName.getValue().trim());
			}

			// Check template names
			if (this.templateNamesSelect.getSelectedItemsList().size() == 0) {
				MainController.getInstance().addError(
						"Please provide at leat one template to use!");
				isValid = false;

			} else {
				campaign.setTemplatesNames(this.templateNamesSelect
						.getSelectedItemsList());
			}

			// Add use templates randomly
			campaign.setUseTemplatesRandomly(this.useTemplatesRandomly
					.getValue());
			// Set allowRepeatTemplates
			campaign.setAllowRepeatTemplates(this.repeatTemplates.getValue());
			campaign.setTrackClicksOnLinks(this.trackLinks.getValue());

			if (startDate.getValue() == null || endDate.getValue() == null) {
				MainController
						.getInstance()
						.addError(
								"Please provide a start and end date for the campaign.");
				isValid = false;

			} else if (this.startDate.getValue() == null
					|| this.endDate.getValue() == null
					|| this.startDate.getValue().after(this.endDate.getValue())
					|| (this.startDate.getValue().compareTo(
							this.endDate.getValue()) == 0)) {
				MainController.getInstance().addError(
						"Please provide a end date later than the start date.");
				isValid = false;
			} else {
				campaign.setStartDate(this.startDate.getValue());
				campaign.setEndDate(this.endDate.getValue());
			}

			if ((this.timeUnits.getValue(this.timeUnits.getSelectedIndex()))
					.equals(TimeUnits.MINUTES.name())) {
				// Validate multiple of 5

				Integer val = 5;
				try {
					val = Integer.valueOf(this.timeBetweenTweets.getValue());
					if ((val % 5) > 0) {

						MainController
								.getInstance()
								.addError(
										"The interval in minutes we use to send tweets is allways multiples of 5.\nProvide a multiple of 5!");

						isValid = false;

					} else {
						campaign.setTimeBetweenTweets(val);
					}
				} catch (NumberFormatException e) {
					MainController.getInstance().addError(
							"The interval must be a valid number.");

					// validated before
					isValid = false;

				}

			} else {
				try {
					campaign.setTimeBetweenTweets(Integer
							.valueOf(this.timeBetweenTweets.getValue()));
				} catch (NumberFormatException e) {
					MainController.getInstance().addError(
							"The interval must be a valid number.");
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

			} else {
				campaign.setStartHourOfTheDay(Integer
						.valueOf(this.startHourOfTheDay
								.getValue(this.startHourOfTheDay
										.getSelectedIndex())));

				campaign.setEndHourOfTheDay(Integer
						.valueOf(this.endHourOfTheDay
								.getValue(this.endHourOfTheDay
										.getSelectedIndex())));

			}

			campaign.setTimeUnit(TimeUnits.valueOf(this.timeUnits
					.getValue(this.timeUnits.getSelectedIndex())));

			campaign.setLimitNumberOfTweetsSent(this.limitTweets.getValue());

			if (campaign.isLimitNumberOfTweetsSent()
					&& this.maxTweets.getValue().trim().isEmpty()) {
				MainController
						.getInstance()
						.addError(
								"Please provide the number of times we should sent a tweet.");
				isValid = false;
			} else {

				try {

					campaign.setMaxTweets(Integer.valueOf(this.maxTweets
							.getValue()));
				} catch (NumberFormatException e) {
					MainController
							.getInstance()
							.addError(
									"Please provide a valid number for the times we should send a tweet!");
					isValid = false;

				}

			}

			if (isValid) {

				if (dataObject != null) {
					campaign.setId(dataObject.getId());
				}
				setUpdating(true);
				parent.saveObject(campaign);

			}

		}

		@Override
		public void refresh() {
			this.campaignName.setValue(dataObject.getName());
			if (this.status != null) {
				this.status.setHTML("Status: <span class=\"label\">"
						+ dataObject.getStatus().toString() + "</span>");
			}

			if (dataObject.getId() != -1) {
				this.campaignName.setEnabled(false);
			}

			this.endDate.setValue(dataObject.getEndDate());
			this.startDate.setValue(dataObject.getStartDate());
			this.timeBetweenTweets.setValue(dataObject.getTimeBetweenTweets()
					+ "");
			// this.filterByTags.setValue(dataObject.getFilterByTemplateTags());
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

			this.templateNamesSelect.setSelectedItemsList(dataObject
					.getTemplatesNames());
			this.useTemplatesRandomly.setValue(dataObject
					.isUseTemplatesRandomly());
			this.repeatTemplates.setValue(dataObject.isAllowRepeatTemplates());
			this.trackLinks.setValue(dataObject.isTrackClicksOnLinks());
			this.limitTweets.setValue(dataObject.isLimitNumberOfTweetsSent());

			maxTweets.setEnabled(dataObject.isLimitNumberOfTweetsSent());
			updateRunningDays();
			this.templateNamesSelect.refreshAvailable();

		}

		@Override
		public void onLinksShortened(Map<String, String> result) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTemplatesNamesListFail(Throwable tr) {
			MainController.getInstance().addException(tr);

		}

		@Override
		public void onTemplatesNamesListLoaded(List<String> list) {
			this.templateNamesSelect.setAvailableItemsList(list);
			this.templateNamesSelect.refreshAvailable();

		}
	}

	/**
	 * 
	 * @author jpereira
	 * 
	 */
	private class SelectableCampaign extends
			SelectableItem<CampaignDTO, CampaignDTOList> {

		private InlineHTML templatesNames;
		private InlineHTML scheduleText;
		private InlineHTML limitsText;
		private InlineHTML statusText;
		private HorizontalPanel statusPanel;
		private InlineHTML startLink;
		private InlineHTML pauseLink;
		private InlineHTML stopLink;
		private InlineHTML resumeLink;
		private InlineHTML resetLink;
		
		private InlineHTML tweetsSend;
		private InlineHTML info;
		private InlineHTML refresh;
		
		

		public SelectableCampaign(
				CampaignDTO theCampaign,
				AbstractListConfigurationWidget<CampaignDTO, CampaignDTOList> theParent,
				boolean isEditable) {
			// Set the parent
			super(theParent, isEditable, false);
			
			setDataObject(theCampaign);

			content
					.add(new InlineHTML("<h3>" + theCampaign.getName()
							+ "</h3>"));

			templatesNames = new InlineHTML();
			content.add(templatesNames);

			scheduleText = new InlineHTML();
			content.add(scheduleText);

			limitsText = new InlineHTML();
			content.add(limitsText);
			statusPanel = new HorizontalPanel();
			statusPanel.setSpacing(4);
			statusText = new InlineHTML();
			startLink = new InlineHTML("Start");
			stopLink = new InlineHTML(" Stop ");
			pauseLink = new InlineHTML(" Pause ");
			resumeLink = new InlineHTML(" Resume ");
			resetLink = new InlineHTML(" Reset ");

			startLink.addStyleName("link");
			stopLink.addStyleName("link");
			pauseLink.addStyleName("link");
			resumeLink.addStyleName("link");
			resetLink.addStyleName("link");

			statusPanel.add(statusText);
			statusPanel.add(startLink);
			statusPanel.add(stopLink);
			statusPanel.add(pauseLink);
			statusPanel.add(resumeLink);
			statusPanel.add(resetLink);

			content.add(statusPanel);
			

			//Create a more Panel
			VerticalPanel vPanel = new VerticalPanel();
			tweetsSend = new InlineHTML();
			info = new InlineHTML();
			refresh = new InlineHTML("Refresh");
			refresh.addStyleName("link");
			vPanel.add(tweetsSend);
			vPanel.add(info);
			vPanel.add(refresh);
			
			DisclosurePanel dPanel = new DisclosurePanel("More...");
			dPanel.setContent(vPanel);
			content.add(dPanel);
			
			
			toolBar = createMenuOptions();
			
			content.add(toolBar);

			refresh();

			refresh.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					reloadData();
					
				}

				
			});
			startLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					changeStatus(CampaignStatus.STARTED);

				}

			});

			stopLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					changeStatus(CampaignStatus.STOPPED);

				}

			});

			pauseLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					changeStatus(CampaignStatus.PAUSED);

				}

			});

			resumeLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					changeStatus(CampaignStatus.STARTED);

				}

			});
			resetLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					changeStatus(CampaignStatus.NOT_STARTED);

				}

			});

		}

		private void reloadData() {
			setProcessing(true);
			MainController.getInstance().getCurrentPersonaController().getCampaign(this, dataObject.getName());
		}
		private void changeStatus(CampaignStatus status) {
			setProcessing(true);
			
			MainController.getInstance().getCurrentPersonaController().changeCampaignStatus(this, dataObject.getName(), status);

		}

		@Override
		public String getSearchableText() {

			return dataObject.getName() + " "
					+ dataObject.getStatus().toString();
		}

		@Override
		protected void refresh() {
			setProcessing(false);
			this.templatesNames.setHTML(getCampaignDescriptionText(dataObject));
			this.scheduleText.setHTML(getCampaignScheduleText(dataObject));
			this.limitsText.setHTML(getCampaignLimitsText(dataObject));
			this.statusText.setHTML(getCampaignStatusText(dataObject));
			
			this.info.setHTML(dataObject.getInfo());
			this.tweetsSend.setHTML("Tweets sent: "+dataObject.getTweetsSent());

			if (dataObject.getStatus().equals(CampaignStatus.NOT_STARTED)) {
				this.stopLink.setVisible(false);
				this.pauseLink.setVisible(false);
				this.startLink.setVisible(true);
				this.resumeLink.setVisible(false);
				this.resetLink.setVisible(false);
			} else if (dataObject.getStatus().equals(CampaignStatus.STARTED)) {
				this.stopLink.setVisible(true);
				this.pauseLink.setVisible(true);
				this.startLink.setVisible(false);
				this.resumeLink.setVisible(false);
				this.resetLink.setVisible(false);
			} else if (dataObject.getStatus().equals(CampaignStatus.PAUSED)) {
				this.stopLink.setVisible(false);
				this.pauseLink.setVisible(false);
				this.startLink.setVisible(false);
				this.resumeLink.setVisible(true);
				this.resetLink.setVisible(false);
			} else if (dataObject.getStatus().equals(CampaignStatus.FINISHED)) {
				this.stopLink.setVisible(false);
				this.pauseLink.setVisible(false);
				this.startLink.setVisible(false);
				this.resumeLink.setVisible(false);
				this.resetLink.setVisible(true);
			} else if (dataObject.getStatus().equals(CampaignStatus.STOPPED)) {
				this.stopLink.setVisible(false);
				this.pauseLink.setVisible(false);
				this.startLink.setVisible(false);
				this.resumeLink.setVisible(false);
				this.resetLink.setVisible(true);
			}
		}

		private String getCampaignStatusText(CampaignDTO dataObject) {
			StringBuffer sb = new StringBuffer();
			sb.append("Status: ");
			sb.append("<span class=\"label\">");
			sb.append(dataObject.getStatus().toString());
			sb.append("</span>. ");

			// Add Links

			return sb.toString();

		}

		private String getCampaignLimitsText(CampaignDTO dataObject) {
			StringBuffer sb = new StringBuffer();

			sb.append("<div>");
			sb.append("Wait ");
			sb.append("<span class=\"label\">");
			sb.append(dataObject.getTimeBetweenTweets());
			sb.append(" </span> ");
			assert (dataObject != null);
			if (dataObject.getTimeUnit() != null) {
				if (dataObject.getTimeUnit().equals(TimeUnits.DAYS)) {
					sb.append("days ");
				} else if (dataObject.getTimeUnit().equals(TimeUnits.HOURS)) {
					sb.append("hours ");
				} else {
					sb.append("minutes ");
				}
			} else {
				sb.append("Invalid ");
			}

			sb.append(" between tweets. ");

			if (dataObject.isLimitNumberOfTweetsSent()) {

				sb.append(" Don't send more than ");
				sb.append("<span class=\"label\">");
				//

				sb.append(dataObject.getMaxTweets());

				sb.append("</span> tweets.");
			} else {
				sb
						.append("<span class=\"label\"> Don't limit number of tweets sent.");
				sb.append("</span>");
			}

			return sb.toString();

		}

		private String getCampaignDescriptionText(CampaignDTO theCampaign) {
			StringBuffer sb = new StringBuffer();
			sb.append("Use templates: ");
			sb.append("<span class=\"label\">");

			for (String tName : dataObject.getTemplatesNames()) {
				sb.append(tName);
				sb.append(" ");

			}
			sb.append("</span>");
			sb.append("<div>");

			sb.append("Use templates randomly: ");
			sb.append("<span class=\"label\">");
			sb.append(dataObject.isUseTemplatesRandomly() ? "Yes" : "No");
			sb.append(" </span> ");

			sb.append("Allow repeat templates: ");
			sb.append("<span class=\"label\">");
			sb.append(dataObject.isAllowRepeatTemplates() ? "Yes" : "No");
			sb.append(" </span> ");

			sb.append("<div>");
			sb.append("Track links sent during this campaign: ");
			sb.append("<span class=\"label\">");
			sb.append(dataObject.isTrackClicksOnLinks() ? "Yes" : "No");
			sb.append(" </span> ");

			sb.append("</div>");

			/*
			 * if (theCampaign.getFilterByTemplateTags() != null &&
			 * !theCampaign.getFilterByTemplateTags().trim().isEmpty()) {
			 * sb.append("Use templates containing tags :\"");
			 * 
			 * 
			 * sb.append(theCampaign.getFilterByTemplateTags());
			 * 
			 * 
			 * 
			 * if (theCampaign.getFilterByTemplateText() != null &&
			 * !theCampaign.getFilterByTemplateText().trim() .isEmpty()) {
			 * sb.append(theCampaign.getFilterOperatorAsText()); }
			 * 
			 * }
			 */
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
