package org.nideasystems.webtools.zwitrng.server;

import java.util.List;

import org.nideasystems.webtools.zwitrng.client.services.TwitterService;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TwitterServiceImpl extends RemoteServiceServlet implements TwitterService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -481643127871478064L;

	@Override
	public List<TwitterUpdateDTO> search(TwitterAccountDTO twitterAccount,
			FilterCriteriaDTO filter) throws Exception{
		return  TwitterServiceAdapter.get().searchStatus(twitterAccount, filter);
		
		
	}

	@Override
	public List<TwitterUpdateDTO> getTwitterUpdates(UpdatesType type,
			TwitterAccountDTO twitterAccount) throws Exception {
		return  TwitterServiceAdapter.get().getUpdates(type,twitterAccount);
		
	}

}
