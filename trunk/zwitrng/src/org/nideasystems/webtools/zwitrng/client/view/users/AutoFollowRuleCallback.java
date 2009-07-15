package org.nideasystems.webtools.zwitrng.client.view.users;

import org.nideasystems.webtools.zwitrng.shared.model.AutoFollowRuleDTO;

public interface AutoFollowRuleCallback {
	
	public void onAutoFollowRuleChanged(AutoFollowRuleDTO rule);
	public void onError(Throwable tr);
	public void onAutoFollowRuleLoaded(AutoFollowRuleDTO rule);
	
	

}
