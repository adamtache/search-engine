package search;

import java.io.IOException;
import crawler.JedisWikiCrawler;
import index.IIndex;
import index.JedisIndex;
import index.JedisMaker;
import parser.Parser;
import redis.clients.jedis.Jedis;
import view.IView;

public class Searcher implements ISearcher {

	private IIndex index;
	private JedisWikiCrawler crawler;
	private IView myView;
	private Parser myParser;

	public Searcher(IView view) throws IOException {
		Jedis jedis = JedisMaker.make();
		this.myView = view;
		this.index = new JedisIndex(jedis, myView);
		this.crawler = new JedisWikiCrawler(index, view);
		this.myParser = new Parser(index);
	}
	
	public ISearchResult getResults(String term) {
		myView.updateStatus("Searcher telling index to create TF-Idf data.");
		return ResultsFactory.getSearchResult(myParser.tokenize(term));
	}

	public void search(String term) throws IOException {
				clearDB();
				crawl();
	}

	private void clearDB(){
		myView.updateStatus("Controller clearing Redis database.");
		clearDatabase();
	}

	private void crawl() throws IOException{
		myView.updateStatus("Controller telling crawler to crawl.");
		crawler.crawl();
		myView.updateStatus("Crawler finished crawling.");
	}

	private void clearDatabase(){
		index.clear();
	}

}