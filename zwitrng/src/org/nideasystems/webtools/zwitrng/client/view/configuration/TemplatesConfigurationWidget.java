package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.Map;
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
		
		MainController.getInstance().getCurrentPersonaController().saveTemplate(object, getSelectedItem());

		
	}

	private void saveNewTemplate(TemplateDTO template) {
		
		isProcessing(true);
		MainController.getInstance().getCurrentPersonaController().createTemplate(template.getTemplateText(), template.getTagsAsText(), this);
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
		MainController.getInstance().getCurrentPersonaController().removeTemplate(theInstance.dataObject, theInstance);
		
		
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
		MainController.getInstance().getCurrentPersonaController().loadTemplates(this);

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

		// private TemplateDTO template = null;
		private TextArea templateText = null;
		private InlineHTML remainingChars = null;
		private TextBox templateTags = null;

		
		public EditableTemplate(
				AbstractListConfigurationWidget<TemplateDTO, TemplateDTOList> parent) {
			super(parent);

			// Create the main wrapper pannel
			contentPanel.add(new InlineHTML("Template text:"));

			// Create the TextArea where the template text is
			templateText = new TextArea();
			templateText.setWidth("580px");
			// update.setHeight("35px");
			templateText.addStyleName("input");
			templateText.setVisibleLines(2);
			contentPanel.add(templateText);

			// Add remaining char info
			remainingChars = new InlineHTML("140");
			contentPanel.add(remainingChars);

			// Add short links link
			InlineHTML shortLinksLink = new InlineHTML("Short Links");
			shortLinksLink.addStyleName("link");
			contentPanel.add(shortLinksLink);

			// Add tags

			contentPanel.add(new InlineHTML("Add tags separaded by spaces:"));
			templateTags = new TextBox();
			contentPanel.add(templateTags);

			templateText.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					updateRemainingChars();
					
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

		protected void updateRemainingChars() {
			int remainingCharsNbr = SendUpdateWidget.DEFAULT_TWEET_SIZE
					- this.templateText.getValue().length();
			remainingChars.setHTML("" + remainingCharsNbr);

		}

		protected void save() {

			if ( templateText.getValue().trim().isEmpty() ) {
				MainController.getInstance().addError("Provide a template Text");
			} else {
				TemplateDTO template = new TemplateDTO();
				template.setTemplateText(templateText.getValue());
				if (dataObject != null ) {
					template.setId(dataObject.getId());
				}
				
				
				String[] tags = StringUtils.splitText(templateTags.getValue());
				for (String tag : tags) {
					template.addTags(tag);
				}
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
				templateText.setValue(newStr + " ");
				updateRemainingChars();

			}
			this.setUpdating(false);

		}

		@Override
		public void focus() {
			templateText.setFocus(true);

		}

		@Override
		public void refresh() {
			this.templateTags.setValue(dataObject.getTagsAsText());
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
		private HTML tagsHtml = null;
		private HTML textHtml = null;

		// EditableTemplate editableTemplate = null;

		public SelectableTemplate(TemplateDTO theTemplate,
				AbstractListConfigurationWidget<TemplateDTO, TemplateDTOList> theParent, boolean isEditable) {
			// Set the parent
			super(theParent,isEditable);
			setDataObject(theTemplate);

			textPanel = new HorizontalPanel();
			textHtml = new HTML();
			textPanel.add(textHtml);

			tags = new HorizontalPanel();
			tagsHtml = new HTML();
			tags.add(tagsHtml);
			tagsHtml.addStyleName("tags");

			content.add(textPanel);
			content.add(tags);
			toolBar = createMenuOptions();
			content.add(toolBar);
			refresh();

		}

		@Override
		protected void refresh() {

			textHtml.setHTML(StringUtils.jsParseText(dataObject
					.getTemplateText()));

			StringBuffer sb = new StringBuffer();
			sb.append("Tags: ");
			for (String tag : dataObject.getTags()) {
				sb.append(tag);
				sb.append(" ");
			}
			sb.append("<div>[template used "+dataObject.getUsedTimes()+" times]</div>");

			tagsHtml.setHTML(sb.toString());
		}


		//protected void select(SelectableTemplate templwidget) {
		//	parent.onSelect(this);
		//}

		


		@Override
		public String getSearchableText() {
			return dataObject.getTagsAsText() + " "
					+ dataObject.getTemplateText();
		}

		

	}

	

	


	
}
