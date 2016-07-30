package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import crawler.WikiCrawler;
import index.Document;
import index.Index;
import index.WikiIndex;

public class SearchController {
	
	private Index indexer;
	private IndexController ic;
	private List<String> urls;
	
	public SearchController() throws IOException{
		indexer = new WikiIndex();
		ic = new IndexController(indexer);
		urls = new WikiCrawler().getCrawled();
	}
	
	public void search() throws IOException{
		Scanner sc = new Scanner(System.in);
		System.out.println("What do you want to search for?");
		String phrase = sc.nextLine();
		search(phrase);
	}
	
	public List<Document> search(String phrase) throws IOException{
		ic.indexUrls(urls);
		ic.calculateTfidf();
		List<Document> documents = ic.getDocuments();
		PriorityQueue<Document> docQueue = new PriorityQueue<Document>(documents.size(), new DocumentComparator(phrase));
		for(Document doc : documents){
			docQueue.add(doc);
		}
		System.out.println("\nYou searched for: " + phrase+". Enjoy your results.");
		List<Document> results = new ArrayList<>(); 
		while(!docQueue.isEmpty()){
			results.add(docQueue.poll());
		}
		return results;
	}

}