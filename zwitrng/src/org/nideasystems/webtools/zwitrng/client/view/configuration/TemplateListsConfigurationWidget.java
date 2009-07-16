package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateListDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateListDTOList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class TemplateListsConfigurationWidget extends AbstractListConfigurationWidget<TemplateListDTO, TemplateListDTOList> {


	@Override
	public void init() {
		super.init();
		if (isEditable) {
			super.addToolBarNewItemMenu("New Template List");
		}
	
	}

	@Override
	public void loadData() {
		
		setCreatingNew(false);
		loadTemplateFragments();

	}
	private void loadTemplateFragments() {
		isProcessing(true);
		contentPanel.clear();
		MainController.getInstance().getCurrentPersonaController().getTemplateFragments(this);
		

	}
	
	@Override
	protected EditableItem<TemplateListDTO, TemplateListDTOList> createEditableItem() {
		
		return new EditableTemplateFragment(this);
	}

	@Override
	protected SelectableItem<TemplateListDTO, TemplateListDTOList> createSelectableItem(
			TemplateListDTO obj,
			AbstractListConfigurationWidget<TemplateListDTO, TemplateListDTOList> parent) {
		return new SelectableTemplateFragment(obj,parent,true);
	}

	@Override
	public void onSuccessLoadObjects(TemplateListDTOList list) {
		isProcessing(false);
		for (TemplateListDTO tmp: list.getTemplateFragmentList()) {
			SelectableItem<TemplateListDTO, TemplateListDTOList> item = createSelectableItem(tmp, this);
			addListItem(item);
		}
		searchValue.setFocus(true);
		
	}

	@Override
	protected void removeItem(
			SelectableItem<TemplateListDTO, TemplateListDTOList> item) {
		MainController.getInstance().getCurrentPersonaController().removeTemplateFragment(item.dataObject, item);
		
	}

	@Override
	public void saveObject(TemplateListDTO object) {
		
		if (object.getId() == -1) {
			saveNewTemplateFragment(object);
			
		} else {
			saveExistingTemplatefragment(object);
		}
		
		
	}
	
	private void saveExistingTemplatefragment(TemplateListDTO object) {
		MainController.getInstance().getCurrentPersonaController().saveTemplateFragment(object, getSelectedItem());
		
	}

	private void saveNewTemplateFragment(TemplateListDTO object) {
		MainController.getInstance().getCurrentPersonaController().createTemplateFragment(object, this);
		
	}

	private class EditableTemplateFragment extends EditableItem<TemplateListDTO, TemplateListDTOList> {

		private static final int DEFAULT_TEMPLATE_LIST_MAX_CHARS = 500;
		private TextBox nameText = null;
		private TextArea templateFragmentText = null;
		private InlineHTML  remainingChars=new InlineHTML(""+DEFAULT_TEMPLATE_LIST_MAX_CHARS);
		
		//private CheckBox repeatInCampaignAndTemplate;
		//private CheckBox maintainOrder;
		
		public EditableTemplateFragment(
				AbstractListConfigurationWidget<TemplateListDTO, TemplateListDTOList> parent) {
			super(parent);
		
			
			if (parent.isCreatingNew) {
				contentPanel.add(new InlineHTML("Create new Template List"));
			} else {
				contentPanel.add(new InlineHTML("Edit Template List"));
			}
			InlineHTML nameLabel = new InlineHTML("Name: ");
			
			nameText = new TextBox();
			contentPanel.add(nameLabel);
			contentPanel.add(nameText);
			contentPanel.add(new InlineHTML("Template Fragment Text"));
			templateFragmentText = new TextArea();
			templateFragmentText.setWidth(Constants.CONFIGURATION_INPUT_MAX_WIDTH);
			// update.setHeight("35px");
			templateFragmentText.addStyleName("input");
			templateFragmentText.setVisibleLines(5);
			contentPanel.add(templateFragmentText);
			contentPanel.add(remainingChars);
			// Add short links link
			InlineHTML shortLinksLink = new InlineHTML("Short Links");
			shortLinksLink.addStyleName("link");
			contentPanel.add(shortLinksLink);
			templateFragmentText.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					// TODO Auto-generated method stub
					HTMLHelper.adjustLines(templateFragmentText, HTMLHelper.getLines(templateFragmentText.getValue()).length, 5, 15);
					updateRemainingChars();
				}
				
			});
			
			//repeatInCampaignAndTemplate = new CheckBox("Repeat in the same template and campaign");
			//contentPanel.add(repeatInCampaignAndTemplate);
			//maintainOrder = new CheckBox("Maintain the order");
			//contentPanel.add(maintainOrder);
			//contentPanel.add(new InlineHTML("Add tags separaded by spaces:"));
			//templateTags = new TextBox();
			//contentPanel.add(templateTags);
			
			shortLinksLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					setUpdating(true);
					SendUpdateWidget.shortLinks(templateFragmentText.getValue(), instance);

				}
			});

			

			
		}
		protected void updateRemainingChars() {
			int remainingCharsNbr = DEFAULT_TEMPLATE_LIST_MAX_CHARS
					- this.templateFragmentText.getValue().length();
			remainingChars.setHTML("" + remainingCharsNbr);

		}
		@Override
		public void focus() {
			this.nameText.setFocus(true);
			
		}

		@Override
		public void refresh() {
			//this.maintainOrder.setValue(dataObject.getMaintainOrder());
			//this.repeatInCampaignAndTemplate.setValue(dataObject.getRepeatInCampaignAndTemplate());
			
			this.nameText.setValue(dataObject.getName());
			this.templateFragmentText.setValue(dataObject.getList());
			if ( dataObject.getId()!=-1) {
				this.nameText.setEnabled(false);
			}
			this.templateFragmentText.setFocus(true);
		}

		@Override
		protected void save() {
			if ( nameText.getValue().trim().isEmpty() ) {
				MainController.getInstance().addError("Provide a name for the List");
			} else if (templateFragmentText.getValue().trim().isEmpty() || templateFragmentText.getValue().length()>500 ) {
				MainController.getInstance().addError("Provide content for the List with no more than 500 characters");
			} else {
				
				TemplateListDTO tDto = new TemplateListDTO();
				tDto.setList(templateFragmentText.getValue());
				tDto.setName(nameText.getValue());
				//tDto.setMaintainOrder(this.maintainOrder.getValue());
				//tDto.setRepeatInCampaignAndTemplate(this.repeatInCampaignAndTemplate.getValue());
				
				
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
	private class SelectableTemplateFragment extends SelectableItem<TemplateListDTO,TemplateListDTOList> implements
			HasMouseOutHandlers, HasMouseOverHandlers, HasDoubleClickHandlers,
			HasClickHandlers {


		
		private InlineHTML textHtml;
		private HTML tagsHtml;
		private InlineHTML name;
		//private InlineHTML moreInfoText; 
		
				
		public SelectableTemplateFragment(TemplateListDTO templateFragment, AbstractListConfigurationWidget<TemplateListDTO, TemplateListDTOList> theParent, boolean aIsEditable) {
			super(theParent, aIsEditable, false);
			setDataObject(templateFragment);
			HorizontalPanel namePanel = new HorizontalPanel();
			namePanel.setSpacing(3);
			
			name = new InlineHTML("");
			namePanel.add(name);
			content.add(namePanel);
			textHtml = new InlineHTML();
			content.add(textHtml);
		//	moreInfoText = new InlineHTML();
		//	moreInfoText.addStyleName("tags");
		//	content.add(moreInfoText);
			
			tagsHtml = new InlineHTML();
			tagsHtml.addStyleName("tags");
			content.add(tagsHtml);
			toolBar = createMenuOptions();
			content.add(toolBar);
			refresh();
			
			

		}


		

		@Override
		public String getSearchableText() {
			return dataObject.getName()+" "+dataObject.getList();
		}


		@Override
		protected void refresh() {
			
			this.name.setHTML("<h3>"+dataObject.getName()+"</h3>");
			
			
			StringBuffer sb = new StringBuffer();
			for (String line:HTMLHelper.getLines(dataObject.getList())) {
				sb.append("<div>"+MainController.jsParseText(line)+"</div>");
			}
			this.textHtml.setHTML(sb.toString());
			//this.moreInfoText.setText(sb.toString());
			
		}


		

	

	}

	
	
}
