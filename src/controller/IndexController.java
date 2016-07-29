package controller;

import java.io.IOException;
import java.util.ArrayList;
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

	public double normalizedTfIdf(String term, String url){
		return this.normalizedTf(term, url) * this.idf(term);
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

	public double normalizedTf(String term, String url){
		return tf(term, url)/getDocEuclideanNorm(url);
	}

	private double getDocEuclideanNorm(String url){
		List<Integer> documentVector = this.getDocumentVector(url);
		double euclideanNorm = 0;
		for(Integer freq : documentVector){
			euclideanNorm += freq*freq;
		}
		return Math.sqrt(euclideanNorm);
	}

	private List<Integer> getDocumentVector(String url){
		List<Integer> documentVector = new ArrayList<>();
		for(String term : indexer.keySet()){
			for(TermCounter tc : indexer.get(term)){
				if(tc.getLabel().equals(url)){
					documentVector.add(tc.get(term));
				}
			}
		}
		return documentVector;
	}

}