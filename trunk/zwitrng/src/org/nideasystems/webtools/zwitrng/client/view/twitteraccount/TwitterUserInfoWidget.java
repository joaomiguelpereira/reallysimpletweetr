package org.nideasystems.webtools.zwitrng.client.view.twitteraccount;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class TwitterUserInfoWidget extends PopupPanel implements
		HasMouseOverHandlers, HasMouseOutHandlers {

	private boolean hasMouseOver = false;
	private Timer hidePanelTimer = null;
	private TwitterUserInfoWidget instance = this;
	private static final String WIDTH = "550px";
	private static final String HEIGHT = "150px";

	public TwitterUserInfoWidget(TwitterAccountDTO account) {
		this.setAnimationEnabled(true);
		
		
		this.setWidget(createTopPanel(account));

		this.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				hasMouseOver = true;
				if (hidePanelTimer != null) {
					hidePanelTimer.cancel();
				}

			}

		});

		this.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				hasMouseOver = false;
				if (hidePanelTimer == null) {
					hidePanelTimer = new HidePanelTime();
				}
				hidePanelTimer.schedule(300);
			}

		});
	}

	private VerticalPanel createTopPanel(TwitterAccountDTO account) {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSpacing(2);
		mainPanel.setWidth(WIDTH);
		//mainPanel.setHeight(HEIGHT);

		HorizontalPanel topPanel = new HorizontalPanel();
		topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Image userImg = new Image(account.getTwitterImageUrl());
		userImg.setWidth(Constants.USER_IMAGE_MEDIUM_WIDTH);
		userImg.setHeight(Constants.USER_IMAGE_MEDIUM_HEIGHT);
		topPanel.add(userImg);

		VerticalPanel infoPanel = new VerticalPanel();
		infoPanel.setSpacing(0);
		
		InlineHTML userNamePlusUserScreeName = new InlineHTML(
				"<span class=\"userName\">" + account.getTwitterName()
						+ "</span> (<span class=\"userScreenName\">"
						+ account.getTwitterScreenName() + "</span>)");
		infoPanel.add(userNamePlusUserScreeName);
		
		if (account.getTwitterLocation() != null
				&& account.getTwitterLocation().length() > 0) {
			
			InlineHTML userLocation = new InlineHTML(
					"Location: <span class=\"userLocation\">"
							+ account.getTwitterLocation() + "</span>");
			infoPanel.add(userLocation);
		}
		
		if (account.getTwitterWeb() != null
				&& account.getTwitterWeb().length() > 1) {

			InlineHTML userWeb = new InlineHTML("<a href=\""+account.getTwitterWeb()+"\" target=\"_blank\" class=\"userWeb\">"
					+ account.getTwitterWeb() + "</a>");
			userWeb.addStyleName("link");
			infoPanel.add(userWeb);
		}

		topPanel.add(infoPanel);
		InlineHTML userDescription = new
		InlineHTML("<span class=\"userBio\">"+account.getTwitterDescription()+"</span>");
		
		VerticalPanel extendedUserInfo = new VerticalPanel();
		extendedUserInfo.addStyleName("activity_panel");
		extendedUserInfo.setWidth(WIDTH);
		Image waitingImg = new Image(Constants.WAITING_IMAGE);
		extendedUserInfo.add(waitingImg);
		HorizontalPanel activity = new HorizontalPanel();
		InlineHTML following = new InlineHTML("Following: "+account.getTwitterFriends());
		InlineHTML followers = new InlineHTML("Followers: "+account.getTwitterFollowers());
		InlineHTML updates = new InlineHTML("Updates: "+account.getTwitterUpdates());
		
		activity.add(following);
		activity.add(followers);
		activity.add(updates);
		extendedUserInfo.add(activity);
		mainPanel.add(topPanel);
		mainPanel.add(userDescription);
		mainPanel.add(extendedUserInfo);
		return mainPanel;
	}

	/**
	 * Timer to hide the panel
	 * 
	 * @author jpereira
	 * 
	 */
	private class HidePanelTime extends Timer {

		@Override
		public void run() {

			instance.hide(true);

		}

	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());

	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	@Override
	public void hide(boolean autoClosed) {
		if (!hasMouseOver) {
			super.hide(autoClosed);
		}

	}

}
