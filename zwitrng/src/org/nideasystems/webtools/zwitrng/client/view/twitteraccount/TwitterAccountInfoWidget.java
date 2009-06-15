package org.nideasystems.webtools.zwitrng.client.view.twitteraccount;

import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This pannel contains the user info
 * @author jpereira
 *
 */
public class TwitterAccountInfoWidget extends VerticalPanel {

	private static final String DEFAULT_IMAGE_URL = "https://static.twitter.com/images/default_profile_bigger.png";
	//HorizontalPanel userInfoPanel = null;
	
	private static final String DEFAULT_HTML = "";
	HorizontalPanel leftPanel = new HorizontalPanel();
	VerticalPanel rightPanel = new VerticalPanel();
	
	Image userImage = null;
	HTML userName = null;
	HTML userScreenName = null;
	HTML userDescription = null;
	HTML userFollowing = null;
	HTML userFollowers = null;
	InlineHTML userUpdates = null;
	InlineHTML lastStatus = null;
	HTML tweetMetadata = null;
	
	public TwitterAccountInfoWidget(final TwitterAccountDTO twitterAccount) {
		super();
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		this.userImage = new Image(twitterAccount==null?DEFAULT_IMAGE_URL:twitterAccount.getTwitterImageUrl());
		horizontalPanel.add(this.userImage);
		
		VerticalPanel lastTwee = new VerticalPanel();
		
		if ( twitterAccount.getTwitterUpdateDto()!= null ) {
			FlowPanel tweetPanel = new FlowPanel();
			final InlineHTML userName = new InlineHTML(twitterAccount.getTwitterName()+" ("+twitterAccount.getTwitterScreenName()+"): ");
			userName.addStyleName("userScreenName");
			tweetPanel.add(userName);
			userName.addStyleName("link");
			userName.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
					MainController.getInstance().getPopupManager().showDelayedUserInfoPopup(userName.getAbsoluteLeft(), userName
							.getAbsoluteTop() + 20, twitterAccount.getTwitterScreenName());
					//createUserPopupPanel(screenName.getAbsoluteLeft(), screenName
					//		.getAbsoluteTop() + 20,null);

				}

			});

			if (twitterAccount.getTwitterUpdateDto().getText() != null ) {
				this.lastStatus = new InlineHTML(HTMLHelper.get().getParsedUpdateHtml(twitterAccount.getTwitterUpdateDto().getText()));
				tweetMetadata  = new HTML(HTMLHelper.get().getParsedMetaDataHtml(twitterAccount.getTwitterUpdateDto()));
			} else {
				this.lastStatus = new InlineHTML("huum, it seems you have no activity on twitter yet :)");
				tweetMetadata = new HTML("");
			}
			
			tweetPanel.addStyleName("tweet");
			
			tweetPanel.add(lastStatus);
			
			
			tweetMetadata.addStyleName("twitterUpdateMetaData");
			lastTwee.add(tweetPanel);
			lastTwee.add(tweetMetadata);
			
			horizontalPanel.add(lastTwee);
			
		}
		
		this.add(horizontalPanel);
		
		
		/*
		
		HorizontalPanel topPannel= new HorizontalPanel();
		HorizontalPanel middlePannel= new HorizontalPanel();
		HorizontalPanel bottomPannel= new HorizontalPanel();
		
	
		//create the pannel to hold image + info personal
		this.leftPanel = new HorizontalPanel();
				
		
		
		
		this.userName = new HTML(twitterAccount==null?DEFAULT_HTML:twitterAccount.getTwitterName());
		topPannel.add(this.userName);
		
		this.userScreenName = new HTML("("+(twitterAccount==null?DEFAULT_HTML:twitterAccount.getTwitterScreenName())+")");
		topPannel.add(this.userScreenName);

	
		this.userDescription = new HTML(twitterAccount==null?DEFAULT_HTML:twitterAccount.getTwitterDescription());
		middlePannel.add(this.userDescription);
		
			
		this.userFollowers =  new HTML("Followers: "+(twitterAccount==null?DEFAULT_HTML:twitterAccount.getTwitterFollowers()));
		bottomPannel.add(this.userFollowers);
		this.userFollowing =  new HTML("Following: "+(twitterAccount==null?DEFAULT_HTML:twitterAccount.getTwitterFriends()));
		bottomPannel.add(this.userFollowing);
		this.userUpdates =  new HTML("Updates: "+(twitterAccount==null?DEFAULT_HTML:twitterAccount.getTwitterUpdates()));
		bottomPannel.add(this.userUpdates);
		this.lastStatus = new HTML(twitterAccount.getTwitterUpdateDto().getText());
		//Window.alert(twitterAccount.getTwitterUpdateDto().getText());
		bottomPannel.add(this.lastStatus);
		
		rightPanel.add(topPannel);
		rightPanel.add(middlePannel);
		rightPanel.add(bottomPannel);
		
		//
		super.add(leftPanel);
		super.add(rightPanel);
		*/
		
		
	}
	public void updateLastStatus(TwitterUpdateDTO update) {
		if (update != null ) {
			this.lastStatus.setHTML(update.getText());
			assert(this.tweetMetadata!=null);
			assert(update.getCreatedAt()!=null);

			assert(this.tweetMetadata!=null);
			this.tweetMetadata.setHTML(HTMLHelper.get().getParsedMetaDataHtml(update));
		}
		
	}
}
