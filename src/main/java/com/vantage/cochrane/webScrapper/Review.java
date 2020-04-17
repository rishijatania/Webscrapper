package com.vantage.cochrane.webScrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Review implements Runnable{
	private String title ; 
	private String url ;
	private String author;
	private Date publishedDate;
	private Topic topic;
	public Review(Topic topic) {
		this.topic=topic;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	
	/**
	 * This Method gets all the Reviews and its metadata associated to the Topics on Cochrane's Library.
	 */
	private void getReviews() {
		WebClient client = App.getClient();
		try {
			HtmlPage page = client.getPage(this.topic.getUrl());
			HtmlAnchor paginationAnchor=null;

			do {
				paginationAnchor = ((HtmlAnchor) page.getFirstByXPath(".//div[@class='pagination-next-link']/a"));
				List<HtmlElement> items = (List<HtmlElement>)(Object)page.getByXPath(".//div[@class='search-results-item-body']") ;
				if(items.isEmpty()){
					System.out.println("No items found !");
				}else{

					for(HtmlElement htmlItem :items) {
						HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//h3[@class='result-title']/a"));
						HtmlDivision author = htmlItem.getFirstByXPath(".//div[@class='search-result-authors']/div") ;

						HtmlDivision publishedDate = htmlItem.getFirstByXPath(".//div[@class='search-result-metadata-block']/div/div[@class='search-result-date']");

						this.title = itemAnchor.getTextContent();
						this.url = itemAnchor.getHrefAttribute();
						this.author = author.getTextContent();
						try {
							this.publishedDate= new SimpleDateFormat("dd MMM yyyy").parse(publishedDate.getTextContent());
						} catch (ParseException e1) {
							e1.printStackTrace();
						}

						topic.addReview(this);
					}
				}
				if(paginationAnchor!=null) {
					page = client.getPage(paginationAnchor.getHrefAttribute());
				}
			} while (paginationAnchor!=null);
		} catch(Exception e) {
			e.printStackTrace();
		}finally  {
			client.close();
		}
	}
	
	@Override
	public void run() {
		getReviews();
		synchronized(this) {
			topic.writeTopicReviewsToFile();
		}
	}
}