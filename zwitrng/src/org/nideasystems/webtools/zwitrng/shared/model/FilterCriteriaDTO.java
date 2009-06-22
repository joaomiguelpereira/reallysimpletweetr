package org.nideasystems.webtools.zwitrng.shared.model;

import org.nideasystems.webtools.zwitrng.shared.UpdatesType;



public class FilterCriteriaDTO implements IModel{
	
	private String searchText = null;
	private String key = null;
	private long sinceId = 1;
	private long maxId = 1;
	private int page = 1;	
	private int resultsPerPage = 20;
	private String refreshUrl = null;
	private double completedIn = 0.0;
	private long statusId = 1;
	
	
	private UpdatesType updatesType = null;

	
	

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	

	public void setUpdatesType(UpdatesType updatesType) {
		this.updatesType = updatesType;
	}

	public UpdatesType getUpdatesType() {
		return updatesType;
	}

	public void setSinceId(long sinceId) {
		this.sinceId = sinceId;
	}

	public long getSinceId() {
		return sinceId;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setMaxId(long maxId) {
		this.maxId = maxId;
	}

	public long getMaxId() {
		return maxId;
	}

	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	public int getResultsPerPage() {
		return resultsPerPage;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	public void setRefreshUrl(String refreshUrl) {
		this.refreshUrl = refreshUrl;
	}

	public String getRefreshUrl() {
		return refreshUrl;
	}

	public void setCompletedIn(double completedIn) {
		this.completedIn = completedIn;
	}

	public double getCompletedIn() {
		return completedIn;
	}

	public void reset() {
		key = null;
		sinceId = 1;
		maxId = 0;
		page = 1;
		//resultsPerPage = 20;
		refreshUrl = null;
		completedIn = 0.0;
		
	}

	public void setStatusId(long tweetId) {
		this.statusId = tweetId;
	}

	public long getStatusId() {
		return statusId;
	}


	
	

	
	

}
