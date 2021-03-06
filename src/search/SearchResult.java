package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import parser.TokenizedData;


/**
 * Represents the results of a search query.
 *
 */
public class SearchResult implements ISearchResult {

	private Map<Document, Double> values; // Document (url, title, snippet) -> value
	private TokenizedData data;

	/**
	 * Constructor.
	 * 
	 * @param map2
	 */
	public SearchResult(Map<Document, Double> map) {
		this.values = map;
	}

	/**
	 * Looks up the relevance of a given URL.
	 * 
	 * @param url
	 * @return
	 */
	public Double getRelevance(Document doc) {
		Double relevance = values.get(doc.getURL());
		return relevance==null ? 0: relevance;
	}

	/**
	 * Computes the union of two search results.
	 * 
	 * @param that
	 * @return New ISearchResult object.
	 */
	public ISearchResult or(ISearchResult that) {
		Map<Document, Double> orMap = new HashMap<>(values);
		Set<Document> thatTerms = that.getValues().keySet();
		for(Document thatTerm : thatTerms){
			orMap.put(thatTerm, totalRelevance(that, thatTerm));
		}
		return new SearchResult(orMap);
	}

	/**
	 * Computes the intersection of two search results.
	 * 
	 * @param that
	 * @return New ISearchResult object.
	 */
	public ISearchResult and(ISearchResult that) {
		Map<Document, Double> andMap = new HashMap<>();
		for(Document thatTerm : that.getValues().keySet()){
			if(this.values.containsKey(thatTerm)){
				andMap.put(thatTerm, totalRelevance(that, thatTerm));
			}
		}
		return new SearchResult(andMap);
	}

	/**
	 * Computes the intersection of two search results.
	 * 
	 * @param that
	 * @return New ISearchResult object.
	 */
	public ISearchResult minus(ISearchResult that) {
		Map<Document, Double> minusMap = new HashMap<>(values);
		for(Document thatTerm : that.getValues().keySet()){
			minusMap.remove(thatTerm);
		}
		return new SearchResult(minusMap);
	}

	private Double totalRelevance(ISearchResult that, Document thatTerm){
		return totalRelevance(that.getRelevance(thatTerm), this.getRelevance(thatTerm));
	}

	/**
	 * Computes the relevance of a search with multiple terms.
	 * 
	 * @param rel1: relevance score for the first search
	 * @param rel2: relevance score for the second search
	 * @return
	 */
	protected Double totalRelevance(Double rel1, Double rel2) {
		// simple starting place: relevance is the sum of the term frequencies.
		return rel1 + rel2;
	}

	/**
	 * Sort the results by relevance.
	 * 
	 * @return List of entries with URL and relevance.
	 */
	public List<Entry<Document, Double>> getResults() {
		List<Entry<Document, Double>> entries = new ArrayList<>(values.entrySet());
		Comparator<Entry<Document, Double>> comparator = new RelevanceComparator();
		Collections.sort(entries, comparator);
		return entries;
	}
	
	public double getNumUrls(){
		return this.values.keySet().size();
	}
	
	public double getNumUrlsWithTerm(String term) {
		int count = 0;
		for(Document doc : values.keySet()){
			if(values.get(doc) > 0){
				count++;
			}
		}
		return count;
	}

	public String getUrl(int result) {
		return this.getResults().get(result).getKey().getURL();
	}
	
	public Map<Document, Double> getValues(){
		return this.values;
	}
		
	public Double tf(String url){
		return this.values.get(url);
	}

	public void setTokenizedData(TokenizedData data) {
		this.data = data;
	}

	public TokenizedData getTokenizedData() {
		return data;
	}

	public boolean checkCorrectedSpelling() {
		TokenizedData data = this.getTokenizedData();
		if(data == null){
			return false;
		}
		return !data.getTokens().equals(data.getSpellCorrected());
	}
	
	public void print() {
		List<Entry<Document, Double>> entries = getResults();
		if(entries == null){
			return;
		}
		for (Entry<Document, Double> entry: entries) {
			System.out.println(entry);
		}
	}
	
}