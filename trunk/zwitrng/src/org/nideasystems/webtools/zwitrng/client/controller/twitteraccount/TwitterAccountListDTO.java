package org.nideasystems.webtools.zwitrng.client.controller.twitteraccount;

import java.util.ArrayList;
import java.util.List;

import org.nideasystems.webtools.zwitrng.shared.model.IModel;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

public class TwitterAccountListDTO implements IModel {
	
	private  List<TwitterAccountDTO> accounts = null;
	
	public TwitterAccountListDTO() {
		accounts = new ArrayList<TwitterAccountDTO>();
	}
	

	public void setAccounts(List<TwitterAccountDTO> accounts) {
		this.accounts = accounts;
	}

	public List<TwitterAccountDTO> getAccounts() {
		return accounts;
	}


	public void add(TwitterAccountDTO twAccount) {
		this.accounts.add(twAccount);
		
	} 

}
