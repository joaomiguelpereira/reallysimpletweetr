package org.nideasystems.webtools.zwitrng.shared;

public enum UpdatesType {
	FRIENDS (1),
	MY(2),
	FAVORITES(3),
	DIRECT(4), MENTIONS(5), SEARCHES(6),SINGLE(3);
	
	private final int val;
	UpdatesType(int val) {
		this.val = val;
	}
	public int getVal() {
		return val;
	}
}
