package org.nideasystems.webtools.zwitrng.client.view.configuration;

import org.nideasystems.webtools.zwitrng.shared.model.IDTO;

public interface ConfigurationListSelectListener<T extends IDTO> {

	public void onItemSelected(T obj);
}
