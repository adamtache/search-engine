package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Represents the results of a search query.
 *
 */
public class WikiSearch implements ISearchResult {

	// map from URLs that contain the term(s) to relevance score
	private Map<String, Integer> counts; // URL -> term count

	/**
	 * Constructor.
	 * 
	 * @param map2
	 */
	public WikiSearch(Map<String, Integer> map2) {
		this.counts = map2;
	}

	/**
	 * Looks up the relevance of a given URL.
	 * 
	 * @param url
	 * @return
	 */
	public Integer getRelevance(String url) {
		Integer relevance = counts.get(url);
		return relevance==null ? 0: relevance;
	}

	/**
	 * Prints the contents in order of term frequency.
	 * 
	 * @param map
	 */
	public void print() {
		List<Entry<String, Integer>> entries = sort();
		if(entries == null){
			return;
		}
		for (Entry<String, Integer> entry: entries) {
			System.out.println(entry);
		}
	}

	/**
	 * Computes the union of two search results.
	 * 
	 * @param that
	 * @return New ISearchResult object.
	 */
	public ISearchResult or(ISearchResult that) {
		Map<String, Integer> orMap = new HashMap<>(counts);
		Set<String> thatTerms = that.getCounts().keySet();
		for(String thatTerm : thatTerms){
			orMap.put(thatTerm, totalRelevance(that, thatTerm));
		}
		return new WikiSearch(orMap);
	}

	/**
	 * Computes the intersection of two search results.
	 * 
	 * @param that
	 * @return New ISearchResult object.
	 */
	public ISearchResult and(ISearchResult that) {
		Map<String, Integer> andMap = new HashMap<>();
		for(String thatTerm : that.getCounts().keySet()){
			if(this.counts.containsKey(thatTerm)){
				andMap.put(thatTerm, totalRelevance(that, thatTerm));
			}
		}
		return new WikiSearch(andMap);
	}

	/**
	 * Computes the intersection of two search results.
	 * 
	 * @param that
	 * @return New ISearchResult object.
	 */
	public ISearchResult minus(ISearchResult that) {
		Map<String, Integer> minusMap = new HashMap<>(counts);
		for(String thatTerm : that.getCounts().keySet()){
			minusMap.remove(thatTerm);
		}
		return new WikiSearch(minusMap);
	}

	private Integer totalRelevance(ISearchResult that, String term){
		return totalRelevance(that.getRelevance(term), this.getRelevance(term));
	}

	/**
	 * Computes the relevance of a search with multiple terms.
	 * 
	 * @param rel1: relevance score for the first search
	 * @param rel2: relevance score for the second search
	 * @return
	 */
	protected Integer totalRelevance(Integer rel1, Integer rel2) {
		// simple starting place: relevance is the sum of the term frequencies.
		return rel1 + rel2;
	}

	/**
	 * Sort the results by relevance.
	 * 
	 * @return List of entries with URL and relevance.
	 */
	
	public List<Entry<String, Integer>> sort() {
		List<Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
		Comparator<Entry<String, Integer>> comparator = new RelevanceComparator();
		Collections.sort(entries, comparator);
		return entries;
	}
	
	
	public int getNumUrls(){
		return this.counts.keySet().size();
	}

	
	public int getNumUrlsWithTerm(String term) {
		int count = 0;
		for(String url : counts.keySet()){
			if(counts.get(url) > 0){
				count++;
			}
		}
		return count;
	}

	
	public String getUrl(int result) {
		return this.sort().get(result).getKey();
	}
	
	
	public Map<String, Integer> getCounts(){
		return this.counts;
	}
	
	
	public Integer tf(String url){
		return this.counts.get(url);
	}
	
}