package org.nideasystems.webtools.zwitrng.client.view.persona;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.view.users.TwitterUsersHtmlUtils;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;

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
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

public class EditableTwitterAccountItem extends VerticalPanel implements
		HasMouseOverHandlers, HasMouseOutHandlers, HasDoubleClickHandlers,
		HasClickHandlers {

	private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	private InlineHTML statusLine;
	private InlineHTML popularityPanel;
	private InlineHTML userActivity;
	private InlineHTML userRateLimitsPanel;

	private Image userImg;
	private PersonaDTO persona;
	private InlineHTML userLine;
	private InlineHTML localtionLine;
	private InlineHTML homePageLine;
	private DefaultHomeView parentView;

	public EditableTwitterAccountItem(PersonaDTO persona,
			DefaultHomeView parentView) {
		this.persona = persona;
		this.parentView = parentView;
		this.add(waitingImg);
		waitingImg.setVisible(false);

		FlexTable table = new FlexTable();
		FlexCellFormatter formatter = table.getFlexCellFormatter();
		// User Image
		userImg = new Image(persona.getTwitterAccount().getTwitterImageUrl());
		userImg.setWidth(Constants.USER_IMAGE_MEDIUM_WIDTH);
		userImg.setHeight(Constants.USER_IMAGE_MEDIUM_HEIGHT);

		table.setWidget(0, 0, userImg);
		formatter.setWidth(0, 0, "50px");
		
		VerticalPanel userInfo1 = new VerticalPanel();
		userLine = new InlineHTML(TwitterUsersHtmlUtils
				.buildUserLineHtml(persona.getTwitterAccount()));

		userInfo1.add(userLine);
		localtionLine = new InlineHTML(TwitterUsersHtmlUtils
				.buildLocationHtml(persona.getTwitterAccount()));
		userInfo1.add(localtionLine);

		homePageLine = new InlineHTML(TwitterUsersHtmlUtils
				.buildUserHomePageHtml(persona.getTwitterAccount()));

		userInfo1.add(homePageLine);

		table.setWidget(0, 1, userInfo1);
		this.add(table);

		this.add(new InlineHTML(TwitterUsersHtmlUtils.buildUserBioHtml(persona
				.getTwitterAccount())));
		// HTML line = new HTML("&nbsp;");
		// this.add(line);
		statusLine = new InlineHTML(TwitterUsersHtmlUtils
				.buildStatusLine(persona.getTwitterAccount()
						.getTwitterStatusText()));
		// this.add(statusLine);

		popularityPanel = new InlineHTML(TwitterUsersHtmlUtils
				.buildUserPopularityString(persona.getTwitterAccount()));
		// add Info
		// this.add(popularityPanel);

		userActivity = new InlineHTML(TwitterUsersHtmlUtils
				.buildUserActivityString(persona.getTwitterAccount()));
		// this.add(userActivity);

		userRateLimitsPanel = new InlineHTML(TwitterUsersHtmlUtils
				.buildUserRateLimitPanel(persona.getTwitterAccount()));
		// this.add(userRateLimitsPanel);

		DisclosurePanel disPanel = new DisclosurePanel("More...");
		disPanel.setAnimationEnabled(true);
		VerticalPanel moreOptions = new VerticalPanel();

		moreOptions.add(this.statusLine);
		HorizontalPanel toolsPanel = buildToolsPanel();
		moreOptions.add(this.popularityPanel);
		moreOptions.add(this.userActivity);
		moreOptions.add(this.userActivity);
		moreOptions.add(this.userRateLimitsPanel);
		moreOptions.add(toolsPanel);
		
		
		disPanel.setContent(moreOptions);
		this.add(disPanel);

		this.setWidth(Constants.MAIN_LIST_ITEM_WIDTH);
		this.setHeight(Constants.MAIN_LIST_ITEM_MIN_HEIGHT);
		this.addStyleName("list_item");
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
				// expandPanel();

			}

		});

	}

	private void refresh() {
		statusLine.setHTML(TwitterUsersHtmlUtils.buildStatusLine(persona
				.getTwitterAccount().getTwitterStatusText()));
		popularityPanel.setHTML(TwitterUsersHtmlUtils
				.buildUserPopularityString(persona.getTwitterAccount()));
		userActivity.setHTML(TwitterUsersHtmlUtils
				.buildUserActivityString(persona.getTwitterAccount()));
		userRateLimitsPanel.setHTML(TwitterUsersHtmlUtils
				.buildUserRateLimitPanel(persona.getTwitterAccount()));

		userImg.setUrl(persona.getTwitterAccount().getTwitterImageUrl());

		userLine.setHTML(TwitterUsersHtmlUtils.buildUserLineHtml(persona
				.getTwitterAccount()));
		localtionLine.setHTML(TwitterUsersHtmlUtils.buildLocationHtml(persona
				.getTwitterAccount()));
		homePageLine.setHTML(TwitterUsersHtmlUtils
				.buildUserHomePageHtml(persona.getTwitterAccount()));

	}

	private HorizontalPanel buildToolsPanel() {
		HorizontalPanel toolsPanel = new HorizontalPanel();

		InlineHTML synchOption = new InlineHTML("Synchronize");
		InlineHTML refreshOption = new InlineHTML("Refresh");
		InlineHTML deleteOption = new InlineHTML("Delete");
		synchOption.addStyleName("link");
		refreshOption.addStyleName("link");
		deleteOption.addStyleName("link");
		toolsPanel.setSpacing(4);
		toolsPanel.add(refreshOption);
		toolsPanel.add(synchOption);
		toolsPanel.add(deleteOption);

		deleteOption.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				parentView.getController().deletePersona(persona);

			}

		});
		refreshOption.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				reloadData();

			}

		});

		synchOption.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				synchronize();

			}

		});

		return toolsPanel;
	}

	private void setProcessing(boolean b) {
		this.waitingImg.setVisible(b);
	}

	private void reloadData() {
		setProcessing(true);
		parentView.getController().getPersonaInfo(persona.getName(), this);
	}

	private void synchronize() {
		setProcessing(true);
		parentView.getController().synchronize(persona, this);
		// TODO Auto-generated method stub

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

	public void onReloadPersonaError(Throwable caught) {
		setProcessing(false);
		MainController.getInstance().addException(caught);

	}

	public void onReloadPersonaSucess(PersonaDTO result) {
		setProcessing(false);
		this.persona = result;
		refresh();

	}

	public void onSynchronizePersonaError(Throwable caught) {
		setProcessing(false);
		MainController.getInstance().addException(caught);

	}

	public void onSynchronizePersonaSucess() {
		setProcessing(false);
		reloadData();

	}

}
