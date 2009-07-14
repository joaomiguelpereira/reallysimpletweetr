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
	
	private  List<TwitterUserDTO> users = null;
	
	public TwitterUserDTOList() {
		users = new ArrayList<TwitterUserDTO>();
	}
	

	public void setAccounts(List<TwitterUserDTO> accounts) {
		this.users = accounts;
	}

	public List<TwitterUserDTO> getAccounts() {
		return users;
	}


	public void add(TwitterUserDTO twAccount) {
		this.users.add(twAccount);
		
	}


	public void setFilter(TwitterUserFilterDTO filter) {
		this.filter = filter;
	}


	public TwitterUserFilterDTO getFilter() {
		return filter;
	} 

}
