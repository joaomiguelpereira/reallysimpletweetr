package org.nideasystems.webtools.zwitrng.client.view;

import java.util.HashMap;
import java.util.Map;


import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * This a tabbed view with updated of the user
 * @author jpereira
 *
 */
public class SearchTabbedView extends AbstractTabbedPanel {
	Map<String, VerticalPanel> tabs = new HashMap<String, VerticalPanel>();

	public SearchTabbedView() {
		super();
		super.setWidth("700px");
		super.setAnimationEnabled(true);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
