package org.nideasystems.webtools.zwitrng.client.services;

import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterServiceAsync {
	void search(TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter, AsyncCallback<List<TwitterUpdateDTO>> callback) throws Exception;
	
	void getTwitterUpdates(TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter, AsyncCallback<List<TwitterUpdateDTO>> callback) throws Exception;
	
}
