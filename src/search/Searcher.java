package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crawler.JedisWikiCrawler;
import index.IIndex;
import index.JedisIndex;
import index.JedisMaker;
import redis.clients.jedis.Jedis;

public class Searcher implements ISearcher {
	
	private JedisIndex index;
	private JedisWikiCrawler crawler;
	private List<ISearchResult> previous_results;
	private ISearchResult current_results;
	
	public Searcher() throws IOException {
		Jedis jedis = JedisMaker.make();
		index = new JedisIndex(jedis);
		crawler = new JedisWikiCrawler(index);
		previous_results = new ArrayList<>();
	}
	
	public void search(String term) {
		clearDatabase();
		crawl();
		this.getResults(term);
	}
	
	public ISearchResult getResults(String term) {
		current_results = SearchResultFactory.search(term, index);
		previous_results.add(current_results);
		return current_results;
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