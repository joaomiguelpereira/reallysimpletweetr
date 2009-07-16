package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;

import org.nideasystems.webtools.zwitrng.shared.model.IDTO;

public abstract class AbstractListConfigurationWidget<T extends IDTO, L extends IDTO>
		extends VerticalPanel implements IConfigurationWidget,
		ConfigurationEditListener<T> {

	protected boolean isEditable = true;

	protected TextBox searchValue;
	protected VerticalPanel contentPanel;
	private Timer timer = null;
	protected T dataObject = null;
	private SelectableItem<T, L> selectedItem;
	private HorizontalPanel searchPanel;

	private HorizontalPanel toolBar;
	// protected ConfigurationController controller = null;

	private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	protected boolean isCreatingNew = false;
	private String maxHeight = null;
	private boolean initialized;
	
	private EditableItem<T, L> editableItemNew = null;

	private List<ConfigurationListSelectListener<T>> selectListeners = new ArrayList<ConfigurationListSelectListener<T>>();


	@Override
	public void init() {
		waitingImg.setVisible(false);
		this.add(waitingImg);
		// Create the toolbar
		toolBar = new HorizontalPanel();
		toolBar.setSpacing(5);
		this.add(toolBar);

		// Create the search
		searchPanel = new HorizontalPanel();
		InlineHTML searchTextLabel = new InlineHTML("Search: ");
		searchValue = new TextBox();
		searchValue.setWidth("150px");
		searchPanel.add(searchTextLabel);
		searchPanel.add(searchValue);
		this.add(searchPanel);

		contentPanel = new VerticalPanel();
		/*ScrollPanel scrollPanel = new ScrollPanel(contentPanel);
		if (maxHeight == null) {
			scrollPanel.setHeight(Constants.CONFIGURATION_PANEL_MAX_HEIGH);
		} else {
			scrollPanel.setHeight(maxHeight);
		}

		this.add(scrollPanel);
*/
		this.add(contentPanel);
		searchValue.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				startSearchTimer();

			}

		});
		initialized = true;
	}

	public void addSelectListener(ConfigurationListSelectListener<T> listener) {
		this.selectListeners.add(listener);

	}


	public void setEditable(boolean b) {
		this.isEditable = b;

	}

	public void setMaxHeight(String string) {
		this.maxHeight = string;

	}

	protected void setDataObject(T obj) {
		this.dataObject = obj;
	}

	protected void addListItem(SelectableItem<T, L> panel) {
		contentPanel.add(panel);

	}

	
	/**
	 * When the list of objects fails to load
	 * 
	 * @param tr
	 */
	public void onFailedLoadObjects(Throwable tr) {
		isProcessing(false);
		MainController.getInstance().addException(tr);
	}

	
	/**
	 * When the list of objects is loaded
	 * 
	 * @param list
	 */
	public abstract void onSuccessLoadObjects(L list);

	/**
	 * Add a tool bar option
	 * 
	 * @param menuString
	 *            The label of the options
	 * @param handler
	 *            the handler
	 */
	protected void addToolBarMenu(String menuString, ClickHandler handler) {
		InlineHTML option = new InlineHTML(menuString);
		option.addStyleName("link");
		option.addClickHandler(handler);
		toolBar.add(option);
	}

	/**
	 * Get the surrent item selected in the list
	 * 
	 * @return
	 */
	public SelectableItem<T, L> getSelectedItem() {
		return selectedItem;
	}

	protected abstract void removeItem(SelectableItem<T, L> item);

	public void onSelect(SelectableItem<T, L> selectedItem) {

		for (ConfigurationListSelectListener<T> listener : selectListeners) {
			listener.onItemSelected(selectedItem.dataObject);
		}
		if (isCreatingNew) {
			toolBar.setVisible(true);
			searchPanel.setVisible(true);
			contentPanel.remove(0);
			editableItemNew = null;
			isCreatingNew = false;
		}
		// check if is the same

		if (selectedItem == null) {
			this.selectedItem = selectedItem;
		} else if (this.selectedItem != null) {

			if (this.selectedItem != selectedItem) {

				this.selectedItem.onUnSelected();
				this.selectedItem = selectedItem;
				this.selectedItem.onSelected();
			}

		} else {
			this.selectedItem = selectedItem;
			this.selectedItem.onSelected();
		}

	}

	protected void startSearchTimer() {
		if (timer == null) {
			timer = new Timer() {
				@Override
				public void run() {
					filter();
				}

			};
		}
		timer.cancel();
		timer.schedule(500);

	}

	protected void filter() {
		// Get all widgets
		int widgetCount = contentPanel.getWidgetCount();
		int[] visibleIndexes = new int[widgetCount];
		for (int i = 0; i < widgetCount; i++) {
			SelectableItem<?, ?> templateWidget = (SelectableItem<?, ?>) contentPanel
					.getWidget(i);
			String lookupValue = searchValue.getValue().toLowerCase();
			String searchSubject = templateWidget.getSearchableText()
					.toLowerCase();
			if (searchSubject.contains(lookupValue)) {
				visibleIndexes[i] = 1; // Show
			} else {
				visibleIndexes[i] = 0;// dont show this
			}
		}

		// hide them
		for (int i = 0; i < widgetCount; i++) {
			SelectableItem<?, ?> template = (SelectableItem<?, ?>) contentPanel
					.getWidget(i);
			template.setVisible(visibleIndexes[i] == 1);
		}
	}

	protected abstract SelectableItem<T, L> createSelectableItem(T obj,
			AbstractListConfigurationWidget<T, L> parent);

	
	public void isProcessing(boolean b) {
		waitingImg.setVisible(b);

	}

	protected abstract EditableItem<T, L> createEditableItem();

	/**
	 * Show a widget to create a new Item
	 */
	private void showNewItem() {
		// Get a editableItem instance
		isCreatingNew = true;
		editableItemNew = createEditableItem();
		contentPanel.insert(editableItemNew, 0);
		
		editableItemNew.setNew(true);
		
		searchPanel.setVisible(false);
		toolBar.setVisible(false);

		editableItemNew.focus();
		editableItemNew.addEditListener(this);

		if (selectedItem != null) {
			selectedItem.onUnSelected();
			selectedItem = null;
		}

	}

	/**
	 * Called before the configuration goes visible
	 */
	public abstract void loadData();

	public void addToolBarNewItemMenu(String label) {

		addToolBarMenu(label, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showNewItem();

			}

		});

	}

	@Override
	public void onError(Throwable error) {
		isProcessing(false);
		if ( editableItemNew!= null) {
			editableItemNew.setUpdating(false);
		}
		
		MainController.getInstance().addException(error);

	}

	@Override
	public void onObjectCreated(T object) {
		isProcessing(false);
		toolBar.setVisible(true);
		searchPanel.setVisible(true);
		contentPanel.remove(0);
		isCreatingNew = false;
		SelectableItem<T, L> newItem = createSelectableItem(object, this);
		contentPanel.insert(newItem, 0);
		onSelect(newItem);

	}

	@Override
	public void onEditCancel() {
		if (isCreatingNew) {
			toolBar.setVisible(true);
			searchPanel.setVisible(true);
			contentPanel.remove(0);
			isCreatingNew = false;
		}

	}

	@Override
	public void onObjectSaved(T object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onObjectRemoved(T object) {
		// TODO Auto-generated method stub

	}

	public boolean isCreatingNew() {
		return isCreatingNew;
	}

	public void setCreatingNew(boolean isCreatingNew) {
		this.isCreatingNew = isCreatingNew;
		if (!this.isCreatingNew && isEditable) {
			toolBar.setVisible(true);
		}
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isInitialized() {
		return initialized;
	}


}
