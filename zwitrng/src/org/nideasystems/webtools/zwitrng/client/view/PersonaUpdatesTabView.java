package org.nideasystems.webtools.zwitrng.client.view;

import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;

import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This contains all updates
 * @author jpereira
 *
 */
public class PersonaUpdatesTabView extends VerticalPanel {
	private FilterCriteriaDTO filter = null;
	
	public PersonaUpdatesTabView(FilterCriteriaDTO filter) {
		super();
		this.filter = filter;
	}

}
