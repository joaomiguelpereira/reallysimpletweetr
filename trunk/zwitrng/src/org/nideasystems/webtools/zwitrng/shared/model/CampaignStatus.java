package org.nideasystems.webtools.zwitrng.shared.model;

public enum CampaignStatus {
	STARTED,
	NOT_STARTED,
	FINISHED,
	STOPPED, PAUSED;

	public String toString() {
		String statusStr = "";
		if ( this.equals(STARTED)) {
			statusStr = "Started";
		} else if (this.equals(NOT_STARTED)) {
			statusStr = "Not Started";
		} else if ( this.equals(FINISHED)) {
			statusStr = "Finished";
		} else if (this.equals(STOPPED)){
			statusStr ="Stopped";
		} else if ( this.equals(PAUSED)) {
			statusStr = "Paused";
		}
		return statusStr;
	}
}
