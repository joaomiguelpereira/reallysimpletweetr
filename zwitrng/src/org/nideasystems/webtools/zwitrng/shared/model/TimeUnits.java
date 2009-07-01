package org.nideasystems.webtools.zwitrng.shared.model;

public enum TimeUnits {
	MINUTES, 
	HOURS,
	DAYS;
	
	public String toString() {
		String ret ="";
		if ( this.equals(MINUTES)) {
			ret = "minutes";
		} else if ( this.equals(HOURS)) {
			ret = "hours";
		} else if ( this.equals(DAYS)) {
			ret = "days";
		} else  {
			ret = "unknown";
		}
		return ret;
	}

}
