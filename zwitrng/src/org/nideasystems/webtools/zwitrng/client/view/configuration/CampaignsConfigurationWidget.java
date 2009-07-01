package org.nideasystems.webtools.zwitrng.client.view.configuration;

import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTODTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTOList;

import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;

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
		// TODO Auto-generated method stub
		return null;
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

		return new SelectableCampaign(obj, parent,true);
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

	private class SelectableCampaign extends
			SelectableItem<CampaignDTO, CampaignDTODTOList> {

		private InlineHTML nameText;
		private InlineHTML filterTags;
		private InlineHTML filterText;
		private InlineHTML filterOperator;
		
		public SelectableCampaign(CampaignDTO theCampaign,
				AbstractListConfigurationWidget<CampaignDTO, CampaignDTODTOList> theParent, boolean isEditable) {
			// Set the parent
			super(theParent,isEditable);
			setDataObject(theCampaign);
			
			HorizontalPanel nameLine = new HorizontalPanel();
			InlineHTML nameLabel = new InlineHTML("Name: ");
			nameLabel.addStyleName("label");
			nameLine.add(nameLabel);
			nameText = new InlineHTML();
			nameLine.add(nameText);
			//Filter
			
			HorizontalPanel filterLine = new HorizontalPanel();
			filterLine.setSpacing(5);
			filterLine.add(new InlineHTML("Use templates with tags: "));
			filterTags = new InlineHTML();
			filterTags.addStyleName("label");
			filterLine.add(filterTags);
			filterOperator = new InlineHTML();
			filterOperator.addStyleName("label");
			filterLine.add(filterOperator);
			filterLine.add(new InlineHTML(" contains text: "));
			filterText = new InlineHTML();
			filterLine.add(filterText);
			filterText.addStyleName("label");
			content.add(nameLine);
			content.add(filterLine);
			toolBar = createMenuOptions();
			content.add(toolBar);
			refresh();

		}
		
		@Override
		public String getSearchableText() {
			
			return dataObject.getName()+" "+dataObject.getFilterByTemplateTags()+" "+dataObject.getFilterByTemplateText();
		}

		@Override
		protected void refresh() {
			this.nameText.setText(dataObject.getName());
			this.filterOperator.setText(" "+dataObject.getFilterOperatorAsText()+" ");
			this.filterTags.setText(dataObject.getFilterByTemplateTags());
			this.filterText.setText(dataObject.getFilterByTemplateText());
		}

	}

}
