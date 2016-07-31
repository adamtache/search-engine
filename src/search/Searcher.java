package search;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import crawler.JedisWikiCrawler;
import index.IIndex;
import index.JedisIndex;
import index.JedisMaker;
import redis.clients.jedis.Jedis;
import view.IView;

public class Searcher implements ISearcher {
	
	private IIndex index;
	private JedisWikiCrawler crawler;
	private Set<ISearchData> prev_data;
	private ISearchData current_data;
	private IView myView;
	
	public Searcher(IView view) throws IOException {
		Jedis jedis = JedisMaker.make();
		this.myView = view;
		this.index = new JedisIndex(jedis, myView);
		crawler = new JedisWikiCrawler(index, view);
		prev_data = new HashSet<>();
	}
	
	public void search(String term) {
		myView.updateStatus("Controller clearing Redis database.");
		clearDatabase();
		myView.updateStatus("Controller telling crawler to crawl.");
		crawl();
	}
	
	public ISearchData getResults(String term) {
		myView.updateStatus("Searcher telling index to create TF-Idf data.");
		current_data = new SearchData(index.getTfIds(term));
		myView.updateStatus("Index created TF-Idf data.");
		prev_data.add(current_data);
		return current_data;
	}
	
	private void crawl() {
		crawler.crawl();
		myView.updateStatus("Crawler finished crawling.");
	}
	
	private void clearDatabase(){
		index.deleteAllKeys();
		index.deleteTermCounters();
		index.deleteURLSets();
	}

}