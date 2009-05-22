package org.nideasystems.webtools.zwitrng.shared;

public enum UpdatesType {
	FRIENDS (1),
	MY(2),
	FAVORITES(3),
	DIRECT(4);
	
	private final int val;
	UpdatesType(int val) {
		this.val = val;
	}
}
