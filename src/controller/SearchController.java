package controller;

import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;

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
	
	public void search(String searchTerm) throws IOException{
		ic.indexUrls(urls);
		ic.calculateTfidf();
		List<Document> documents = ic.getDocuments();
//		Scanner sc = new Scanner(System.in);
		System.out.println("What do you want to search for?");
//		String search = sc.nextLine();
		String search = searchTerm;
		PriorityQueue<Document> docQueue = new PriorityQueue<Document>(documents.size(), new DocumentComparator(search));
		for(Document doc : documents){
			docQueue.add(doc);
		}
		System.out.println("\nYou searched for: " + search+". Enjoy your results.");
		while(!docQueue.isEmpty()){
			Document doc = docQueue.poll();
			System.out.println(doc.getUrl()+" TFIDF: " + doc.get(search));
		}
	}

}