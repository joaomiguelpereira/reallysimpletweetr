package org.nideasystems.webtools.zwitrng.client.view.configuration;

import org.nideasystems.webtools.zwitrng.shared.model.IModel;

public interface ConfigurationListSelectListener<T extends IModel> {

	public void onItemSelected(T obj);
}
