package org.nideasystems.webtools.zwitrng.server;

import java.util.List;

import org.nideasystems.webtools.zwitrng.client.services.TwitterService;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.StatusDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TwitterServiceImpl extends RemoteServiceServlet implements TwitterService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -481643127871478064L;

	@Override
	public List<StatusDTO> search(TwitterAccountDTO twitterAccount,
			FilterCriteriaDTO filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
