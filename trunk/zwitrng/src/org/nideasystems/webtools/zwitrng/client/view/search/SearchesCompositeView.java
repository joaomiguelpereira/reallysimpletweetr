package org.nideasystems.webtools.zwitrng.client.view.search;

import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.view.AbstractTabbedPanel;


import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * This a tabbed view with updated of the user
 * @author jpereira
 *
 */
public class SearchesCompositeView extends AbstractTabbedPanel {
	Map<String, VerticalPanel> tabs = new HashMap<String, VerticalPanel>();

	public SearchesCompositeView() {
		super();
		
	}

	@Override
	public void init() {
		super.setWidth("700px");
		super.setAnimationEnabled(true);
		//add the default tab
		SearchView defaultSearchView = new SearchView();
		defaultSearchView.setController(getController());
		defaultSearchView.setName("Default");
		defaultSearchView.init();
		this.add(defaultSearchView,defaultSearchView.getName());
		this.selectTab(0);
		
		
		
		
		
	}
}
