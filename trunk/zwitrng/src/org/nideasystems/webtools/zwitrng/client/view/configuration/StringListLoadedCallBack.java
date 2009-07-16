package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.List;

public interface StringListLoadedCallBack {

	public void onTemplatesNamesListLoaded(List<String> list);
	public void onTemplatesNamesListFail(Throwable tr);
}
