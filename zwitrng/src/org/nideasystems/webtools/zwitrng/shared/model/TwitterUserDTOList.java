package org.nideasystems.webtools.zwitrng.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TwitterUserDTOList implements IDTO, Serializable {
	
	//The Filter used to retrieve this DTO
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1233614198902341175L;

	private TwitterUserFilterDTO filter;
	
	private  List<TwitterAccountDTO> accounts = null;
	
	public TwitterUserDTOList() {
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


	public void setFilter(TwitterUserFilterDTO filter) {
		this.filter = filter;
	}


	public TwitterUserFilterDTO getFilter() {
		return filter;
	} 

}
