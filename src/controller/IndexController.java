package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.select.Elements;

import fetcher.Fetcher;
import index.Document;
import index.Index;
import index.TermCounter;

public class IndexController {

	private Index indexer;
	private Fetcher fetcher;
	private List<Document> documents;

	public IndexController(Index index){
		this.indexer = index;
		this.fetcher = index.getFetcher();
		this.documents = new ArrayList<>();
	}

	public void indexUrls(List<String> urls) throws IOException{
		for(String url : urls){
			Elements paragraphs = fetcher.fetch("https://en.wikipedia.org" + url);
			indexer.indexPage(url, paragraphs);
			documents.add(new Document(url));
		}
	}

	public void calculateTfidf(){
		Set<String> terms = indexer.keySet();
		for(String term : terms){
			for(TermCounter tc : indexer.get(term)){
				String url = tc.getLabel();
				double tfidf = normalizedTfIdf(term, url);
				getDocument(url).add(term, tfidf);
			}
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
	
	private Document getDocument(String url){
		for(Document doc : documents){
			if(doc.getUrl().equals(url)){
				return doc;
			}
		}
		return null;
	}

	public List<Document> getDocuments() {
		return documents;
	}

}