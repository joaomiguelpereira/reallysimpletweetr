package org.nideasystems.webtools.zwitrng.server.amazon;

import java.io.Serializable;

public class AmazonBook implements Serializable {

	private String detailUrl;
	private String asin;
	private String author;
	private String title;
	private String brand;
	private String label;
	private String isbn;
	private String lowestPrice;
	
	
	
	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getIsbn() {
		return isbn;
	}


	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}


	public String getLowestPrice() {
		return lowestPrice;
	}


	public void setLowestPrice(String lowesPrice) {
		this.lowestPrice = lowesPrice;
	}


	public String getBrand() {
		return brand;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 4393031794956051149L;


	public String getDetailUrl() {
		return detailUrl;
	}


	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}


	public String getAsin() {
		return asin;
	}


	public void setAsin(String string) {
		this.asin = string;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public void setBrand(String string) {
		// TODO Auto-generated method stub
		
	}


}
