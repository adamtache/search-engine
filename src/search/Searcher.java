package search;

import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Map.Entry;
import crawler.JedisWikiCrawler;
import index.JedisIndex;
import index.JedisMaker;
import redis.clients.jedis.Jedis;

public class Searcher implements ISearcher {
	
	private JedisIndex index;
	private JedisWikiCrawler crawler;
	private Set<ISearchResult> previous_results;
	private ISearchResult current_results;
	
	public Searcher() throws IOException {
		Jedis jedis = JedisMaker.make();
		index = new JedisIndex(jedis);
		crawler = new JedisWikiCrawler(index);
		previous_results = new HashSet<>();
	}
	
	public void search(String term) {
		clearDatabase();
		crawl();
	}
	
	public PriorityQueue<Entry<String, Double>> getResults(String term) {
		current_results = SearchResultFactory.search(term, index);
		previous_results.add(current_results);
		return index.getTfIds(term);
	}
	
	private void crawl() {
		crawler.crawl();
	}
	
	private void clearDatabase(){
		index.deleteAllKeys();
		index.deleteTermCounters();
		index.deleteURLSets();
	}

}