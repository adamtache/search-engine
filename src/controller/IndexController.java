package controller;

import java.io.IOException;
import java.util.List;
import org.jsoup.select.Elements;
import model.fetcher.Fetcher;
import model.index.Index;
import model.index.TermCounter;

public class IndexController {
	
	private Index indexer;
	private Fetcher fetcher;
	
	public IndexController(Index index){
		this.indexer = index;
		this.fetcher = index.getFetcher();
	}
	
	public void indexUrls(List<String> urls) throws IOException{
		for(String url : urls){
			Elements paragraphs = fetcher.fetch("https://en.wikipedia.org" + url);
			indexer.indexPage(url, paragraphs);
		}
	}
	
	public double tfIdf(String term, String url){
		return this.tf(term, url) * this.idf(term);
	}

	public double tf(String term, String url){
		for(TermCounter tc : indexer.get(term)){
			if(tc.getLabel().equals(url)){
				return tc.get(term);
			}
		}
		return 0;
	}
	
	public double idf(String term){
		int numDocuments = indexer.getIndexed().size();
		int documentFrequency = 0;
		for(TermCounter tc : indexer.get(term)){
			if(tc.get(term) > 0){
				documentFrequency++;
			}
		}
		return Math.log((double) numDocuments/documentFrequency);
	}
	
}