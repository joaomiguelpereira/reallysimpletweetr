package org.nideasystems.webtools.zwitrng.shared.model;

public class TwitterUserFilterDTO implements IDTO{


	private TwitterUserType type = TwitterUserType.FRIENDS;
	private int page = 1;
	private int count = 20;

	private String twitterUserScreenName =null;
	public void setType(TwitterUserType type) {
		this.type = type;
	}

	public TwitterUserType getType() {
		return type;
	}

	public String getTwitterUserScreenName() {
		return this.twitterUserScreenName;
	}

	public void setTwitterUserScreenName(String twitterUserScreenName) {
		this.twitterUserScreenName = twitterUserScreenName;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}
	
	
}
