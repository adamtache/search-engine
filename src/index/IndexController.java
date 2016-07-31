package index;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import search.Document;
import search.WikiSearch;

/**
 * Provides 
 * 
 * @param args
 * @throws IOException
 */

public class IndexController {

	private List<Document> documents;

	public IndexController(){
		this.documents = new ArrayList<>();
	}

	public void calculateTfidf(String term, WikiSearch search){
		List<Entry<String, Integer>> counts = search.sort();
		this.createDocuments(counts);
		for(Document doc : documents){
			doc.addTerm(term, this.tfIdf(doc.getUrl(), search, term));
		}
	}

	public double tfIdf(String url, WikiSearch search, String term){
		return search.getRelevance(url) * this.idf(search, term);
	}

	public double idf(WikiSearch search, String term){
		int numDocuments = search.getNumUrls();
		int documentFrequency = 0;
		search.getNumUrlsWithTerm(term);
		return Math.log((double) numDocuments/documentFrequency);
	}

	//	public double normalizedTfIdf(String term, String url, WikiSearch search){
	//	return this.normalizedTf(search, url) * this.idf(search, term);
	//}

	//	public double normalizedTf(WikiSearch search, String url){
	//		return search.getRelevance(url)/getDocEuclideanNorm(url);
	//	}
	//
	//	private double getDocEuclideanNorm(String url){
	//		List<Integer> documentVector = this.getDocumentVector(url);
	//		double euclideanNorm = 0;
	//		for(Integer freq : documentVector){
	//			euclideanNorm += freq*freq;
	//		}
	//		return Math.sqrt(euclideanNorm);
	//	}
	//
	//	private List<Integer> getDocumentVector(String url){
	//		List<Integer> documentVector = new ArrayList<>();
	//		for(String term : indexer.keySet()){
	//			for(TermCounter tc : indexer.get(term)){
	//				if(tc.getLabel().equals(url)){
	//					documentVector.add(tc.get(term));
	//				}
	//			}
	//		}
	//		return documentVector;
	//	}

	private void createDocuments(List<Entry<String, Integer>> counts){
		for(Entry<String, Integer> entry : counts){
			documents.add(new Document(entry.getKey()));
		}
	}

	public List<Document> getDocuments() {
		return documents;
	}

}