package org.nideasystems.webtools.zwitrng.client.controller;

public interface IMainController {

	public void addError(String errorMsg);
	public void addException(Throwable tr);
	public void isProcessing(boolean isProcessing);
}
