package org.nideasystems.webtools.zwitrng.client.view.persona;

import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PersonaToolsWidget extends
		AbstractVerticalPanelView<PersonaController> {
	Button deleteBt = null;
	HorizontalPanel buttons = null;
	HorizontalPanel options = null;
	HTML optionsMenu = null;

	PopupPanel menuPopup = null;

	public PersonaToolsWidget() {
		super();

	}

	@Override
	public void init() {

		this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		/*
		 * buttons = new HorizontalPanel(); // newStatus = new
		 * HorizontalPanel(); this.deleteBt = new Button();
		 * this.deleteBt.setText("Delete"); this.deleteBt.setTitle("Delete");
		 * this.deleteBt.addClickHandler(new ClickHandler() {
		 * 
		 * @Override public void onClick(ClickEvent event) {
		 * getController().delete(); }
		 * 
		 * });
		 * 
		 * // **************************************** buttons.add(deleteBt);
		 */
		options = new HorizontalPanel();
		optionsMenu = new HTML("Options");
		optionsMenu.addStyleName("link");
		options.add(optionsMenu);

		// optionsMenu.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		options.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		this.add(options);
		// super.add(buttons);

		optionsMenu.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				openMenuOptions();

			}

		});

	}

	private void openMenuOptions() {
		int left = optionsMenu.getAbsoluteLeft();
		int top = optionsMenu.getAbsoluteTop();

		if (menuPopup == null) {

			menuPopup = new PopupPanel();

			VerticalPanel options = new VerticalPanel();
			HTML deleteOption = new HTML("Delete");
			deleteOption.addStyleName("link");
			
			deleteOption.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getController().delete();
					menuPopup.hide(true);
				}

			});
			options.add(deleteOption);
			menuPopup.setWidget(options);
			menuPopup.setAnimationEnabled(true);
			menuPopup.setAutoHideEnabled(true);
			menuPopup.setPopupPosition(left, top + 20);
		}
		menuPopup.show();
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		Window.alert("Is Updating");

	}

	public void refresh() {
		/*
		 * newStatusTxa.setValue(""); remainingChars.setText("140");
		 */
	}

}
