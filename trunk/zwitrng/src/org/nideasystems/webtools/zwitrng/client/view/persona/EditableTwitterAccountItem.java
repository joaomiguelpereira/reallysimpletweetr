package org.nideasystems.webtools.zwitrng.client.view.persona;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.view.users.TwitterUsersHtmlUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

public class EditableTwitterAccountItem extends VerticalPanel implements
		HasMouseOverHandlers, HasMouseOutHandlers, HasDoubleClickHandlers,
		HasClickHandlers {

	private InlineHTML statusLine;
	private Widget popularityPanel;
	private Widget userActivity;
	private Widget userRateLimitsPanel;

	public EditableTwitterAccountItem(TwitterAccountDTO account) {
		FlexTable table = new FlexTable();
		FlexCellFormatter formatter = table.getFlexCellFormatter();

		// User Image
		Image userImg = new Image(account.getTwitterImageUrl());
		userImg.setWidth(Constants.USER_IMAGE_MEDIUM_WIDTH);
		userImg.setHeight(Constants.USER_IMAGE_MEDIUM_HEIGHT);

		table.setWidget(0, 0, userImg);
		formatter.setWidth(0, 0, "50px");
		VerticalPanel userInfo1 = new VerticalPanel();
		userInfo1.add(new InlineHTML(TwitterUsersHtmlUtils
				.buildUserLineHtml(account)));
		userInfo1.add(new InlineHTML(TwitterUsersHtmlUtils
				.buildLocationHtml(account)));

		// userInfo1.add(userLine)
		userInfo1.add(new InlineHTML(TwitterUsersHtmlUtils
				.buildUserHomePageHtml(account)));

		table.setWidget(0, 1, userInfo1);
		this.add(table);

		this
				.add(new InlineHTML(TwitterUsersHtmlUtils
						.buildUserBioHtml(account)));
		// HTML line = new HTML("&nbsp;");
		// this.add(line);
		statusLine = new InlineHTML(TwitterUsersHtmlUtils.buildStatusLine(account
				.getTwitterStatusText()));
		this.add(statusLine);

		popularityPanel = TwitterUsersHtmlUtils.buildUserPopularityPanel(account,
				false);
		// add Info
		this
				.add(popularityPanel);

		
		userActivity = TwitterUsersHtmlUtils.buildUserActivityPanel(account); 
		this.add(userActivity);

		userRateLimitsPanel = TwitterUsersHtmlUtils.buildUserRateLimitPanel(account);
		this.add(userRateLimitsPanel);

		this.statusLine.setVisible(false);
		this.popularityPanel.setVisible(false);
		this.userActivity.setVisible(false);
		this.userRateLimitsPanel.setVisible(false);
		this.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);
		this.setHeight(Constants.EDITABLE_TEMPLATE_MIN_HEIGHT);
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
				expandPanel();
				
			}

			
		});
		
	}

	private void expandPanel() {
		this.statusLine.setVisible(!this.statusLine.isVisible());
		this.popularityPanel.setVisible(!this.popularityPanel.isVisible());
		this.userActivity.setVisible(!this.userActivity.isVisible());
		this.userRateLimitsPanel.setVisible(!this.userRateLimitsPanel.isVisible());
		
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

}
