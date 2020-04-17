package com.vantage.cochrane.webScrapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Topic {
	private String title ; 
	private String url ;
	private ArrayList<Review> reviews;
	Topic(){
		reviews= new ArrayList<Review>();
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
	public ArrayList<Review> getReviews() {
		return reviews;
	}
	public void addReview(Review review) {
		reviews.add(review);
	}

	/**
	 * This Method writes the extracted information onto a text file
	 */
	public void writeTopicReviewsToFile() {
		try {
			for(Review review:reviews) {
				App.writer.write(App.baseUrl+review.getUrl()+ "|" + this.title + "|" + review.getTitle() + "|" + review.getAuthor() + "|" + formatOutputDate(review.getPublishedDate())+ "\n\n");
			};
			System.out.println( this.title + ":" + reviews.size());
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private static String formatOutputDate(Date date) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

}