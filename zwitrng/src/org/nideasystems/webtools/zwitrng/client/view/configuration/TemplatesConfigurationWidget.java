package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class TemplatesConfigurationWidget extends
		AbstractListConfigurationWidget<TemplateDTO, TemplateDTOList> {

	@Override
	public void init() {
		super.init();
		if (isEditable) {
			super.addToolBarNewItemMenu("New Template");
		}

	}

	// ////////
	// Public
	// ////////
	@Override
	public void loadData() {
		setCreatingNew(false);
		loadTemplates();

	}

	@Override
	protected SelectableItem<TemplateDTO, TemplateDTOList> createSelectableItem(
			TemplateDTO template,
			AbstractListConfigurationWidget<TemplateDTO, TemplateDTOList> parent) {
		return new SelectableTemplate(template, parent, isEditable);

	}

	@Override
	public void saveObject(TemplateDTO object) {

		if (object.getId() == -1) {
			saveNewTemplate(object);

		} else {
			saveExistingTemplate(object);
		}

	}

	private void saveExistingTemplate(TemplateDTO object) {

		MainController.getInstance().getCurrentPersonaController()
				.saveTemplate(object, getSelectedItem());

	}

	private void saveNewTemplate(TemplateDTO template) {

		isProcessing(true);
		MainController.getInstance().getCurrentPersonaController()
				.createTemplate(template, this);
	}

	@Override
	public void onSuccessLoadObjects(TemplateDTOList result) {
		isProcessing(false);

		for (TemplateDTO template : result.getTemplates()) {
			SelectableItem<TemplateDTO, TemplateDTOList> ePanel = createSelectableItem(
					template, this);
			addListItem(ePanel);
		}
		searchValue.setFocus(true);
	}

	@Override
	protected void removeItem(
			SelectableItem<TemplateDTO, TemplateDTOList> theInstance) {
		MainController.getInstance().getCurrentPersonaController()
				.removeTemplate(theInstance.dataObject, theInstance);

	}

	@Override
	protected EditableItem<TemplateDTO, TemplateDTOList> createEditableItem() {

		return new EditableTemplate(this);
	}

	// //////////////
	// Private
	// /////////////////////
	private void loadTemplates() {
		isProcessing(true);
		contentPanel.clear();
		MainController.getInstance().getCurrentPersonaController()
				.loadTemplates(this);

	}

	@Override
	public void onFailedLoadObjects(Throwable tr) {
		Window.alert("Error: " + tr.getMessage());
		isProcessing(false);

	}

	/**
	 * Widget for the edit template
	 * 
	 * @author jpereira
	 * 
	 */
	public class EditableTemplate extends
			EditableItem<TemplateDTO, TemplateDTOList> {

		private static final int DEFAULT_TEMPLATE_MAX_CHARS = 4096;
		// private TemplateDTO template = null;
		private TextArea templateText = null;
		private InlineHTML remainingChars = null;
		private TextBox templateName = null;

		public EditableTemplate(
				AbstractListConfigurationWidget<TemplateDTO, TemplateDTOList> parent) {
			super(parent);
			if (parent.isCreatingNew) {
				contentPanel.add(new InlineHTML("<h3>Create new Template</h3>"));
			} else {
				contentPanel.add(new InlineHTML("<h3>Edit Template</h3>"));
			}
			

			contentPanel.add(new InlineHTML("Name"));
			templateName = new TextBox();
			templateName.setMaxLength(25);
			contentPanel.add(templateName);
			
			// Create the main wrapper pannel
			contentPanel.add(new InlineHTML("Template text: (One tweet per line.)"));
			contentPanel.add(new InlineHTML("Use [xxx|yyy] to insert inline random lists of words. Use {{listName}} to insert a pre-defined random list of words. User {username_n} to insert the screenname for nth user. Use ((feed)) to include a feed."));

			// Create the TextArea where the template text is
			templateText = new TextArea();
			templateText.setWidth(Constants.CONFIGURATION_INPUT_MAX_WIDTH);
			// update.setHeight("35px");
			templateText.addStyleName("input");
			templateText.setVisibleLines(10);
			contentPanel.add(templateText);

			// Add remaining char info
			remainingChars = new InlineHTML("140");
			contentPanel.add(remainingChars);

			// Add short links link
			InlineHTML shortLinksLink = new InlineHTML("Short Links");
			shortLinksLink.addStyleName("link");
			contentPanel.add(shortLinksLink);

			// Add tags

		
			
			templateText.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					updateRemainingChars();
					adjustLines(templateText);
					
				}

				
				
			});
			shortLinksLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					setUpdating(true);
					SendUpdateWidget.shortLinks(templateText.getValue(), instance);

				}
			});
			

			
		}

		private void adjustLines(TextArea area) {
			int lines = HTMLHelper.getLines(area.getValue()).length;
			HTMLHelper.adjustLines(area, lines, 10, 25);

		}

		protected void updateRemainingChars() {
			int remainingCharsNbr = DEFAULT_TEMPLATE_MAX_CHARS
					- this.templateText.getValue().length();
			remainingChars.setHTML("" + remainingCharsNbr);

		}

		protected void save() {

			boolean hasErrors = false;
			if (templateText.getValue().trim().isEmpty()
					|| templateText.getValue().length() > DEFAULT_TEMPLATE_MAX_CHARS) {
				MainController.getInstance().addError(
						"Provide a template Text with no more than "
								+ DEFAULT_TEMPLATE_MAX_CHARS + " chars");
				hasErrors = true;
			}
			if (templateName.getValue().trim().isEmpty()) {
				MainController.getInstance().addError(
						"Provide a name for the template.");
				hasErrors = true;
			}
			if (!hasErrors) {
				TemplateDTO template = new TemplateDTO();
				template.setTemplateText(templateText.getValue());
				if (dataObject != null) {
					template.setId(dataObject.getId());
				}

				template.setName(templateName.getValue().trim());
				// Ignore Tags
				// String[] tags =
				// StringUtils.splitText(templateTags.getValue());
				// for (String tag : tags) {
				// template.addTags(tag);
				// }
				setUpdating(true);
				parent.saveObject(template);

			}

		}

		@Override
		public void setVisible(boolean visible) {
			super.setVisible(visible);
			templateText.setFocus(visible);

			templateText.setCursorPos(templateText.getValue().length());

		}

		@Override
		public void onLinksShortened(Map<String, String> result) {

			if (result != null) {
				String newStr = HTMLHelper.get().replaceText(
						templateText.getValue(), result);
				templateText.setFocus(true);
				templateText.setValue(newStr);
				updateRemainingChars();
			}
			this.setUpdating(false);
		}

		@Override
		public void focus() {
			if (parent.isCreatingNew) {
				templateName.setFocus(true);
			} else {
				templateText.setFocus(true);
			}
		}

		@Override
		public void refresh() {

			this.templateName.setValue(dataObject.getName());
			this.templateName.setEnabled(false);
			this.templateText.setValue(dataObject.getTemplateText());
			this.templateText.setFocus(true);
			updateRemainingChars();

		}
	}

	/**
	 * A Selectable Template Widget
	 * 
	 * @author jpereira
	 * 
	 */
	public class SelectableTemplate extends
			SelectableItem<TemplateDTO, TemplateDTOList> {

		// Model
		// private TemplateDTO template = null;

		HorizontalPanel textPanel = null;
		HorizontalPanel tags = null;
		private HTML additionalInfoHtml = null;
		private HTML textHtml = null;

		// EditableTemplate editableTemplate = null;

		public SelectableTemplate(
				TemplateDTO theTemplate,
				AbstractListConfigurationWidget<TemplateDTO, TemplateDTOList> theParent,
				boolean isEditable) {
			// Set the parent
			
			super(theParent, isEditable, false);
			
			setDataObject(theTemplate);

			content
					.add(new InlineHTML("<h3>" + theTemplate.getName()
							+ "</h2>"));
			textPanel = new HorizontalPanel();
			textHtml = new HTML();
			textPanel.add(textHtml);

			tags = new HorizontalPanel();
			additionalInfoHtml = new HTML();
			tags.add(additionalInfoHtml);
			additionalInfoHtml.addStyleName("tags");

			content.add(textPanel);
			content.add(tags);
			toolBar = createMenuOptions();
			content.add(toolBar);
			refresh();

		}

		@Override
		protected void refresh() {

			String tempText = StringUtils.jsParseText(dataObject
					.getTemplateText());

			StringBuffer sb = new StringBuffer();
			for (String line : HTMLHelper.getLines(tempText)) {
				sb.append("<div>" + line + "</div>");
			}
			textHtml.setHTML(sb.toString());

			additionalInfoHtml.setHTML("<div>[template used "
					+ dataObject.getUsedTimes() + " times]</div>");
		}

		@Override
		public String getSearchableText() {
			return dataObject.getName() + " " + dataObject.getTemplateText();
		}

	}

}
