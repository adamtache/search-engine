package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import crawler.JedisWikiCrawler;
import index.IndexController;
import index.JedisIndex;
import index.JedisMaker;
import redis.clients.jedis.Jedis;

public class Searcher {
	
	private JedisIndex index;
	private IndexController ic;
	private JedisWikiCrawler crawler;
	private PriorityQueue<Document> results;
	
	public Searcher() throws IOException{
		Jedis jedis = JedisMaker.make();
		index = new JedisIndex(jedis);
		ic = new IndexController();
		crawler = new JedisWikiCrawler(index);
	}
	
	public List<Document> search(String term) throws IOException{
		crawler.crawl();
		WikiSearch search = WikiSearch.search(term, index);
		ic.calculateTfidf(term, search);
		List<Document> documents = ic.getDocuments();
		results = new PriorityQueue<Document>(documents.size(), new DocumentComparator(term));
		for(Document doc : documents){
			results.add(doc);
		}
		System.out.println("\nYou searched for: " + term+". Enjoy your results.");
		List<Document> res = new ArrayList<>(); 
		while(!results.isEmpty()){
			res.add(results.poll());
		}
		return res;
	}

}