package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;

import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTOList;


import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class FeedSetConfigurationWidget extends
		AbstractListConfigurationWidget<FeedSetDTO, FeedSetDTOList> {

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
		return new SelectableFeedSet(obj, parent, true);
	}

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
		for (FeedSetDTO feedSet : list.getFeedSets()) {
			SelectableItem<FeedSetDTO, FeedSetDTOList> item = createSelectableItem(
					feedSet, this);
			addListItem(item);
		}

	}

	@Override
	protected void removeItem(SelectableItem<FeedSetDTO, FeedSetDTOList> item) {
		MainController.getInstance().getCurrentPersonaController()
				.removeFeedSet(item.dataObject, item);

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
		MainController.getInstance().getCurrentPersonaController().saveFeedSet(
				object, getSelectedItem());

	}

	private void saveNewFeedSet(FeedSetDTO object) {
		MainController.getInstance().getCurrentPersonaController()
				.createFeedSet(object, this);

	}

	public class EditableFeedSet extends
			EditableItem<FeedSetDTO, FeedSetDTOList> {

		private static final int DEFAULT_TEMPLATE_LIST_MAX_CHARS = 500;
		private TextBox feedSetName;
		private TextArea urlList;
		private InlineHTML  remainingChars=new InlineHTML(""+DEFAULT_TEMPLATE_LIST_MAX_CHARS);
		

		public EditableFeedSet(
				AbstractListConfigurationWidget<FeedSetDTO, FeedSetDTOList> parent) {
			super(parent);
			
			if (parent.isCreatingNew) {
				contentPanel.add(new InlineHTML("<h3>Create new Feed List</h3>"));
			} else {
				contentPanel.add(new InlineHTML("<h3>Edit Feed List</h3>"));
			}
			
			InlineHTML textLabel = new InlineHTML("Name: ");
			feedSetName = new TextBox();
			contentPanel.add(textLabel);
			contentPanel.add(feedSetName);

			InlineHTML feedUrlTitle = new InlineHTML(
					"Add URLs for the feeds, one URL per line:");
			urlList = new TextArea();

			urlList.setWidth(Constants.CONFIGURATION_INPUT_MAX_WIDTH);
			urlList.addStyleName("input");
			urlList.setVisibleLines(5);

			contentPanel.add(feedUrlTitle);
			contentPanel.add(urlList);
			contentPanel.add(remainingChars);

			urlList.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					updateRemainingChars();
					HTMLHelper.adjustLines(urlList, HTMLHelper.getLines(urlList.getValue()).length, 5, 10);
				}
				
			});
			
			
		}
		protected void updateRemainingChars() {
			int remainingCharsNbr = DEFAULT_TEMPLATE_LIST_MAX_CHARS
					- this.urlList.getValue().length();
			remainingChars.setHTML("" + remainingCharsNbr);

		}

		@Override
		public void focus() {
			if (this.dataObject == null) {
				this.feedSetName.setFocus(true);

			} else {
				this.urlList.setFocus(true);
			}

		}

		@Override
		public void refresh() {
			this.feedSetName.setValue(dataObject.getName());
			if ( dataObject!=null) {
				this.feedSetName.setEnabled(false);
			}
			StringBuffer sb = new StringBuffer();
			for (String url : dataObject.getFeedUrls()) {
				sb.append(url);
				sb.append("\n");
			}
			this.urlList.setValue(sb.toString());


			
	

		}

		@Override
		protected void save() {

			boolean isValid = true;
			if (this.feedSetName.getValue().trim().length() == 0 ) {
				MainController.getInstance().addError(
						"Provide a name for the Feed Set");
				isValid = false;
			}

			if (this.urlList.getValue().trim().length() == 0 ||this.urlList.getValue().trim().length()>500) {
				MainController.getInstance().addError(
						"Provide at least one URL and try not exceed the 500 characters");
				isValid = false;
			}

			//

			if (isValid) {

				FeedSetDTO dto = new FeedSetDTO();
				if (dataObject != null) {
					dto.setId(dataObject.getId());
				}

				dto.setName(this.feedSetName.getValue());
				//

				List<String> urlList = getLines(this.urlList.getValue());
				for (String url : urlList) {
					dto.addFeedUrl(url);
				}

				
				setUpdating(true);
				parent.saveObject(dto);

			}

		}

		private List<String> getLines(String text) {
			List<String> retList = new ArrayList<String>();
			// Split text by \n
			String[] lines = text.split("\\n");
			for (String line : lines) {
				retList.add(line);
			}

			return retList;
		}

		@Override
		public void onLinksShortened(Map<String, String> result) {
			// DO nothing here

		}

	}

	public class SelectableFeedSet extends
			SelectableItem<FeedSetDTO, FeedSetDTOList> {
		private HTML nameHtml;
		private HTML urlHtml;
		private HTML filterHtml;

		public SelectableFeedSet(
				FeedSetDTO obj,
				AbstractListConfigurationWidget<FeedSetDTO, FeedSetDTOList> parent,
				boolean isEditable) {
			super(parent, isEditable, false);
			setDataObject(obj);

			
			nameHtml = new HTML();
			urlHtml = new HTML();
			filterHtml = new HTML();

			content.add(nameHtml);
			content.add(urlHtml);
			content.add(filterHtml);

			this.filterHtml.addStyleName("tags");
			toolBar = createMenuOptions();
			content.add(toolBar);
			refresh();
			

		}

		@Override
		public String getSearchableText() {
			return dataObject.getName();
		}

		@Override
		protected void refresh() {
			nameHtml.setHTML("<h3>"+this.dataObject.getName()+"</h3>");
			
			StringBuffer sb = new StringBuffer();
			for (String feedUrl : this.dataObject.getFeedUrls()) {
				sb.append("<div>");
				sb.append(MainController.jsParseText(feedUrl));
				sb.append("</div>");
			}
			this.urlHtml.setHTML(sb.toString());

		}


	}

}
