package controller;

import java.io.IOException;

import crawler.JedisWikiCrawler;
import index.IIndex;
import index.JedisIndex;
import index.JedisMaker;
import redis.clients.jedis.Jedis;
import search.ISearchResult;
import search.ISearcher;
import search.Searcher;
import view.IView;

public class Controller implements IController {

	private ISearcher mySearcher;
	private IView myView;
	private JedisWikiCrawler crawler;
	private IIndex index;

	public Controller(IView view) throws IOException {
		this.myView = view;
		this.index = new JedisIndex(new JedisMaker().make(), myView);
		this.mySearcher = new Searcher(view, index);
		this.crawler = new JedisWikiCrawler(index, view);
	}

	@Override
	public void start(){
		clearDB();
		crawl();
		createDocuments();
	}

	private void createDocuments(){
		try {
			Jedis index = new JedisMaker().make();
		} catch (IOException e) {
			e.printStackTrace();
		}
		index.addDocumentsToDB();
	}

	private void clearDB(){
		myView.updateStatus("Controller clearing Redis database.");
		clearDatabase();
	}

	private void crawl() {
		myView.updateStatus("Controller telling crawler to crawl.");
		try {
			crawler.crawl();
		} catch (IOException e) {
			e.printStackTrace();
		}
		myView.updateStatus("Crawler finished crawling.");
	}

	private void clearDatabase(){
		index.clear();
	}

	@Override
	public void search(String term) throws IOException {
		myView.updateStatus("Controller telling searcher to search.");
		mySearcher.search(term);
	}

	@Override
	public void display() {
		myView.updateStatus("Controller telling view to display data.");
		myView.display();
	}

	@Override
	public void goTo(int page){
		myView.updateStatus("Controller telling view to display page.");
		myView.display(page);
	}

	@Override
	public ISearchResult getResults() {
		myView.updateStatus("View getting results from searcher to send to MainScreen.");
		return mySearcher.getResults(myView.getSearchTerm());
	}

	@Override
	public String getResultUrl(int result) {
		return getResults().getUrl(result);
	}

}