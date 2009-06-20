package org.nideasystems.webtools.zwitrng.client.controller;

import java.util.List;

import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.SelectSendingAccountWindow;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public interface IMainController {

	public void addError(String errorMsg);
	public void addException(Throwable tr);
	public void isProcessing(boolean isProcessing);
	public List<TwitterAccountDTO> getAllTwitterAccounts(
			SelectSendingAccountWindow selectSendingAccountWindow);
	public TwitterAccountController getTwitterAccountController(String userScreenName);
}