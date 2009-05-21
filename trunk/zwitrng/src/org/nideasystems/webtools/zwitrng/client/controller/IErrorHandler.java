package org.nideasystems.webtools.zwitrng.client.controller;

public interface IErrorHandler {

	public void addError(String errorMsg);
	public void addException(Throwable tr);
}
