package org.nideasystems.webtools.zwitrng.client.services;

import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * The twitter service
 * @author jpereira
 *
 */

@RemoteServiceRelativePath("twitterService")
public interface TwitterService extends RemoteService{
	List<TwitterUpdateDTO> search(TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter) throws Exception;
	List<TwitterUpdateDTO> getTwitterUpdates(TwitterAccountDTO twitterAccount, FilterCriteriaDTO filter) throws Exception;
	
}
