package org.nideasystems.webtools.zwitrng.client.view.configuration;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.IModel;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTOList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TemplateFragmentsConfigurationWidget extends AbstractListConfigurationWidget<TemplateFragmentDTO, TemplateFragmentDTOList> {

	private HorizontalPanel searchPanel = null;
	private TextBox searchValue = null;
	private VerticalPanel contentPanel = null;
	private SelectableTemplateFragment selected = null;

	@Override
	public void init() {
		super.init();
		// ToolBar
		HorizontalPanel toolBar = new HorizontalPanel();
		toolBar.setSpacing(5);
		InlineHTML newTemplate = new InlineHTML("New Fragment");
		newTemplate.addStyleName("link");
		newTemplate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showNewTemplate();
				
			}

			
		});
		toolBar.add(newTemplate);
		
		this.add(toolBar);
		
		// Search bar

		// Search
		searchPanel = new HorizontalPanel();
		InlineHTML searchTextLabel = new InlineHTML("Search: ");
		searchValue = new TextBox();
		searchValue.setWidth("150px");
		searchPanel.add(searchTextLabel);
		searchPanel.add(searchValue);
		this.add(searchPanel);
		contentPanel = new VerticalPanel();
		ScrollPanel scrollPanel = new ScrollPanel(contentPanel);
		scrollPanel.setHeight(Constants.CONFIGURATION_PANEL_MAX_HEIGH);
		this.add(scrollPanel);
	}

	@Override
	public void loadData() {
		loadTemplateFragments();

	}
	
	/**
	 * Add a EditableTemplateFragmentwidget on top of the list.
	 */
	private void showNewTemplate() {
		//Since I'm using a new, pass it null
		EditableTemplateFragment editable = new EditableTemplateFragment(null);
		contentPanel.insert(editable, 0);
	}
	public void select(SelectableTemplateFragment what) {
		if (this.selected != null) {
			if (this.selected != what) {

				this.selected.onUnSelected();
				this.selected = what;
				this.selected.onSelected();
			}

		} else {
			this.selected = what;
			this.selected.onSelected();
		}
	}

	private void loadTemplateFragments() {
		isProcessing(true);
		contentPanel.clear();
		//getController().getTemplateFragments(this);

	}

	public void onFailedLoadTemplateFragments(Throwable caught) {
		Window.alert("Error: " + caught.getMessage());
		isProcessing(false);
	}

	public void onSuccessLoadTemplateFragments(TemplateFragmentDTOList result) {
		isProcessing(false);
		for (TemplateFragmentDTO temFrag : result.getTemplateFragmentList()) {
			SelectableTemplateFragment templateFrag = new SelectableTemplateFragment(
					temFrag, this, isEditable);
			contentPanel.add(templateFrag);
		}
	}

	private class EditableTemplateFragment extends VerticalPanel {
		
		public EditableTemplateFragment(TemplateFragmentDTO templateFragment) {
			InlineHTML nameLabel = new InlineHTML("Name");
			TextBox nameValue = new TextBox();
			
			this.add(nameLabel);
			this.add(nameValue);
			
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

		private TemplateFragmentDTO templateFragment;

		private InlineHTML textHtml;
		private HTML tagsHtml;
		private InlineHTML templateFragName;
		private SelectableTemplateFragment instance;
		private HorizontalPanel toolsBar;
		private boolean isEditing = false;
		private TemplateFragmentsConfigurationWidget parent;
		
		public SelectableTemplateFragment(TemplateFragmentDTO templateFragment, TemplateFragmentsConfigurationWidget theParent, boolean aIsEditable) {
			super(theParent, aIsEditable);
			this.instance = this;
			this.templateFragment = templateFragment;
			this.parent = theParent;
			this.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);
			this.setHeight(Constants.EDITABLE_TEMPLATE_MIN_HEIGHT);
			this.addStyleName("list_item");

			VerticalPanel mainPanel = new VerticalPanel();

			HorizontalPanel topPanel = new HorizontalPanel();

			InlineHTML nameLabel = new InlineHTML("Name: ");
			nameLabel.addStyleName("label");
			topPanel.add(nameLabel);

			templateFragName = new InlineHTML();
			topPanel.add(templateFragName);
			mainPanel.add(topPanel);
			textHtml = new InlineHTML();
			mainPanel.add(textHtml);

			HorizontalPanel tags = new HorizontalPanel();
			tagsHtml = new HTML();
			tags.add(tagsHtml);
			tags.addStyleName("tags");
			
			
			mainPanel.add(tags);

			this.add(mainPanel);
			toolsBar = createToolBar();
			toolsBar.setVisible(false);
			mainPanel.add(toolsBar);
			refresh();

			this.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					addStyleName("list_item_over");
				}

			});
			this.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					removeStyleName("list_item_over");

				}
			});

			this.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					parent.select(instance);


				}

			});

			this.addDoubleClickHandler(new DoubleClickHandler() {

				@Override
				public void onDoubleClick(DoubleClickEvent event) {
					setEditing(!isEditing);


				}

			});

		}

		public void onSelected() {
			addStyleName("list_item_selected");
			// add toolbar
			if (!isEditing) {
				this.toolsBar.setVisible(true);
			}
			
		}

		public void onUnSelected() {
			setEditing(false);

			removeStyleName("list_item_selected");
			this.toolsBar.setVisible(false);
			
		}

		/**
		 * Create the toolbar
		 * @return
		 */
		private HorizontalPanel createToolBar() {
			HorizontalPanel panel = new HorizontalPanel();

			FlexTable table = new FlexTable();

			// panel.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);

			panel.setSpacing(5);
			// Image edit
			// Image edit = new Image(Constants.EDIT_ICON);
			InlineHTML edit = new InlineHTML("Edit");
			edit.addStyleName("link");
			edit.setTitle("Edit this template Fragment");
			//edit.setWidth(Constants.MINI_TOOLBAR_ICON_WIDTH);
			//edit.setHeight(Constants.MINI_TOOLBAR_ICON_HEIGHT);
			table.setWidget(0, 0, edit);
			// panel.add(edit);
			// Image remove = new Image(Constants.REMOVE_ICON);
			InlineHTML remove = new InlineHTML("Delete");
			remove.addStyleName("link");
			remove.setTitle("Delete this template Fragment");
			//remove.setWidth(Constants.MINI_TOOLBAR_ICON_WIDTH);
			//remove.setHeight(Constants.MINI_TOOLBAR_ICON_HEIGHT);
			table.setWidget(0, 1, remove);

			panel.add(table);

			panel.setVisible(false);

			// Add handler
			remove.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (Window
							.confirm("Are you sure you want to delete the template:\n"
									+ templateFragment.getName())) {
						isProcessing(true);
						removeTemplateFragment(instance);
					}

				}

			});
			// Add handler
			edit.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					setEditing(true);
				}

				

			});

			return panel;
		}

	/*	private void setEditing(boolean b) {
			Window.alert("set editing");
			
		}*/
		protected void removeTemplateFragment(
				SelectableTemplateFragment instance2) {
			Window.alert("Removing");

		}

		

		/*private void refresh() {
			templateFragName.setHTML(templateFragment.getName());

			textHtml.setHTML(templateFragment.getList());

			StringBuffer sb = new StringBuffer();
			sb.append("Tags: ");
			for (String tag : templateFragment.getTags()) {
				sb.append(tag);
				sb.append(" ");
			}

			tagsHtml.setHTML(sb.toString());
		}*/

		@Override
		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return addDomHandler(handler, MouseOutEvent.getType());

		}

		@Override
		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());
		}

		@Override
		public HandlerRegistration addDoubleClickHandler(
				DoubleClickHandler handler) {
			return addDomHandler(handler, DoubleClickEvent.getType());
		}

		@Override
		public HandlerRegistration addClickHandler(ClickHandler handler) {

			return addDomHandler(handler, ClickEvent.getType());
		}

		@Override
		protected HorizontalPanel createMenuOptions() {
			// TODO Auto-generated method stub
			return null;
		}

		

		@Override
		protected void refresh() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getSearchableText() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected EditableItem<TemplateFragmentDTO, TemplateFragmentDTOList> createEditableItem() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onEditCancel() {
			// TODO Auto-generated method stub
			
		}

		

		@Override
		public void saveObject(TemplateFragmentDTO object) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(Throwable tr) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onObjectCreated(TemplateFragmentDTO object) {
			// TODO Auto-generated method stub
			
		}

	}
	@Override
	protected void filter() {
		// TODO Auto-generated method stub
		
	}



	
	@Override
	protected SelectableItem<TemplateFragmentDTO,TemplateFragmentDTOList> createSelectableItem(
			TemplateFragmentDTO obj,
			AbstractListConfigurationWidget<TemplateFragmentDTO, TemplateFragmentDTOList> parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onSuccessLoadObjects(TemplateFragmentDTOList list) {
		// TODO Auto-generated method stub
		
	}


	
	@Override
	protected EditableItem<TemplateFragmentDTO, TemplateFragmentDTOList> createEditableItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveObject(TemplateFragmentDTO object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onObjectSaved(TemplateFragmentDTO object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeItem(
			SelectableItem<TemplateFragmentDTO, TemplateFragmentDTOList> item) {
		// TODO Auto-generated method stub
		
	}

}
