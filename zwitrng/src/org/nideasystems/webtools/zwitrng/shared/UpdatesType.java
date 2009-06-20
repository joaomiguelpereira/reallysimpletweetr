package org.nideasystems.webtools.zwitrng.shared;

public enum UpdatesType {
	FRIENDS (1),
	MY(2),
	FAVORITES(3),
	DIRECT_RECEIVED(4), MENTIONS(5), SEARCHES(6),SINGLE(7), CONVERSATION(8), STATUS(9), TWEET(10), DIRECT_SENT(11);
	
	private final int val;
	UpdatesType(int val) {
		this.val = val;
	}
	public int getVal() {
		return val;
	}
}
