package org.nideasystems.webtools.zwitrng.client.services;

import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.StatusDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TwitterServiceAsync {
	void search(TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter, AsyncCallback<List<StatusDTO>> callback);
	
}
