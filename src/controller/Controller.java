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

public class Controller implements IController {

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
		clearDB();
		crawl();
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
	public void display() {
		myView.updateStatus("Controller initializing display of data.");
		myView.display();
	}

	@Override
	public void goTo(int page){
		myView.updateStatus("Controller initializing display of page.");
		myView.display(page);
	}

	@Override
	public ISearchResult getResults(String query) {
		myView.updateStatus("Controller obtaining search results.");
		ISearchResult result = ResultsFactory.getSearchResult(myParser.tokenize(query));
		this.myCurrentResult = result;
		return result;
	}
	
	@Override
	public ISearchResult getCurrentResult(){
		return this.myCurrentResult;
	}

}