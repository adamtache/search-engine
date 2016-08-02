package search;

import java.io.IOException;
import java.util.List;

import crawler.JedisWikiCrawler;
import index.IIndex;
import index.JedisIndex;
import index.JedisMaker;
import parser.EvaluationData;
import parser.Parser;
import parser.TreeEvaluator;
import parser.TreeFactory;
import parser.nodes.Node;
import redis.clients.jedis.Jedis;
import view.IView;

public class Searcher implements ISearcher {
	
	private IIndex index;
	private JedisWikiCrawler crawler;
	private ISearchResult current_data;
	private IView myView;
	private Parser myParser;
	private TreeFactory myTreeFactory;
	
	public Searcher(IView view) throws IOException {
		Jedis jedis = JedisMaker.make();
		this.myView = view;
		this.index = new JedisIndex(jedis, myView);
		this.crawler = new JedisWikiCrawler(index, view);
		this.myParser = new Parser(index);
		this.myTreeFactory = new TreeFactory(index);
	}
	
	public void search(String term) throws IOException {
//		clearDB();
//		crawl();
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
	
	public ISearchResult getResults(String term) {
		myView.updateStatus("Searcher telling index to create TF-Idf data.");
		List<String> tokens = myParser.tokenize(term);
		List<Node> roots = myTreeFactory.createRoot(tokens);
		current_data = this.evaluateRoots(roots);
		myView.updateStatus("Final data: " + current_data.getResults());
		return current_data;
	}
	
	private ISearchResult evaluateRoots(List<Node> roots){
		return new TreeEvaluator().evaluateRoots(roots, index);
	}
	
	private void clearDatabase(){
		index.clear();
	}

}