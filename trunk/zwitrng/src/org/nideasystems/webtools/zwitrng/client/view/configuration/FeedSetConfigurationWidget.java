package org.nideasystems.webtools.zwitrng.client.view.configuration;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.MainController;

import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTOList;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;


public class FeedSetConfigurationWidget extends AbstractListConfigurationWidget<FeedSetDTO, FeedSetDTOList>{

	
	@Override
	public void init() {
		super.init();
		if (isEditable) {
			super.addToolBarNewItemMenu("New Feed Set");
		}
	}

	
	@Override
	protected EditableItem<FeedSetDTO, FeedSetDTOList> createEditableItem() {
		
		return new EditableFeedSet(this);
	}

	@Override
	protected SelectableItem<FeedSetDTO, FeedSetDTOList> createSelectableItem(
			FeedSetDTO obj,
			AbstractListConfigurationWidget<FeedSetDTO, FeedSetDTOList> parent) {
		return new SelectableFeedSet(obj, parent, true);	}

	@Override
	public void loadData() {
		setCreatingNew(false);
		loadFeedSets();
		
	}

	private void loadFeedSets() {
		isProcessing(true);
		contentPanel.clear();
		MainController.getInstance().getCurrentPersonaController()
				.loadFeedSets(this);
		
	}


	@Override
	public void onSuccessLoadObjects(FeedSetDTOList list) {
		
		isProcessing(false);
		for (FeedSetDTO feedSet: list.getFeedSets()) {
			SelectableItem<FeedSetDTO, FeedSetDTOList> item = createSelectableItem(
					feedSet, this);
			addListItem(item);
		}
		
	}

	@Override
	protected void removeItem(SelectableItem<FeedSetDTO, FeedSetDTOList> item) {
		MainController.getInstance().getCurrentPersonaController().removeFeedSet(item.dataObject, item);
		
	}

	@Override
	public void saveObject(FeedSetDTO object) {
		if (object.getId() == -1) {
			saveNewFeedSet(object);
		} else {
			saveExistingFeedSet(object);
		}
	}
	
	private void saveExistingFeedSet(FeedSetDTO object) {
		MainController.getInstance().getCurrentPersonaController().saveFeedSet(object, getSelectedItem());
		
	}

	private void saveNewFeedSet(FeedSetDTO object) {
		MainController.getInstance().getCurrentPersonaController().createFeedSet(object, this);
		
	}

	public class EditableFeedSet extends EditableItem<FeedSetDTO, FeedSetDTOList> {

		private TextBox feedSetName;
		private TextArea urlList;
		private TextArea filter;
		public EditableFeedSet(AbstractListConfigurationWidget<FeedSetDTO,FeedSetDTOList> parent) {
			super(parent);
			InlineHTML textLabel = new InlineHTML("Name: ");
			feedSetName = new TextBox();
			contentPanel.add(textLabel);
			contentPanel.add(feedSetName);
			
			InlineHTML feedUrlTitle = new InlineHTML("Add URL for the feeds, one per line:");
			urlList = new TextArea();
			
			urlList.setWidth("580px");
			// update.setHeight("35px");
			urlList.addStyleName("input");
			urlList.setVisibleLines(4);
			
			contentPanel.add(feedUrlTitle);
			contentPanel.add(urlList);
			
			InlineHTML filterTitle = new InlineHTML("Use posts with words, one word per line:");
			filter = new TextArea();
			
			filter.setWidth("580px");
			// update.setHeight("35px");
			filter.addStyleName("input");
			filter.setVisibleLines(4);
			
			contentPanel.add(filterTitle);
			contentPanel.add(filter);

			
			
			
			

			
			
		}
		@Override
		public void focus() {
			if ( this.dataObject == null ) {
				this.feedSetName.setFocus(true);
				
			} else {
				this.urlList.setFocus(true);
			}
			
		}

		@Override
		public void refresh() {
			this.feedSetName.setValue(dataObject.getName());
			
			StringBuffer sb = new StringBuffer();
			for (String url: dataObject.getFeedUrls()) {
				sb.append(url);
				sb.append("\n");
			}
			this.urlList.setValue(sb.toString());
			
			sb = new StringBuffer();
			for (String filter: dataObject.getFilter()) {
				sb.append(filter);
				sb.append("\n");
			}
			this.filter.setValue(sb.toString());
			
			
		}

		@Override
		protected void save() {
			
			boolean isValid = true;
			if (this.feedSetName.getValue().trim().length()==0) {
				MainController.getInstance().addError("Provide a name for the Feed Set");
				isValid = false;
			}
			
			if ( this.urlList.getValue().trim().length()==0) {
				MainController.getInstance().addError("Provide at least one URL");
				isValid = false;
			}
			
			//
			
			if (isValid) {
				
				FeedSetDTO dto = new FeedSetDTO();
				if (dataObject!=null) {
					dto.setId(dataObject.getId());
				}
				
				dto.setName(dataObject.getName());
				//
				
				List<String> urlList = getLines(this.urlList.getValue());
				for (String url:urlList) {
					dto.addFeedUrl(url);
				}
					

				List<String> filterList = getLines(this.filter.getValue());
				for (String filter:filterList) {
					dto.addFilter(filter);
				}
				
				setUpdating(true);
				parent.saveObject(dto);

			}
			
			
			
			
		}

		private List<String> getLines(String text) {
			List<String> retList = new ArrayList<String>();
			//Split text by \n
			String[] lines = text.split("\\n");
			for (String line:lines) {
				retList.add(line);
			}
			
			return retList;
		}
		@Override
		public void onLinksShortened(Map<String, String> result) {
			//DO nothing here
			
		}
		
	}
	public class SelectableFeedSet extends SelectableItem<FeedSetDTO, FeedSetDTOList> {
		private HTML nameHtml;
		private HTML urlHtml;
		private HTML filterHtml;
		
		public SelectableFeedSet(FeedSetDTO obj,
				AbstractListConfigurationWidget<FeedSetDTO, FeedSetDTOList> parent, boolean isEditable) {
			super(parent,isEditable);
			setDataObject(obj);
			
			nameHtml = new HTML();
			urlHtml = new HTML();
			filterHtml = new HTML();
			
			content.add(nameHtml);
			content.add(urlHtml);
			content.add(filterHtml);
			
			this.filterHtml.addStyleName("tags");
			
			
		}
		@Override
		public String getSearchableText() {
			return dataObject.getFilter()+" "+dataObject.getName();
		}

		@Override
		protected void refresh() {
			String nameText = "<span class=\"label\">Name:</span> "+dataObject.getName();
			nameHtml.setHeight(nameText);
			
			StringBuffer sb = new StringBuffer();
			for (String feedUrl: this.dataObject.getFeedUrls()) {
				sb.append("<a href=\""+feedUrl+"\">"+getTruncatedUrl(feedUrl)+"</a>");
				sb.append(",");
			}
			this.urlHtml.setHTML(sb.toString());
			
			
			
			sb = new StringBuffer();
			
			for (String filter: this.dataObject.getFilter()) {
				sb.append(filter);
				sb.append(",");
			}
			this.filterHtml.setHeight(sb.toString());
			
			
			
			
			
		}
		private String getTruncatedUrl(String feedUrl) {
			String retFeed = feedUrl;
			if (feedUrl.length()>50) {
				retFeed = feedUrl.substring(0, 50);
				retFeed = retFeed +"...";
			}
			return retFeed;
		}
		
	}

}
