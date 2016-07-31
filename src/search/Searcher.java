package search;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import crawler.JedisWikiCrawler;
import index.IIndex;
import index.JedisIndex;
import index.JedisMaker;
import redis.clients.jedis.Jedis;

public class Searcher implements ISearcher {
	
	private IIndex index;
	private JedisWikiCrawler crawler;
	private Set<ISearchData> prev_data;
	private ISearchData current_data;
	
	public Searcher() throws IOException {
		Jedis jedis = JedisMaker.make();
		index = new JedisIndex(jedis);
		crawler = new JedisWikiCrawler(index);
		prev_data = new HashSet<>();
	}
	
	public void search(String term) {
		System.out.println("Controller clearing Redis database.");
		clearDatabase();
		System.out.println("Controller telling crawler to crawl.");
		crawl();
	}
	
	public ISearchData getResults(String term) {
		System.out.println("Searcher telling index to create TF-Idf data.");
		current_data = new SearchData(index.getTfIds(term));
		System.out.println("Index created TF-Idf data.");
		prev_data.add(current_data);
		return current_data;
	}
	
	private void crawl() {
		crawler.crawl();
		System.out.println("Crawler finished crawling.");
	}
	
	private void clearDatabase(){
		index.deleteAllKeys();
		index.deleteTermCounters();
		index.deleteURLSets();
	}

}