package org.nideasystems.webtools.zwitrng.server.amazon;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.nideasystems.webtools.zwitrng.server.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC;

import twitter4j.org.json.JSONObject;
import twitter4j.org.json.XML;

public class AmazonAdapter {
	
	private static final Logger log = Logger.getLogger(AmazonAdapter.class.getName());
	
	private static final String BOOKS_SEARCH_INDEX = "Books";
	private static final String ACCESS_KEY ="0H8E46FBZKK1RWK1RT02";
	
	private static final String BASE_URL ="http://ecs.amazonaws.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId="+ACCESS_KEY+"&Operation=ItemSearch&SearchIndex="+BOOKS_SEARCH_INDEX+"&Title=Harry%20Potter&Version=2008-08-19";
	//http://ecs.amazonaws.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=[ID]&Operation=ItemSearch&SearchIndex=Books&Title=Harry%20Potter&Version=2008-08-19
	
	private static ThreadLocal<AmazonAdapter> amazonAdapter = new ThreadLocal<AmazonAdapter>() {

		
		@Override
		protected AmazonAdapter initialValue() {
			return new AmazonAdapter();
		}
		
	};
	
	public List<AmazonBook> findItems(AmazonLocale locale,String searchIndex, String title) throws Exception{
		
		log.fine("________________________________________Starting AMAZON______________________-- ");
		List<AmazonBook> bookList = new ArrayList<AmazonBook>();
		String theUrl = buildUrl(locale,searchIndex,title);
		
		
		
		
			try {
				URL url = new URL(theUrl );
				
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
				Document doc = builder.parse(url.openStream());
		
				NodeList itemNodes = doc.getElementsByTagName("Item");
				
				for(int i=0; i< itemNodes.getLength(); i++) {
					AmazonBook book = new AmazonBook();
					Element element=(Element)itemNodes.item(i);
					log.fine("ASIN:"+XMLUtils.getElementValue(element, "ASIN"));
					
					book.setAsin(XMLUtils.getElementValue(element, "ASIN"));
					
					
					log.fine("DetailPageURL:"+XMLUtils.getElementValue(element, "DetailPageURL"));
					book.setDetailUrl(XMLUtils.getElementValue(element, "DetailPageURL"));
					
					log.fine("SalesRank:"+XMLUtils.getElementValue(element, "SalesRank"));
					
					//log.fine("SalesRank:"+element.getElementsByTagName("SalesRank").item(0));
					
					
					
					
					NodeList nList = element.getElementsByTagName("ItemAttributes");
					
					log.fine("Author:"+XMLUtils.getElementValue((Element)nList.item(0), "Author"));
					
					book.setAuthor(XMLUtils.getElementValue((Element)nList.item(0), "Author"));
					log.fine("Title:"+XMLUtils.getElementValue((Element)nList.item(0), "Title"));
					book.setTitle(XMLUtils.getElementValue((Element)nList.item(0), "Title"));
					
					log.fine("Binding:"+XMLUtils.getElementValue((Element)nList.item(0), "Binding"));
					
					
					log.fine("ISBN:"+XMLUtils.getElementValue((Element)nList.item(0), "ISBN"));
					book.setIsbn(XMLUtils.getElementValue((Element)nList.item(0), "ISBN"));
					
					log.fine("Label:"+XMLUtils.getElementValue((Element)nList.item(0), "Label"));
					book.setLabel(XMLUtils.getElementValue((Element)nList.item(0), "Label"));
					
					log.fine("Brand:"+XMLUtils.getElementValue((Element)nList.item(0), "Brand"));
					book.setBrand(XMLUtils.getElementValue((Element)nList.item(0), "Brand"));
					NodeList offer = element.getElementsByTagName("OfferSummary");
					Element offerSummary = (Element)offer.item(0);
					
					Element lowestPrice = (Element)offerSummary.getElementsByTagName("LowestUsedPrice").item(0); 
					
					log.fine("lowestPrice:"+XMLUtils.getElementValue(lowestPrice, "FormattedPrice"));
					book.setLowestPrice(XMLUtils.getElementValue(lowestPrice, "FormattedPrice"));
					bookList.add(book);
					
					/*
					amazonBook.setAsin(jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getLong("ASIN"));
					amazonBook.setAuthor(jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getJSONObject("ItemAttributes").getString("Author"));
					amazonBook.setTitle(jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getJSONObject("ItemAttributes").getString("Title"));
					amazonBook.setBrand(jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getJSONObject("ItemAttributes").getString("Brand"));
					amazonBook.setIsbn(jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getJSONObject("ItemAttributes").getString("ISBN"));
					amazonBook.setLabel(jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getJSONObject("ItemAttributes").getString("Label"));
					
					amazonBook.setLowestPrice(jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getJSONObject("OfferSummary").getJSONObject("LowestUsedPrice").getString("FormattedPrice"));
					 */
					
					//ISBN
					
					
					
				}
			
				/*
				StringWriter stw = new StringWriter();
				Transformer serializer = TransformerFactory.newInstance().newTransformer();
				serializer.transform(new DOMSource(doc), new StreamResult(stw));
				AmazonBook amazonBook = new AmazonBook();
				String xmlStr = stw.toString();	
				log.fine("XML as a String: "+xmlStr);
				JSONObject jSonObj = XML.toJSONObject(xmlStr);
				log.fine("XML as a JSon: "+jSonObj.toString());
				log.fine(": "+jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").toString());
				
				
				log.fine("DetailPageURL: "+jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getString("DetailPageURL"));
				log.fine("ASIN "+jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getLong("ASIN"));
				log.fine("ItemAttributes "+jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getJSONObject("ItemAttributes").toString());
				log.fine("Author "+jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getJSONObject("ItemAttributes").getString("Author"));
				log.fine("Title "+jSonObj.getJSONObject("ItemSearchResponse").getJSONObject("Items").getJSONArray("Item").getJSONObject(0).getJSONObject("ItemAttributes").getString("Title"));
			*/
			
			} catch (TransformerFactoryConfigurationError e) {
				log.severe("Error parsing XML Stream from AMAZON");
				throw new Exception(e);
			}			
			
			
				//bookList.add(amazonItem);
				
				

		return bookList;
	}
	private String buildUrl(AmazonLocale locale,String searchIndex, String title) {
		
		String urlEncoded = locale.locale()+"?Service=AWSECommerceService&AWSAccessKeyId="+ACCESS_KEY+"&Operation=ItemSearch&SearchIndex="+searchIndex+"&ResponseGroup=Medium&Title="+title;
		return urlEncoded;
		
	}
	public static AmazonAdapter get() {
		return amazonAdapter.get();
	}
	private AmazonAdapter() {
		
	}
	public AmazonBook getRandomBook(String searchTerms) {
		AmazonBook retBook = null;
		List<AmazonBook> books = null;
		try {
			books = AmazonAdapter.get().findItems(
					AmazonLocale.UK, "Books", searchTerms);
			if (books != null) {
				
				
				for (AmazonBook book : books) {
					/*outBuffer.append("<div>---------------</div>");
					outBuffer.append("<div>Title: " + book.getTitle()
							+ "</div>");
					outBuffer.append("<div>Author: " + book.getAuthor()
							+ "</div>");
					outBuffer.append("<div>URL: " + book.getDetailUrl()
							+ "</div>");
					outBuffer.append("<div>ISBN: " + book.getIsbn() + "</div>");
					outBuffer.append("<div>ASIN: " + book.getAsin() + "</div>");
					outBuffer.append("<div>BRAND: " + book.getBrand()
							+ "</div>");
					outBuffer.append("<div>LABEL: " + book.getLabel()
							+ "</div>");
					outBuffer.append("<div>Lowest Price: "
							+ book.getLowestPrice() + "</div>");*/

				}

			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if ( books!= null && books.size()>0) {
			double rand = Math.random();
			int index = (int) Math.round(rand * (books.size() - 1));
			retBook = books.get(index);
		}
		return retBook;

	}

}
