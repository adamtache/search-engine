package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import crawler.WikiCrawler;
import index.Index;
import index.IndexController;
import index.WikiIndex;

public class Searcher {
	
	private Index indexer;
	private IndexController ic;
	private List<String> urls;
	private PriorityQueue<Document> results;
	
	public Searcher() throws IOException{
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
		results = new PriorityQueue<Document>(documents.size(), new DocumentComparator(phrase));
		for(Document doc : documents){
			results.add(doc);
		}
		System.out.println("\nYou searched for: " + phrase+". Enjoy your results.");
		List<Document> res = new ArrayList<>(); 
		while(!results.isEmpty()){
			res.add(results.poll());
		}
		return res;
	}

}