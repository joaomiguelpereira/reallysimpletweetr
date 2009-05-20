package org.nideasystems.webtools.zwitrng.client.view.widgets;

import org.nideasystems.webtools.zwitrng.shared.model.TwittUpdate;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * This is a wifget rendering a single update from
 * @author jpereira
 *
 */
public class TwitterUpdateWidget extends HorizontalPanel {
	private HorizontalPanel leftPanel = new HorizontalPanel();
	private HorizontalPanel rightPanel = new HorizontalPanel();

	public TwitterUpdateWidget(TwittUpdate twittUpdate) {
		super();
		VerticalPanel topPanel = new VerticalPanel();

		HTML updateText = new HTML("<div>" + twittUpdate.getUpdate() + "<div>");
		topPanel.add(updateText);
		HTML timeUpdate = new HTML("<div>" + twittUpdate.getTimeUpdate()
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

}
