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

	private Map<String, Double> values; // URL -> value
	private TokenizedData data;

	/**
	 * Constructor.
	 * 
	 * @param map2
	 */
	public SearchResult(Map<String, Double> map2) {
		this.values = map2;
	}

	/**
	 * Looks up the relevance of a given URL.
	 * 
	 * @param url
	 * @return
	 */
	public Double getRelevance(String url) {
		Double relevance = values.get(url);
		return relevance==null ? 0: relevance;
	}

	/**
	 * Prints the contents in order of term frequency.
	 * 
	 * @param map
	 */
	public void print() {
		List<Entry<String, Double>> entries = getResults();
		if(entries == null){
			return;
		}
		for (Entry<String, Double> entry: entries) {
			System.out.println(entry);
		}
	}

	/**
	 * Computes the union of two search results.
	 * 
	 * @param that
	 * @return New ISearchResult object.
	 */
	@Override
	public ISearchResult or(ISearchResult that) {
		Map<String, Double> orMap = new HashMap<>(values);
		Set<String> thatTerms = that.getValues().keySet();
		for(String thatTerm : thatTerms){
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
	@Override
	public ISearchResult and(ISearchResult that) {
		Map<String, Double> andMap = new HashMap<>();
		for(String thatTerm : that.getValues().keySet()){
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
	@Override
	public ISearchResult minus(ISearchResult that) {
		Map<String, Double> minusMap = new HashMap<>(values);
		for(String thatTerm : that.getValues().keySet()){
			minusMap.remove(thatTerm);
		}
		return new SearchResult(minusMap);
	}

	private Double totalRelevance(ISearchResult that, String term){
		return totalRelevance(that.getRelevance(term), this.getRelevance(term));
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
	
	public List<Entry<String, Double>> getResults() {
		List<Entry<String, Double>> entries = new ArrayList<>(values.entrySet());
		Comparator<Entry<String, Double>> comparator = new RelevanceComparator();
		Collections.sort(entries, comparator);
		return entries;
	}
	
	public double getNumUrls(){
		return this.values.keySet().size();
	}
	
	public double getNumUrlsWithTerm(String term) {
		int count = 0;
		for(String url : values.keySet()){
			if(values.get(url) > 0){
				count++;
			}
		}
		return count;
	}

	
	public String getUrl(int result) {
		return this.getResults().get(result).getKey();
	}
	
	
	public Map<String, Double> getValues(){
		return this.values;
	}
	
	
	public Double tf(String url){
		return this.values.get(url);
	}

	@Override
	public void setTokenizedData(TokenizedData data) {
		this.data = data;
	}

	@Override
	public TokenizedData getTokenizedData() {
		return data;
	}

	@Override
	public boolean checkCorrectedSpelling() {
		TokenizedData data = this.getTokenizedData();
		if(data == null){
			return false;
		}
		return !data.getTokens().equals(data.getSpellCorrected());
	}
	
}