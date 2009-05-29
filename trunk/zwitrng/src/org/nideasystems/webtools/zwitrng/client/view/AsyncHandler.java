package org.nideasystems.webtools.zwitrng.client.view;

public interface AsyncHandler {
	
	public void onSuccess(Object arg);

	public void onFailure(Throwable tr);
	

}
