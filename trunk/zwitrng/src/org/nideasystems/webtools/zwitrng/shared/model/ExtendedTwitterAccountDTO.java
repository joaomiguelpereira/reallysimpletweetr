package org.nideasystems.webtools.zwitrng.shared.model;

public class ExtendedTwitterAccountDTO implements IModel {
	
	private boolean imFollowing = false;
	private boolean imBlocking = false;
	public void setImFollowing(boolean imFollowing) {
		this.imFollowing = imFollowing;
	}
	public boolean isImFollowing() {
		return imFollowing;
	}
	public void setImBlocking(boolean imBlocking) {
		this.imBlocking = imBlocking;
	}
	public boolean isImBlocking() {
		return imBlocking;
	}
	

}
