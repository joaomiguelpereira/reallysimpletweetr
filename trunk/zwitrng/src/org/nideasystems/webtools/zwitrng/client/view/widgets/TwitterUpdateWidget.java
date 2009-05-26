package org.nideasystems.webtools.zwitrng.client.view.widgets;

import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;



import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This is a wifget rendering a single update from
 * 
 * @author jpereira
 * 
 */
public class TwitterUpdateWidget extends AbstractVerticalPanelView {

	private HorizontalPanel fullTweet = new HorizontalPanel();
	private VerticalPanel tweet = new VerticalPanel();
	//private HorizontalPanel rightPanel = new HorizontalPanel();
	private TwitterUpdateDTO twitterUpdate;
	private final SendUpdateWidget sendUpdate = new SendUpdateWidget();
	private boolean isSendUpdateVisible = false;
	final private HorizontalPanel tweetOptions = new HorizontalPanel();

	public TwitterUpdateWidget() {
		super();
		super.setWidth("700px");

	}

	@Override
	public void init() {
		// it's better now do some HTML
		fullTweet.setWidth("700px");
		

		fullTweet.setSpacing(5);
		// Set the image
		Image userImg = new Image(twitterUpdate.getTwitterAccount()
				.getTwitterImageUrl());
		userImg.setWidth("48px");
		userImg.setHeight("48px");
		
		
		// Create the update info
		// TODO: Parse html
		HTML updateText = new HTML("<span class=\"userScreenName\">"
				+ twitterUpdate.getTwitterAccount().getTwitterScreenName()
				+ " </span><span class=\"text\">" + twitterUpdate.getText()
				+ "<span>");

		HorizontalPanel tweetMetaInfoPanel = new HorizontalPanel();

		HTML tweetUpdateMetaData = new HTML("<span class=\"createdAt\">"
				+ twitterUpdate.getCreatedAt()
				+ "<span> from <span class=\"source\">"
				+ twitterUpdate.getSource() + "</span>");
		tweetUpdateMetaData.setStyleName("twitterUpdateMetaData");
		
		tweet.add(updateText);
		tweet.add(tweetMetaInfoPanel);
		tweet.setWidth("552px");
		
		fullTweet.add(userImg);
		fullTweet.add(tweet);
		super.add(fullTweet);

		
		//Show Tweet options
		HTML showTweetOptions = new HTML("Show Options");
		showTweetOptions.setStyleName("twitterUpdateMetaData");
		showTweetOptions.addStyleName("link");
		
		tweetMetaInfoPanel.add(tweetUpdateMetaData);
		tweetMetaInfoPanel.add(showTweetOptions);
		
		

		showTweetOptions.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				tweetOptions.setVisible(!tweetOptions.isVisible());
				sendUpdate.setVisible(false);
			}

		});

		tweetOptions.setSpacing(5);
		HTML followUser = new HTML("unfollow user (do later)");
		HTML retweet = new HTML("retweet");
		
		HTML reply = new HTML("reply");
		
		followUser.addStyleName("link");
		retweet.addStyleName("link");
		reply.addStyleName("link");
		
		tweetOptions.add(followUser);
		tweetOptions.add(retweet);
		tweetOptions.add(reply);

		retweet.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				if ( sendUpdate.getType() == SendUpdateWidget.RETWEET && sendUpdate.isVisible() ) {
					isSendUpdateVisible = false;
					
				} else {
					isSendUpdateVisible  = true;
				}
				sendUpdate.setVisible( isSendUpdateVisible );
				sendUpdate.setType(SendUpdateWidget.RETWEET);
				sendUpdate.setInResponseTo(twitterUpdate);
				
			
				sendUpdate.refresh();			
				
			}
			
		});
		reply.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if ( sendUpdate.getType() == SendUpdateWidget.REPLY && sendUpdate.isVisible() ) {
					isSendUpdateVisible = false;
					
				} else {
					isSendUpdateVisible  = true;
				}
				sendUpdate.setVisible( isSendUpdateVisible );
				sendUpdate.setType(SendUpdateWidget.REPLY);
				sendUpdate.setInResponseTo(twitterUpdate);
				
				sendUpdate.refresh();
				
			}
			
		});
		
		
		sendUpdate.setController(getController());
		sendUpdate.init();
		sendUpdate.setVisible(false);
		tweetOptions.add(sendUpdate);
		tweetOptions.setVisible(false);

		super.add(tweetOptions);
		super.add(sendUpdate);
				

	}

	public void setTwitterUpdate(TwitterUpdateDTO twitterUpdate) {
		this.twitterUpdate = twitterUpdate;
	}

	public TwitterUpdateDTO getTwitterUpdate() {
		return twitterUpdate;
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		Window.alert("Is Updating");

	}

}
