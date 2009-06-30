package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;

import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTOList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class TemplateFragmentsConfigurationWidget extends AbstractListConfigurationWidget<TemplateFragmentDTO, TemplateFragmentDTOList> {


	@Override
	public void init() {
		super.init();
		if (isEditable) {
			super.addToolBarNewItemMenu("New Template Fragment");
		}
	
	}

	@Override
	public void loadData() {
		
		isCreatingNew = false;
		loadTemplateFragments();

	}
	private void loadTemplateFragments() {
		isProcessing(true);
		contentPanel.clear();
		MainController.getInstance().getCurrentPersonaController().getTemplateFragments(this);
		

	}
	
	@Override
	protected EditableItem<TemplateFragmentDTO, TemplateFragmentDTOList> createEditableItem() {
		
		return new EditableTemplateFragment(this);
	}

	@Override
	protected SelectableItem<TemplateFragmentDTO, TemplateFragmentDTOList> createSelectableItem(
			TemplateFragmentDTO obj,
			AbstractListConfigurationWidget<TemplateFragmentDTO, TemplateFragmentDTOList> parent) {
		return new SelectableTemplateFragment(obj,parent,true);
	}

	@Override
	public void onSuccessLoadObjects(TemplateFragmentDTOList list) {
		isProcessing(false);
		for (TemplateFragmentDTO tmp: list.getTemplateFragmentList()) {
			SelectableItem<TemplateFragmentDTO, TemplateFragmentDTOList> item = createSelectableItem(tmp, this);
			addListItem(item);
		}
		searchValue.setFocus(true);
		
	}

	@Override
	protected void removeItem(
			SelectableItem<TemplateFragmentDTO, TemplateFragmentDTOList> item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveObject(TemplateFragmentDTO object) {
		
		if (object.getId() == -1) {
			saveNewTemplateFragment(object);
			
		} else {
			saveExistingTemplatefragment(object);
		}
		
		
	}
	
	private void saveExistingTemplatefragment(TemplateFragmentDTO object) {
		MainController.getInstance().getCurrentPersonaController().saveTemplateFragment(object, getSelectedItem());
		
	}

	private void saveNewTemplateFragment(TemplateFragmentDTO object) {
		isProcessing(true);
		MainController.getInstance().getCurrentPersonaController().createTemplateFragment(object, this);
		
	}

	private class EditableTemplateFragment extends EditableItem<TemplateFragmentDTO, TemplateFragmentDTOList> {

		private TextBox nameText = null;
		private TextArea templateFragmentText = null;
		private TextBox templateTags = null;
		
		public EditableTemplateFragment(
				AbstractListConfigurationWidget<TemplateFragmentDTO, TemplateFragmentDTOList> parent) {
			super(parent);
		
			
			
			InlineHTML nameLabel = new InlineHTML("Name: ");
			nameText = new TextBox();
			contentPanel.add(nameLabel);
			contentPanel.add(nameText);
			contentPanel.add(new InlineHTML("Template Fragment Text"));
			templateFragmentText = new TextArea();
			templateFragmentText.setWidth("620px");
			// update.setHeight("35px");
			templateFragmentText.addStyleName("input");
			templateFragmentText.setVisibleLines(2);
			contentPanel.add(templateFragmentText);
			// Add short links link
			InlineHTML shortLinksLink = new InlineHTML("Short Links");
			shortLinksLink.addStyleName("link");
			contentPanel.add(shortLinksLink);

			contentPanel.add(new InlineHTML("Add tags separaded by spaces:"));
			templateTags = new TextBox();
			contentPanel.add(templateTags);
			
			shortLinksLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					setUpdating(true);
					SendUpdateWidget.shortLinks(templateFragmentText.getValue(), instance);

				}
			});

			

			
		}
		
		@Override
		public void focus() {
			this.nameText.setFocus(true);
			
		}

		@Override
		public void refresh() {
			
			this.nameText.setValue(dataObject.getName());
			this.templateFragmentText.setValue(dataObject.getList());
			this.templateTags.setValue(dataObject.getTagsAsString());
			if ( dataObject.getId()!=-1) {
				this.nameText.setEnabled(false);
			}
			this.templateFragmentText.setFocus(true);
		}

		@Override
		protected void save() {
			if ( nameText.getValue().trim().isEmpty() ) {
				MainController.getInstance().addError("Provide a name for the Fragment");
			} else if (templateFragmentText.getValue().trim().isEmpty() ) {
				MainController.getInstance().addError("Provide content for the Fragment");
			} else {
				
				TemplateFragmentDTO tDto = new TemplateFragmentDTO();
				tDto.setList(templateFragmentText.getValue());
				tDto.setName(nameText.getValue());
				
				String[] tags = StringUtils.splitText(templateTags.getValue());
				for (String tag : tags) {
					tDto.addTag(tag);
				}
				
				if ( dataObject != null ) {
					tDto.setId(dataObject.getId());
				}
				setUpdating(true);
				parent.saveObject(tDto);
			}
			
		}

		@Override
		public void onLinksShortened(Map<String, String> result) {
			if (result != null) {
				String newStr = HTMLHelper.get().replaceText(
						templateFragmentText.getValue(), result);
				templateFragmentText.setFocus(true);
				templateFragmentText.setValue(newStr + " ");

			}
			this.setUpdating(false);

		}
		
	}
	
	/**
	 * 
	 * @author jpereira
	 * 
	 */
	private class SelectableTemplateFragment extends SelectableItem<TemplateFragmentDTO,TemplateFragmentDTOList> implements
			HasMouseOutHandlers, HasMouseOverHandlers, HasDoubleClickHandlers,
			HasClickHandlers {


		
		private InlineHTML textHtml;
		private HTML tagsHtml;
		private InlineHTML name;
		
				
		public SelectableTemplateFragment(TemplateFragmentDTO templateFragment, AbstractListConfigurationWidget<TemplateFragmentDTO, TemplateFragmentDTOList> theParent, boolean aIsEditable) {
			super(theParent, aIsEditable);
			setDataObject(templateFragment);
			HorizontalPanel namePanel = new HorizontalPanel();
			InlineHTML namelabel = new InlineHTML("Name: ");
			name = new InlineHTML("");
			namePanel.add(namelabel);
			namePanel.add(name);
			content.add(namePanel);
			textHtml = new InlineHTML();
			content.add(textHtml);
			tagsHtml = new InlineHTML();
			tagsHtml.addStyleName("tags");
			content.add(tagsHtml);
			toolBar = createMenuOptions();
			content.add(toolBar);
			refresh();
			
			

		}


		

		@Override
		public String getSearchableText() {
			return dataObject.getName()+" "+dataObject.getList()+" "+dataObject.getTagsAsString();
		}


		@Override
		protected void refresh() {
			this.name.setHTML(dataObject.getName());
			this.textHtml.setHTML(StringUtils.jsParseText(dataObject.getList()));
			this.tagsHtml.setHTML("Tags: "+dataObject.getTagsAsString());
			
		}


		

	

	}

	
	
}
