package org.nideasystems.webtools.zwitrng.client.view.widgets;

import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * This is a wifget rendering a single update from
 * @author jpereira
 *
 */
public class TwitterUpdateWidget extends AbstractVerticalPanelView {
	private HorizontalPanel leftPanel = new HorizontalPanel();
	private HorizontalPanel rightPanel = new HorizontalPanel();
	private TwitterUpdateDTO twitterUpdate;
	
	public TwitterUpdateWidget() {
		super();
		}

	@Override
	public void init() {
		VerticalPanel topPanel = new VerticalPanel();

		HTML updateText = new HTML("<div>" + twitterUpdate.getText() + "<div>");
		topPanel.add(updateText);
		HTML timeUpdate = new HTML("<div>" + twitterUpdate.getSource()
				+ "<div>");
		topPanel.add(timeUpdate);
		leftPanel.add(topPanel);

		ListBox actions = new ListBox();
		actions.addItem("Action 2");
		actions.addItem("Action 3");

		rightPanel.add(actions);
		super.add(leftPanel);
		super.add(rightPanel);

		
	}

	public void setTwitterUpdate(TwitterUpdateDTO twitterUpdate) {
		this.twitterUpdate = twitterUpdate;
	}

	public TwitterUpdateDTO getTwitterUpdate() {
		return twitterUpdate;
	}

}
