package org.nideasystems.webtools.zwitrng.shared.model;

public enum CampaignStatus {
	RUNNING,
	NOT_STARTED,
	FINISHED,
	CANCELED;

	public String toString() {
		String statusStr = "";
		if ( this.equals(RUNNING)) {
			statusStr = "Running";
		} else if (this.equals(NOT_STARTED)) {
			statusStr = "Not Started";
		} else if ( this.equals(FINISHED)) {
			statusStr = "Finished";
		} else if (this.equals(CANCELED)){
			statusStr ="Canceled";
		} else {
			statusStr = "Unknow";
		}
		return statusStr;
	}
}
