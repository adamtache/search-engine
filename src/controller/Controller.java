package controller;

import java.io.IOException;

import crawler.JedisWikiCrawler;
import index.IIndex;
import index.JedisIndex;
import index.JedisMaker;
import parser.Parser;
import search.ISearchResult;
import search.ResultsFactory;
import view.IView;

public class Controller {

	private IView myView;
	private JedisWikiCrawler crawler;
	private IIndex index;
	private Parser myParser;
	private ISearchResult myCurrentResult;

	public Controller(IView view) throws IOException {
		this.myView = view;
		new JedisMaker();
		this.index = new JedisIndex(JedisMaker.make(), myView);
		this.crawler = new JedisWikiCrawler(index, view);
		this.myParser = new Parser(index);
	}

	public void initialize(){
		//		System.out.println("RESETTING");
		//		myView.updateStatus("Controller resetting Redis database.");
		//		index.reset();
		//		System.out.println("CRAWLING");
		//		crawl();
	}

	public ISearchResult getResults(String query) {
		myView.updateStatus("Controller obtaining search results.");
		if(index.hasQueryData(query)){
			return index.getQueryResult(query);
		}
		ISearchResult result = ResultsFactory.getSearchResult(myParser.tokenize(query));
		this.index.storeQuery(query, result);
		this.myCurrentResult = result;
		return result;
	}

	private void crawl() {
		myView.updateStatus("Controller activating crawling.");
		try {
			crawler.crawl();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("FINISHED CRAWLING");
		myView.updateStatus("Crawler finished crawling.");
		index.addDocumentsToDB();
	}

	public void display(ISearchResult result) {
		myView.updateStatus("Controller initializing display of data.");
		myView.display(result);
	}

	public void goToLucky(ISearchResult result){
		myView.updateStatus("Controller initializing display of page.");
		myView.display(result.getUrl(0));
	}

	public String getLuckyResult() {
		return myCurrentResult.getUrl(0);
	}

}