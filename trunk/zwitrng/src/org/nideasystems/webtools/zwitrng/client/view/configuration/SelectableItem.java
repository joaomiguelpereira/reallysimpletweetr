package org.nideasystems.webtools.zwitrng.client.view.configuration;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.shared.model.IModel;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class SelectableItem<T extends IModel, L extends IModel>
		extends VerticalPanel implements HasMouseOutHandlers,
		HasMouseOverHandlers, HasDoubleClickHandlers, HasClickHandlers,
		ConfigurationEditListener<T> {

	boolean isEditable = true;
	boolean isEditing = false;
	protected HorizontalPanel toolBar;
	protected SelectableItem<T, L> instance;
	protected AbstractListConfigurationWidget<T, L> parent;
	protected VerticalPanel content;
	protected T dataObject;
	protected EditableItem<T, L> editableInstance;

	/**
	 * Constructor
	 * 
	 * @param theParent
	 */
	public SelectableItem(AbstractListConfigurationWidget<T, L> theParent,
			boolean aIsEditable) {
		parent = theParent;
		instance = this;
		this.isEditable = aIsEditable;
		this.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);
		this.setHeight(Constants.EDITABLE_TEMPLATE_MIN_HEIGHT);
		this.addStyleName("list_item");
		content = new VerticalPanel();
		this.add(content);
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

		this.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (isEditable && !isEditing) {
					setEditing(true);
				}

			}

		});

		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				select(instance);

			}

		});
	}

	/**
	 * Create the Menu Options
	 * 
	 * @return
	 */
	// protected abstract HorizontalPanel createMenuOptions();
	protected HorizontalPanel createMenuOptions() {

		HorizontalPanel panel = new HorizontalPanel();

		FlexTable table = new FlexTable();

		// panel.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);

		panel.setSpacing(5);
		// Image edit
		// Image edit = new Image(Constants.EDIT_ICON);
		InlineHTML edit = new InlineHTML("Edit");
		edit.addStyleName("link");
		edit.setTitle("Edit this item");
		edit.setWidth(Constants.MINI_TOOLBAR_ICON_WIDTH);
		edit.setHeight(Constants.MINI_TOOLBAR_ICON_HEIGHT);
		table.setWidget(0, 0, edit);
		// panel.add(edit);
		// Image remove = new Image(Constants.REMOVE_ICON);
		InlineHTML remove = new InlineHTML("Delete");
		remove.addStyleName("link");
		remove.setTitle("Delete this item");
		remove.setWidth(Constants.MINI_TOOLBAR_ICON_WIDTH);
		remove.setHeight(Constants.MINI_TOOLBAR_ICON_HEIGHT);
		table.setWidget(0, 1, remove);

		panel.add(table);

		panel.setVisible(false);

		// Add handler
		remove.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (Window
						.confirm("Are you sure you want to delete this item?")) {
					parent.removeItem(instance);
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

	/**
	 * 
	 * @param instance
	 */
	protected void select(SelectableItem<T, L> instance) {
		parent.onSelect(instance);

	}

	public void onUnSelected() {
		setEditing(false);
		removeStyleName("list_item_selected");
		this.toolBar.setVisible(false);

	}

	protected void setEditing(boolean editing) {
		
			if (editableInstance == null) {
				editableInstance = createEditableItem();
				if ( editableInstance!= null ) {
					editableInstance.addEditListener(this);
					this.add(editableInstance);		
				}
			}
			if (editableInstance!=null) {
				editableInstance.setDataObject(this.dataObject);
				editableInstance.refresh();
				content.setVisible(!editing);
				editableInstance.setVisible(editing);
			
			}
			
	}

	

	protected EditableItem<T, L> createEditableItem() {
		return parent.createEditableItem();
	}

	
	//protected abstract EditableItem<T, L> createEditableItem();

	@Override
	public void onEditCancel() {
		setEditing(false);

	}

	protected void setDataObject(T obj) {
		dataObject = obj;
	}

	public void onSelected() {
		addStyleName("list_item_selected");
		// add toolbar
		if (!isEditing) {
			this.toolBar.setVisible(true);
		}
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());

	}

	@Override
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {

		return addDomHandler(handler, ClickEvent.getType());
	}

	protected abstract void refresh();

	public abstract String getSearchableText();

	@Override
	public void onObjectSaved(T object) {
		if (editableInstance != null) {
			editableInstance.setUpdating(false);
		}
		this.dataObject = object;
		refresh();
		onUnSelected();
		onSelected();

	}

	@Override
	public void saveObject(T object) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(Throwable tr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onObjectCreated(T object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onObjectRemoved(T object) {
		parent.contentPanel.remove(this);
		parent.onSelect(null);
		// this.removeFromParent();

	}

}
