package org.nideasystems.webtools.zwitrng.client.view.widgets;

import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This is a wifget rendering a single update from
 * 
 * @author jpereira
 * 
 */
public class TwitterUpdateWidget extends AbstractVerticalPanelView {

	private HorizontalPanel container = new HorizontalPanel();
	private VerticalPanel middlePanel = new VerticalPanel();
	private HorizontalPanel rightPanel = new HorizontalPanel();
	private TwitterUpdateDTO twitterUpdate;
	

	public TwitterUpdateWidget() {
		super();
		super.setWidth("700px");

	}

	@Override
	public void init() {
		container.setWidth("700px");
		
		
		container.setSpacing(5);
		Image img = new Image(twitterUpdate.getTwitterAccount()
				.getTwitterImageUrl());
		img.setWidth("48px");
		container.add(img);

		HTML updateText = new HTML("<div>" + twitterUpdate.getText() + "<div>");
		
		HTML bottom = new HTML("<span class=\"createdAt\">"
				+ twitterUpdate.getCreatedAt()
				+ "<span> from <span class=\"source\">"
				+ twitterUpdate.getSource() + "</span>");
		bottom.setStyleName("twitterUpdateMetaData");
		middlePanel.add(updateText);
		middlePanel.add(bottom);
		middlePanel.setWidth("552px");
		container.add(middlePanel);
		ListBox actions = new ListBox();
		actions.addItem("Action 2");
		actions.addItem("Action 3");
		rightPanel.add(actions);
		rightPanel.setWidth("100px");
		container.add(rightPanel);
		super.add(container);

	}

	public void setTwitterUpdate(TwitterUpdateDTO twitterUpdate) {
		this.twitterUpdate = twitterUpdate;
	}

	public TwitterUpdateDTO getTwitterUpdate() {
		return twitterUpdate;
	}

}
