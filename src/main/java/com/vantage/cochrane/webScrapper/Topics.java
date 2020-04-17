package com.vantage.cochrane.webScrapper;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Topics {
	private ArrayList<Topic> topics = new ArrayList<Topic>();

	public void addTopic(Topic topic) {
		topics.add(topic);
	}

	public ArrayList<Topic> getTopics() {
		return topics;
	}
	/**
	 * This method fetches all the topics and their Url's for data extractions from Cochrane's library
	 */
	public void fetchAllTopics() {
		WebClient client= App.getClient();
		try {
			String searchUrl = App.baseUrl + "/reviews/topics";
			HtmlPage page = client.getPage(searchUrl);
			List<HtmlElement> items = (List<HtmlElement>)(Object)page.getByXPath(".//li[@class='browse-by-list-item']") ;
			if(items.isEmpty()){
				System.out.println("No items found !");
			}else{
				items.forEach(htmlItem ->{
					HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//a"));
					HtmlButton title = ((HtmlButton) htmlItem.getFirstByXPath(".//button[@class='btn-link browse-by-list-item-link']")) ;
					Topic topic =new Topic();
					topic.setUrl(itemAnchor.getHrefAttribute());
					topic.setTitle(title.getTextContent());	
					this.addTopic(topic);
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}

}
