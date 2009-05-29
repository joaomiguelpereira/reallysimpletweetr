package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.List;

public class TwitterUpdateDTOList implements IModel{
	
	private List<TwitterUpdateDTO> twitterUpdatesList = new ArrayList<TwitterUpdateDTO>();
	
	public void addTwitterUpdate(TwitterUpdateDTO update) {
		this.twitterUpdatesList.add(update);
	}
	public List<TwitterUpdateDTO> getTwitterUpdatesList() {
		return this.twitterUpdatesList;
	}

}
