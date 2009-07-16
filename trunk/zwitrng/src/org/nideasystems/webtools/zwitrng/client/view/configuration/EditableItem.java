package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.ArrayList;
import java.util.List;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.updates.ShortLinksListenerCallBack;

import org.nideasystems.webtools.zwitrng.shared.model.IDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class EditableItem<T extends IDTO, L extends IDTO> extends
		VerticalPanel implements ShortLinksListenerCallBack {
	protected T dataObject;
	protected boolean isNew = false;
	private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	private List<ConfigurationEditListener<T>> editListeners = new ArrayList<ConfigurationEditListener<T>>();
	protected EditableItem<T, L> instance = null;
	AbstractListConfigurationWidget<T, L> parent;
	protected VerticalPanel contentPanel;

	public EditableItem(AbstractListConfigurationWidget<T, L> theParent) {
		instance = this;
		parent = theParent;

		waitingImg.setVisible(false);
		this.add(waitingImg);
		this.addStyleName("list_item_selected");
		contentPanel = new VerticalPanel();
		this.add(contentPanel);
		this.setWidth(Constants.MAIN_LIST_ITEM_WIDTH);

		HorizontalPanel toolsPanel = new HorizontalPanel();
		toolsPanel.setSpacing(5);

		InlineHTML closeLink = new InlineHTML("Cancel");
		closeLink.addStyleName("link");
		toolsPanel.add(closeLink);
		Button saveButton = new Button("Save");
		toolsPanel.add(saveButton);
		this.add(toolsPanel);

		closeLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				close(true);

			}

		});

		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				save();

			}

		});

	}
	public void setUpdating(boolean b) {
		this.waitingImg.setVisible(b);
	}

	
	protected abstract void save();

	protected void close(boolean b) {
		for (ConfigurationEditListener<T> listener : getEditListener()) {
			listener.onEditCancel();
		}
	}

	protected void addEditListener(ConfigurationEditListener<T> listener) {
		this.editListeners.add(listener);
	}

	protected List<ConfigurationEditListener<T>> getEditListener() {
		return editListeners;
	}

	public void setDataObject(T obj) {
		this.dataObject = obj;
	}

	public void setNew(boolean b) {
		this.isNew = b;
	}

	public abstract void focus();
	public abstract void refresh();
}
