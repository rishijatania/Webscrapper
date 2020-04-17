package com.vantage.cochrane.webScrapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;


/**
 * The Following Application reads the Cochrane's LIbrary to add it as a source of Medical Journal index.
 * @author rishijatania
 *
 */

public class App 
{
	public static String baseUrl = "https://www.cochranelibrary.com/cdsr";
	public static File file = new File("cochrane_reviews.txt");
	public static BufferedWriter writer;

	public static void main(String[] args ) {
		Topics topics=new Topics();
		topics.fetchAllTopics();

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("cochrane_reviews.txt"), "utf-8"));

			topics.getTopics().stream().forEach(topic -> {
				Thread object = new Thread(new Review(topic)); 
				object.start(); 
			}); 
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Creates a Http WebClient for accessing a webpage. 
	 * @return WebClient
	 */
	
	public static WebClient getClient() {
		WebClient client = new WebClient(BrowserVersion.CHROME);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		return client;
	}

}
