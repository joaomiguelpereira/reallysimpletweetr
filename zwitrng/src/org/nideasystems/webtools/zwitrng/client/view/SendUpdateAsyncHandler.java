package org.nideasystems.webtools.zwitrng.client.view;

public interface SendUpdateAsyncHandler {
	
	public void onSuccess(Object arg);

	public void onFailure(Throwable tr);
	
	public void onCancel();
	
	

}
