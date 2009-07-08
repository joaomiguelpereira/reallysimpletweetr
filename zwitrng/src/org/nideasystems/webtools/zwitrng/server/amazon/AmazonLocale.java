package org.nideasystems.webtools.zwitrng.server.amazon;

public enum AmazonLocale {
	
	US("http://ecs.amazonaws.com/onca/xml"),
	UK("http://ecs.amazonaws.co.uk/onca/xml"),
	DE("http://ecs.amazonaws.de/onca/xml"),
	JP("http://ecs.amazonaws.jp/onca/xml"),
	FR("http://ecs.amazonaws.fr/onca/xml"),
	CA("http://ecs.amazonaws.ca/onca/xml");
	
	private final String locale;
	AmazonLocale(String locale) {
		this.locale = locale;
	}
	
	public String locale() {
		return this.locale;
	}
	
	

}
