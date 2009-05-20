package org.nideasystems.webtools.zwitrng.client.view;

import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteria;

import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This contains all updates
 * @author jpereira
 *
 */
public class PersonaUpdatesView extends VerticalPanel {
	private FilterCriteria filter = null;
	
	public PersonaUpdatesView(FilterCriteria filter) {
		super();
		this.filter = filter;
	}

}
