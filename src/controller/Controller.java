package controller;

import java.io.IOException;
import java.util.List;

import crawler.JedisWikiCrawler;
import crawler.YouTubeVideoCrawler;
import index.IIndex;
import index.JedisMaker;
import index.WebIndex;
import index.YouTubeIndex;
import parser.Parser;
import search.ISearchResult;
import search.ResultsFactory;
import view.IView;

public class Controller {

	private IView myView;
	private JedisWikiCrawler crawler;
	private IIndex webIndex;
	private IIndex ytIndex;
	private Parser myParser;
	private ISearchResult myLastResult;

	public Controller(IView view) throws IOException {
		this.myView = view;
		new JedisMaker();
		this.webIndex = new WebIndex(JedisMaker.make(), myView);
		this.ytIndex = new YouTubeIndex(JedisMaker.make(), myView);
		this.crawler = new JedisWikiCrawler(webIndex, view);
		this.myParser = new Parser();
	}

	public void initialize() throws IOException{
//		setupWebSearch();
//		setupYouTubeSearch();
	}
	
	private void setupWebSearch() throws IOException{
		webIndex.reset();
		crawl();
		webIndex.addDocumentsToDB();
		System.out.println("HERE");
	}
	
	private void setupYouTubeSearch() throws IOException{
		ytIndex.deleteDocData();
		YouTubeVideoCrawler ytCrawler = new YouTubeVideoCrawler(ytIndex);
		ytCrawler.crawl();
		ytIndex.addDocumentsToDB();
	}

	private void crawl() {
		myView.updateStatus("Controller activating crawling.");
		try {
			crawler.crawl();
		} catch (IOException e) {
			e.printStackTrace();
		}
		myView.updateStatus("Crawler finished crawling.");
	}

	public void display(ISearchResult result) {
		myView.updateStatus("Controller initializing display of data.");
		myView.display(result);
	}

	public void goToLucky(ISearchResult result){
		myView.updateStatus("Controller initializing display of page.");
		myView.display(result.getUrl(0));
	}
	
	public ISearchResult getResults(String query) {
		myView.updateStatus("Controller obtaining search results.");
		if(webIndex.hasQueryData(query)){
			return webIndex.getQueryResult(query);
		}
		ISearchResult result = ResultsFactory.getSearchResult(myParser.tokenize(query), webIndex);
		this.webIndex.storeQuery(query, result);
		this.myLastResult = result;
		return result;
	}
	
	public ISearchResult getYTResults(String query) {
		myView.updateStatus("Controller obtaining search results.");
		if(ytIndex.hasQueryData(query)){
			return ytIndex.getQueryResult(query);
		}
		ISearchResult result = ResultsFactory.getSearchResult(myParser.tokenize(query), ytIndex);
		this.ytIndex.storeQuery(query, result);
		this.myLastResult = result;
		return result;
	}

	public String getLuckyResult() {
		return myLastResult.getUrl(0);
	}

	public String getSpellCorrected() {
		String spellCorrected = "";
		List<String> corrected = myLastResult.getTokenizedData().getSpellCorrected();
		for(int x=0; x<corrected.size(); x++){
			spellCorrected+=corrected+" ";
		}
		return spellCorrected;
	}

	public void displaySpellCorrected() {
		display(getResults(getSpellCorrected()));
	}

}