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
			Elements paragraphs = fetcher.read(url);
			indexer.indexPage(url, paragraphs);
		}
	}
	
	public double tfIdf(String term){
		return this.tf(term) * this.idf(term);
	}

	public double tf(String term){
		return indexer.get(term).size();
	}
	
	public double idf(String term){
		int documentFrequency = 0;
		for(TermCounter counter : indexer.get(term)){
			if(counter.get(term) > 0){
				documentFrequency++;
			}
		}
		int numDocuments = indexer.getIndexed().size();
		return Math.log(numDocuments/documentFrequency);
	}
	
}