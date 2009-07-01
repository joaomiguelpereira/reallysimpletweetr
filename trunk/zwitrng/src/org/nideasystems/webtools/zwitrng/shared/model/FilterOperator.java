package org.nideasystems.webtools.zwitrng.shared.model;

public enum FilterOperator {

	OR,
	AND;
	
	public String toString() {
		if (this.equals(OR)) {
			return "or";
		} 
		return "and";
	}
}
