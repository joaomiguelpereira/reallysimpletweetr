package org.nideasystems.webtools.zwitrng.client.view.twitteraccount;

import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This pannel contains the user info
 * @author jpereira
 *
 */
public class TwitterAccountInfoWidget extends HorizontalPanel {

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
	HTML userUpdates = null;
	HTML lastStatus = null;
	
	public TwitterAccountInfoWidget(TwitterAccountDTO twitterAccount) {
		super();
		
		HorizontalPanel topPannel= new HorizontalPanel();
		HorizontalPanel middlePannel= new HorizontalPanel();
		HorizontalPanel bottomPannel= new HorizontalPanel();
		
	
		//create the pannel to hold image + info personal
		this.leftPanel = new HorizontalPanel();
				
		this.userImage = new Image(twitterAccount==null?DEFAULT_IMAGE_URL:twitterAccount.getTwitterImageUrl());
		this.leftPanel.add(this.userImage);
		
		
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
		
		
		
	}
	public void updateLastStatus(TwitterUpdateDTO update) {
		this.lastStatus.setHTML(update.getText());
	}
}
